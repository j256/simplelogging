package com.j256.simplelogging;

import com.j256.simplelogging.Log4jLog;

public class Log4jLogTest extends BaseLogTest {

	public Log4jLogTest() {
		super(new Log4jLog("Log4jLogTest"));
	}
}
