package com.j256.simplelogging.backend;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.j256.simplelogging.LogBackendFactory;
import com.j256.simplelogging.LogBackendType;
import com.j256.simplelogging.Logger;
import com.j256.simplelogging.LoggerFactory;

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

	@Test(expected = UnsatisfiedLinkError.class)
	public void testCoverage() {
		new AndroidLogBackend("classnametoolong01234567890123456789");
	}
}
