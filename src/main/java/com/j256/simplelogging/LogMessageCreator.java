package com.j256.simplelogging;

/**
 * Object that creates a message when called.
 * 
 * @author graywatson
 */
public interface LogMessageCreator {

	/**
	 * Return the log message associated with this entry. This will be called only if the log level is enabled.
	 */
	public String createLogMessage();
}
