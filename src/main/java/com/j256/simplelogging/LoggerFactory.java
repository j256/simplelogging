package com.j256.simplelogging;

import java.lang.reflect.Constructor;

import com.j256.simplelogging.LogBackend.Level;

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
	 * Set the log factory to be a specific instance. This allows you to easily redirect log messages to your own
	 * {@link LogBackend} implementation.
	 */
	public static void setLogFactory(LogBackendFactory logFactory) {
		LoggerFactory.logBackendFactory = logFactory;
	}

	/**
	 * Return a logger associated with a particular class name.
	 */
	public static Logger getLogger(String className) {
		if (logBackendFactory == null) {
			logBackendFactory = findLogFactory();
		}
		return new Logger(logBackendFactory.createLogBackend(className));
	}

	/**
	 * Return the single class name from a class-name string.
	 */
	public static String getSimpleClassName(String className) {
		// get the last part of the class name
		String[] parts = className.split("\\.");
		if (parts.length <= 1) {
			return className;
		} else {
			return parts[parts.length - 1];
		}
	}

	/**
	 * Return the most appropriate log backend factory. This should _never_ return null.
	 */
	private static LogBackendFactory findLogFactory() {

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
	 * Log backend factory for generating backend instances.
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
		SLF4J("org.slf4j.LoggerFactory", "com.j256.simplelogging.Slf4jLoggingLogBackend"),
		/**
		 * WARNING: Android log must be _before_ commons logging since Android provides commons logging but logging
		 * messages are ignored that are sent there. Grrrrr.
		 */
		ANDROID("android.util.Log", "com.j256.simplelogging.AndroidLogBackend"),
		COMMONS_LOGGING("org.apache.commons.logging.LogFactory", "com.j256.simplelogging.CommonsLoggingLogBackend"),
		LOG4J2("org.apache.logging.log4j.LogManager", "com.j256.simplelogging.Log4j2LogBackend"),
		LOG4J("org.apache.log4j.Logger", "com.j256.simplelogging.Log4jLogBackend"),
		// this should always be at the end as the fall-back, so it's always available
		LOCAL(LocalLogBackend.class.getName(), LocalLogBackend.class.getName()) {
			@Override
			public LogBackend createLogBackend(String classLabel) {
				return new LocalLogBackend(classLabel);
			}

			@Override
			public boolean isAvailable() {
				// always available
				return true;
			}
		},
		// we put this down here because it's always available but we rarely want to use it
		JAVA_UTIL("java.util.logging.Logger", "com.j256.simplelogging.JavaUtilLogBackend"),
		NULL(NullLogBackend.class.getName(), NullLogBackend.class.getName()) {
			@Override
			public LogBackend createLogBackend(String classLabel) {
				return new NullLogBackend(classLabel);
			}

			@Override
			public boolean isAvailable() {
				// never chosen automatically
				return false;
			}
		},
		// end
		;

		private final String detectClassName;
		private final String logClassName;

		private LogBackendType(String detectClassName, String logClassName) {
			this.detectClassName = detectClassName;
			this.logClassName = logClassName;
		}

		@Override
		public LogBackend createLogBackend(String classLabel) {
			try {
				return createLogFromClassName(classLabel);
			} catch (Exception e) {
				// oh well, fall back to the local log
				LogBackend backend = new LocalLogBackend(classLabel);
				backend.log(Level.WARNING, "Unable to call constructor with single String argument for class "
						+ logClassName + ", so had to use local log: " + e.getMessage());
				return backend;
			}
		}

		/**
		 * Return true if the log class is available. This typically is testing to see if a class is available on the
		 * classpath.
		 */
		public boolean isAvailable() {
			if (!isAvailableTestClass()) {
				return false;
			}
			try {
				// try to actually use the logger which resolves problems with the Android stub
				LogBackend backend = createLogFromClassName(getClass().getName());
				backend.isLevelEnabled(Level.INFO);
				return true;
			} catch (Exception e) {
				return false;
			}
		}

		/**
		 * Try to create the log from the class name which may throw.
		 */
		private LogBackend createLogFromClassName(String classLabel) throws Exception {
			Class<?> clazz = Class.forName(logClassName);
			@SuppressWarnings("unchecked")
			Constructor<LogBackend> constructor = (Constructor<LogBackend>) clazz.getConstructor(String.class);
			return constructor.newInstance(classLabel);
		}

		/**
		 * Is this class available meaning that we should use this logger. This is package permissions for testing
		 * purposes.
		 */
		boolean isAvailableTestClass() {
			try {
				Class.forName(detectClassName);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}
}
