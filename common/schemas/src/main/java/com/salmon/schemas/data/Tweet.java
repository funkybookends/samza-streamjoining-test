package com.salmon.schemas.data;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Tweet
{
	UUID tweetId;
	UUID userId;
	Date date;
	String text;
}
