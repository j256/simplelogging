package com.j256.simplelogging;

import com.j256.simplelogging.Log4j2Log;

public class Log4j2LogTest extends BaseLogTest {

	public Log4j2LogTest() {
		super(new Log4j2Log("Log4j2LogTest"));
	}
}
