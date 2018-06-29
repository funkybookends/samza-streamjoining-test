package com.salmon.tweetpusher.service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.stereotype.Component;

import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserFollowers;
import com.salmon.schemas.data.UserFollows;
import com.salmon.tweetpusher.binding.TweetPusherBinding;

@Component
public class TweetSubscriberService
{
	private static final Logger LOG = LoggerFactory.getLogger(TweetSubscriberService.class);

	private final Map<UUID, Consumer<Tweet>> subscribers = new ConcurrentHashMap<>();

	private final QueryableStoreRegistry queryableStoreRegistry;

	@Autowired
	public TweetSubscriberService(final QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	@StreamListener
	public void process(@Input(TweetPusherBinding.TWEETS) KStream<UUID, Tweet> tweetStream,
	                    @Input(TweetPusherBinding.USER_FOLLOWERS) KTable<UUID, UserFollowers> userFollowers,
	                    @Input(TweetPusherBinding.USER_FOLLOWS) KTable<UUID, UserFollows> userFollows)
	{
		tweetStream
			.peek((id, tweet) -> LOG.info("Notifying subscribers for tweet {}", id))
			.foreach(this::handleTweet);
	}

	private void handleTweet(final UUID tweetId, final Tweet tweet)
	{
		final UUID tweeterId = tweet.getUserId();
		final ReadOnlyKeyValueStore<UUID, UserFollowers> store = queryableStoreRegistry.getQueryableStoreType(TweetPusherBinding.USER_FOLLOWERS_STORE, QueryableStoreTypes.keyValueStore());
		final UserFollowers tweeterFollowers = store.get(tweeterId);

		if (tweeterFollowers != null)
		{
			tweeterFollowers.getFollowers().stream()
				.map(subscribers::get)
				.filter(Objects::nonNull)
				.peek((consumer -> LOG.info("Notifying!")))
				.forEach(consumer -> consumer.accept(tweet));
		}
	}

	public void subscribe(UUID user, Consumer<Tweet> consumer)
	{
		subscribers.put(user, consumer);
	}

	public void unsubscribe(UUID user)
	{
		subscribers.remove(user);
	}
}
