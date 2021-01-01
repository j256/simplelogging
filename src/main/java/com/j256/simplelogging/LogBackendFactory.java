package com.j256.simplelogging;

/**
 * Factory for generating log backend instances.
 */
public interface LogBackendFactory {
	/**
	 * Create a log backend implementation from the class-label.
	 */
	public LogBackend createLogBackend(String classLabel);
}
