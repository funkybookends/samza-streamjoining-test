package com.salmon.eventproducer.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.salmon.eventproducer.bindings.EventProducerBinding;
import com.salmon.schemas.data.FollowRequest;
import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;
import com.thedeanda.lorem.LoremIpsum;

@Component
public class UserRunner implements ApplicationRunner
{
	private static final Logger LOG = LoggerFactory.getLogger(UserRunner.class);

	private static final List<String> FIRST_NAME = Arrays.asList("Caspar", "James", "Tomo", "Christian", "Richard", "Miriam", "DDS", "Nick", "Anna");
	private static final List<String> SURNAME = Arrays.asList("Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliet", "Kilo");
	private static final List<String> USER_TYPES = Arrays.asList("Recruiter", "Candidate");

	private static final List<UserData> USERS = Collections.synchronizedList(new ArrayList<>());
	private static final List<FollowRequest> FOLLOW_REQUESTS = Collections.synchronizedList(new ArrayList<>());

	private static final Random RANDOM = new Random();
	private final EventProducerBinding eventProducerBinding;

	@Autowired
	public UserRunner(final EventProducerBinding eventProducerBinding)
	{
		this.eventProducerBinding = eventProducerBinding;
		LOG.info("Created user runner");
	}

	@Override
	public void run(final ApplicationArguments args)
	{
		this.registerUser();
		this.registerUser();
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::registerUser, 1, 60, TimeUnit.SECONDS);
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::tweet, 1, 3, TimeUnit.SECONDS);
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::createFollow, 10, 5, TimeUnit.SECONDS);
	}

	private void registerUser()
	{
		final int firstNameIndex = RANDOM.nextInt(FIRST_NAME.size());
		final int surnameNameIndex = RANDOM.nextInt(SURNAME.size());
		final int typeIndex = firstNameIndex % USER_TYPES.size();

		final UserData userData = UserData.builder()
			.userId(UUID.randomUUID())
			.username((FIRST_NAME.get(firstNameIndex) + "_" + SURNAME.get(surnameNameIndex)).toLowerCase(Locale.ENGLISH))
			.userType(USER_TYPES.get(typeIndex))
			.build();

		for (final UserData user : USERS)
		{
			if (user.getUsername().equals(userData.getUsername()))
			{
				LOG.warn("User exits: breaking");
				return;
			}
		}

		try
		{
			Message<UserData> message = MessageBuilder.withPayload(userData)
				.setHeader(KafkaHeaders.MESSAGE_KEY, userData.getUserId().toString().getBytes())
				.build();

			eventProducerBinding.usersOut().send(message);

			USERS.add(userData);

			LOG.info("New {}", userData);
		}
		catch (final Exception exception)
		{
			LOG.warn("Error sending: {}", userData, exception);
		}
	}

	private void tweet()
	{
		final UserData user = USERS.get(RANDOM.nextInt(USERS.size()));

		final Tweet tweet = Tweet.builder()
			.tweetId(UUID.randomUUID())
			.userId(user.getUserId())
			.date(new Date())
			.text(LoremIpsum.getInstance().getWords(5, 10))
			.build();

		try
		{
			Message<Tweet> message = MessageBuilder.withPayload(tweet)
				.setHeader(KafkaHeaders.MESSAGE_KEY, tweet.getTweetId().toString().getBytes())
				.build();

			eventProducerBinding.pageViewsOut().send(message);

			LOG.info("User {} tweeted {}", user, tweet);
		}
		catch (final Exception exception)
		{
			LOG.warn("Error sending: {}", tweet, exception);
		}
	}

	private void createFollow()
	{
		final UserData first = USERS.get(RANDOM.nextInt(USERS.size()));
		final UserData second = USERS.get(RANDOM.nextInt(USERS.size()));

		if (first == second)
		{
			return;
		}

		for (final FollowRequest followRequest : FOLLOW_REQUESTS)
		{
			if (followRequest.same(first, second))
			{
				return;
			}
		}

		final FollowRequest followRequest = FollowRequest.builder()
			.followRequestId(UUID.randomUUID())
			.follower(first.getUserId())
			.follows(second.getUserId())
			.dateRequested(new Date())
			.build();

		try
		{
			Message<FollowRequest> message = MessageBuilder.withPayload(followRequest)
				.setHeader(KafkaHeaders.MESSAGE_KEY, followRequest.getFollowRequestId().toString().getBytes())
				.build();

			eventProducerBinding.followRequestsOut().send(message);

			LOG.info("User {} is following {}", first, second);
		}
		catch (final Exception exception)
		{
			LOG.warn("Error sending: {}", followRequest, exception);
		}
	}
}
