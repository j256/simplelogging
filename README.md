Java Simple Logging
===================

The goal of this library is to be a small logging facade that backends to a number of standard logging packages and that
can be copied into another project.  This allows you to write your code and include log messages without having a fixed
dependency on any one logging package.  I include this code into my libraries and so they can stay agnostic.  This
ogging code allows you to write messages with the slf4j-style `{}` argument support and then backend to a number
of other logging libraries or you can easily implement your own.  It also handles arrays appropriately and supports up
to 4 arguments before forcing the caller to pass in an object array or calling a different `logArgs(...)` method
for variable arguments.

I understand that this facade seems similar to other logging systems which separate their API from the implementation.
What I don't like the requirement on classpath order to satisfy the connection between the API and the backend.
SimpleLogging includes calls directly to specific backend APIs which can be chosen through code or configuration.
This direct calling allows for more control over the backend selection and possibly fewer dependencies.

* The source code be found on the [git repository](https://github.com/j256/simplelogging).  [![CircleCI](https://circleci.com/gh/j256/simplelogging.svg?style=svg)](https://circleci.com/gh/j256/simplelogging) [![CodeCov](https://img.shields.io/codecov/c/github/j256/simplelogging.svg)](https://codecov.io/github/j256/simplelogging/)
* Maven packages are published via [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/) [![javadoc](https://javadoc.io/badge2/com.j256.simplelogging/simplelogging/javadoc.svg)](https://javadoc.io/doc/com.j256.simplelogging/simplelogging)

Enjoy.  Gray Watson

# Supported Logging Backends

The following logging implementations will be discovered on the classpath in this order.

1. SLF4J (often paired with logback)
2. Android native Log
3. Logback directly (using slf4j-api)
4. Apache Commons Logging
5. LOG4J2 (version 2+)
6. LOG4J (older)
7. AWS lambda logging.
8. Local log implementation that can write to a simple file.
9. Simple console output.
10. Java util logging which is usually available in the JRE but never chosen directly. 

# Getting Started

To get started you instantiate a `Logger` using the `LoggerFactory`:

```java
// usually a logger will be per-class, getLogger() also can take a String label
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
...
// log trace message with arguments
// toString() on the args is only called if trace messages is enabled
logger.trace("some trace information: {} and {}", arg1, arg2);
...
// NOTE: exception argument comes _before_ the message format to not confuse the arguments
logger.error(exception, "http client threw getting URL: {}", url);
```

# Maven Configuration

* Maven packages are published via [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/)

``` xml
<dependency>
	<groupId>com.j256.simplelogging</groupId>
	<artifactId>simplelogging</artifactId>
	<version>1.8</version>
</dependency>
```

# Dependencies

Simplelogging has no direct dependencies.  It has a number of optional dependencies that will only be referenced if
they are already in your application's classpath.

# ChangeLog Release Notes

See the [ChangeLog.txt file](src/main/javadoc/doc-files/changelog.txt).
