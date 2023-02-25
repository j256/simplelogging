package com.j256.simplelogging;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

public class FluentLoggerTest {

	private FluentLogger fluentLogger;
	private LogBackend mockBackend;
	private final Throwable throwable = new Throwable();

	@Before
	public void before() {
		mockBackend = createMock(LogBackend.class);
		fluentLogger = new FluentLogger(mockBackend);
	}

	@Test
	public void testNoOutput() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(false);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").arg(1).log();
		verify(mockBackend);
	}

	@Test
	public void testNormal() {
		FluentLogger.setGlobalLogLevel(Level.TRACE);
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		int arg = 123;
		mockBackend.log(Level.TRACE, "hello " + arg);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").arg(arg).log();
		verify(mockBackend);
	}

	@Test
	public void testArgs() {
		FluentLogger.setGlobalLogLevel(Level.TRACE);
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		int arg1 = 123;
		boolean arg2 = false;
		long arg3 = 456;
		float arg4 = 789;
		mockBackend.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello {} {}").arg(123).args(new Object[] { arg2, arg3, arg4 }).log();
		verify(mockBackend);
	}

	@Test
	public void testExtraArgs() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		int arg1 = 456;
		boolean arg2 = true;
		long arg3 = 4327842372743L;
		mockBackend.log(Level.TRACE, "hello " + arg1);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").arg(arg1).arg(arg2).arg(arg3).log();
		verify(mockBackend);
	}

	@Test
	public void testTooFewArgs() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		int arg1 = 456;
		boolean arg2 = true;
		mockBackend.log(Level.TRACE, "hello " + arg1 + " " + arg2 + " " + /* no arg3 */ " " /* no arg 4 */);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello {} {} {} {}").arg(arg1).arg(arg2).log();
		verify(mockBackend);
	}

	@Test
	public void testNoArgs() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		mockBackend.log(Level.TRACE, "hello {}");
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").log();
		verify(mockBackend);
	}

	@Test
	public void testNoArgStrings() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		mockBackend.log(Level.TRACE, "hello");
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("hello").log();
		verify(mockBackend);
	}

	@Test
	public void testArgsFromMethod() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		double arg1 = 1.0;
		mockBackend.log(Level.TRACE, "hello " + arg1);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE) //
				.msg("hello {}")
				.args(new Object[] { arg1 })
				.log();
		verify(mockBackend);
	}

	@Test
	public void testCoverage() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(false);
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		boolean arg1 = true;
		mockBackend.log(Level.TRACE, "hello " + arg1, throwable);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE) //
				.msg("hello {}")
				.arg(arg1)
				.arg((byte) 1)
				.arg('x')
				.arg((short) 1)
				.arg(1)
				.arg(1L)
				.arg(1.0F)
				.arg(1.0)
				.arg("String")
				.args(new Object[0])
				.throwable(throwable)
				.log();
		fluentLogger.atLevel(Level.TRACE) //
				.msg("hello {}")
				.arg(arg1)
				.arg((byte) 1)
				.arg('x')
				.arg((short) 1)
				.arg(1)
				.arg(1L)
				.arg(1.0F)
				.arg(1.0)
				.arg("String")
				.args(new Object[0])
				.throwable(throwable)
				.log();
		verify(mockBackend);
	}

	@Test
	public void testMsgIncreasesNumArgs() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		char arg1 = 'b';
		short arg2 = 2344;
		mockBackend.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE) //
				.args(new Object[] { arg1 })
				// this increases the number args from 1 to 2
				.msg("hello {} {}")
				.arg(arg2)
				.log();
		verify(mockBackend);
	}

	@Test
	public void testMsgDoesntIncreaseNumArgs() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		float arg1 = 99.0F;
		double arg2 = 2344.0;
		mockBackend.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE) //
				.args(new Object[] { arg1, arg2 })
				.msg("hello {} {}")
				.log();
		verify(mockBackend);
	}

	@Test
	public void testJustThrowable() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		mockBackend.log(Level.TRACE, FluentContextImpl.JUST_THROWABLE_MESSAGE, throwable);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE)//
				.throwable(throwable)
				.log();
		verify(mockBackend);
	}

	@Test
	public void testJustArgs() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		String arg1 = "ewpjfwfwe";
		long arg2 = 1343134;
		mockBackend.log(Level.TRACE, "'" + arg1 + "', '" + arg2 + "'");
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).arg(arg1).arg(arg2).log();
		verify(mockBackend);
	}

	@Test
	public void testNoMessage() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).log();
		verify(mockBackend);
	}

	@Test
	public void testTwoMessages() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		String msg1 = "ewpjfwfwe";
		String msg2 = "fjpefewpjfewjpo";
		mockBackend.log(Level.TRACE, msg1);
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg(msg1).msg(msg2).log();
		verify(mockBackend);
	}

	@Test
	public void testExample() {
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		Object[] args = new Object[] { 1, 2, 3 };
		mockBackend.log(Level.TRACE, "1 + 2 = 3");
		mockBackend.log(Level.TRACE, "integer args: [1, 2, 3]");
		replay(mockBackend);
		fluentLogger.atLevel(Level.TRACE).msg("{} + {} = {}").args(args).log();
		fluentLogger.atLevel(Level.TRACE).msg("integer args: {}").arg(args).log();
		verify(mockBackend);
	}

	@Test
	public void testAtEachLevel() {
		String prefix = "logging level ";
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(true);
		mockBackend.log(Level.TRACE, prefix + Level.TRACE);
		expect(mockBackend.isLevelEnabled(Level.DEBUG)).andReturn(true);
		mockBackend.log(Level.DEBUG, prefix + Level.DEBUG);
		expect(mockBackend.isLevelEnabled(Level.INFO)).andReturn(true);
		mockBackend.log(Level.INFO, prefix + Level.INFO);
		expect(mockBackend.isLevelEnabled(Level.WARNING)).andReturn(true);
		mockBackend.log(Level.WARNING, prefix + Level.WARNING);
		expect(mockBackend.isLevelEnabled(Level.ERROR)).andReturn(true);
		mockBackend.log(Level.ERROR, prefix + Level.ERROR);
		expect(mockBackend.isLevelEnabled(Level.FATAL)).andReturn(true);
		mockBackend.log(Level.FATAL, prefix + Level.FATAL);
		replay(mockBackend);
		fluentLogger.atTrace().msg(prefix + "{}").arg(Level.TRACE).log();
		fluentLogger.atDebug().msg(prefix + "{}").arg(Level.DEBUG).log();
		fluentLogger.atInfo().msg(prefix + "{}").arg(Level.INFO).log();
		fluentLogger.atWarn().msg(prefix + "{}").arg(Level.WARNING).log();
		fluentLogger.atError().msg(prefix + "{}").arg(Level.ERROR).log();
		fluentLogger.atFatal().msg(prefix + "{}").arg(Level.FATAL).log();
		verify(mockBackend);
	}
}
