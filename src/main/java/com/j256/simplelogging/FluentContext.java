package com.j256.simplelogging;

/**
 * Context for our fluent logger calls from {@link FluentLogger}. The {@link #msg(String)} method should be called to
 * set the actual log output. To end the chain and log the message you should call the {@link #log()} method.
 * 
 * @author graywatson
 */
public interface FluentContext {

	/**
	 * Set the required log message on the context.
	 */
	public FluentContext msg(String message);

	/**
	 * Set the optional throwable on the context.
	 */
	public FluentContext throwable(Throwable th);

	/**
	 * Set the number of arguments so that the context doesn't have to guess. This can be wrong and won't cause an error
	 * if it is incorrect but it may save some array reallocations and copies if it is correct.
	 */
	public FluentContext numArgs(int numArgs);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(Object arg);

	/**
	 * Add an array of arguments to the log message.
	 */
	public FluentContext args(Object[] args);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(boolean arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(byte arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(char arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(short arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(int arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(long arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(float arg);

	/**
	 * Add argument to the log message.
	 */
	public FluentContext arg(double arg);

	/**
	 * Log the message to output.
	 */
	public void log();
}
