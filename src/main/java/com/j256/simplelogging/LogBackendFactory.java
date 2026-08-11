package com.j256.simplelogging;

/**
 * Factory for generating LogBackend instances.
 *
 * From SimpleLogging: https://github.com/j256/simplelogging
 *
 * @author graywatson
 */
public interface LogBackendFactory {

	/**
	 * Return true if the backend factory is available on the classpath and wired correctly. Typically the factory is
	 * available if it can be instantiated, but sometimes there are some additional checks needed to test if it is fully
	 * available.
	 */
	public boolean isAvailable();

	/**
	 * Create a log backend implementation from the class-label.
	 */
	public LogBackend createLogBackend(String classLabel);
}
