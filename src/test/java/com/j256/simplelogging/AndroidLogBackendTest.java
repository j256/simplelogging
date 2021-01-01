package com.j256.simplelogging;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AndroidLogBackendTest {

	@Test
	public void testEnowork() {
		LogBackendFactory factory = LoggerFactory.getLogBackendFactory();
		try {
			LoggerFactory.setLogBackendFactory(LogBackendType.ANDROID);
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.trace("hello");
			assertEquals(LogBackendType.ANDROID, LoggerFactory.getLogBackendFactory());
		} finally {
			LoggerFactory.setLogBackendFactory(factory);
		}
	}
}