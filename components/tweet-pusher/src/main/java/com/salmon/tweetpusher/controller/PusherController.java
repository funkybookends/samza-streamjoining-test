package com.salmon.tweetpusher.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salmon.tweetpusher.service.TweetSubscriberService;

@RestController
public class PusherController
{
	private static final Logger LOG = LoggerFactory.getLogger(PusherController.class);

	private final TweetSubscriberService subscriberService;

	@Autowired
	public PusherController(final TweetSubscriberService subscriberService)
	{
		this.subscriberService = subscriberService;
	}

	@PutMapping(path = "/subscribe/{id}/logging")
	public void loggingSubscribe(@PathVariable("id") final String id)
	{
		subscriberService.subscribe(UUID.fromString(id), tweet -> LOG.info("Tweet Notification for {}: {}", id, tweet));
	}

	@DeleteMapping(path = "/ubsubscribe/{id}")
	public void unsubscribe(@PathVariable("id") final String id)
	{
		subscriberService.unsubscribe(UUID.fromString(id));
	}
}
