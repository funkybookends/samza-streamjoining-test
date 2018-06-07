package com.salmon.samza.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData
{
	@JsonProperty("user_id") private final String userId;
	@JsonProperty("user_type") private final String userType;

	@JsonCreator
	public UserData(@JsonProperty("user_id") final String userId,
	                @JsonProperty("user_type") final String userType)
	{
		this.userId = userId;
		this.userType = userType;
	}

	public String getUserId()
	{
		return userId;
	}

	public String getUserType()
	{
		return userType;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final UserData userData = (UserData) o;
		return Objects.equals(userId, userData.userId) &&
			Objects.equals(userType, userData.userType);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(userId, userType);
	}
}
