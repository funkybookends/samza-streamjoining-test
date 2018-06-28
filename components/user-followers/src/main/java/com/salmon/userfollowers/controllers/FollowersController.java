package com.salmon.userfollowers.controllers;

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

import com.salmon.schemas.data.UserFollowers;
import com.salmon.userfollowers.bindings.UserFollowersBinding;


@RestController
public class FollowersController
{
	private static final Logger LOG = LoggerFactory.getLogger(FollowersController.class);

	private final QueryableStoreRegistry queryableStoreRegistry;

	@Autowired
	public FollowersController(final QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	@GetMapping(path = "/followers/user-by-id/{id}")
	public UserFollowers getTweetsByUser(@PathVariable("id") final String uuid)
	{
		final ReadOnlyKeyValueStore<UUID, UserFollowers> store = queryableStoreRegistry.getQueryableStoreType(UserFollowersBinding.USER_FOLLOWERS, QueryableStoreTypes.keyValueStore());

		store.all().forEachRemaining(entry -> LOG.info("Entry: {}, {}", entry.key, entry.value));

		return store.get(UUID.fromString(uuid));
	}
}
