package com.j256.simplelogging;

import com.j256.simplelogging.JavaUtilLogBackend.JavaUtilLogBackendFactory;

public class JavaUtilLogBackendTest extends BaseLogBackendTest {

	public JavaUtilLogBackendTest() {
		super(new JavaUtilLogBackendFactory());
	}
}
