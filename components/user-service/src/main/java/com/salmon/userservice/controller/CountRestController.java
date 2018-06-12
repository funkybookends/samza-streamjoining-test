package com.salmon.userservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.userservice.bindings.AnalyticsBinding;

@RestController
public class CountRestController
{
	private final QueryableStoreRegistry registry;

	@Autowired
	public CountRestController(final QueryableStoreRegistry registry)
	{
		this.registry = registry;
	}

	@GetMapping("/counts")
	public Map<String, Long> counts()
	{
		final HashMap<String, Long> result = new HashMap<>();

		final ReadOnlyKeyValueStore<String, Long> store = this.registry.getQueryableStoreType(AnalyticsBinding.PAGE_COUNT_MV, QueryableStoreTypes.keyValueStore());

		final KeyValueIterator<String, Long> all = store.all();

		while (all.hasNext())
		{
			final KeyValue<String, Long> next = all.next();
			result.put(next.key, next.value);
		}

		return result;
	}

}
