package com.salmon.samza.service;

import org.apache.samza.application.StreamApplication;
import org.apache.samza.config.Config;
import org.apache.samza.operators.StreamGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStreamApplication implements StreamApplication
{
	private static final Logger LOG = LoggerFactory.getLogger(UserStreamApplication.class);

	@Override
	public void init(final StreamGraph graph,
	                 final Config config)
	{
		LOG.info("{}", config);
		LOG.info("{}", graph);
	}
}
