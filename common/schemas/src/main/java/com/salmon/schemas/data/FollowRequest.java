package com.salmon.schemas.data;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Value
@Builder
public class FollowRequest
{
	UUID followRequestId;
	UUID follower;
	UUID follows;
	Date dateRequested;

	public boolean same(final UserData follower, final UserData follows)
	{
		return this.follower.equals(follower.getUserId()) && this.follows.equals(follows.getUserId());
	}
}
