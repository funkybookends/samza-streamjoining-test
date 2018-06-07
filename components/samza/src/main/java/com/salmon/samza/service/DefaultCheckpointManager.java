package com.salmon.samza.service;

import org.apache.samza.checkpoint.Checkpoint;
import org.apache.samza.checkpoint.CheckpointManager;
import org.apache.samza.container.TaskName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCheckpointManager implements CheckpointManager
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultCheckpointManager.class);

	@Override
	public void start()
	{
		LOG.info("start");
	}

	@Override
	public void register(final TaskName taskName)
	{
		LOG.info("register");
	}

	@Override
	public void writeCheckpoint(final TaskName taskName, final Checkpoint checkpoint)
	{
		LOG.info("writeCheckpoint");
	}

	@Override
	public Checkpoint readLastCheckpoint(final TaskName taskName)
	{
		LOG.info("readLastCheckpoint");
		return null;
	}

	@Override
	public void stop()
	{
		LOG.info("stop");
	}
}
