package com.salmon.tweetenricher.processors;

import java.util.UUID;

import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.salmon.schemas.data.EnrichedTweet;
import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.data.UserTweets;
import com.salmon.schemas.serde.JsonSerde;
import com.salmon.schemas.serde.UUIDSerde;
import com.salmon.tweetenricher.bindings.AnalyticsBinding;

@Component
public class TweetEnricher
{
	private static final Logger LOG = LoggerFactory.getLogger(TweetEnricher.class);

	private static final UUIDSerde UUID_SERDE = new UUIDSerde();

	private Joined<UUID, Tweet, UserData> joined = Joined.with(UUID_SERDE,
		JsonSerde.forClass(Tweet.class),
		JsonSerde.forClass(UserData.class));

	private Joined<UUID, Tweet, UserData> userJoinger = Joined.with(UUID_SERDE,
		JsonSerde.forClass(Tweet.class),
		JsonSerde.forClass(UserData.class));

	@StreamListener
	@SendTo(AnalyticsBinding.ENRICHED_TWEETS_OUT)
	public KStream<UUID, EnrichedTweet> enrichTweets(@Input(AnalyticsBinding.TWEETS_IN) KStream<UUID, Tweet> tweetsStream,
	                                                 @Input(AnalyticsBinding.USERS_IN) KTable<UUID, UserData> usersTable)
	{
		LOG.info("UsersTable: {}", usersTable.queryableStoreName());

		return tweetsStream.selectKey((tweetId, tweet) -> tweet.getUserId())
			.join(usersTable, EnrichedTweet::enrich, this.joined)
			.selectKey((userId, enrichedTweet) -> enrichedTweet.getTweetId());
	}

	@StreamListener
	public void createUserTweets(@Input(AnalyticsBinding.TWEETS_IN) KStream<UUID, Tweet> tweetsStream,
	                             @Input(AnalyticsBinding.USERS_IN) KTable<UUID, UserData> usersTable)
	{
		tweetsStream.selectKey((tweetId, tweet) -> tweet.getUserId())
			.join(usersTable, UserTweets::enrich, userJoinger)
			.groupByKey()
			.reduce(UserTweets::reduce, AnalyticsBinding.TWEETS_STORE);
	}
}
