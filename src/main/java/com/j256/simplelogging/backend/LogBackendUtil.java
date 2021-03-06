package com.j256.simplelogging.backend;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Some common utility methods.
 * 
 * @author graywatson
 */
public class LogBackendUtil {

	/**
	 * Return a string equivalent to the throwable for logging.
	 */
	public static String throwableToString(Throwable throwable) {
		StringWriter writer = new StringWriter();
		throwable.printStackTrace(new PrintWriter(writer));
		return writer.toString();
	}
}
