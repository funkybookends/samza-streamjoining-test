package usertweets.processors;

import java.util.UUID;

import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Serialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;
import com.salmon.schemas.data.UserTweets;
import com.salmon.schemas.serde.JsonSerde;
import com.salmon.schemas.serde.UUIDSerde;

import usertweets.bindings.UserTweetsBinding;

@Component
public class UserTweetsJoiner
{
	private static final Logger LOG = LoggerFactory.getLogger(UserTweetsJoiner.class);

	private static final UUIDSerde UUID_SERDE = new UUIDSerde();

	private Joined<UUID, Tweet, UserData> userJoinger = Joined.with(UUID_SERDE,
		JsonSerde.forClass(Tweet.class),
		JsonSerde.forClass(UserData.class));

	@StreamListener
	public void createUserTweets(@Input(UserTweetsBinding.TWEETS_IN) KStream<UUID, Tweet> tweetsStream,
	                             @Input(UserTweetsBinding.USERS_IN) KTable<UUID, UserData> usersTable)
	{
		tweetsStream
			.peek((tweetId, tweet) -> LOG.info("Received tweet: {}", tweet))
			.selectKey((tweetId, tweet) -> tweet.getUserId())
			.join(usersTable, UserTweets::enrich, userJoinger)
			.groupByKey(Serialized.with(UUID_SERDE, JsonSerde.forClass(UserTweets.class)))
			.reduce(UserTweets::reduce, UserTweetsBinding.TWEETS_STORE)
			.toStream()
			.peek((userId, userTweets) -> LOG.info("Updated: {}", userTweets));
	}
}
