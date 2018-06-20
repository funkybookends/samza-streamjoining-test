package com.salmon.userservice.serde;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmon.userservice.data.PageViewEvent;

public class PageViewEventSerde implements Serde<PageViewEvent>
{
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey)
	{

	}

	@Override
	public void close()
	{

	}

	@Override
	public Serializer<PageViewEvent> serializer()
	{
		return new Serializer<PageViewEvent>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public byte[] serialize(final String topic, final PageViewEvent data)
			{
				if (data == null)
				{
					return null;
				}

				try
				{
					return OBJECT_MAPPER.writeValueAsBytes(data);
				}
				catch (JsonProcessingException e)
				{
					throw new RuntimeException(e);
				}
			}

			@Override
			public void close()
			{

			}
		};
	}

	@Override
	public Deserializer<PageViewEvent> deserializer()
	{
		return new Deserializer<PageViewEvent>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public PageViewEvent deserialize(final String topic, final byte[] data)
			{
				if (data == null)
				{
					return null;
				}

				try
				{
					return OBJECT_MAPPER.readValue(data, PageViewEvent.class);
				}
				catch (IOException e)
				{
					throw new UncheckedIOException(e);
				}
			}

			@Override
			public void close()
			{

			}
		};
	}
}
