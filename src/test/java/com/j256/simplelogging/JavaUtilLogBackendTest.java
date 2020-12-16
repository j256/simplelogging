package com.j256.simplelogging;

import com.j256.simplelogging.JavaUtilLogBackend;

public class JavaUtilLogBackendTest extends BaseLogBackendTest {

	public JavaUtilLogBackendTest() {
		super(new JavaUtilLogBackend("JavaUtilLogBackendTest"));
	}
}
