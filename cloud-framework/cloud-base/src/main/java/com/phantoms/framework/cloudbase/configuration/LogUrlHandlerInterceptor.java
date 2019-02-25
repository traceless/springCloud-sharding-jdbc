package com.phantoms.framework.cloudbase.configuration;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.phantoms.framework.cloudbase.handlerInterceptor.AbstractHandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 抽象类，controller拦截器，打印请求日志
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月2日 	Created
 *
 * </pre>
 * @since 1.
 */
@Component
@Slf4j
public class LogUrlHandlerInterceptor extends AbstractHandlerInterceptor{
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {       //请求进入这个拦截器
        log.info("-----request url:{}",request.getRequestURI());
        Enumeration<String>  hearder = request.getHeaderNames();
        while (hearder.hasMoreElements()) {
            String name = (String) hearder.nextElement();
            String value = request.getHeader(name);
            log.debug(name + " -- " +value);
        }
        return true;
    }

    @Override
    public List<String> getAddPathPatterns() {
        List<String> list = new ArrayList<String>();
        list.add("/**");
        return list;
    }

    @Override
    public List<String> getExcludePathPatterns() {
        return new ArrayList<String>();
    }
 
}
