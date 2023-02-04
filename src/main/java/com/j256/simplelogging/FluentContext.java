package com.j256.simplelogging;

/**
 * Context for our fluent logger calls that is returned by a call to {@link FluentLogger#atLevel(Level)}. The
 * {@link #msg(String)} method should be called once to set the message format for the log output. To end the chain and
 * write out the message you should call the {@link #log()} method.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 *
 * @author graywatson
 */
public interface FluentContext {

	/**
	 * Set the required log message on the context. Only the first call to this method is honored.
	 */
	public FluentContext msg(String message);

	/**
	 * Set the optional throwable on the context.
	 */
	public FluentContext throwable(Throwable th);

	/**
	 * Add object argument to the log message.
	 */
	public FluentContext arg(Object arg);

	/**
	 * Add boolean argument to the log message.
	 */
	public FluentContext arg(boolean arg);

	/**
	 * Add byte primitive argument to the log message.
	 */
	public FluentContext arg(byte arg);

	/**
	 * Add char primitive argument to the log message.
	 */
	public FluentContext arg(char arg);

	/**
	 * Add short primitive argument to the log message.
	 */
	public FluentContext arg(short arg);

	/**
	 * Add int primitive argument to the log message.
	 */
	public FluentContext arg(int arg);

	/**
	 * Add long primitive argument to the log message.
	 */
	public FluentContext arg(long arg);

	/**
	 * Add float primitive argument to the log message.
	 */
	public FluentContext arg(float arg);

	/**
	 * Add double primitive argument to the log message.
	 */
	public FluentContext arg(double arg);

	/**
	 * Add an array of object arguments to the log message, each element of which will match a {} from the message. To
	 * add an array to be associated with a single {} and displayed as [arg1, arg2, ...] then you need to use the method
	 * {@link #arg(Object)}. If you don't do this then the single {} will only display the first element of the array
	 * passed in here.
	 * 
	 * For example, the following code which calls this method will output: "1 + 2 = 3"
	 * 
	 * <pre>
	 * logger.msg("{} + {} = {}").args(new Object[] { 1, 2, 3 }).log();
	 * </pre>
	 * 
	 * While the following code which calls {@link #arg(Object)} will output: "integer args: [1, 2, 3]"
	 * 
	 * <pre>
	 * logger.msg("integer args: {}").arg(new Object[] { 1, 2, 3 }).log();
	 * </pre>
	 */
	public FluentContext args(Object[] args);

	/**
	 * Log the message to output if the level is enabled. Must be at the end of the method call chain.
	 */
	public void log();
}
