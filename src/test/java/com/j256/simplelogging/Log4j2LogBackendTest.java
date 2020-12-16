package com.j256.simplelogging;

import com.j256.simplelogging.Log4j2LogBackend;

public class Log4j2LogBackendTest extends BaseLogBackendTest {

	public Log4j2LogBackendTest() {
		super(new Log4j2LogBackend("Log4j2LogBackendTest"));
	}
}
