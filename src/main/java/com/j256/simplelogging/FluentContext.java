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
	 * Add argument to the log message.
	 */
	public FluentContext arg(Object arg);

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
	 * Add an array of arguments to the log message.
	 */
	public FluentContext args(Object[] args);

	/**
	 * Log the message to output if the level is enabled.
	 */
	public void log();
}