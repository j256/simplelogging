package com.j256.simplelogging;

/**
 * Log that delegates to Apache Log4j.
 * 
 * @author graywatson
 */
public class Log4jLogBackend implements LogBackend {

	private final org.apache.log4j.Logger logger;

	public Log4jLogBackend(String className) {
		this.logger = org.apache.log4j.Logger.getLogger(className);
	}

	@Override
	public boolean isLevelEnabled(Level level) {
		return logger.isEnabledFor(levelToLog4jLevel(level));
	}

	@Override
	public void log(Level level, String msg) {
		logger.log(levelToLog4jLevel(level), msg);
	}

	@Override
	public void log(Level level, String msg, Throwable t) {
		logger.log(levelToLog4jLevel(level), msg, t);
	}

	private org.apache.log4j.Level levelToLog4jLevel(com.j256.simplelogging.LogBackend.Level level) {
		switch (level) {
			case TRACE:
				return org.apache.log4j.Level.TRACE;
			case DEBUG:
				return org.apache.log4j.Level.DEBUG;
			case INFO:
				return org.apache.log4j.Level.INFO;
			case WARNING:
				return org.apache.log4j.Level.WARN;
			case ERROR:
				return org.apache.log4j.Level.ERROR;
			case FATAL:
				return org.apache.log4j.Level.FATAL;
			default:
				return org.apache.log4j.Level.INFO;
		}
	}
}
