package com.salmon.userservice.processors;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.salmon.userservice.UserServiceApplication;
import com.salmon.userservice.bindings.AnalyticsBinding;
import com.salmon.userservice.data.PageViewEvent;

@Component
public class PageViewsEventProcessor
{
	private static final Logger LOG = LoggerFactory.getLogger(PageViewsEventProcessor.class);

	@StreamListener
	@SendTo(AnalyticsBinding.PAGE_COUNT_OUT)
	public KStream<String, Long> process(@Input(AnalyticsBinding.PAGE_VIEWS_IN) KStream<String, PageViewEvent> events)
	{
		return events
			.peek((key, value) -> LOG.info("Processed page event: {}", value))
			.filter((key, pageViewEvent) -> pageViewEvent.getDuration() >= 500)
			.map((userId, pageViewEvent) -> new KeyValue<>(pageViewEvent.getPage(), "0"))
			.groupByKey()
			.count(Materialized.as(AnalyticsBinding.PAGE_COUNT_MV))
			.toStream();
	}

}
