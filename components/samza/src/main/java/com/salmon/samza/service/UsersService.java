package com.salmon.samza.service;

import org.apache.samza.config.Config;
import org.apache.samza.storage.kv.KeyValueStore;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.task.InitableTask;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskContext;
import org.apache.samza.task.TaskCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salmon.samza.data.UserData;

public class UsersService implements StreamTask, InitableTask
{
	private static final Logger LOG = LoggerFactory.getLogger(UsersService.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private KeyValueStore<String, UserData> userDataTable;

	@Override
	public void init(final Config config,
	                 final TaskContext context) throws Exception
	{
		LOG.info("Initiated");
		userDataTable = (KeyValueStore<String, UserData>) context.getStore("users-store");
		LOG.info("User Table: {}", userDataTable);
	}

	@Override
	public void process(final IncomingMessageEnvelope envelope,
	                    final MessageCollector collector,
	                    final TaskCoordinator coordinator) throws Exception
	{
		LOG.info("Envelope: {}", envelope);
		final String userId = envelope.getKey().toString();
		final UserData userData = OBJECT_MAPPER.readValue(envelope.getMessage().toString(), UserData.class);

		LOG.info("{}", userData);
		userDataTable.put(userId, userData);
		LOG.info("Successfully put in table");
	}
}
