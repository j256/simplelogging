Java Simple Logging Package
===========================

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://mvnrepository.com/artifact/com.j256.simplelogging/simplelogging/latest)
[![javadoc](https://javadoc.io/badge2/com.j256.simplelogging/simplelogging/javadoc.svg)](https://javadoc.io/doc/com.j256.simplelogging/simplelogging)
[![ChangeLog](https://img.shields.io/github/v/release/j256/simplelogging?label=changelog&display_name=release)](https://github.com/j256/simplelogging/blob/master/src/main/javadoc/doc-files/changelog.txt)
[![Documentation](https://img.shields.io/github/v/release/j256/simplelogging?label=documentation&display_name=release)](https://htmlpreview.github.io/?https://github.com/j256/simplelogging/blob/master/src/main/javadoc/doc-files/simplelogging.html)
[![CodeCov](https://img.shields.io/codecov/c/github/j256/simplelogging.svg)](https://codecov.io/github/j256/simplelogging/)
[![CircleCI](https://circleci.com/gh/j256/simplelogging.svg?style=shield)](https://circleci.com/gh/j256/simplelogging)
[![GitHub License](https://img.shields.io/github/license/j256/simplelogging)](https://github.com/j256/simplelogging/blob/master/LICENSE.txt)

The goal of this library is to be a small logging facade that backends to a number of standard logging packages and that
can be copied into another project.  This allows you to write your code and include log messages without having a fixed
dependency on any one logging package.  I include this code into my libraries and so they can stay agnostic.  This
logging code allows you to write messages with the slf4j-style `{}` argument support, handles arrays appropriately, and
supports up to 4 arguments before forcing the caller to pass in an object array or calling a different `logArgs(...)`
method for variable arguments.  It also supports "fluent" logging where you can chain log methods together to build your
log message which generates no objects if the log level is not enabled.

This library is similar to other logging systems which separate the API from the logging implementation. SimpleLogging
is better than the others because it calls directly to specific backend APIs which can be chosen through code or
configuration. this direct calling allows for more control over the backend selection and usually fewer dependencies.

SimpleLogging is also designed to be copied into your open source project so you don't have to add a maven dependency.
Just copy the java files from ``src/main/java`` into your source tree and rename the packages as necessary.  Please also
copy the ``SIMPLELOGGING_LICENSE.txt`` file which is the very permissive ISC license.  You may want to change the
constants in ``LoggerConstants.java``.

* The source code be found on the [git repository](https://github.com/j256/simplelogging)
* Maven packages are published via [Maven Central](https://mvnrepository.com/artifact/com.j256.simplelogging/simplelogging/latest)
* [Documentation for the library](https://htmlpreview.github.io/?https://github.com/j256/simplelogging/blob/master/src/main/javadoc/doc-files/simplelogging.html).  More on the [home page](https://256stuff.com/sources/simplelogging/).
* [Javadoc documentation](https://javadoc.io/doc/com.j256.simplelogging/simplelogging)

Enjoy.  Gray Watson

# Supported Logging Backends

The following logging implementations will be discovered in this order.  You can also select a machine through
code or configuration.

1. Android native Log
2. Logback directly (using slf4j-api)
3. LOG4J2 (version 2+)
4. LOG4J (older, accessed only through reflection if already on the classpath so no dependencies)
5. AWS lambda logging.
6. SLF4J (often paired with logback)
8. Apache Commons Logging
8. Local log implementation that can write to a simple file.
9. Simple console output.
10. Java util logging which is usually available in the JRE but never chosen directly. 

[Backend discovery documentation](https://256stuff.com/sources/simplelogging/docs/backend-discovery)

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

[Getting started documentation](https://256stuff.com/sources/simplelogging/docs/getting-started)

# "Fluent" Logging with Method Chaining Supported

SimpleLogging also supports "fluent" logging where you can chain log methods together to build your
log message which will generate no objects if the log level is not enabled.  For example, even with
port being an `int` primitive below, there are no objects generated by thus call unless `TRACE` log
level is enabled.

```java
private static final FluentLogger fluentLogger =
    LoggerFactory.getFluentLogger(MyClass.class);
...
// this generates no objects even due to auto-boxing unless trace is enabled
fluentLogger.atLevel(Level.TRACE)
    .msg("connected to host '{}' port '{}'")
    .arg(host)
    .arg(port)
    .log();
```

[Fluent logging documentation](https://256stuff.com/sources/simplelogging/docs/fluent-logging)

# Examples of Argument Processing

Here are some more examples showing the details about the argument logic:

```java
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
...

// no arguments but a {} pattern
logger.info("connected to host '{}'");
// outputs: connected to host '{}'

// missing argument to second {} displays as empty string
String host = "host1";
logger.info("connected to host '{}' port '{}'", host);
// outputs: connected to host 'host1' port ''

// extra argument (port) is ignored
String host = "host1";
int port = 443;
logger.info("connected to host '{}'", host, port);
// outputs: connected to host 'host1'

// null argument
String host = null;
logger.info("connected to host '{}' failed", host);
// outputs: connected to host 'null'

// arguments that are arrays
String[] hosts = new String[] { "srv1", "srv2" };
logger.info("connected to hosts {} failed", hosts);
// outputs: connected to hosts [srv1, srv2]

// logging of Host which implements LogArgumentCreator where
// host.createLogArg() method returns the string: host4
Host host = new Host();
logger.info("connected to host '{}'", host);
// outputs: connected to host 'host4'

// logging of Server where server.toString() returns: srv3
Server server = new Server();
logger.info("connected to host '{}'", server);
// outputs: connected to host 'srv3'
```

# Maven Configuration

Maven packages are published via [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://mvnrepository.com/artifact/com.j256.simplelogging/simplelogging/latest)

``` xml
<dependency>
	<groupId>com.j256.simplelogging</groupId>
	<artifactId>simplelogging</artifactId>
	<version>3.0</version>
</dependency>
```

# Dependencies

SimpleLogging has no direct dependencies.  It has a number of optional dependencies that will only be referenced if
they are already in your application's classpath.

# ChangeLog Release Notes

See the [![ChangeLog](https://img.shields.io/github/v/release/j256/simplelogging?label=changelog)](https://github.com/j256/simplelogging/blob/master/src/main/javadoc/doc-files/changelog.txt)
