Java Simple Logging
===================

I've written this code into a number of different systems of mine including ORMLite and I [finally] have decided that
it is worth it's own release.  The goal is a small-ish library that backends to a number of standard logging packages.
This allows you to write a library and include log messages without having a fixed dependency on any one logging
package.  I include this code into my libraries and so they can stay agnostic.  The logging code allows you to write
messages with slf4j-style {} arguments and then backend to any number of other logging libraries or you can easily
implement your own.

* The source code be found on the [git repository](https://github.com/j256/simplelogging).  [![CircleCI](https://circleci.com/gh/j256/simplelogging.svg?style=svg)](https://circleci.com/gh/j256/simplelogging) [![CodeCov](https://img.shields.io/codecov/c/github/j256/simplelogging.svg)](https://codecov.io/github/j256/simplelogging/)
* Maven packages are published via [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/)

# Supported Logging Backends

The following logging backends will be discovered on the classpath in this order.

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
// log trace messagge with arguments. toString() on the args only called if trace messages enabled
logger.trace("some trace information: {} and {}", arg1, arg2);
...
// exception messages coment _before_ the message format to not confuse the arguments
logger.error(exception, "http client threw an exception getting URL: {}", url);
```

Enjoy,
Gray Watson
