package com.salmon.schemas.data;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import lombok.extern.java.Log;

@Value
@AllArgsConstructor
@Builder
@Log
@Wither
public class UserData
{
	UUID userId;
	String username;
	String userType;

	public static UserData merge(final UserData left, final UserData right)
	{
		log.info("Merging: " + left.toString() + " with " + right.toString());

		assert left.userId.equals(right.userId);

		return UserData.builder()
			.userId(left.userId)
			.username(left.username)
			.build();
	}
}
