package com.j256.simplelogging.backend;

import java.util.concurrent.atomic.AtomicLong;

import com.j256.simplelogging.Level;
import com.j256.simplelogging.LogBackend;
import com.j256.simplelogging.LogBackendFactory;
import com.j256.simplelogging.LoggerConstants;

/**
 * Log backend that delegates to the internal Android logger.
 * 
 * From SimpleLogging: https://github.com/j256/simplelogging
 *
 * <p>
 * To see log messages you will do something like:
 * </p>
 * 
 * <pre>
 * adb shell setprop log.tag.YourActivity VERBOSE
 * </pre>
 * 
 * <p>
 * <b>NOTE:</b> Unfortunately, Android variables are limited in size so this class takes that last 23 (sic) characters
 * of the class name if it is larger than 23 characters. For example, if the class is AndroidDatabaseConnection you
 * would do:
 * </p>
 * 
 * <pre>
 * adb shell setprop log.tag.droidDatabaseConnection VERBOSE
 * </pre>
 * 
 * <p>
 * To see all log messages use "log.tag." followed by the constant in {@link LoggerConstants#ANDROID_ALL_LOGS_NAME}.
 * Something like:
 * </p>
 * 
 * <pre>
 * adb shell setprop log.tag.simplelogging DEBUG
 * </pre>
 * 
 * @author graywatson
 */
public class AndroidLogBackend implements LogBackend {

	/** check to see if the android level has changed every so often */
	private final static int REFRESH_LEVEL_CACHE_EVERY = 200;
	/** maximum allowed length of the tag per Android spec */
	private final static int MAX_TAG_LENGTH = 23;
	private final String className;

	// we do this because supposedly Log.isLoggable() always does IO so we want to slow down the refreshes
	private final AtomicLong levelCacheCount = new AtomicLong();
	private final boolean levelCache[];

	public AndroidLogBackend(String className) {
		// get the last part of the class name
		String simpleName;
		// get the last part of the class name
		int index = className.lastIndexOf('.');
		if (index < 0 || index == className.length() - 1) {
			simpleName = className;
		} else {
			simpleName = className.substring(index + 1);
		}

		// make sure that our tag length is not too long
		int length = simpleName.length();
		if (length > MAX_TAG_LENGTH) {
			simpleName = className.substring(length - MAX_TAG_LENGTH, length);
		}
		this.className = simpleName;
		// find the maximum level value
		int maxLevel = 0;
		for (Level level : Level.values()) {
			int androidLevel = levelToAndroidLevel(level);
			if (androidLevel > maxLevel) {
				maxLevel = androidLevel;
			}
		}
		levelCache = new boolean[maxLevel + 1];
		refreshLevelCache();
	}

	@Override
	public boolean isLevelEnabled(Level level) {
		// we don't care if this is not synchronized, it will be updated sooner or later and multiple updates are fine.
		if (levelCacheCount.incrementAndGet() >= REFRESH_LEVEL_CACHE_EVERY) {
			refreshLevelCache();
			// publishes the update as well
			levelCacheCount.set(0);
		}
		int androidLevel = levelToAndroidLevel(level);
		if (androidLevel < levelCache.length) {
			return levelCache[androidLevel];
		} else {
			return doIsLevelEnabled(androidLevel);
		}
	}

	@Override
	public void log(Level level, String msg) {
		switch (level) {
			case TRACE:
				android.util.Log.v(className, msg);
				break;
			case DEBUG:
				android.util.Log.d(className, msg);
				break;
			/* INFO below */
			case WARNING:
				android.util.Log.w(className, msg);
				break;
			case ERROR:
				android.util.Log.e(className, msg);
				break;
			case FATAL:
				// no fatal level
				android.util.Log.e(className, msg);
				break;
			case INFO:
			default:
				android.util.Log.i(className, msg);
				break;
		}
	}

	@Override
	public void log(Level level, String msg, Throwable t) {
		switch (level) {
			case TRACE:
				android.util.Log.v(className, msg, t);
				break;
			case DEBUG:
				android.util.Log.d(className, msg, t);
				break;
			/* INFO below */
			case WARNING:
				android.util.Log.w(className, msg, t);
				break;
			case ERROR:
				android.util.Log.e(className, msg, t);
				break;
			case FATAL:
				// no level higher than e
				android.util.Log.e(className, msg, t);
				break;
			case INFO:
			default:
				android.util.Log.i(className, msg, t);
				break;
		}
	}

	private void refreshLevelCache() {
		Level enabledLevel = null;
		for (Level level : Level.values()) {
			int androidLevel = levelToAndroidLevel(level);
			if (androidLevel >= levelCache.length) {
				continue;
			}
			boolean enabled;
			if (enabledLevel != null && enabledLevel.isEnabled(level)) {
				// no need for us to check WARNING if INFO enabled
				enabled = true;
			} else {
				enabled = doIsLevelEnabled(androidLevel);
				if (enabled && enabledLevel == null) {
					enabledLevel = level;
				}
			}
			levelCache[androidLevel] = enabled;
		}
	}

	private boolean doIsLevelEnabled(int androidLevel) {
		// this is supposedly expensive with an IO operation for each call so we cache them into levelCache[]
		return (android.util.Log.isLoggable(className, androidLevel)
				|| android.util.Log.isLoggable(LoggerConstants.ANDROID_ALL_LOGS_NAME, androidLevel));
	}

	private int levelToAndroidLevel(Level level) {
		switch (level) {
			case TRACE:
				return android.util.Log.VERBOSE;
			case DEBUG:
				return android.util.Log.DEBUG;
			/* INFO below */
			case WARNING:
				return android.util.Log.WARN;
			case ERROR:
				return android.util.Log.ERROR;
			case FATAL:
				return android.util.Log.ERROR;
			case INFO:
			default:
				return android.util.Log.INFO;
		}
	}

	/**
	 * Factory for generating AndroidLogBackend instances.
	 */
	public static class AndroidLogBackendFactory implements LogBackendFactory {
		@Override
		public boolean isAvailable() {
			// if we were able to load the classes here then it is available.
			return true;
		}

		@Override
		public LogBackend createLogBackend(String classLabel) {
			return new AndroidLogBackend(classLabel);
		}
	}
}
