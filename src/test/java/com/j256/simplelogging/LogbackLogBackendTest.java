package com.j256.simplelogging;

import com.j256.simplelogging.LogbackLogBackend.LogbackLogBackendFactory;

public class LogbackLogBackendTest extends BaseLogBackendTest {

	public LogbackLogBackendTest() {
		super(new LogbackLogBackendFactory());
	}
}
