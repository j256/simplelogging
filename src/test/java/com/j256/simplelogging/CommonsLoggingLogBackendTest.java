package com.j256.simplelogging;

import com.j256.simplelogging.CommonsLoggingLogBackend.CommonsLoggingLogBackendFactory;

public class CommonsLoggingLogBackendTest extends BaseLogBackendTest {

	public CommonsLoggingLogBackendTest() {
		super(new CommonsLoggingLogBackendFactory());
	}
}
