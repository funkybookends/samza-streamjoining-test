package com.salmon.tweetpusher.binding;

import java.util.UUID;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;

import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserFollowers;
import com.salmon.schemas.data.UserFollows;

public interface TweetPusherBinding
{
	String USER_FOLLOWERS_STORE = "followers-mv";
	String USER_FOLLOWS_STORE = "follows-mv";

	String USER_FOLLOWS = "follows-in";
	String USER_FOLLOWERS = "followers-in";
	String TWEETS = "tweets-in";

	@Input(USER_FOLLOWS)
	KTable<UUID, UserFollows> userFollowsIn();

	@Input(USER_FOLLOWERS)
	KTable<UUID, UserFollowers> userFollowersIn();

	@Input(TWEETS)
	KStream<UUID, Tweet> tweetsIn();
}
