package com.j256.simplelogging.backend;

import org.junit.Ignore;

import com.j256.simplelogging.backend.Log4j2LogBackend.Log4j2LogBackendFactory;

@Ignore("won't work if we are running under java < 8")
public class Log4j2LogBackendTest extends BaseLogBackendTest {

	public Log4j2LogBackendTest() {
		super(new Log4j2LogBackendFactory());
	}
}
