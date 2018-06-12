package com.salmon.userservice;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableBinding(AnalyticsBinding.class)
public class UserServiceApplication
{

	@Component
	public static class PageViewEventSource implements ApplicationRunner
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
					LOG.info("Sent: {}", pageViewEvent);
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

	@Component
	public static class PageViewsEventProcessor
	{
		private static final Logger LOG = LoggerFactory.getLogger(PageViewsEventProcessor.class);

		@StreamListener
		@SendTo(AnalyticsBinding.PAGE_COUNT_OUT)
		public KStream<String, Long> process(@Input(AnalyticsBinding.PAGE_VIEWS_IN) KStream<String, PageViewEvent> events)
		{
			return events
				.filter((key, pageViewEvent) -> pageViewEvent.getDuration() >= 500)
				.peek((key, pageViewEvent) -> LOG.info("Peeking at event: {}", pageViewEvent))
				.map((userId, pageViewEvent) -> new KeyValue<>(pageViewEvent.getPage(), "0"))
				.groupByKey()
				.count(Materialized.as(AnalyticsBinding.PAGE_COUNT_MV))
				.toStream();
		}

	}

	@Component
	public static class PageCountSink
	{
		private static final Logger LOG = LoggerFactory.getLogger(PageCountSink.class);

		@StreamListener
		public void process(@Input(AnalyticsBinding.PAGE_COUNT_IN) KTable<String, Long> counts)
		{
			counts.toStream()
				.foreach((key, value) -> LOG.info("key: {}, value, {}", key, value));
		}

	}

	public static void main(String[] args)
	{
		SpringApplication.run(UserServiceApplication.class, args);
	}
}

interface AnalyticsBinding
{
	String PAGE_VIEWS_OUT = "pvout";
	String PAGE_VIEWS_IN = "pvin";
	String PAGE_COUNT_MV = "pcmv";
	String PAGE_COUNT_OUT = "pcout";
	String PAGE_COUNT_IN = "pcin";

	@Input(PAGE_VIEWS_IN)
	KStream<String, PageViewEvent> pageViewsIn();

	@Output(PAGE_VIEWS_OUT)
	MessageChannel pageViewsOut();

	@Input(PAGE_COUNT_IN)
	KTable<String, Long> pageCountIn();

	@Output(PAGE_COUNT_OUT)
	KStream<String, Long> pageCountOut();
}

class PageViewEvent
{
	private String userId;
	private String page;
	private long duration;

	public PageViewEvent(final String userId, final String page, final long duration)
	{
		this.userId = userId;
		this.page = page;
		this.duration = duration;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	public String getPage()
	{
		return page;
	}

	public void setPage(final String page)
	{
		this.page = page;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(final long duration)
	{
		this.duration = duration;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final PageViewEvent that = (PageViewEvent) o;
		return duration == that.duration &&
			Objects.equals(userId, that.userId) &&
			Objects.equals(page, that.page);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(userId, page, duration);
	}

	@Override
	public String toString()
	{
		return "PageViewEvent{" +
			"userId='" + userId + '\'' +
			", page='" + page + '\'' +
			", duration=" + duration +
			'}';
	}
}