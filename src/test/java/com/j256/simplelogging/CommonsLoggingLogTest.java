package com.j256.simplelogging;

import com.j256.simplelogging.CommonsLoggingLog;

public class CommonsLoggingLogTest extends BaseLogTest {

	public CommonsLoggingLogTest() {
		super(new CommonsLoggingLog("CommonsLoggingLogTest"));
	}
}
