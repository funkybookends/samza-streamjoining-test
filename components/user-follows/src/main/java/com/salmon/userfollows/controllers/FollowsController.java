package com.salmon.userfollows.controllers;

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

import com.salmon.schemas.data.UserFollows;

import com.salmon.userfollows.bindings.UserFollowsBinding;

@RestController
public class FollowsController
{
	private static final Logger LOG = LoggerFactory.getLogger(FollowsController.class);

	private final QueryableStoreRegistry queryableStoreRegistry;

	@Autowired
	public FollowsController(final QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	@GetMapping(path = "/follows/user-by-id/{id}")
	public UserFollows getTweetsByUser(@PathVariable("id") final String uuid)
	{
		final ReadOnlyKeyValueStore<UUID, UserFollows> store = queryableStoreRegistry.getQueryableStoreType(UserFollowsBinding.USER_FOLLOWS, QueryableStoreTypes.keyValueStore());

		store.all().forEachRemaining(entry -> LOG.info("Entry: {}, {}", entry.key, entry.value));

		return store.get(UUID.fromString(uuid));
	}
}
