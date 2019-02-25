package com.phantoms.framework.cloudbase.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import com.phantoms.framework.cloudbase.exception.DefaultErrorHandler;
import com.phantoms.framework.cloudbase.response.CommonResult;

/**
 * 这里是捕抓所有的Controller异常信息，按自定义的数据格式返回
 * 设置http状态，resp.status == 500 这样客户端就能捕抓到FeignException异常
 * 建议在zuul-gateway对结果或者异常进行统一转换。
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月1日 	Created
 *
 * </pre>
 * @since 1.
 */
@ControllerAdvice
public class GlobalConvertFeignException {
    private static Logger logger = LoggerFactory.getLogger(GlobalConvertFeignException.class);

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("author_test_ABC", "123");
    }
    
    /**
     * 系统异常处理，比如：404,500
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResult defaultErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception e) throws Exception {
        logger.error("", e);
        return DefaultErrorHandler.defaultErrorHandler(req, resp, e);
        
    }
}