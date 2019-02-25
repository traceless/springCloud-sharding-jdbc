package com.phantoms.framework.cloudbase.handlerInterceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.phantoms.framework.cloudbase.configuration.LogUrlHandlerInterceptor;

/**
 * 抽象类，controller拦截器，请自行实现
 * example {@link LogUrlHandlerInterceptor}
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月2日 	Created
 *
 * </pre>
 * @since 1.
 */
public abstract class AbstractHandlerInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {       //请求进入这个拦截器
        // 例如未登录 if(request.getSession().getAttribute("user") == null) return false;
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO
    }
    
    /**
     * 获取拦截路径, like "/","/login","/error","/static/**"
     * TODO Add comments here.
     * @return
     */
    public abstract List<String> getAddPathPatterns();
    
    /**
     * 获取不拦截路径 like "/logout"
     * TODO Add comments here.
     * @return
     */
    public abstract List<String> getExcludePathPatterns();
    
}
