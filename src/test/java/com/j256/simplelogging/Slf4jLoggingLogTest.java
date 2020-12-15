package com.j256.simplelogging;

import com.j256.simplelogging.Slf4jLoggingLog;

public class Slf4jLoggingLogTest extends BaseLogTest {

	public Slf4jLoggingLogTest() {
		super(new Slf4jLoggingLog("Slf4jLoggingLogTest"));
	}
}
