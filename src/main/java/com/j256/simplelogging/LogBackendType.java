package com.j256.simplelogging;

import com.j256.simplelogging.backend.ConsoleLogBackend.ConsoleLogBackendFactory;
import com.j256.simplelogging.backend.LocalLogBackend.LocalLogBackendFactory;
import com.j256.simplelogging.backend.NullLogBackend.NullLogBackendFactory;

/**
 * Default logging backends that are supported. The class names are specified as strings in the constructor so there is
 * not a direct dependency placed on them since these classes may reference types not on the classpath.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 *
 * @author graywatson
 */
public enum LogBackendType implements LogBackendFactory {
	/**
	 * Android Log mechanism. See: https://developer.android.com/reference/android/util/Log
	 * 
	 * <p>
	 * WARNING: Android log must be before commons logging since Android provides commons logging but logging messages
	 * are ignored that are sent there. Grrrrr.
	 * </p>
	 */
	ANDROID("AndroidLogBackend$AndroidLogBackendFactory"),
	/**
	 * Logback direct. See: http://logback.qos.ch/
	 */
	LOGBACK("LogbackLogBackend$LogbackLogBackendFactory"),
	/**
	 * Version 2 of the log4j package. See https://logging.apache.org/log4j/2.x/
	 */
	LOG4J2("Log4j2LogBackend$Log4j2LogBackendFactory"),
	/**
	 * SLF4J which is often paired with logback. See: http://www.slf4j.org/ This should be below logback and log4j2
	 * since those are typical backends used by slf4j.
	 */
	SLF4J("Slf4jLoggingLogBackend$Slf4jLoggingLogBackendFactory"),
	/**
	 * Old version of the log4j package accessed through reflection so as to not show up as a dependency. This will only
	 * be used if the log4j jar is supplied. See https://logging.apache.org/log4j/2.x/
	 */
	LOG4J("Log4jLogBackend$Log4jLogBackendFactory"),
	/**
	 * Support for the logger available inside AWS lambda SDK.
	 */
	LAMBDA("LambdaLoggerLogBackend$LambdaLoggerLogBackendFactory"),
	/**
	 * Apache commons logging. See https://commons.apache.org/proper/commons-logging/
	 */
	COMMONS_LOGGING("CommonsLoggingLogBackend$CommonsLoggingLogBackendFactory"),
	/**
	 * Local simple log backend that writes to a output file.
	 * 
	 * <p>
	 * NOTE: any loggers defined below this will not be auto-detected because this is always available.
	 * </p>
	 */
	LOCAL(new LocalLogBackendFactory()),
	/**
	 * Simple log backend that writes out to System.out or System.err.
	 */
	CONSOLE(new ConsoleLogBackendFactory()),
	/**
	 * Internal JVM logging implementation almost always available. We put this below the LOCAL log because it's always
	 * available but we don't want to auto-detect it. See:
	 * https://docs.oracle.com/javase/7/docs/api/java/util/logging/package-summary.html
	 */
	JAVA_UTIL("JavaUtilLogBackend$JavaUtilLogBackendFactory"),
	/**
	 * Logging backend which ignores all messages. Used to disable all logging. This is never chosen automatically.
	 */
	NULL(NullLogBackendFactory.getSingleton()),
	// end
	;

	private final LogBackendFactory factory;

	private LogBackendType(LogBackendFactory factory) {
		this.factory = factory;
	}

	private LogBackendType(String factoryClassName) {
		this.factory = detectFactory(factoryClassName);
	}

	@Override
	public LogBackend createLogBackend(String classLabel) {
		return factory.createLogBackend(classLabel);
	}

	/**
	 * Return true if the log class is available. This typically is testing to see if a class is available on the
	 * classpath.
	 */
	@Override
	public boolean isAvailable() {
		if (this == LogBackendType.LOCAL) {
			// always available
			return true;
		} else if (this == LogBackendType.NULL || !this.factory.isAvailable()) {
			// LogBackendType.NULL is never available or check the factory availability method
			return false;
		} else {
			// we might have defaulted to using the local-log backend if it was not available
			return !(factory instanceof LocalLogBackendFactory);
		}
	}

	/**
	 * Return true if the log class is available. This typically is testing to see if a class is available on the
	 * classpath.
	 */
	public static boolean isAvailable(LogBackendFactory logBackendFactory) {
		if (logBackendFactory instanceof LogBackendType) {
			return ((LogBackendType) logBackendFactory).isAvailable();
		}
		try {
			if (!logBackendFactory.isAvailable()) {
				return false;
			} else {
				/*
				 * We make this call here to exercise the class because we have seen that sometimes (Android) can have
				 * fake classes that only throw if they are really used.
				 */
				logBackendFactory.createLogBackend("test").isLevelEnabled(Level.INFO);
				return true;
			}
		} catch (Throwable th) {
			return false;
		}
	}

	/**
	 * Try to detect if the logger class is available and if calling the factory to make a logger works.
	 */
	private LogBackendFactory detectFactory(String factoryClassName) {
		try {
			// this might throw if there is some issue with the package which (I gather) can happen when obfuscated
			String fullClassName;
			if (factoryClassName.indexOf('.') >= 0) {
				fullClassName = factoryClassName;
			} else {
				// NOTE: in some situations class.getPackage() will return null -- maybe due to obfuscation
				Package pack = LocalLogBackendFactory.class.getPackage();
				if (pack == null) {
					// I doubt that this is going to be able to find the class but who knows
					fullClassName = factoryClassName;
				} else {
					fullClassName = pack.getName() + '.' + factoryClassName;
				}
			}

			// sometimes the constructor works but it's not fully wired
			LogBackendFactory factory = (LogBackendFactory) Class.forName(fullClassName).newInstance();
			// we may really need to use the class before we see issues
			factory.createLogBackend("test").isLevelEnabled(Level.INFO);
			if (factory.isAvailable()) {
				return factory;
			} else {
				/*
				 * We don't immediately report on this issue because this log factory will most likely never be used. If
				 * it is, the first thing that the factory will do is log a warning.
				 */
				String queuedWarning = "Factory class " + fullClassName + " for log type " + this
						+ ", is not available, using local log";
				return new LocalLogBackendFactory(queuedWarning);
			}
		} catch (Throwable th) {
			/*
			 * We catch throwable here because we could get linkage errors or obfuscation issues. We don't immediately
			 * report on this issue because this log factory will most likely never be used. If it is, the first thing
			 * that the factory will do is log a warning.
			 */
			String queuedWarning = "Unable to create instance of class " + factoryClassName + " for log type " + this
					+ ", using local log: " + th;
			return new LocalLogBackendFactory(queuedWarning);
		}
	}
}
