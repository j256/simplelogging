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
	public void testArgsFromMethod() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		double arg1 = 1.0;
		mockLog.log(Level.TRACE, "hello " + arg1);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).msg("hello {}").args(new Object[] { arg1 }).log();
		verify(mockLog);
	}

	@Test
	public void testCoverage() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(false);
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		boolean arg1 = true;
		mockLog.log(Level.TRACE, "hello " + arg1, throwable);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE)
				.msg("hello {}")
				.numArgs(100)
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
		fluentLogger.atLevel(Level.TRACE)
				.msg("hello {}")
				.numArgs(100)
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
	public void testNumArgs() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		char arg1 = 'b';
		short arg2 = 2344;
		mockLog.log(Level.TRACE, "hello " + arg1 + " " + arg2);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE)
				.msg("hello {} {}")
				.numArgs(-1)
				.numArgs(-1)
				.numArgs(3)
				.arg(arg1)
				.arg(arg2)
				.numArgs(4)
				.numArgs(1)
				.log();
		verify(mockLog);
	}

	@Test
	public void testNoMessage() {
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(true).times(2);
		mockLog.log(Level.TRACE, FluentLogger.EMPTY_MESSAGE);
		replay(mockLog);
		fluentLogger.atLevel(Level.TRACE).arg("ignored").log();
		verify(mockLog);
	}
}
