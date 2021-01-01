package com.j256.simplelogging.backend;

import com.j256.simplelogging.backend.CommonsLoggingLogBackend.CommonsLoggingLogBackendFactory;

public class CommonsLoggingLogBackendTest extends BaseLogBackendTest {

	public CommonsLoggingLogBackendTest() {
		super(new CommonsLoggingLogBackendFactory());
	}
}
