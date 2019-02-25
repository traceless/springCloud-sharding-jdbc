package com.phantoms.framework.cloudbase.log;

import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingSystemProperties;

import ch.qos.logback.core.rolling.RollingFileAppender;


public class MyRollingFileAppender<E> extends RollingFileAppender<E> {

    private static String LOG_FILE_PATH = null;

    @Override
    public void start() {
        super.start();
        String logfile = System.getProperty(LogFile.FILE_PROPERTY);
        String LOG_FILE = System.getProperty(LoggingSystemProperties.LOG_FILE);
        System.out.println(" -----------System.getProperty    logging.file=" + logfile + "---LOG_FILE:" + LOG_FILE);
        System.out.println(" -----------MyRollingFileAppender config of logging-file=" + this.fileName);
        // this.fileName
        // 会有多次（三次）初始化的过程，第一次读取系统设置，第二次读取文件logback.xml设置，第三次读取应用的配置application(xxx).xml配置
        // 因为路径是根据 app.id 变量进行定义的，所以取最后一次的值存放在临时
        LOG_FILE_PATH = this.fileName;
    }

    public static final String getLoggingFile() {
        return LOG_FILE_PATH;
    }
}
