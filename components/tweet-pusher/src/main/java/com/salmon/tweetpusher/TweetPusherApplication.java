package com.salmon.tweetpusher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.salmon.tweetpusher.binding.TweetPusherBinding;


@SpringBootApplication
@EnableBinding(TweetPusherBinding.class)
public class TweetPusherApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TweetPusherApplication.class, args);
	}
}

