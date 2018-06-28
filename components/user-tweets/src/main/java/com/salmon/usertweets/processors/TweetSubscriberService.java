// package com.salmon.usertweets.processors;
//
// import java.util.Map;
// import java.util.UUID;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.function.Consumer;
//
// import org.apache.kafka.streams.kstream.KStream;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cloud.stream.annotation.Input;
// import org.springframework.cloud.stream.annotation.StreamListener;
// import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
// import org.springframework.stereotype.Component;
//
// import com.salmon.schemas.data.Tweet;
// import com.salmon.usertweets.bindings.UserTweetsBinding;
//
// @Component
// public class TweetSubscriberService
// {
// 	private final Map<UUID, Consumer<Tweet>> subscribedUsers = new ConcurrentHashMap<>();
//
// 	private final QueryableStoreRegistry queryableStoreRegistry;
//
// 	@Autowired
// 	public TweetSubscriberService(final QueryableStoreRegistry queryableStoreRegistry)
// 	{
// 		this.queryableStoreRegistry = queryableStoreRegistry;
// 	}
//
// 	@StreamListener
// 	public void process(@Input(UserTweetsBinding.TWEETS_IN) KStream<UUID, Tweet> tweetKStream)
// 	{
//
//
// 	}
//
// 	public void subscribe(UUID user, Consumer<Tweet> consumer)
// 	{
// 		subscribedUsers.put(user, consumer);
// 	}
//
// 	public void unsubscribe(UUID user)
// 	{
// 		subscribedUsers.remove(user);
// 	}
// }
