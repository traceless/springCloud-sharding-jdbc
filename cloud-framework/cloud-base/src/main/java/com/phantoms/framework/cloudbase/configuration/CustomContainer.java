package com.phantoms.framework.cloudbase.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;


/**
 * 可以根据appName 自动修改上下文路径
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月1日 	Created
 *
 * </pre>
 * @since 1.
 */
@Component
//@EnableApolloConfig
public class CustomContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    private final static Logger logger = LoggerFactory.getLogger(CustomContainer.class);

    @Value("${spring.application.name}")
    private String              appName;

    @Value("${server.servlet.context-path:/}")
    private String              contextPath;
    
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        logger.info("-------------reset contextPath = {}--------------", appName);
        // if(contextPath.equals("/")) factory.setContextPath("/" + appName);
    }

}

