package com.salmon.userservice.serde;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmon.userservice.data.UserData;

public class UserDataSerde implements Serde<UserData>
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
	public Serializer<UserData> serializer()
	{
		return new Serializer<UserData>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public byte[] serialize(final String topic, final UserData data)
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
	public Deserializer<UserData> deserializer()
	{
		return new Deserializer<UserData>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public UserData deserialize(final String topic, final byte[] data)
			{
				if (data == null)
				{
					return null;
				}

				try
				{
					return OBJECT_MAPPER.readValue(data, UserData.class);
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
