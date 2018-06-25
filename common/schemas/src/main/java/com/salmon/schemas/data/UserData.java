package com.salmon.schemas.data;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.java.Log;

@Value
@AllArgsConstructor
@Builder
@Log
public class UserData
{
	UUID userId;
	String username;
	String userType;
	long usageTime;
	Date registrationDate;
	Date lastVisitedDate;
	Date lastTweetDate;
	Set<UUID> tweets;
	Set<UUID> following;

	public static UserData merge(final UserData left, final UserData right)
	{
		log.info("Merging: " + left.toString() + " with " + right.toString());

		assert left.userId.equals(right.userId);

		return UserData.builder()
			.userId(left.userId)
			.username(left.username)
			.usageTime(left.usageTime + right.usageTime)
			.registrationDate(left.registrationDate)
			.lastVisitedDate(left.lastVisitedDate.toInstant().isAfter(right.lastVisitedDate.toInstant()) ? left.lastVisitedDate : right.lastVisitedDate)
			.lastTweetDate(left.lastTweetDate.toInstant().isAfter(right.lastTweetDate.toInstant()) ? left.lastTweetDate : right.lastTweetDate)
			.tweets(left.tweets)
			.tweets(right.tweets)
			.following(left.following)
			.following(right.following)
			.build();
	}
}
