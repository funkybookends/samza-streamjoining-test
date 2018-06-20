package com.salmon.eventproducer.runner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
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

import com.salmon.eventproducer.bindings.AnalyticsBinding;
import com.salmon.eventproducer.data.PageViewEvent;
import com.salmon.eventproducer.data.UserData;

@Component
public class UserRunner implements ApplicationRunner
{
	private static final Logger LOG = LoggerFactory.getLogger(UserRunner.class);

	public static final List<String> NAMES = Arrays.asList("Caspar", "James", "Tomo", "Christian", "Richard", "Miriam", "DDS", "Nick", "Anna");
	public static final List<String> PAGES = Arrays.asList("Blog", "About", "SiteMap", "views", "page", "news", "sport", "entertainment");
	private static final List<String> USER_TYPES = Arrays.asList("Recruiter", "Candidate");

	private static final Random RANDOM = new Random();
	private final AnalyticsBinding analyticsBinding;

	@Autowired
	public UserRunner(final AnalyticsBinding analyticsBinding)
	{
		this.analyticsBinding = analyticsBinding;
		LOG.info("Created user runner");
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception
	{
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::createUser, 1, 3, TimeUnit.SECONDS);
		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::createView, 1, 1, TimeUnit.SECONDS);
	}

	private void createUser()
	{
		final int userNameIndex = RANDOM.nextInt(NAMES.size());
		final int typeIndex = userNameIndex % USER_TYPES.size();

		final String name = NAMES.get(userNameIndex);
		final String userType = USER_TYPES.get(typeIndex);

		final UserData userData = new UserData(name, userType);

		try
		{
			Message<UserData> message = MessageBuilder.withPayload(userData)
				.setHeader(KafkaHeaders.MESSAGE_KEY, userData.getUserId().getBytes())
				.build();

			analyticsBinding.usersOut().send(message);

			LOG.info("Sent user Data: {}", userData);
		}
		catch (final Exception exception)
		{
			LOG.warn("Error sending: {}", userData, exception);
		}
	}

	private void createView()
	{
		final String page = PAGES.get(RANDOM.nextInt(PAGES.size()));
		final String userId = NAMES.get(RANDOM.nextInt(NAMES.size()));

		final PageViewEvent pageViewEvent = new PageViewEvent(userId, page, Math.random() > 0.5 ? 10 : 1000);

		try
		{
			Message<PageViewEvent> message = MessageBuilder.withPayload(pageViewEvent)
				.setHeader(KafkaHeaders.MESSAGE_KEY, pageViewEvent.getUserId().getBytes())
				.build();

			analyticsBinding.pageViewsOut().send(message);

			LOG.info("Sent: {}", pageViewEvent);
		}
		catch (final Exception exception)
		{
			LOG.warn("Error sending: {}", pageViewEvent, exception);
		}
	}
}
