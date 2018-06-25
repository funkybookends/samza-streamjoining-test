package com.salmon.schemas.data;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.java.Log;

@Value
@AllArgsConstructor
@Builder
@Log
public class EnrichedTweet
{
	UUID tweetId;
	UUID userId;
	String text;
	String userType;
	String username;
	Date tweetDate;

	public static EnrichedTweet enrich(final Tweet tweet, final UserData user)
	{
		log.info("Enriching " + tweet.toString() + " with " + user.toString());

		return EnrichedTweet.builder()
			.tweetId(tweet.getTweetId())
			.userId(user.getUserId())
			.text(tweet.getText())
			.userType(user.getUserType())
			.username(user.getUsername())
			.tweetDate(tweet.getDate())
			.build();
	}
}
