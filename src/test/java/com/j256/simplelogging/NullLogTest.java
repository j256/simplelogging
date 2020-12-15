package com.j256.simplelogging;

import org.junit.Test;

import com.j256.simplelogging.Logger;
import com.j256.simplelogging.LoggerFactory;
import com.j256.simplelogging.NullLog;
import com.j256.simplelogging.NullLog.NullLogFactory;

public class NullLogTest extends BaseLogTest {

	public NullLogTest() {
		super(new NullLog(null));
	}

	@Test
	public void testStuff() {
		LoggerFactory.setLogFactory(new NullLogFactory());
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.fatal("shouldn't see this");
	}
}
