package com.j256.simplelogging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.junit.Test;

import com.j256.simplelogging.LocalLogBackend;
import com.j256.simplelogging.LogBackend;
import com.j256.simplelogging.LogBackend.Level;

public class LocalLogBackendTest extends BaseLogBackendTest {

	public LocalLogBackendTest() {
		super(new LocalLogBackend("CommonsLoggingLogBackendTest"));
	}

	@Test
	public void testLevelProperty() {
		LogBackend log = new LocalLogBackend("foo");
		if (log.isLevelEnabled(Level.TRACE)) {
			return;
		}
		System.setProperty(LocalLogBackend.LOCAL_LOG_LEVEL_PROPERTY, "TRACE");
		try {
			log = new LocalLogBackend("foo");
			assertTrue(log.isLevelEnabled(Level.TRACE));
		} finally {
			System.clearProperty(LocalLogBackend.LOCAL_LOG_LEVEL_PROPERTY);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidLevelProperty() {
		System.setProperty(LocalLogBackend.LOCAL_LOG_LEVEL_PROPERTY, "not a valid level");
		try {
			new LocalLogBackend("foo");
		} finally {
			System.clearProperty(LocalLogBackend.LOCAL_LOG_LEVEL_PROPERTY);
		}
	}

	@Test(timeout = 10000)
	public void testFileProperty() throws Exception {
		String logPath = "target/foo.txt";
		File logFile = new File(logPath);
		logFile.delete();
		LocalLogBackend.openLogFile(logPath);
		try {
			LocalLogBackend log = new LocalLogBackend("foo");
			assertTrue(log.isLevelEnabled(Level.FATAL));
			String msg = "fpjwefpwejfpwfjwe";
			log.log(Level.FATAL, msg);
			log.flush();
			assertTrue(logFile.exists());
			while (logFile.length() < msg.length()) {
				Thread.sleep(100);
			}
		} finally {
			LocalLogBackend.openLogFile(null);
			logFile.delete();
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidFileProperty() {
		LocalLogBackend.openLogFile("not-a-proper-directory-name-we-hope/foo.txt");
	}

	@Test
	public void testNotEnabled() {
		String logPath = "target/foo.txt";
		File logFile = new File(logPath);
		logFile.delete();
		LocalLogBackend.openLogFile(logPath);
		try {
			LocalLogBackend log = new LocalLogBackend("foo");
			if (log.isLevelEnabled(Level.TRACE)) {
				return;
			}
			String msg = "fpjwefpwejfpwfjwe";
			log.log(Level.TRACE, msg);
			log.flush();
			assertTrue(logFile.exists());
			assertEquals(0, logFile.length());
		} finally {
			LocalLogBackend.openLogFile(null);
			logFile.delete();
		}
	}

	@Test
	public void testInvalidLevelsFile() {
		StringWriter stringWriter = new StringWriter();
		// invalid line
		stringWriter.write("x\n");
		// invalid level
		stringWriter.write("com\\.foo\\.myclass\\.StatementExecutor = INVALID_LEVEL\n");
		LocalLogBackend.readLevelResourceFile(new ByteArrayInputStream(stringWriter.toString().getBytes()));
	}

	@Test
	public void testIoErrorsReadingLevelFile() {
		InputStream errorStream = new InputStream() {
			@Override
			public int read() throws IOException {
				throw new IOException("simulated exception");
			}

			@Override
			public void close() throws IOException {
				throw new IOException("simulated exception");
			}
		};
		LocalLogBackend.readLevelResourceFile(errorStream);
	}

	@Test
	public void testInputStreamNull() {
		LocalLogBackend.readLevelResourceFile(null);
	}

}
