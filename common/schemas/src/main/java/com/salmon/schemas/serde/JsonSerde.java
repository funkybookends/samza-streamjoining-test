package com.salmon.schemas.serde;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Log
public class JsonSerde<T> implements Serde<T>
{
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final Class<T> type;

	private static final Map<Class<?>, JsonSerde<?>> SERDES = new HashMap<>();

	private JsonSerde(final Class<T> type)
	{
		this.type = type;
	}

	public static <T> JsonSerde<T> forClass(final Class<T> clazz)
	{
		return (JsonSerde<T>) SERDES.computeIfAbsent(clazz, JsonSerde::new);
	}

	@Override
	public void configure(final Map<String, ?> configs, final boolean isKey)
	{

	}

	@Override
	public void close()
	{

	}

	@Override
	public Serializer<T> serializer()
	{
		return new Serializer<T>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public byte[] serialize(final String topic, final T data)
			{
				if (data == null)
				{
					log.warning("Null data for seralization " + this.toString());
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
	public Deserializer<T> deserializer()
	{
		return new Deserializer<T>()
		{
			@Override
			public void configure(final Map<String, ?> configs, final boolean isKey)
			{

			}

			@Override
			public T deserialize(final String topic, final byte[] data)
			{
				if (data == null)
				{
					log.warning("Null data for deserialization" + this.toString());
					return null;
				}

				try
				{
					return OBJECT_MAPPER.readValue(data, type);
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

	@Override
	public String toString()
	{
		return "JsonSerde{" +
			"type=" + type +
			'}';
	}
}
