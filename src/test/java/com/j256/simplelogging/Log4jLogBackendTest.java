package com.j256.simplelogging;

import com.j256.simplelogging.Log4jLogBackend.Log4jLogBackendFactory;

public class Log4jLogBackendTest extends BaseLogBackendTest {

	public Log4jLogBackendTest() {
		super(new Log4jLogBackendFactory());
	}
}
