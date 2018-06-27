package usertweets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import usertweets.bindings.UserTweetsBinding;

@SpringBootApplication
@EnableBinding(UserTweetsBinding.class)
public class TweetEnricherApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TweetEnricherApplication.class, args);
	}
}

