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
	 * "LOGBACK") or a class name that implements {@link LogBackendFactory} (such as
	 * "com.j256.simplelogging.backend.LogbackLogBackend$LogbackLogBackendFactory").
	 */
	public static final String LOG_TYPE_SYSTEM_PROPERTY = "com.j256.simplelogger.backend";

	/**
	 * Name of the Android tag that is used with {@link Log#isLoggable(String, int)} to determine if the global logs are
	 * enabled.
	 */
	public static final String ANDROID_ALL_LOGS_NAME = "simplelogging";

	/**
	 * You can set the log level by setting the System.setProperty(LocalLogBackend.LOCAL_LOG_LEVEL_PROPERTY, "trace").
	 * Acceptable values are: TRACE, DEBUG, INFO, WARN, ERROR, and FATAL.
	 */
	public static final String LOCAL_LOG_LEVEL_PROPERTY = "com.j256.simplelogging.level";

	/**
	 * You can also redirect the log to a file by setting the
	 * System.setProperty(LocalLogBackend.LOCAL_LOG_FILE_PROPERTY, "log.out"). Otherwise, log output will go to stdout.
	 */
	public static final String LOCAL_LOG_FILE_PROPERTY = "com.j256.simplelogging.file";

	/**
	 * It also supports a file simpleLoggingLocalLog.properties file which contains lines such as:
	 * 
	 * <pre>
	 * # regex-pattern = Level
	 * com\.foo\.yourclass.*=DEBUG
	 * com\.foo\.yourclass\.BaseMappedStatement=TRACE
	 * com\.foo\.yourclass\.MappedCreate=TRACE
	 * com\.foo\.yourclass\.StatementExecutor=TRACE
	 * </pre>
	 */
	public static final String LOCAL_LOG_PROPERTIES_FILE = "/simpleLoggingLocalLog.properties";

	private LoggerConstants() {
		// only here for static usage
	}
}
