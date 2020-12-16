package com.j256.simplelogging;

/**
 * Class which fronts various log code which may or may not be in the classpath.
 * 
 * @author graywatson
 */
public interface LogBackend {

	/**
	 * Returns true if the log mode is in trace or higher.
	 */
	public boolean isLevelEnabled(Level level);

	/**
	 * Log a trace message.
	 */
	public void log(Level level, String message);

	/**
	 * Log a trace message with a throwable.
	 */
	public void log(Level level, String message, Throwable t);
}
