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
public class UserFollows
{
	UUID userId;
	@Singular("follows")
	Set<UUID> following;

	public static UserFollows enrich(final FollowRequest followRequest, final UserData userData)
	{
		return UserFollows.builder()
			.userId(userData.getUserId())
			.follows(followRequest.getFollows())
			.build();
	}

	public static UserFollows reduce(final UserFollows left, final UserFollows right)
	{
		return UserFollows.builder()
			.userId(left.getUserId())
			.following(left.getFollowing())
			.following(right.getFollowing())
			.build();
	}
}
