package com.j256.simplelogging;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;

import com.j256.simplelogging.backend.CommonsLoggingLogBackend;
import com.j256.simplelogging.backend.JavaUtilLogBackend;
import com.j256.simplelogging.backend.LocalLogBackend;
import com.j256.simplelogging.backend.Log4j2LogBackend;
import com.j256.simplelogging.backend.Log4jLogBackend;
import com.j256.simplelogging.backend.NullLogBackend;
import com.j256.simplelogging.backend.Slf4jLoggingLogBackend;

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
	public void testGetFluentLoggerClass() {
		LoggerFactory.setLogBackendFactory(null);
		assertNotNull(LoggerFactory.getFluentLogger(getClass()));
	}

	@Test
	public void testGetFluentLoggerString() {
		assertNotNull(LoggerFactory.getFluentLogger(getClass().getName()));
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
		checkLog(LogBackendType.SLF4J, Slf4jLoggingLogBackend.class, true);
		checkLog(LogBackendType.ANDROID, LocalLogBackend.class, false);
		checkLog(LogBackendType.COMMONS_LOGGING, CommonsLoggingLogBackend.class, true);
		checkLog(LogBackendType.LOG4J2, Log4j2LogBackend.class, true);
		checkLog(LogBackendType.LOG4J, Log4jLogBackend.class, true);
		checkLog(LogBackendType.LOCAL, LocalLogBackend.class, true);
		checkLog(LogBackendType.JAVA_UTIL, JavaUtilLogBackend.class, true);
		checkLog(LogBackendType.NULL, NullLogBackend.class, false);
	}

	@Test
	public void testLogTypeKnownLog() {
		LogBackend backend = LogBackendType.LOCAL.createLogBackend(getClass().getName());
		assertTrue(backend instanceof LocalLogBackend);
	}

	@Test
	public void testGetSimpleClassName() {
		String first = "foo";
		assertEquals(first, LoggerFactory.getSimpleClassName(first));
		String second = "bar";
		String className = first + "." + second;
		assertEquals(second, LoggerFactory.getSimpleClassName(className));
		className = first + ".";
		assertEquals(className, LoggerFactory.getSimpleClassName(className));
	}

	@Test
	public void testLogTypeProperty() {
		LogBackendFactory factory = LoggerFactory.getLogBackendFactory();
		try {
			LoggerFactory.setLogBackendFactory(null);
			System.setProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY, LogBackendType.NULL.name());
			LoggerFactory.getLogger("foo");
			assertEquals(LogBackendType.NULL, LoggerFactory.getLogBackendFactory());
		} finally {
			LoggerFactory.setLogBackendFactory(factory);
			System.clearProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY);
		}
	}

	@Test
	public void testSetLogFactory() {
		OurLogFactory ourLogFactory = new OurLogFactory();
		LogBackend log = createMock(LogBackend.class);
		ourLogFactory.log = log;
		LoggerFactory.setLogBackendFactory(ourLogFactory);

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
		LoggerFactory.setLogBackendFactory(null);
		System.setProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY, "some.wrong.class");
		try {
			// this should work and not throw
			LoggerFactory.getLogger(getClass());
		} finally {
			System.clearProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY);
		}
	}

	@Test
	public void testLogFactoryFind() {
		LoggerFactory.setLogBackendFactory(null);
		System.clearProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY);
		// this should work and not throw
		LoggerFactory.getLogger(getClass());
	}

	@Test
	public void testSetLoggerBackend() {
		try {
			LoggerFactory.setLogBackendType(LogBackendType.LOCAL);
			try {
				LoggerFactory.setLogBackendType(LogBackendType.ANDROID);
			} catch (Exception e) {
				// expected
			}
		} finally {
			LoggerFactory.setLogBackendFactory(null);
		}
	}

	private void checkLog(LogBackendType logType, Class<?> logClass, boolean available) {
		assertEquals(logType + " available should be " + available, available, logType.isAvailable());
		LogBackend backend = logType.createLogBackend(getClass().getName());
		assertNotNull(logType + " should not general null log", backend);
		assertEquals(logClass, backend.getClass());
	}

	@Test
	public void testLogFactoryAsClass() {
		LoggerFactory.setLogBackendFactory(null);
		System.setProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY, OurLogFactory.class.getName());
		try {
			OurLogFactory.lastClassLabel = null;
			// this should work and not throw
			String label = "fopwejfwejfwe";
			LoggerFactory.getLogger(label);
			assertSame(label, OurLogFactory.lastClassLabel);
		} finally {
			System.clearProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY);
		}
	}

	@Test
	public void testLogFactoryAsClassPrivateConstructor() {
		LoggerFactory.setLogBackendFactory(null);
		System.setProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY, OurLogFactoryPrivate.class.getName());
		try {
			OurLogFactoryPrivate.lastClassLabel = null;
			// this shouldn't use the factory because constructor not public
			String label = "fopwejfwejfwe";
			LoggerFactory.getLogger(label);
			assertNull(OurLogFactoryPrivate.lastClassLabel);
		} finally {
			System.clearProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY);
		}
	}

	@Test
	public void testLogFactoryAsClassNotLoggerFactoryBackend() {
		LoggerFactory.setLogBackendFactory(null);
		System.setProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY, Object.class.getName());
		try {
			// this shouldn't use the factory because class is not a LoggerFactoryBackend
			LoggerFactory.getLogger("fopwejfwejfwe");
		} finally {
			System.clearProperty(LoggerConstants.LOG_TYPE_SYSTEM_PROPERTY);
		}
	}

	public static class OurLogFactory implements LogBackendFactory {

		LogBackend log;
		static String lastClassLabel;

		@Override
		public LogBackend createLogBackend(String classLabel) {
			OurLogFactory.lastClassLabel = classLabel;
			return log;
		}
	}

	private static class OurLogFactoryPrivate implements LogBackendFactory {

		LogBackend log;
		static String lastClassLabel;

		@Override
		public LogBackend createLogBackend(String classLabel) {
			OurLogFactory.lastClassLabel = classLabel;
			return log;
		}
	}
}
