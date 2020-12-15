Java Simple Logging
===================

I've written this code into a number of different systems of mine including ORMLite and I [finally] have decided that
it is worth it's own release.  The goal is a small-ish library that backends to a number of standard logging packages.
It allows you to write logging code with slf4j-style {} arguments but then backend to any number of other logging
libraries or implement your own.

* The source code be found on the [git repository](https://github.com/j256/simplelogging).  [![CircleCI](https://circleci.com/gh/j256/simplelogging.svg?style=svg)](https://circleci.com/gh/j256/simplelogging) [![CodeCov](https://img.shields.io/codecov/c/github/j256/simplelogging.svg)](https://codecov.io/github/j256/simplelogging/)
* Maven packages are published via [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.j256.simplelogging/simplelogging/)

# Getting Started

To get started you instantiate a `Logger` using the `LoggerFactory`:
```java
// usually a logger will be per-class, getLogger() also can take a String label
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
...
// log a trace statement with arguments, the toString() on the args won't be called unless trace messages enabled
logger.trace("some trace information: {} and {}", arg1, arg2);
...
// exception messages coment _before_ the message format to not confuse the arguments
logger.error(exception, "http client threw an exception getting URL: {}", url);
```

Enjoy, Gray Watson
