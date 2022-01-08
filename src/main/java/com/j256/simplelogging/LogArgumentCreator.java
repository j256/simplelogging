package com.j256.simplelogging;

/**
 * Object that creates a log message argument when called. This will only be referenced if the log level is enabled
 * which allows lazy message creation.
 * 
 * @author graywatson
 */
public interface LogArgumentCreator {

	/**
	 * Return the log message argument string for this entity.
	 */
	public String createArg();
}
