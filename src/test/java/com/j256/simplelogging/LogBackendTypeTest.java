package com.j256.simplelogging;

import static org.junit.Assert.*;

import org.junit.Test;

public class LogBackendTypeTest {

	@Test
	public void test() {
		for (LogBackendType type : LogBackendType.values()) {
			if (type == LogBackendType.ANDROID || type == LogBackendType.NULL) {
				assertFalse(type + " should not be available", type.isAvailable());
				// NOTE: type.createLogBackend() defers to LocalLog
				continue;
			}

			assertTrue(type + " should be available", type.isAvailable());
			assertNotNull(type.createLogBackend(getClass().getSimpleName()));
		}
	}
}
