package com.salmon.userservice.processors;

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

import com.salmon.schemas.data.EnrichedTweet;
import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.serde.JsonSerde;
import com.salmon.schemas.serde.UUIDSerde;
import com.salmon.userservice.bindings.AnalyticsBinding;

@Component
public class PageViewsEnricher
{
	private static final Logger LOG = LoggerFactory.getLogger(PageViewsEnricher.class);
	private static final UUIDSerde UUID_SERDE = new UUIDSerde();

	private Joined<UUID, Tweet, UserData> joined = Joined.with(UUID_SERDE,
		JsonSerde.forClass(Tweet.class),
		JsonSerde.forClass(UserData.class));

	@StreamListener
	@SendTo(AnalyticsBinding.ENRICHED_TWEETS_OUT)
	public KStream<UUID, EnrichedTweet> process(@Input(AnalyticsBinding.TWEETS_IN) KStream<UUID, Tweet> tweetsStream,
	                                            @Input(AnalyticsBinding.USERS_IN) KStream<UUID, UserData> userRegistrations)
	{
		LOG.info("Creating join");
		final KTable<UUID, UserData> usersTable = userRegistrations
			.peek((uuid, userData) -> LOG.info("Peeking at User {}, {}", uuid, userData))
			.groupByKey(Serialized.with(UUID_SERDE, JsonSerde.forClass(UserData.class)))
			.reduce(UserData::merge, AnalyticsBinding.USERS_MV);

		return tweetsStream
			.peek((uuid, tweet) -> LOG.info("Peeking at Tweet: {}, {}", uuid, tweet))
			.join(usersTable, EnrichedTweet::enrich, this.joined);
	}

}
