package com.j256.simplelogging;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class LogArgumentCreatorTest {

	private Logger logger;
	private LogBackend mockLog;

	@Before
	public void before() {
		mockLog = createMock(LogBackend.class);
		logger = new Logger(mockLog);
		assertSame(mockLog, logger.getLogBackend());
	}

	@Test
	public void testArgAtStart() {
		String arg = "x";
		String end = " yyy";
		expect(mockLog.isLevelEnabled(Level.TRACE)).andReturn(false);
		expect(mockLog.isLevelEnabled(Level.INFO)).andReturn(true);
		mockLog.log(Level.INFO, arg + end);
		LogArgumentCreator logMessageCreator = createMock(LogArgumentCreator.class);
		expect(logMessageCreator.createArg()).andReturn(arg);
		replay(mockLog, logMessageCreator);
		// this shouldn't be logged which means no calls to the creator
		logger.trace("{}" + end, logMessageCreator);
		// this should be logged
		logger.info("{}" + end, logMessageCreator);
		verify(mockLog, logMessageCreator);
	}
}
