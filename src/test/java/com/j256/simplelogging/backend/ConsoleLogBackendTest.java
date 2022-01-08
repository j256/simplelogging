package com.j256.simplelogging.backend;

import com.j256.simplelogging.backend.ConsoleLogBackend.ConsoleLogBackendFactory;

public class ConsoleLogBackendTest extends BaseLogBackendTest {

	public ConsoleLogBackendTest() {
		super(new ConsoleLogBackendFactory());
	}
}
