package com.salmon.userservice.controllers;

import java.util.UUID;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.schemas.data.UserData;
import com.salmon.userservice.bindings.AnalyticsBinding;

@RestController
public class UsersController
{
	private final QueryableStoreRegistry queryableStoreRegistry;

	@Autowired
	public UsersController(final QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	@GetMapping(path = "/user/by-uuid/{id}")
	public UserData getUserData(final @PathVariable("id") String userId)
	{
		final ReadOnlyKeyValueStore<UUID, UserData> store = queryableStoreRegistry.getQueryableStoreType(AnalyticsBinding.USERS_MV, QueryableStoreTypes.keyValueStore());
		return store.get(UUID.fromString(userId));
	}
}
