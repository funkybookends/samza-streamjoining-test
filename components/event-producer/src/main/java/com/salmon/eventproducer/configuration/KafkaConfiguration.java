package com.salmon.eventproducer.configuration;

import java.util.HashMap;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import static com.salmon.eventproducer.controller.UserController.USER_TOPIC;

@Configuration
@EnableKafka
public class KafkaConfiguration
{
	@Bean
	public NewTopic userTopic()
	{
		final HashMap<String, String> config = new HashMap<>();
		config.put("log.cleanup.policy", "compact");

		return new NewTopic(USER_TOPIC, 1, (short) 1).configs(config);
	}
}
