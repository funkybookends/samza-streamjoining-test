package com.salmon.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.salmon.userservice.bindings.AnalyticsBinding;

@SpringBootApplication
@EnableBinding(AnalyticsBinding.class)
public class UserServiceApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(UserServiceApplication.class, args);
	}
}

