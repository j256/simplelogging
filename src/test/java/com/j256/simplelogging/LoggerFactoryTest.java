package com.j256.simplelogging;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;

import com.j256.simplelogging.Log.Level;
import com.j256.simplelogging.LoggerFactory.LogFactory;
import com.j256.simplelogging.LoggerFactory.LogType;

public class LoggerFactoryTest {

	@Test
	public void testGetLoggerClass() {
		assertNotNull(LoggerFactory.getLogger(getClass()));
	}

	@Test
	public void testGetLoggerString() {
		assertNotNull(LoggerFactory.getLogger(getClass().getName()));
	}

	@Test
	public void testConstructor() throws Exception {
		@SuppressWarnings("rawtypes")
		Constructor[] constructors = LoggerFactory.class.getDeclaredConstructors();
		assertEquals(1, constructors.length);
		constructors[0].setAccessible(true);
		constructors[0].newInstance();
	}

	@Test
	public void testLogTypes() {
		checkLog(LogType.SLF4J, Slf4jLoggingLog.class, true);
		checkLog(LogType.ANDROID, LocalLog.class, false);
		checkLog(LogType.COMMONS_LOGGING, CommonsLoggingLog.class, true);
		checkLog(LogType.LOG4J2, Log4j2Log.class, true);
		checkLog(LogType.LOG4J, Log4jLog.class, true);
		checkLog(LogType.LOCAL, LocalLog.class, true);
		checkLog(LogType.JAVA_UTIL, JavaUtilLog.class, true);
		checkLog(LogType.NULL, NullLog.class, false);
	}

	@Test
	public void testLogTypeKnownLog() {
		Log log = LoggerFactory.LogType.LOCAL.createLog(getClass().getName());
		assertTrue(log instanceof LocalLog);
	}

	@Test
	public void testGetSimpleClassName() {
		String first = "foo";
		String name = LoggerFactory.getSimpleClassName(first);
		assertEquals(first, name);
		String second = "bar";
		String className = first + "." + second;
		name = LoggerFactory.getSimpleClassName(className);
		assertEquals(second, name);
	}

	@Test
	public void testSetLogFactory() {
		OurLogFactory ourLogFactory = new OurLogFactory();
		Log log = createMock(Log.class);
		ourLogFactory.log = log;
		LoggerFactory.setLogFactory(ourLogFactory);

		String message = "hello";
		expect(log.isLevelEnabled(Level.INFO)).andReturn(true);
		log.log(Level.INFO, message);

		replay(log);
		Logger logger = LoggerFactory.getLogger("test");
		logger.info(message);
		verify(log);
	}

	@Test
	public void testLogFactoryProperty() {
		LoggerFactory.setLogFactory(null);
		String logTypeProp = System.getProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY);
		System.setProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY, "some.wrong.class");
		try {
			// this should work and not throw
			LoggerFactory.getLogger(getClass());
		} finally {
			if (logTypeProp == null) {
				System.clearProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY);
			} else {
				System.setProperty(LoggerFactory.LOG_TYPE_SYSTEM_PROPERTY, logTypeProp);
			}
		}
	}

	private void checkLog(LogType logType, Class<?> logClass, boolean available) {
		assertEquals(logType + " available should be " + available, available, logType.isAvailable());
		Log log = logType.createLog(getClass().getName());
		assertNotNull(logType + " should not general null log", log);
		assertEquals(logClass, log.getClass());
	}

	private static class OurLogFactory implements LogFactory {

		Log log;

		@Override
		public Log createLog(String classLabel) {
			return log;
		}
	}
}
