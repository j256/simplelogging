package com.j256.simplelogging.backend;

import com.j256.simplelogging.backend.LambdaLoggerLogBackend.LambdaLoggerLogBackendFactory;

public class LambdaLoggerLogBackendTest extends BaseLogBackendTest {

	public LambdaLoggerLogBackendTest() {
		super(new LambdaLoggerLogBackendFactory());
	}
}
