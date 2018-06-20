package com.salmon.userservice.bindings;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.salmon.userservice.data.EnrichedPageViewEvent;
import com.salmon.userservice.data.PageViewEvent;
import com.salmon.userservice.data.UserData;

public interface AnalyticsBinding
{
	String PAGE_VIEWS_IN = "pageviewsin";
	@Input(PAGE_VIEWS_IN)
	KStream<String, PageViewEvent> pageViewsIn();

	String USERS_IN = "users";
	@Input(USERS_IN)
	KTable<String, UserData> usersIn();

	String ENRICHED_PAGE_VIEWS_OUT = "enrichedpageviewsout";
	@Output(ENRICHED_PAGE_VIEWS_OUT)
	KStream<String, EnrichedPageViewEvent> enrichedPageViewsOut();
}
