package com.salmon.userfollows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.salmon.userfollows.bindings.UserFollowsBinding;

@SpringBootApplication
@EnableBinding(UserFollowsBinding.class)
public class TweetEnricherApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TweetEnricherApplication.class, args);
	}
}

