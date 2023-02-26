package com.j256.simplelogging.backend;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.j256.simplelogging.Level;
import com.j256.simplelogging.LogBackendFactory;

public class Log4jLogBackendTest extends BaseLogBackendTest {

	private static LogBackendFactory factory;

	static {
		// we have to do this because we want to use the log4j class but only if it's on the classpath
		try {
			factory = new Log4jLogBackend.Log4jLogBackendFactory();
			// now we test the factory to make sure it works
			factory.createLogBackend("testing").isLevelEnabled(Level.TRACE);
		} catch (Throwable th) {
			factory = new Log4j2LogBackend.Log4j2LogBackendFactory();
		}
	}

	public Log4jLogBackendTest() {
		super(factory);
	}

	@Test
	public void testEnabled() {
		assertTrue(log.isLevelEnabled(Level.TRACE));
	}
}
