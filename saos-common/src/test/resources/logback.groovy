import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.WARN
import static ch.qos.logback.classic.Level.toLevel
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.OnConsoleStatusListener


statusListener(OnConsoleStatusListener)

def defaultPattern = "%d{HH:mm:ss.SSS} %-5level: [%thread] %logger{36} - %msg%n"
def logDirectory = System.properties.getProperty('catalina.base', System.getProperty('java.io.tmpdir'))+"/logs"

def props = new Properties()
def propsFile = new File(System.properties['user.home'], '.icm/saos.logback-test.properties')
if (propsFile.exists()) {
  propsFile.withInputStream {
    props.load(it)
  }
}

addInfo("log directory: ${logDirectory}")


appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "${defaultPattern}"
  }
}

appender("FILE", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "${defaultPattern}"
    }
    file = "${logDirectory}/saos.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern="${logDirectory}/saos.%d{yyyy-MM-dd}.log"
        maxHistory=30
    }
}


root(toLevel(props.logLevel ?: "INFO"), ["STDOUT", "FILE"])

println "===Test logger levels==="
for (e in props) {
    println e.key + ":" + e.value
  if (e.key.startsWith('logger.')) { logger(e.key[7..-1], toLevel(e.value.trim())) }
}


