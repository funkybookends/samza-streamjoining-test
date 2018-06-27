package com.salmon.userfollows.bindings;

import java.util.UUID;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

import com.salmon.schemas.data.FollowRequest;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.data.UserFollows;

public interface UserFollowsBinding
{
	String USER_FOLLOWS = "user-follows-mv";

	String FOLLOWS_IN = "follows-in";
	@Input(FOLLOWS_IN) KStream<UUID, FollowRequest> followsIn();

	String USERS_IN = "users-in";
	@Input(USERS_IN) KTable<UUID, UserData> usersIn();

	String FOLLOWS_OUT = "follows-out";
	@Output(FOLLOWS_OUT) KStream<UUID, UserFollows> followsOut();
}
