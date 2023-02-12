package com.j256.simplelogging;

/**
 * Object that creates a log message argument when called. This will only be referenced if the log level is enabled
 * which allows lazy message creation. This can be used on an object when the default toString() can't be changed or
 * otherwise is not appropriate for the log messages -- for example, when security information needs to be masked.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 * 
 * @author graywatson
 */
public interface LogArgumentCreator {

	/**
	 * Return the log message argument string for this entity to be expanded into a {} in a log message.
	 */
	public String createLogArg();
}
