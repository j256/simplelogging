package com.j256.simplelogging.backend;

import com.j256.simplelogging.backend.JavaUtilLogBackend.JavaUtilLogBackendFactory;

public class JavaUtilLogBackendTest extends BaseLogBackendTest {

	public JavaUtilLogBackendTest() {
		super(new JavaUtilLogBackendFactory());
	}
}
