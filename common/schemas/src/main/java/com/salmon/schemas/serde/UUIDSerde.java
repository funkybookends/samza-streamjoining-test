package com.salmon.schemas.serde;

import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import lombok.extern.java.Log;

@Log
public class UUIDSerde implements Serde<UUID>
{
	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey)
	{

	}

	@Override
	public void close()
	{

	}

	@Override
	public Serializer<UUID> serializer()
	{
		return new Serializer<UUID>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public byte[] serialize(final String topic, final UUID data)
			{
				if (data == null)
				{
					return null;
				}
				log.fine("Serializing UUID: " + data.toString() + " for topic: " + topic);
				return data.toString().getBytes();
			}

			@Override
			public void close()
			{

			}
		};
	}

	@Override
	public Deserializer<UUID> deserializer()
	{
		return new Deserializer<UUID>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public UUID deserialize(final String topic, final byte[] data)
			{
				if (data == null)
				{
					return null;
				}
				final String uuidString = new String(data);
				log.fine("Deserializing UUID: " + uuidString + " for topic: " + topic);
				return UUID.fromString(uuidString);
			}

			@Override
			public void close()
			{

			}
		};
	}
}
