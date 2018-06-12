package com.salmon.userservice.processors;

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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.salmon.userservice.bindings.AnalyticsBinding;
import com.salmon.userservice.data.PageViewEvent;

@Component
public class PageViewEventSource implements ApplicationRunner
{
	private static final Logger LOG = LoggerFactory.getLogger(PageViewEventSource.class);
	private final MessageChannel pageViewsOut;

	@Autowired
	public PageViewEventSource(AnalyticsBinding binding)
	{
		LOG.info("Started PageViewEventSource");
		this.pageViewsOut = binding.pageViewsOut();
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception
	{
		LOG.info("Running PageViewEventSource");
		final List<String> names = Arrays.asList("Caspar", "James", "Tomo", "Christian", "Richard", "Miriam", "DDS", "Nick", "Anna");
		final List<String> pages = Arrays.asList("Blog", "About", "SiteMap", "views", "page", "news", "sport", "entertainment");

		final Runnable runnable = () ->
		{
			final Random random = new Random();
			final String name = names.get(random.nextInt(names.size()));
			final String page = pages.get(random.nextInt(pages.size()));

			final PageViewEvent pageViewEvent = new PageViewEvent(name, page, Math.random() > 0.5 ? 10 : 1000);

			final Message<PageViewEvent> message = MessageBuilder.withPayload(pageViewEvent)
				.setHeader(KafkaHeaders.MESSAGE_KEY, pageViewEvent.getUserId().getBytes())
				.build();

			try
			{
				this.pageViewsOut.send(message);
				// LOG.info("Sent: {}", pageViewEvent);
			}
			catch (final Exception exception)
			{
				exception.printStackTrace();
				LOG.warn("Error sending: {}", message, exception);
			}
		};

		Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 1, TimeUnit.SECONDS);
	}
}
