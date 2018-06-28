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
public class UserFollowers
{
	UUID userId;
	@Singular
	Set<UUID> followers;

	public static UserFollowers enrich(final FollowRequest followRequest, final UserData userData)
	{
		return UserFollowers.builder()
			.userId(userData.getUserId())
			.follower(followRequest.getFollower())
			.build();
	}

	public static UserFollowers reduce(final UserFollowers left, final UserFollowers right)
	{
		return UserFollowers.builder()
			.userId(left.getUserId())
			.followers(left.getFollowers())
			.followers(right.getFollowers())
			.build();
	}
}
