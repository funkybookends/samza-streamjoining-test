// package com.salmon.userservice.processors;
//
// import org.apache.kafka.common.serialization.Serdes;
// import org.apache.kafka.streams.kstream.KTable;
// import org.apache.kafka.streams.kstream.Materialized;
// import org.apache.kafka.streams.kstream.Serialized;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cloud.stream.annotation.Input;
// import org.springframework.cloud.stream.annotation.StreamListener;
// import org.springframework.stereotype.Component;
//
// import com.salmon.userservice.bindings.AnalyticsBinding;
// import com.salmon.userservice.data.UserData;
// import com.salmon.userservice.serde.UserDataSerde;
//
// @Component
// public class UsersTableProvider
// {
// 	private static final Logger LOG = LoggerFactory.getLogger(UsersTableProvider.class);
//
// 	@StreamListener
// 	public void mat(@Input(AnalyticsBinding.USERS_IN) KTable<String, UserData> usersTable) throws Exception
// 	{
// 		usersTable.
// 		LOG.info("UsersTable: {}, {}", usersTable, usersTable.queryableStoreName());
//
// 		usersTable.toStream()
// 			.groupByKey(Serialized.with(Serdes.String(), new UserDataSerde()))
// 			.reduce(this::reduce, Materialized.as(AnalyticsBinding.USERS_MV));
// 	}
//
// 	private UserData reduce(final UserData userData, final UserData userData1)
// 	{
// 		return userData1;
// 	}
// }
