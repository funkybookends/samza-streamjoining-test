package usertweets.bindings;

import java.util.UUID;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;

import com.salmon.schemas.data.Tweet;
import com.salmon.schemas.data.UserData;

public interface UserTweetsBinding
{
	String TWEETS_IN = "tweets-in";
	String TWEETS_STORE = "tweets-mv";

	@Input(TWEETS_IN) KStream<UUID, Tweet> tweetsIn();

	String USERS_IN = "users-in";
	@Input(USERS_IN) KTable<UUID, UserData> usersIn();
}
