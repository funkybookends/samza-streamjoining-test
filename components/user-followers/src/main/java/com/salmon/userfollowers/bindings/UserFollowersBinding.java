package com.salmon.userfollowers.bindings;

import java.util.UUID;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.salmon.schemas.data.FollowRequest;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.data.UserFollowers;
import com.salmon.schemas.data.UserFollows;

public interface UserFollowersBinding
{
	String USER_FOLLOWERS = "user-followers-mv";

	String FOLLOWS_IN = "follows-in";
	@Input(FOLLOWS_IN) KStream<UUID, FollowRequest> followsIn();

	String USERS_IN = "users-in";
	@Input(USERS_IN) KTable<UUID, UserData> usersIn();

	String FOLLOWERS_OUT = "followers-out";
	@Output(FOLLOWERS_OUT) KStream<UUID, UserFollowers> followersOut();
}
