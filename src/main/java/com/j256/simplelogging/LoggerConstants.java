package com.j256.simplelogging;

import android.util.Log;

/**
 * Class to have one place in case you want to tweak the logging constants for your application.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 * 
 * @author graywatson
 */
public class LoggerConstants {

	/**
	 * System property used to set the logger backend. Can be one of the values of {@link LogBackendType} (such as
	 * "LOGBACK") or the class name that implements {@link LogBackendFactory} (such as
	 * "com.j256.simplelogging.backend.LogbackLogBackend.LogbackLogBackendFactory").
	 */
	public static final String LOG_TYPE_SYSTEM_PROPERTY = "com.j256.simplelogger.backend";

	/**
	 * Name of the Android tag that is used with {@link Log#isLoggable(String, int)} to determine if the global logs are
	 * enabled.
	 */
	public static final String ANDROID_ALL_LOGS_NAME = "simplelogging";

	private LoggerConstants() {
		// only here for static usage
	}
}
