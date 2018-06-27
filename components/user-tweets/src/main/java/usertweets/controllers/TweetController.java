package usertweets.controllers;

import java.util.UUID;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.schemas.data.UserTweets;

import usertweets.bindings.UserTweetsBinding;

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
}
