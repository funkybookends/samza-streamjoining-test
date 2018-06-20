package com.salmon.userservice.controllers;

import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.userservice.bindings.AnalyticsBinding;
import com.salmon.userservice.data.UserData;

@RestController
public class UsersController
{
	private final QueryableStoreRegistry queryableStoreRegistry;

	@Autowired
	public UsersController(final QueryableStoreRegistry queryableStoreRegistry)
	{
		this.queryableStoreRegistry = queryableStoreRegistry;
	}

	@GetMapping(path = "/user/{id}")
	public UserData getUserData(final @PathVariable("id") String userId)
	{
		final ReadOnlyKeyValueStore<String, UserData> store = queryableStoreRegistry.getQueryableStoreType(AnalyticsBinding.USERS_MV, QueryableStoreTypes.keyValueStore());
		return store.get(userId);
	}
}
