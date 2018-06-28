package com.salmon.userfollowers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.salmon.userfollowers.bindings.UserFollowersBinding;


@SpringBootApplication
@EnableBinding(UserFollowersBinding.class)
public class TweetEnricherApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TweetEnricherApplication.class, args);
	}
}

