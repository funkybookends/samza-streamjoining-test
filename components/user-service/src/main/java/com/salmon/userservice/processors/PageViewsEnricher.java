package com.salmon.userservice.processors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.salmon.userservice.bindings.AnalyticsBinding;
import com.salmon.userservice.data.EnrichedPageViewEvent;
import com.salmon.userservice.data.PageViewEvent;
import com.salmon.userservice.data.UserData;
import com.salmon.userservice.serde.PageViewEventSerde;
import com.salmon.userservice.serde.UserDataSerde;

@Component
public class PageViewsEnricher
{
	private static final Logger LOG = LoggerFactory.getLogger(PageViewsEnricher.class);

	private Joined<String, PageViewEvent, UserData> joined = Joined.with(Serdes.String(), new PageViewEventSerde(), new UserDataSerde());

	@StreamListener
	@SendTo(AnalyticsBinding.ENRICHED_PAGE_VIEWS_OUT)
	public KStream<String, EnrichedPageViewEvent> process(@Input(AnalyticsBinding.PAGE_VIEWS_IN) KStream<String, PageViewEvent> pageViewEvents,
	                                                      @Input(AnalyticsBinding.USERS_IN) KTable<String, UserData> usersTable)
	{
		LOG.info("Creating join");
		return pageViewEvents.peek((userId, pageViewEvent) -> LOG.info("Processing {}:{}", userId, pageViewEvent))
			.join(usersTable, this::join, this.joined);
	}

	private EnrichedPageViewEvent join(PageViewEvent pageView, UserData user)
	{
		LOG.info("Joining pageView: {} with user: {}", pageView, user);
		return new EnrichedPageViewEvent(pageView.getUserId(), user.getUserType(), pageView.getPage(), pageView.getDuration());
	}

}
