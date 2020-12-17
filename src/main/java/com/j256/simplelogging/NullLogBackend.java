package com.j256.simplelogging;

import com.j256.simplelogging.LoggerFactory.LogBackendFactory;

/**
 * Log backend that ignores all log requests.
 * 
 * @author graywatson
 */
public class NullLogBackend implements LogBackend {

	/**
	 * This has an ignored param to match the other log implementations.
	 */
	public NullLogBackend(String classLabel) {
		// no-op
	}

	@Override
	public boolean isLevelEnabled(Level level) {
		return false;
	}

	@Override
	public void log(Level level, String msg) {
		// no-op
	}

	@Override
	public void log(Level level, String msg, Throwable throwable) {
		// no-op
	}

	/**
	 * Factory for generating NullLogBackend instances. This can be used with the
	 * {@link LoggerFactory#setLogBackendFactory(LogBackendFactory)} method to completely disable all logging.
	 */
	public static class NullLogBackendFactory implements LogBackendFactory {
		@Override
		public LogBackend createLogBackend(String classLabel) {
			return new NullLogBackend(classLabel);
		}
	}
}
