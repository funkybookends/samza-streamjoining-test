package com.salmon.eventproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import com.salmon.eventproducer.bindings.EventProducerBinding;

@SpringBootApplication
@EnableBinding(EventProducerBinding.class)
public class Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}
