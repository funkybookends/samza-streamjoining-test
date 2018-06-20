package com.salmon.userservice.serde;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmon.userservice.data.EnrichedPageViewEvent;

public class EnrichedPageViewEventSerde implements Serde<EnrichedPageViewEvent>
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
	public Serializer<EnrichedPageViewEvent> serializer()
	{
		return new Serializer<EnrichedPageViewEvent>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public byte[] serialize(final String topic, final EnrichedPageViewEvent data)
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
	public Deserializer<EnrichedPageViewEvent> deserializer()
	{
		return new Deserializer<EnrichedPageViewEvent>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public EnrichedPageViewEvent deserialize(final String topic, final byte[] data)
			{
				if (data == null)
				{
					return null;
				}

				try
				{
					return OBJECT_MAPPER.readValue(data, EnrichedPageViewEvent.class);
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
