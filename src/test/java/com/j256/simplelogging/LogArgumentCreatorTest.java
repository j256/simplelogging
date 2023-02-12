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
	private LogBackend mockBackend;

	@Before
	public void before() {
		mockBackend = createMock(LogBackend.class);
		logger = new Logger(mockBackend);
		assertSame(mockBackend, logger.getLogBackend());
	}

	@Test
	public void testArgAtStart() {
		String arg = "x";
		String end = " yyy";
		expect(mockBackend.isLevelEnabled(Level.TRACE)).andReturn(false);
		expect(mockBackend.isLevelEnabled(Level.INFO)).andReturn(true);
		mockBackend.log(Level.INFO, arg + end);
		LogArgumentCreator logMessageCreator = createMock(LogArgumentCreator.class);
		expect(logMessageCreator.createLogArg()).andReturn(arg);
		replay(mockBackend, logMessageCreator);
		// this shouldn't be logged which means no calls to the creator
		logger.trace("{}" + end, logMessageCreator);
		// this should be logged
		logger.info("{}" + end, logMessageCreator);
		verify(mockBackend, logMessageCreator);
	}
}
