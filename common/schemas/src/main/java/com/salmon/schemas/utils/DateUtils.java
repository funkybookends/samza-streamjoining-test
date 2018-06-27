package com.salmon.schemas.utils;

import java.util.Date;

public final class DateUtils
{
	public static Date latest(final Date first, final Date second)
	{
		return first.after(second) ? first : second;
	}
}
