package com.j256.simplelogging;

import com.j256.simplelogging.Slf4jLoggingLogBackend.Slf4jLoggingLogBackendFactory;

public class Slf4jLoggingLogBackendTest extends BaseLogBackendTest {

	public Slf4jLoggingLogBackendTest() {
		super(new Slf4jLoggingLogBackendFactory());
	}
}
