package com.j256.simplelogging;

import java.lang.reflect.Array;

/**
 * Base class which does the logging to the backend.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 *
 * @author graywatson
 */
public abstract class BaseLogger {

	private final static String ARG_STRING = "{}";
	private final static int ARG_STRING_LENGTH = ARG_STRING.length();
	protected final static Object UNKNOWN_ARG = new Object();
	private final static int DEFAULT_FULL_MESSAGE_LENGTH = 128;
	private final static String NO_MESSAGE_MESSAGE = "no log message";

	private static Level globalLevel;

	private final LogBackend backend;

	public BaseLogger(LogBackend backend) {
		this.backend = backend;
	}

	/**
	 * Set the log level for all of the loggers. This should be done very early in an application's main or launch
	 * methods. It allows the caller to set a filter on all log messages. Set to null to disable any global log level
	 * filtering of messages and go back to the per-log level matching.
	 */
	public static void setGlobalLogLevel(Level level) {
		BaseLogger.globalLevel = level;
	}

	/**
	 * Return true if logging level is enabled else false.
	 */
	public boolean isLevelEnabled(Level level) {
		return backend.isLevelEnabled(level);
	}

	/**
	 * Return the count of the number of arg strings in the message.
	 */
	public int countArgStrings(String msg) {
		int count = 0;
		int index = 0;
		while (true) {
			int found = msg.indexOf(BaseLogger.ARG_STRING, index);
			if (found < 0) {
				return count;
			}
			count++;
			index = found + BaseLogger.ARG_STRING_LENGTH;
		}
	}

	/**
	 * Get the underlying log backend implementation for testing purposes.
	 */
	public LogBackend getLogBackend() {
		return backend;
	}

	/**
	 * log-if-enabled with just a msg.
	 */
	protected void logIfEnabled(Level level, Throwable throwable, String msg) {
		logIfEnabled(level, throwable, msg, UNKNOWN_ARG, UNKNOWN_ARG, UNKNOWN_ARG, UNKNOWN_ARG, null);
	}

	/**
	 * log-if-enabled with a msg and an arg-array.
	 */
	protected void logIfEnabled(Level level, Throwable throwable, String msg, Object[] argArray) {
		logIfEnabled(level, throwable, msg, UNKNOWN_ARG, UNKNOWN_ARG, UNKNOWN_ARG, UNKNOWN_ARG, argArray);
	}

	/**
	 * Main log-if-enabled method with all argument combinations.
	 */
	protected void logIfEnabled(Level level, Throwable throwable, String msg, Object arg0, Object arg1, Object arg2,
			Object arg3, Object[] argArray) {
		if (globalLevel != null && !globalLevel.isEnabled(level)) {
			// don't log the message if the global-level is set and not enabled
		} else if (backend.isLevelEnabled(level)) {
			String fullMsg;
			if (arg0 == UNKNOWN_ARG && argArray == null) {
				// this will just output the message without parsing which will not parse any extraneous {}
				fullMsg = msg;
			} else if (msg == null) {
				// if msg is null then just spit out the arguments
				fullMsg = argMessage(arg0, arg1, arg2, arg3, argArray);
			} else {
				// do the whole {} expansion thing
				fullMsg = buildFullMessage(msg, arg0, arg1, arg2, arg3, argArray);
			}
			if (fullMsg == null) {
				fullMsg = NO_MESSAGE_MESSAGE;
			}
			if (throwable == null) {
				backend.log(level, fullMsg);
			} else {
				backend.log(level, fullMsg, throwable);
			}
		}
	}

	/**
	 * Return a combined single message from the msg (with possible {}) and optional arguments.
	 */
	private String buildFullMessage(String msg, Object arg0, Object arg1, Object arg2, Object arg3, Object[] argArray) {
		StringBuilder sb = null;
		int lastIndex = 0;
		int argCount = 0;
		while (true) {
			int argIndex = msg.indexOf(ARG_STRING, lastIndex);
			// no more {} arguments?
			if (argIndex == -1) {
				break;
			}
			if (sb == null) {
				// we build this lazily in case there is no {} in the msg
				sb = new StringBuilder(DEFAULT_FULL_MESSAGE_LENGTH);
			}
			// add the string before the arg-string
			if (lastIndex < argIndex) {
				sb.append(msg, lastIndex, argIndex);
			}
			// shift our last-index past the arg-string
			lastIndex = argIndex + ARG_STRING_LENGTH;
			// add the argument, if we still have any
			appendArg(sb, argCount++, arg0, arg1, arg2, arg3, argArray);
		}
		if (sb == null) {
			// if we have yet to create a StringBuilder then just return the msg which has no {}
			return msg;
		} else {
			// spit out the end of the msg
			if (lastIndex < msg.length()) {
				sb.append(msg, lastIndex, msg.length());
			}
			return sb.toString();
		}
	}

	/**
	 * Build a message from a collection of objects.
	 */
	private String argMessage(Object arg0, Object arg1, Object arg2, Object arg3, Object[] argArray) {
		StringBuilder sb = new StringBuilder(DEFAULT_FULL_MESSAGE_LENGTH);
		boolean first = true;
		int argCount = 0;
		sb.append('\'');
		while (true) {
			if (first) {
				first = false;
			} else {
				sb.append("', '");
			}
			if (!appendArg(sb, argCount++, arg0, arg1, arg2, arg3, argArray)) {
				break;
			}
		}
		if (argCount == 0) {
			// might not get here but let's be careful out there
			return null;
		}
		// we take off the ", '" at the end of the last arg because we can't tell ahead of time how many there are
		sb.setLength(sb.length() - 3);
		return sb.toString();
	}

	/**
	 * Append an argument from the individual arguments or the array.
	 */
	private boolean appendArg(StringBuilder sb, int argCount, Object arg0, Object arg1, Object arg2, Object arg3,
			Object[] argArray) {
		if (argArray == null) {
			switch (argCount) {
				case 0:
					return appendArg(sb, arg0);
				case 1:
					return appendArg(sb, arg1);
				case 2:
					return appendArg(sb, arg2);
				case 3:
					return appendArg(sb, arg3);
				default:
					// we have too many {} so we just ignore them
					return false;
			}
		} else if (argCount < argArray.length) {
			return appendArg(sb, argArray[argCount]);
		} else {
			// we have too many {} so we just ignore them
			return false;
		}
	}

	private boolean appendArg(StringBuilder sb, Object arg) {
		if (arg == UNKNOWN_ARG) {
			// ignore it
			return false;
		} else if (arg == null) {
			// this is what sb.append(null) does
			sb.append("null");
		} else if (arg.getClass().isArray()) {
			// we do a special thing if we have an array argument
			sb.append('[');
			int length = Array.getLength(arg);
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				// may go recursive in case we have an array of arrays
				appendArg(sb, Array.get(arg, i));
			}
			sb.append(']');
		} else if (arg instanceof LogArgumentCreator) {
			// call the method to get the argument which can be null
			String str = ((LogArgumentCreator) arg).createArg();
			sb.append(str);
		} else {
			// might as well do the toString here because we know it isn't null
			sb.append(arg.toString());
		}
		return true;
	}
}
