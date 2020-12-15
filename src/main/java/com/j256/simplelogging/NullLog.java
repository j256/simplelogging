package com.j256.simplelogging;

import com.j256.simplelogging.LoggerFactory.LogFactory;

/**
 * Log that ignores all log requests.
 * 
 * @author graywatson
 */
public class NullLog implements Log {

	/**
	 * This has an ignored param to match the other log implementations.
	 */
	public NullLog(String classLabel) {
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
	 * Factory for generating NullLog instances. This can be used with the
	 * {@link LoggerFactory#setLogFactory(LogFactory)} method to completely disable all logging.
	 */
	public static class NullLogFactory implements LogFactory {
		@Override
		public Log createLog(String classLabel) {
			return new NullLog(classLabel);
		}
	}
}
