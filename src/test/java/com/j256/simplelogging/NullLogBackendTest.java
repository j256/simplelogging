package com.j256.simplelogging;

import org.junit.Test;

import com.j256.simplelogging.Logger;
import com.j256.simplelogging.LoggerFactory;
import com.j256.simplelogging.NullLogBackend;
import com.j256.simplelogging.NullLogBackend.NullLogBackendFactory;

public class NullLogBackendTest extends BaseLogBackendTest {

	public NullLogBackendTest() {
		super(new NullLogBackend());
	}

	@Test
	public void testStuff() {
		LoggerFactory.setLogBackendFactory(new NullLogBackendFactory());
		Logger logger = LoggerFactory.getLogger(getClass());
		logger.fatal("shouldn't see this");
	}
}
