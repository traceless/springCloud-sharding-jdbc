package com.phantoms.framework.cloudbase.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.phantoms.framework.cloudbase.handlerInterceptor.AbstractHandlerInterceptor;

/**
 * 自定义controller拦截器，实现AbstractHandlerInterceptor即可
 * example {@link LogUrlHandlerInterceptor}
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月2日 	Created
 *
 * </pre>
 * @since 1.
 */
@Configuration
public class AutoRegisterInterceptor extends WebMvcConfigurationSupport {

    @Autowired
    private ApplicationContext                      applicationContext;

    private Map<String, AbstractHandlerInterceptor> interceptorList;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        interceptorList = applicationContext.getBeansOfType(AbstractHandlerInterceptor.class);
        interceptorList.forEach((k, interceptor) -> {
            InterceptorRegistration registration = registry.addInterceptor(interceptor);
            registration.addPathPatterns(interceptor.getAddPathPatterns());
            registration.excludePathPatterns(interceptor.getExcludePathPatterns());
        });

    }

}
