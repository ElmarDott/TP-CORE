# Logger

@**since**: 1.0 > @**api-version**: 1.2 > **Dependencies**: Logback | SLF4j

Files:

* **API Interface** org.europa.together.business.[Logger](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/business/Logger.java)
* **Domain Object** ENUM org.europa.together.domain.[LogLevel](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/domain/LogLevel.java)
* **Implementation** org.europa.together.application.[LogbackLogger](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/application/LogbackLogger.java)
* **Service** org.europa.together.service.[LoggingService](https://git.elmar-dott.com/scm/repo/TogetherPlatform/TP-CORE/code/sources/Releases/src/main/java/org/europa/together/service/LoggingService.java)

---

The Logger is implemented as Proxy pattern and wrap the SLF4j and Logback Logging Framework. The encapsulation of the Logger in a own class provides a high flexibility for future maintenance, in the case functionality improvements will be needed.

The Logger supports the following LogLevels in the given order from low to high: **TRACE** | **DEBUG** | **INFO** | **WARN** | **ERROR**. The default LogLevel for productive systems should be WARN. INFO is useful for test stages to get detail information about class loading and possible performance optimizations. For example constructors contains a logging entry (INFO) “instance class”. To improve the understandably of log messages, its recommended to use the following logging strategy:

- ​	Constructors will be logged with INFO
- ​	Exceptions caught in the catch Block will be logged with ERROR
- ​	Exceptions caught in the finally block will be logged with WARN
- ​	Developer output typical will logged as DEBUG

Modules contain many external libraries. It is possible and even recommended to manage the logging of these libraries with Logback too. The recommendation for the LogLevel of external libraries in productive systems is: WARN or ERROR.

The configuration comes with a XML file called logback.xml and is placed in the root classpath of the application. The default entry comes with the LogLevel DEBUG, a reload scan period of 30 seconds and a console appender. Each module contains an independent log configuration and the log output is piped to an log file named like the module name. WAR modules have an rolling policy which cuts the log files in parts of 5 mega byte. For more details about the configuration please check the Documentation of Logback project.

To provide a possibility of manipulation the logging configuration inside the application, the LoggingService was established in TP-CORE version 1.1.

For more coding examples, how the logger could be used, please consult the test cases.

**Sample:**

```java
import org.europa.together.business.Logger;
import org.europa.together.application.LogbackLogger;
import org.europa.together.domain.LogLevel;

public class FooImpl implements Foo {

  private static final Logger LOGGER
                       = new LogbackLogger(Foo.class);
  public FooImpl() {
    try {
      LOGGER.log("instance class", LogLevel.INFO);
    } catch(Exception ex) {
      LOGGER.catchException(ex);
    }
  }
}
```
Configuration:
```xml
<?xml version="1.0" encoding="UTF-8"?>

<configuration debug="true" scan="false">

  <statusListener class="ch.qos.logback.core.status
.OnConsoleStatusListener"/>

  <appender name="CONSOLE"
            class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%5p %date{yyyy-MM-dd HH:mm:ss}
            %-50logger{50} | %m%n</pattern>
    </encoder>
  </appender>

  <root level="TRACE">
    <appender-ref ref="CONSOLE" />
  </root>
</configuration>
```