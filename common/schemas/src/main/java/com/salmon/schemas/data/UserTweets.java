package com.salmon.schemas.data;

import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class UserTweets
{
	UUID userId;
	@Singular Set<Tweet> tweets;

	public static UserTweets enrich(final Tweet tweet, final UserData userData)
	{
		return UserTweets.builder()
			.userId(userData.getUserId())
			.tweet(tweet)
			.build();
	}

	public static UserTweets reduce(final UserTweets left, final UserTweets right)
	{
		return UserTweets.builder()
			.userId(left.getUserId())
			.tweets(left.getTweets())
			.tweets(right.getTweets())
			.build();
	}
}
