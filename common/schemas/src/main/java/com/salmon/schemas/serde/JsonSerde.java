package com.salmon.schemas.serde;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.salmon.schemas.data.EnrichedTweet;
import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;

import lombok.ToString;
import lombok.extern.java.Log;

@Log
@ToString
public class JsonSerde<T> implements Serde<T>
{
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
		.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
		.registerModule(new Jdk8Module())
		.registerModule(new JavaTimeModule())
		.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

	private final Class<T> type;

	private static final Map<Class<?>, JsonSerde<?>> SERDES = new HashMap<>();

	protected JsonSerde(final Class<T> type)
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
					log.warning("Null data for seralization " + this.toString() + " for topic: " + topic);
					return null;
				}

				try
				{
					log.fine("Serializing " + data.toString() + " for topic: " + topic);
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
					log.warning("Null data for deserialization" + this.toString() + " for topic: " + topic);
					return null;
				}

				try
				{
					final T value = OBJECT_MAPPER.readValue(data, type);

					log.fine("Deserialized: " + value.toString() + " for topic: " + topic);

					return value;
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

	public static class TweetSerde extends JsonSerde<Tweet>
	{
		public TweetSerde()
		{
			super(Tweet.class);
		}
	}

	public static class UserDataSerde extends JsonSerde<UserData>
	{
		public UserDataSerde()
		{
			super(UserData.class);
		}
	}

	public static class EnrichedTweetSerde extends JsonSerde<EnrichedTweet>
	{
		public EnrichedTweetSerde()
		{
			super(EnrichedTweet.class);
		}
	}
}
