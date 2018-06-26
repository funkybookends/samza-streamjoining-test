package com.salmon.userservice.processors;

import java.util.UUID;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.properties.KafkaStreamsExtendedBindingProperties;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.salmon.schemas.data.EnrichedTweet;
import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.serde.JsonSerde;
import com.salmon.schemas.serde.UUIDSerde;
import com.salmon.userservice.bindings.AnalyticsBinding;

import static org.apache.kafka.streams.KeyValue.pair;

@Component
public class TweetEnricher
{
	private static final Logger LOG = LoggerFactory.getLogger(TweetEnricher.class);
	private static final UUIDSerde UUID_SERDE = new UUIDSerde();

	private Joined<UUID, Tweet, UserData> joined = Joined.with(UUID_SERDE,
		JsonSerde.forClass(Tweet.class),
		JsonSerde.forClass(UserData.class));

	private KafkaStreamsExtendedBindingProperties properties;

	@Autowired
	public TweetEnricher(final KafkaStreamsExtendedBindingProperties properties)
	{
		this.properties = properties;
		LOG.info("{}", properties);
		LOG.info("{}", properties.getBindings().keySet());
		LOG.info("MaterializedAs {}", properties.getBindings().get("users-in").getConsumer().getMaterializedAs());
	}

	@StreamListener
	@SendTo(AnalyticsBinding.ENRICHED_TWEETS_OUT)
	public KStream<UUID, EnrichedTweet> process(@Input(AnalyticsBinding.TWEETS_IN) KStream<UUID, Tweet> tweetsStream,
	                                            @Input(AnalyticsBinding.USERS_IN) KTable<UUID, UserData> usersTable)
	{
		LOG.info("UsersTable: {}", usersTable.queryableStoreName());

		return tweetsStream
			.map(this::keyOnUserId)
			.join(usersTable, EnrichedTweet::enrich, this.joined)
			.map(this::keyOnTweetId);
	}

	private KeyValue<UUID, EnrichedTweet> keyOnTweetId(final UUID userId, final EnrichedTweet enrichedTweet)
	{
		return pair(enrichedTweet.getTweetId(), enrichedTweet);
	}

	private KeyValue<UUID, Tweet> keyOnUserId(final UUID tweetId, final Tweet tweet)
	{
		return pair(tweet.getUserId(), tweet);
	}

}
