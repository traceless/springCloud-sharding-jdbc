package com.phantoms.framework.cloudbase.configuration;

import java.util.Set;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import com.phantoms.framework.cloudbase.log.MyRollingFileAppender;

/**
 * 
 * ApolloConfig 可以动态调整日志级别，spring-admin也可以实现了
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月7日 	Created
 *
 * </pre>
 * @since 1.
 */
@Configuration
//@EnableApolloConfig
@Slf4j
public class MyLoggerConfiguration {

    private static final String     LOGGER_TAG = "logging.level.";

    @Autowired
    private LogbackLoggingSystem    loggingSystem;

    @Autowired
    private ConfigurableEnvironment environment;

//    @ApolloConfig
//    private Config                  config;

    @Autowired
    private ApplicationContext      applicationContext;

    @Value("${spring.application.name}")
    private String                  appName;

/*  @ApolloConfigChangeListener
    private void configChangeListter(ConfigChangeEvent changeEvent) {
        refreshLoggingLevels();
    }

    @PostConstruct
    private void refreshLoggingLevels() {
        Set<String> keyNames = config.getPropertyNames();
        for (String key : keyNames) {
            if (containsIgnoreCase(key, LOGGER_TAG)) {
                String strLevel = config.getProperty(key, "info");
                LogLevel level = LogLevel.valueOf(strLevel.toUpperCase());
                loggingSystem.setLogLevel(key.replace(LOGGER_TAG, ""), level);
                log.info("----------apollo refreshLoggingLevels:{}:{} ------", key, strLevel);
            }
        }
    }
 */

    /**
     * 用于spring boot admin actuator.jar -> LogFileWebEndpoint的配置
     */
    @PostConstruct
    private void initActuatorLogFileWebEndpoint() {
        if (null == environment.getProperty(LogFile.FILE_PROPERTY)) {
            // 如果环境没有定义,即loggin.file=null，那么使用默认的路径
            String logFile = MyRollingFileAppender.getLoggingFile();
            Assert.notNull(logFile, "--------please configure the logback.xml use MyRollingFileAppender------");
            // actuator.jar->LogFileWebEndpoint会读取环境的loggin.file值，用于在admin上在线看日志信息
            this.environment.setActiveProfiles(LogFile.FILE_PROPERTY, logFile);
            return;
        }
        // 如果apollo配置或者本地配置了，则刷新环境。按道理本地配置了是不需要刷新的，这里暂时兼容性执行刷新
        // ApplicationEnvironmentPreparedEvent 被(还可能有其他监听者)
        // ConfigFileApplicationListener,ConfigServerBootstrapApplicationListener和LoggingApplicationListener
        // 所以这里触发 ApplicationEnvironmentPreparedEvent 事件应该没啥问题，未验证。。
        // 也可以重新写一个 LoggingApplicationListener
        // 的监听器，新增一个自定义event直接执行里面的initialize实现。
        applicationContext.publishEvent(new ApplicationEnvironmentPreparedEvent(new SpringApplication(Object.class),
            null,
            environment));
    }

    private static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        int len = searchStr.length();
        int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (str.regionMatches(true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

}
