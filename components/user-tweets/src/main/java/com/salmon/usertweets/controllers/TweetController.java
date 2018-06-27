package com.salmon.usertweets.controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserFollows;
import com.salmon.schemas.data.UserTweets;

import com.salmon.usertweets.bindings.UserTweetsBinding;

@RestController
public class TweetController
{
	private static final Logger LOG = LoggerFactory.getLogger(TweetController.class);

	private final QueryableStoreRegistry queryableStoreRegistry;

	@Autowired
	public TweetController(final QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	@GetMapping(path = "/tweets/user-by-id/{id}")
	public UserTweets getTweetsByUser(@PathVariable("id") final String uuid)
	{
		final ReadOnlyKeyValueStore<UUID, UserTweets> store = queryableStoreRegistry.getQueryableStoreType(UserTweetsBinding.TWEETS_STORE, QueryableStoreTypes.keyValueStore());

		store.all().forEachRemaining(entry -> LOG.info("Entry: {}, {}", entry.key, entry.value));

		return store.get(UUID.fromString(uuid));
	}

	@GetMapping(path = "/tweets/for-user/{id}")
	public List<Tweet> getTweetsForUser(@PathVariable("id") final String followerId)
	{
		final ReadOnlyKeyValueStore<UUID, UserFollows> store = queryableStoreRegistry.getQueryableStoreType(UserTweetsBinding.FOLLOWS_STORE, QueryableStoreTypes.keyValueStore());

		final UserFollows userFollows = store.get(UUID.fromString(followerId));

		if (userFollows == null || userFollows.getFollowing().isEmpty())
		{
			LOG.info("No user of no follows");
			return Collections.emptyList();
		}

		final ReadOnlyKeyValueStore<UUID, UserTweets> tweetsStore = queryableStoreRegistry.getQueryableStoreType(UserTweetsBinding.TWEETS_STORE, QueryableStoreTypes.keyValueStore());

		return userFollows.getFollowing().stream()
			.peek(followingUUID -> LOG.info("User {} is following {}", followerId, followingUUID))
			.map(tweetsStore::get)
			.filter(Objects::nonNull)
			.flatMap(userTweets -> userTweets.getTweets().stream())
			.sorted(Comparator.comparing(Tweet::getDate))
			.collect(Collectors.toList());
	}
}
