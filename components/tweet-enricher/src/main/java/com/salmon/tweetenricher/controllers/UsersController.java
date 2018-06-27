package com.salmon.tweetenricher.controllers;

import java.util.UUID;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.schemas.data.UserData;

@RestController
public class UsersController
{
	private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);

	private static final String USERS_IN_MATERIALIZED_AS_PROPERTY = "${spring.cloud.stream.kafka.streams.bindings.users-in.consumer.materialized-as}";

	private final QueryableStoreRegistry queryableStoreRegistry;
	private final String storeName;

	@Autowired
	public UsersController(final QueryableStoreRegistry queryableStoreRegistry,
	                       @Value(USERS_IN_MATERIALIZED_AS_PROPERTY) final String storeName)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
		this.storeName = storeName;
		LOG.info("Store Name: {}", storeName);
	}

	@GetMapping(path = "/user/by-uuid/{id}")
	public UserData getUserData(final @PathVariable("id") String userId)
	{
		final ReadOnlyKeyValueStore<UUID, UserData> store = queryableStoreRegistry.getQueryableStoreType(storeName,QueryableStoreTypes.keyValueStore());

		store.all().forEachRemaining(entry -> LOG.info("Entry: {}, {}", entry.key, entry.value));

		return store.get(UUID.fromString(userId));
	}
}
