package com.salmon.eventproducer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmon.eventproducer.data.UserData;

@RestController
public class UserController
{
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	public static final String USER_TOPIC = "user.data.byuserid";

	private final KafkaTemplate<String, String> kafkaTemplate;

	public UserController(final KafkaTemplate<String, String> kafkaTemplate)
	{
		this.kafkaTemplate = kafkaTemplate;
	}

	@PostMapping("/user/{user_id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateUser(@PathVariable("user_id") final String userId,
	                       @RequestBody final UserData userData) throws JsonProcessingException
	{
		kafkaTemplate.send(USER_TOPIC, userId, OBJECT_MAPPER.writeValueAsString(userData));
	}
}
