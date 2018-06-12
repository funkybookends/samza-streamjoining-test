package com.salmon.userservice.data;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrichedPageViewEvent
{
	@JsonProperty("user_id") private final String userId;
	@JsonProperty("user_type") private final String userType;
	@JsonProperty("page_id") private final String pageId;
	@JsonProperty("duration_ms") private final long duration;

	@JsonCreator
	public EnrichedPageViewEvent(@JsonProperty("user_id") final String userId,
	                             @JsonProperty("user_type") final String userType,
	                             @JsonProperty("page_id") final String pageId,
	                             @JsonProperty("duration_ms") final long duration)
	{
		this.userId = userId;
		this.userType = userType;
		this.pageId = pageId;
		this.duration = duration;
	}

	public String getUserId()
	{
		return userId;
	}

	public String getUserType()
	{
		return userType;
	}

	public String getPageId()
	{
		return pageId;
	}

	public long getDuration()
	{
		return duration;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final EnrichedPageViewEvent that = (EnrichedPageViewEvent) o;
		return duration == that.duration &&
			Objects.equals(userId, that.userId) &&
			Objects.equals(userType, that.userType) &&
			Objects.equals(pageId, that.pageId);
	}

	@Override
	public int hashCode()
	{

		return Objects.hash(userId, userType, pageId, duration);
	}

	@Override
	public String toString()
	{
		return "EnrichedPageViewEvent{" +
			"userId='" + userId + '\'' +
			", userType='" + userType + '\'' +
			", pageId='" + pageId + '\'' +
			", duration=" + duration +
			'}';
	}
}
