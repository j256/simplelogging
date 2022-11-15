package com.j256.simplelogging;

import java.util.Arrays;

/**
 * Context implementation that records the message and associated arguments and calls through to
 * {@link Logger#log(Level, Throwable, String)} or other methods when the {@link #log()} method is called.
 * 
 * @author graywatson
 */
public class FluentContextImpl implements FluentContext {

	private final static int DEFAULT_NUM_ARGS = 3;
	final static String JUST_THROWABLE_MESSAGE = "throwable";

	private final Logger logger;
	private final Level level;
	private String msg;
	private Throwable throwable;
	private Object[] args;
	private int argCount;

	public FluentContextImpl(Logger logger, Level level) {
		this.logger = logger;
		this.level = level;
	}

	@Override
	public FluentContext msg(String msg) {
		if (this.msg != null) {
			// only the first call is honored in case we want to set max arguments
			return this;
		}
		this.msg = msg;

		// now we count the number of arguments to initialize our arguments array
		int count = 0;
		int index = 0;
		while (true) {
			int found = msg.indexOf(Logger.ARG_STRING, index);
			if (found < 0) {
				break;
			}
			count++;
			index = found + Logger.ARG_STRING.length();
		}
		if (count > 0) {
			if (args == null) {
				args = new Object[count];
			} else if (args.length < count) {
				args = Arrays.copyOf(args, count);
			}
		}
		return this;
	}

	@Override
	public FluentContext throwable(Throwable throwable) {
		this.throwable = throwable;
		return this;
	}

	@Override
	public FluentContext arg(Object arg) {
		addArg(arg);
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
	public void log() {
		if (msg == null) {
			// if we have no message but we do have arguments then build a message like: '{}', '{}', ...
			if (argCount > 0) {
				StringBuilder sb = new StringBuilder(Logger.DEFAULT_FULL_MESSAGE_LENGTH);
				for (int i = 0; i < argCount; i++) {
					if (i > 0) {
						sb.append(", ");
					}
					// might as well expand the entire string here
					sb.append('\'').append(args[i]).append('\'');
				}
				logger.log(level, throwable, sb.toString());
			} else if (throwable == null) {
				// ignore log line if no message, args, or throwable
			} else {
				// just log a throwable with a minimal message
				logger.log(level, throwable, JUST_THROWABLE_MESSAGE);
			}
		} else if (argCount == 0) {
			// no arguments
			logger.log(level, throwable, msg);
		} else {
			if (argCount != args.length) {
				// make the array smaller otherwise we may get null args in the message
				args = Arrays.copyOf(args, argCount);
			}
			logger.log(level, throwable, msg, args);
		}
		// null the fields to help the gc
		msg = null;
		throwable = null;
		args = null;
		argCount = 0;
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
