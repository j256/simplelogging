package com.j256.simplelogging;

import com.j256.simplelogging.LocalLogBackend.LocalLogBackendFactory;
import com.j256.simplelogging.NullLogBackend.NullLogBackendFactory;

/**
 * Factory that creates {@link Logger} instances. It uses reflection to see what logging backends are available on the
 * classpath and tries to find the most appropriate one.
 * 
 * <p>
 * To set the logger to a particular type, set the system property ("com.j256.simplelogger.backend") contained in
 * {@link #LOG_TYPE_SYSTEM_PROPERTY} to be name of one of the enumerated types in {@link LoggerFactory.LogBackendType}.
 * </p>
 */
public class LoggerFactory {

	public static final String LOG_TYPE_SYSTEM_PROPERTY = "com.j256.simplelogger.backend";
	private static LogBackendFactory logBackendFactory;

	/**
	 * For static calls only.
	 */
	private LoggerFactory() {
	}

	/**
	 * Return a logger associated with a particular class.
	 */
	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 * Return a logger associated with a particular class name.
	 */
	public static Logger getLogger(String className) {
		if (logBackendFactory == null) {
			logBackendFactory = findLogBackendFactory();
		}
		return new Logger(logBackendFactory.createLogBackend(className));
	}

	/**
	 * Get the currently assigned log factory or null if none.
	 */
	public static LogBackendFactory getLogBackendFactory() {
		return LoggerFactory.logBackendFactory;
	}

	/**
	 * Set the log backend factory to be a specific instance. This allows you to easily redirect log messages to your
	 * own {@link LogBackendFactory} implementation.
	 */
	public static void setLogBackendFactory(LogBackendFactory LogBackendFactory) {
		LoggerFactory.logBackendFactory = LogBackendFactory;
	}

	/**
	 * Return the single class name from a class-name string.
	 */
	public static String getSimpleClassName(String className) {
		// get the last part of the class name
		int index = className.lastIndexOf('.');
		if (index < 0 || index == className.length() - 1) {
			return className;
		} else {
			return className.substring(index + 1);
		}
	}

	/**
	 * Return the most appropriate log backend factory. This should _never_ return null.
	 */
	private static LogBackendFactory findLogBackendFactory() {

		// see if the log-type was specified as a system property
		String logTypeString = System.getProperty(LOG_TYPE_SYSTEM_PROPERTY);
		if (logTypeString != null) {
			try {
				return LogBackendType.valueOf(logTypeString);
			} catch (IllegalArgumentException e) {
				LogBackend backend = new LocalLogBackend(LoggerFactory.class.getName());
				backend.log(Level.WARNING, "Could not find valid log-type from system property '"
						+ LOG_TYPE_SYSTEM_PROPERTY + "', value '" + logTypeString + "'");
			}
		}

		for (LogBackendType logType : LogBackendType.values()) {
			if (logType.isAvailable()) {
				return logType;
			}
		}
		// fall back is always LOCAL, probably never reached
		return LogBackendType.LOCAL;
	}

	/**
	 * Factory for generating log backend instances.
	 */
	public interface LogBackendFactory {
		/**
		 * Create a log backend implementation from the class-label.
		 */
		public LogBackend createLogBackend(String classLabel);
	}

	/**
	 * Type of logging backends that are supported. The classes are specified as strings so there is not a direct
	 * dependency placed on them since these classes may reference types not on the classpath.
	 */
	public enum LogBackendType implements LogBackendFactory {
		/**
		 * SLF4J. See: http://www.slf4j.org/
		 */
		SLF4J("com.j256.simplelogging.Slf4jLoggingLogBackend$Slf4jLoggingLogBackendFactory"),
		/**
		 * Android Log mechanism. See: https://developer.android.com/reference/android/util/Log
		 * 
		 * <p>
		 * WARNING: Android log must be before commons logging since Android provides commons logging but logging
		 * messages are ignored that are sent there. Grrrrr.
		 * </p>
		 */
		ANDROID("com.j256.simplelogging.AndroidLogBackend$AndroidLogBackendFactory"),
		/**
		 * Apache commons logging. See https://commons.apache.org/proper/commons-logging/
		 */
		COMMONS_LOGGING("com.j256.simplelogging.CommonsLoggingLogBackend$CommonsLoggingLogBackendFactory"),
		/**
		 * Version 2 of the log4j package. See https://logging.apache.org/log4j/2.x/
		 */
		LOG4J2("com.j256.simplelogging.Log4j2LogBackend$Log4j2LogBackendFactory"),
		/**
		 * Old version of the log4j package. See https://logging.apache.org/log4j/2.x/
		 */
		LOG4J("com.j256.simplelogging.Log4jLogBackend$Log4jLogBackendFactory"),
		/**
		 * Local simple log backend that writes to a output file.
		 * 
		 * <p>
		 * NOTE: this should always be at the end as the fall-back, so it's always available
		 * </p>
		 */
		LOCAL(new LocalLogBackendFactory()),
		/**
		 * Internal JVM logging implementation almost always available. We put this down here because it's always
		 * available but we rarely want to use it. See
		 * https://docs.oracle.com/javase/7/docs/api/java/util/logging/package-summary.html.
		 */
		JAVA_UTIL("com.j256.simplelogging.JavaUtilLogBackend$JavaUtilLogBackendFactory"),
		/**
		 * Logging backend which ignores all messages. Used to disable all logging. This is never chosen automatically.
		 */
		NULL(new NullLogBackendFactory()),
		// end
		;

		private final LogBackendFactory factory;

		private LogBackendType(LogBackendFactory factory) {
			this.factory = factory;
		}

		private LogBackendType(String logBackendFactoryClassName) {
			this.factory = detectFactory(logBackendFactoryClassName);
		}

		@Override
		public LogBackend createLogBackend(String classLabel) {
			return factory.createLogBackend(classLabel);
		}

		/**
		 * Return true if the log class is available. This typically is testing to see if a class is available on the
		 * classpath.
		 */
		public boolean isAvailable() {
			/*
			 * If this is LOCAL then it is always available. NULL is never available. If it is another LogBackendType
			 * then we might have defaulted to using the local-log backend if it was not available.
			 */
			return (this == LOCAL || (this != NULL && !(factory instanceof LocalLogBackendFactory)));
		}

		/**
		 * Try to detect if the logger class is available and if calling the factory to make a logger works.
		 */
		private LogBackendFactory detectFactory(String factoryClassName) {
			try {
				// sometimes the constructor works but it's not fully wired
				LogBackendFactory factory = (LogBackendFactory) Class.forName(factoryClassName).newInstance();
				// we may really need to use the class before we see issues
				factory.createLogBackend("test").isLevelEnabled(Level.INFO);
				return factory;
			} catch (Throwable th) {
				// we catch throwable here because we could get linkage errors
				LogBackend backend = new LocalLogBackend(LogBackendType.class.getSimpleName() + "." + this);
				backend.log(Level.WARNING, "Unable to get new instance of class " + factoryClassName
						+ ", so had to use local log: " + th.getMessage());
				return new LocalLogBackendFactory();
			}
		}
	}
}
