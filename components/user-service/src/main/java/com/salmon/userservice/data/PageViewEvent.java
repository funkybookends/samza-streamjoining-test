package com.salmon.userservice.data;

import java.util.Objects;

public class PageViewEvent
{
	private String userId;
	private String page;
	private long duration;

	public PageViewEvent(final String userId, final String page, final long duration)
	{
		this.userId = userId;
		this.page = page;
		this.duration = duration;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(final String userId)
	{
		this.userId = userId;
	}

	public String getPage()
	{
		return page;
	}

	public void setPage(final String page)
	{
		this.page = page;
	}

	public long getDuration()
	{
		return duration;
	}

	public void setDuration(final long duration)
	{
		this.duration = duration;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final PageViewEvent that = (PageViewEvent) o;
		return duration == that.duration &&
			Objects.equals(userId, that.userId) &&
			Objects.equals(page, that.page);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(userId, page, duration);
	}

	@Override
	public String toString()
	{
		return "PageViewEvent{" +
			"userId='" + userId + '\'' +
			", page='" + page + '\'' +
			", duration=" + duration +
			'}';
	}
}
