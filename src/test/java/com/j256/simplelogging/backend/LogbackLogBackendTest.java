package com.j256.simplelogging.backend;

import com.j256.simplelogging.backend.LogbackLogBackend.LogbackLogBackendFactory;

public class LogbackLogBackendTest extends BaseLogBackendTest {

	public LogbackLogBackendTest() {
		super(new LogbackLogBackendFactory());
	}
}
