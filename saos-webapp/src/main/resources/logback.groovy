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
import ch.qos.logback.classic.net.SMTPAppender
import ch.qos.logback.classic.PatternLayout
import com.google.common.base.Preconditions

statusListener(OnConsoleStatusListener)

def HOSTNAME = hostname

def defaultPattern = "%d{HH:mm:ss.SSS} %-5level: [%thread] %logger{36} - %msg%n"
def logDirectory = System.properties.getProperty('catalina.base', System.getProperty('java.io.tmpdir'))+"/logs"

def props = new Properties()


def defaultPropsFile = new File(getClass().getClassLoader().getResource("logback.default.properties").getFile());

def customPropsFile = new File(System.properties['user.home'], '.icm/saos.logback.properties')


if (!defaultPropsFile.exists() && !customPropsFile.exists()) {
    throw new IllegalStateException("no default or custom logback properties file has been found");
}

if (defaultPropsFile.exists()) {
    defaultPropsFile.withInputStream {
        props.load(it)
    }
}

if (customPropsFile.exists()) {
    println "custom logback properies found: " + customPropsFile.getAbsolutePath()
    customPropsFile.withInputStream {
        props.load(it)
    }
}
    
println "====Logger properties==="    
for (e in props) {
    println e.key + ":" + e.value 
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

def mailTo = props."mailAppender.mailTo"



if (mailTo) {


    def mailFrom = props."mailAppender.from";
    def mailSmtpHost = props."mailAppender.smtpHost"
    def mailSubjectPattern = props."mailAppender.subjectPattern"
    def mailContentPattern = props."mailAppender.contentPattern"
    
    Preconditions.checkNotNull(mailFrom);
    Preconditions.checkNotNull(mailSmtpHost);
    Preconditions.checkNotNull(mailSubjectPattern);
    Preconditions.checkNotNull(mailContentPattern);
    
    mailFrom = mailFrom.replace("\${HOSTNAME}", "${HOSTNAME}");
    
    appender("MAIL", SMTPAppender) {
        smtpHost = mailSmtpHost
        to = mailTo
        from = mailFrom
        subject = mailSubjectPattern
        layout(PatternLayout) {
            pattern = mailContentPattern
        }
    }

    root(toLevel(props.logLevel ?: "INFO"), ["STDOUT", "FILE", "MAIL"])


} else {

    root(toLevel(props.logLevel ?: "INFO"), ["STDOUT", "FILE"])

}

for (e in props) {
  
  if (e.key.startsWith('logger.')) {
    logger(e.key[7..-1], toLevel(e.value.trim())) 
  }
}


