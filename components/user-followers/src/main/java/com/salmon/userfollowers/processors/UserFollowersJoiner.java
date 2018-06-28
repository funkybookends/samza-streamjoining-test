package com.salmon.userfollowers.processors;

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
import com.salmon.schemas.data.UserFollowers;
import com.salmon.schemas.serde.JsonSerde;
import com.salmon.schemas.serde.UUIDSerde;
import com.salmon.userfollowers.bindings.UserFollowersBinding;

@Component
public class UserFollowersJoiner
{
	private static final Logger LOG = LoggerFactory.getLogger(UserFollowersJoiner.class);

	private static final UUIDSerde UUID_SERDE = new UUIDSerde();

	private Joined<UUID, FollowRequest, UserData> joiner = Joined.with(UUID_SERDE,
		JsonSerde.forClass(FollowRequest.class),
		JsonSerde.forClass(UserData.class));

	@StreamListener
	@SendTo(UserFollowersBinding.FOLLOWERS_OUT)
	public KStream<UUID, UserFollowers> createUserTweets(@Input(UserFollowersBinding.FOLLOWS_IN) KStream<UUID, FollowRequest> tweetsStream,
	                                                     @Input(UserFollowersBinding.USERS_IN) KTable<UUID, UserData> usersTable)
	{
		return tweetsStream
			.selectKey((tweetId, followRequest) -> followRequest.getFollows())
			.join(usersTable, UserFollowers::enrich, joiner)
			.groupByKey(Serialized.with(UUID_SERDE, JsonSerde.forClass(UserFollowers.class)))
			.reduce(UserFollowers::reduce, UserFollowersBinding.USER_FOLLOWERS)
			.toStream()
			.peek((userId, followers) -> LOG.info("Updated Followers: {}", userId));
	}
}
