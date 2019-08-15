import ch.qos.logback.classic.AsyncAppender

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    }
}

//logging.path in Config Server will be picked up by Spring and populated as LOG_PATH in System properties
println "\n========================="
println "LOG_PATH=" + System.getProperty("LOG_PATH")

def logDir = System.getProperty("LOG_PATH")
if (logDir == null) { //workaround intermediate state
    root(INFO, ["STDOUT"])
} else {
    appender("FILE", RollingFileAppender) {
        encoder(PatternLayoutEncoder) {
            pattern = "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
        }
        rollingPolicy(TimeBasedRollingPolicy) {
            FileNamePattern = "${logDir}/ethereum-management-%d{yyyy-MM-dd}.zip"
        }
    }

    appender("ASYNC", AsyncAppender) {
        appenderRef('FILE')
    }

    logger("org.springframework", WARN, ["ASYNC", 'STDOUT'], false)

    root(INFO, ["ASYNC", 'STDOUT'])
}