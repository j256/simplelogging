Java Simple Logging
===================

I've written this code into a number of different libraries of mine including [ORMLite](https://ormlite.com/) and I
have decided that it is worth it's own release.  The goal is a small-ish library that backends to a number of standard
logging packages.  This allows you to write your code and include log messages without having a fixed dependency on any
one logging package.  I include this code into my libraries and so they can stay agnostic.  I understand that this is
the goal of commons-logging as well but I really want the slf4j-style {} arguments which aren't supported.  This logging
code allows you to write messages with the {} argument support and then backend to any number of other logging libraries
or you can easily implement your own.  It also handles arrays appropriately and supports up to 4 arguments before
forcing the caller to pass in an object array.

* The source code be found on the [git repository](https://github.com/j256/simplelogging).  [![CircleCI](https://circleci.com/gh/j256/simplelogging.svg?style=svg)](https://circleci.com/gh/j256/simplelogging) [![CodeCov](https://img.shields.io/codecov/c/github/j256/simplelogging.svg)](https://codecov.io/github/j256/simplelogging/)
* Maven packages are published via [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/)

# Supported Logging Backends

The following logging implementations will be discovered on the classpath in this order.

1. SLF4J (often paired with logback)
2. Android native Log
3. Apache Commons Logging
4. LOG4J2 (version 2+)
5. LOG4J (older)
6. Local log implementation that can write to a simple file.
7. Java util logging which is usually available in the JRE but never chosen directly. 

# Getting Started

To get started you instantiate a `Logger` using the `LoggerFactory`:
```java
// usually a logger will be per-class, getLogger() also can take a String label
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
...
// log trace message with arguments. toString() on the args only called if trace messages enabled
logger.trace("some trace information: {} and {}", arg1, arg2);
...
// NOTE: exception argument comes _before_ the message format to not confuse the arguments
logger.error(exception, "http client threw getting URL: {}", url);
```

Enjoy,
Gray Watson
