package com.j256.simplelogging;

import com.j256.simplelogging.Log4jLogBackend;

public class Log4jLogBackendTest extends BaseLogBackendTest {

	public Log4jLogBackendTest() {
		super(new Log4jLogBackend("Log4jLogBackendTest"));
	}
}
