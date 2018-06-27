package com.salmon.userfollows.processors;

import java.util.UUID;

import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Serialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.salmon.schemas.data.FollowRequest;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.data.UserFollows;
import com.salmon.schemas.serde.JsonSerde;
import com.salmon.schemas.serde.UUIDSerde;

import com.salmon.userfollows.bindings.UserFollowsBinding;

@Component
public class UserFollowsJoiner
{
	private static final Logger LOG = LoggerFactory.getLogger(UserFollowsJoiner.class);

	private static final UUIDSerde UUID_SERDE = new UUIDSerde();

	private Joined<UUID, FollowRequest, UserData> joiner = Joined.with(UUID_SERDE,
		JsonSerde.forClass(FollowRequest.class),
		JsonSerde.forClass(UserData.class));

	@StreamListener
	@SendTo(UserFollowsBinding.FOLLOWS_OUT)
	public KStream<UUID, UserFollows> createUserTweets(@Input(UserFollowsBinding.FOLLOWS_IN) KStream<UUID, FollowRequest> tweetsStream,
	                                                   @Input(UserFollowsBinding.USERS_IN) KTable<UUID, UserData> usersTable)
	{
		return tweetsStream
			.peek((followId, followRequest) -> LOG.info("Received followRequest: {}", followRequest))
			.selectKey((tweetId, followRequest) -> followRequest.getFollower())
			.join(usersTable, UserFollows::enrich, joiner)
			.groupByKey(Serialized.with(UUID_SERDE, JsonSerde.forClass(UserFollows.class)))
			.reduce(UserFollows::reduce, UserFollowsBinding.USER_FOLLOWS)
			.toStream()
			.peek((userId, userTweets) -> LOG.info("Updated: {}", userTweets));
	}
}
