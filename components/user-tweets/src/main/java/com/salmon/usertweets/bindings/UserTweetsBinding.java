package com.salmon.usertweets.bindings;

import java.util.UUID;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;

import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.data.UserFollowers;
import com.salmon.schemas.data.UserFollows;

public interface UserTweetsBinding
{
	String TWEETS_IN = "tweets-in";
	String TWEETS_STORE = "tweets-mv";
	String FOLLOWS_STORE = "follows-mv";
	String USER_FOLLOWERS_MV = "followers-mv";

	@Input(TWEETS_IN) KStream<UUID, Tweet> tweetsIn();

	String USERS_IN = "users-in";
	@Input(USERS_IN) KTable<UUID, UserData> usersIn();

	String USER_FOLLOWS_IN = "follows-in";
	@Input(USER_FOLLOWS_IN) KTable<UUID, UserFollows> userFollowsIn();

	String USER_FOLLOWERS_IN = "followers-in";
	@Input(USER_FOLLOWERS_IN) KTable<UUID, UserFollowers> userFollowersIn();
}
