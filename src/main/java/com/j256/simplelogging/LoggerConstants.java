package com.j256.simplelogging;

/**
 * Class to have one place in case you want to tweak the logging constants for your application.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 * 
 * @author graywatson
 */
public class LoggerConstants {

	/**
	 * The default log level for all loggers being created. If set to null, then there is no default global log setting
	 * and the logger backend will determine what log levels are active. Set to Level.OFF to disable all logging by
	 * default.
	 */
	public static final Level DEFAULT_GLOBAL_LOG_LEVEL = null;

	/**
	 * System property used to set the logger backend. Can be one of the values of {@link LogBackendType} (such as
	 * "LOGBACK") or a class name that implements {@link LogBackendFactory} (such as
	 * "com.j256.simplelogging.backend.LogbackLogBackend$LogbackLogBackendFactory").
	 */
	public static final String LOG_BACKEND_SYSTEM_PROPERTY = "com.j256.simplelogger.backend";

	/**
	 * File path to the properties for the simplelogging library. Lines are in the form field = value. Supported fields
	 * include 'backend = type-or-class' and 'locallog.class-regex-pattern = level'.
	 */
	public static final String PROPERTIES_CONFIG_FILE = "/simplelogging.properties";

	/**
	 * Name of the Android tag that is used with android.util.Log#isLoggable(String, int) to determine if the global
	 * logs are enabled.
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

	private LoggerConstants() {
		// only here for static usage
	}
}
