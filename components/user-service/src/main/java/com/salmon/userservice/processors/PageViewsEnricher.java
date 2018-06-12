package com.salmon.userservice.processors;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.salmon.userservice.bindings.AnalyticsBinding;
import com.salmon.userservice.data.EnrichedPageViewEvent;
import com.salmon.userservice.data.PageViewEvent;
import com.salmon.userservice.data.UserData;

@Component
public class PageViewsEnricher
{
	private static final Logger LOG = LoggerFactory.getLogger(PageViewsEnricher.class);

	@StreamListener
	@SendTo(AnalyticsBinding.ENRICHED_PAGE_VIEWS_OUT)
	public KStream<String, EnrichedPageViewEvent> process(@Input(AnalyticsBinding.PAGE_VIEWS_IN) KStream<String, PageViewEvent> pageViewEvents,
	                                                      @Input(AnalyticsBinding.USERS_IN) KTable<String, UserData> usersTable)
	{
		LOG.info("Creating join");
		return pageViewEvents.join(usersTable, this::join);
	}

	private EnrichedPageViewEvent join(PageViewEvent pageView, UserData user)
	{
		LOG.info("Joining pageView: {} with user: {}", pageView, user);
		return new EnrichedPageViewEvent(pageView.getUserId(), user.getUserType(), pageView.getPage(), pageView.getDuration());
	}

}
