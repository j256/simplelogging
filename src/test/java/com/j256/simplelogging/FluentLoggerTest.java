package com.j256.simplelogging;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

public class FluentLoggerTest {

	private FluentLogger fluentLogger;
	private LogBackend mockLog;
	private final Throwable throwable = new Throwable();

	@Before
	public void before() {
		mockLog = createMock(LogBackend.class);
		fluentLogger = new FluentLogger(new Logger(mockLog));
	}

	@Test
	public void testNoOutput() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(false);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").arg(1).log();
		verify(mockLog);
	}

	@Test
	public void testNormal() {
		FluentLogger.setGlobalLogLevel(Level.TRACE);
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		int arg = 123;
		mockLog.log(Level.TRACE, "hello " + arg);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").arg(arg).log();
		verify(mockLog);
	}

	@Test
	public void testArgs() {
		FluentLogger.setGlobalLogLevel(Level.TRACE);
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		int arg1 = 123;
		boolean arg2 = false;
		long arg3 = 456;
		float arg4 = 789;
		mockLog.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello {} {}").arg(123).args(new Object[] { arg2, arg3, arg4 }).log();
		verify(mockLog);
	}

	@Test
	public void testExtraArgs() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		int arg1 = 456;
		boolean arg2 = true;
		mockLog.log(Level.TRACE, "hello " + arg1);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").arg(arg1).arg(arg2).log();
		verify(mockLog);
	}

	@Test
	public void testNoArgs() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		mockLog.log(Level.TRACE, "hello {}");
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").log();
		verify(mockLog);
	}

	@Test
	public void testNoArgStrings() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		mockLog.log(Level.TRACE, "hello");
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello").log();
		verify(mockLog);
	}

	@Test
	public void testArgsFromMethod() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		double arg1 = 1.0;
		mockLog.log(Level.TRACE, "hello " + arg1);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE) //
				.msg("hello {}")
				.args(new Object[] { arg1 })
				.log();
		verify(mockLog);
	}

	@Test
	public void testCoverage() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(false);
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		boolean arg1 = true;
		mockLog.log(Level.TRACE, "hello " + arg1, throwable);
		replay(mockLog);
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
		verify(mockLog);
	}

	@Test
	public void testStringIncreasesNumArgs() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		char arg1 = 'b';
		short arg2 = 2344;
		mockLog.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE) //
				.args(new Object[] { arg1 })
				.msg("hello {} {}")
				.arg(arg2)
				.log();
		verify(mockLog);
	}

	@Test
	public void testStringDoesntIncreaseNumArgs() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		float arg1 = 99.0F;
		double arg2 = 2344.0;
		mockLog.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE) //
				.args(new Object[] { arg1, arg2 })
				.msg("hello {} {}")
				.log();
		verify(mockLog);
	}

	@Test
	public void testJustThrowable() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		mockLog.log(Level.TRACE, FluentContextImpl.JUST_THROWABLE_MESSAGE, throwable);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE)//
				.throwable(throwable)
				.log();
		verify(mockLog);
	}

	@Test
	public void testJustArgs() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		String arg1 = "ewpjfwfwe";
		long arg2 = 1343134;
		mockLog.log(Level.TRACE, "'" + arg1 + "', '" + arg2 + "'");
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).arg(arg1).arg(arg2).log();
		verify(mockLog);
	}

	@Test
	public void testNoMessage() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).log();
		verify(mockLog);
	}

	@Test
	public void testTwoMessages() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		String msg1 = "ewpjfwfwe";
		String msg2 = "fjpefewpjfewjpo";
		mockLog.log(Level.TRACE, msg1);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg(msg1).msg(msg2).log();
		verify(mockLog);
	}
}
