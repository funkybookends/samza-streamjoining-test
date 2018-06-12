package com.salmon.userservice.processors;

import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.salmon.userservice.UserServiceApplication;
import com.salmon.userservice.bindings.AnalyticsBinding;

@Component
public class PageCountSink
{
	private static final Logger LOG = LoggerFactory.getLogger(PageCountSink.class);

	@StreamListener
	public void process(@Input(AnalyticsBinding.PAGE_COUNT_IN) KTable<String, Long> counts)
	{
		counts.toStream()
			.foreach((key, value) -> LOG.info("key: {}, value, {}", key, value));
	}

}
