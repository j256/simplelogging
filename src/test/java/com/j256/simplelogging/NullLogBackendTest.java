package com.j256.simplelogging;

import org.junit.Test;

import com.j256.simplelogging.Logger;
import com.j256.simplelogging.LoggerFactory;
import com.j256.simplelogging.NullLogBackend;
import com.j256.simplelogging.NullLogBackend.NullLogFactory;

public class NullLogBackendTest extends BaseLogBackendTest {

	public NullLogBackendTest() {
		super(new NullLogBackend(null));
	}

	@Test
	public void testStuff() {
		LoggerFactory.setLogFactory(new NullLogFactory());
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.fatal("shouldn't see this");
	}
}
