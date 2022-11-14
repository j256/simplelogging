package com.j256.simplelogging;

import java.util.Arrays;

/**
 * "Fluent" version of the the {@link Logger} class which uses chaining as opposed to many methods to support the
 * logging calls which causes no additional object if logging level is not enabled. Idea from Google's fluent logging
 * (https://google.github.io/flogger/).
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 *
 * @author graywatson
 */
public class FluentLogger {

	private final static int DEFAULT_NUM_ARGS = 3;
	final static String JUST_THROWABLE_MESSAGE = "got throwable";
	private final static MuteContext MUTE_CONTEXT = new MuteContext();

	private final Logger logger;

	public FluentLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * Set the log level for all of the loggers. This should be done very early in an application's main or launch
	 * methods. It allows the caller to set a filter on all log messages. Set to null to disable any global log level
	 * filtering of messages and go back to the per-log level matching.
	 * 
	 * NOTE: this is a call through to {@link Logger#setGlobalLogLevel(Level)}.
	 */
	public static void setGlobalLogLevel(Level level) {
		Logger.setGlobalLogLevel(level);
	}

	/**
	 * Return true if logging level is enabled else false.
	 */
	public boolean isLevelEnabled(Level level) {
		return logger.isLevelEnabled(level);
	}

	/**
	 * Start of the chaining that sets the level of the message. If this level is not enabled then this method returns a
	 * mute context and is in effect a no-op.
	 */
	public FluentContext atLevel(Level level) {
		if (isLevelEnabled(level)) {
			return new StandardContext(logger, level);
		} else {
			return MUTE_CONTEXT;
		}
	}

	/**
	 * Standard context that records the message and associated arguments.
	 */
	private static class StandardContext implements FluentContext {

		private final Logger logger;
		private final Level level;
		private String message;
		private Throwable throwable;
		private Object[] args;
		private int argCount;

		public StandardContext(Logger logger, Level level) {
			this.logger = logger;
			this.level = level;
		}

		@Override
		public FluentContext msg(String message) {
			this.message = message;
			return this;
		}

		@Override
		public FluentContext throwable(Throwable throwable) {
			this.throwable = throwable;
			return this;
		}

		@Override
		public FluentContext numArgs(int numArgs) {
			if (numArgs <= 0) {
				// ignore
			} else if (args == null) {
				args = new Object[numArgs];
			} else if (numArgs > args.length) {
				args = Arrays.copyOf(args, numArgs);
			} else {
				// not much point for us to adjust the args down now
			}
			return this;
		}

		@Override
		public FluentContext arg(Object arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext args(Object[] addArgs) {
			if (this.args == null) {
				args = addArgs;
				argCount = addArgs.length;
			} else {
				// extend the array if necessary
				if (args.length - argCount < addArgs.length) {
					this.args = Arrays.copyOf(args, argCount + addArgs.length);
				}
				for (int i = 0; i < addArgs.length; i++) {
					args[argCount++] = addArgs[i];
				}
			}
			return this;
		}

		@Override
		public FluentContext arg(boolean arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(byte arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(char arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(short arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(int arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(long arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(float arg) {
			addArg(arg);
			return this;
		}

		@Override
		public FluentContext arg(double arg) {
			addArg(arg);
			return this;
		}

		@Override
		public void log() {
			if (message == null) {
				if (throwable == null) {
					// ignore the log line
				} else {
					logger.log(level, throwable, JUST_THROWABLE_MESSAGE);
				}
			} else if (argCount == 0 || args == null) {
				logger.log(level, throwable, message);
			} else {
				if (argCount != args.length) {
					// make the array smaller otherwise we may get null args in the message
					args = Arrays.copyOf(args, argCount);
				}
				logger.log(level, throwable, message, args);
			}
		}

		private void addArg(Object arg) {
			if (args == null) {
				args = new Object[DEFAULT_NUM_ARGS];
			} else if (argCount >= args.length) {
				args = Arrays.copyOf(args, args.length * 2);
			}
			args[argCount++] = arg;
		}
	}

	/**
	 * Context that doesn't do anything.
	 */
	private static class MuteContext implements FluentContext {

		@Override
		public FluentContext msg(String message) {
			return this;
		}

		@Override
		public FluentContext throwable(Throwable th) {
			return this;
		}

		@Override
		public FluentContext numArgs(int numArgs) {
			return this;
		}

		@Override
		public FluentContext arg(Object arg) {
			return this;
		}

		@Override
		public FluentContext args(Object[] args) {
			return this;
		}

		@Override
		public FluentContext arg(boolean arg) {
			return this;
		}

		@Override
		public FluentContext arg(byte arg) {
			return this;
		}

		@Override
		public FluentContext arg(char arg) {
			return this;
		}

		@Override
		public FluentContext arg(short arg) {
			return this;
		}

		@Override
		public FluentContext arg(int arg) {
			return this;
		}

		@Override
		public FluentContext arg(long arg) {
			return this;
		}

		@Override
		public FluentContext arg(float arg) {
			return this;
		}

		@Override
		public FluentContext arg(double arg) {
			return this;
		}

		@Override
		public void log() {
			// no-op
		}
	}
}
