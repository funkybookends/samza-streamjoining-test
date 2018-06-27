package com.salmon.tweetenricher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.salmon.tweetenricher.bindings.AnalyticsBinding;

@SpringBootApplication
@EnableBinding(AnalyticsBinding.class)
public class TweetEnricherApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TweetEnricherApplication.class, args);
	}
}

