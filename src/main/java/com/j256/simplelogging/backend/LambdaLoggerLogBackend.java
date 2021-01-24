package com.j256.simplelogging.backend;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.LambdaRuntime;
import com.j256.simplelogging.Level;
import com.j256.simplelogging.LogBackend;
import com.j256.simplelogging.LogBackendFactory;

/**
 * Backend for AWS lambda logger.
 * 
 * @author graywatson
 */
public class LambdaLoggerLogBackend implements LogBackend {

	private final LambdaLogger lambdaLogger;

	public LambdaLoggerLogBackend(String className) {
		this.lambdaLogger = LambdaRuntime.getLogger();
	}

	@Override
	public boolean isLevelEnabled(Level level) {
		// always true so you should use Logger#setGlobalLogLevel() to set the level
		return true;
	}

	@Override
	public void log(Level level, String msg) {
		if (msg.endsWith("\n")) {
			lambdaLogger.log(level + " " + msg);
		} else {
			lambdaLogger.log(level + " " + msg + "\n");
		}
	}

	@Override
	public void log(Level level, String msg, Throwable throwable) {
		log(level, msg);
		log(level, LogBackendUtil.throwableToString(throwable));
	}

	/**
	 * Factory for generating JavaUtilLogBackend instances.
	 */
	public static class LambdaLoggerLogBackendFactory implements LogBackendFactory {
		@Override
		public LogBackend createLogBackend(String classLabel) {
			return new LambdaLoggerLogBackend(classLabel);
		}
	}
}
