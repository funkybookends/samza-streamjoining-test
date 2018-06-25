package com.salmon.eventproducer.bindings;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AnalyticsBinding
{
	String TWEETS_OUT = "tweets-out";

	@Output(TWEETS_OUT)
	MessageChannel pageViewsOut();

	String USERS_OUT = "users-out";

	@Output(USERS_OUT)
	MessageChannel usersOut();
}
