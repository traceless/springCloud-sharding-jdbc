package com.phantoms.framework.cloudbase.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.phantoms.framework.cloudbase.response.CommonResult;

/**
 * 缺省错误处理，用于将4xx，5xx 异常
 */
public class DefaultErrorHandler {

    private static Logger       logger          = LoggerFactory.getLogger(DefaultErrorHandler.class);

    private static final String END_POINTS_URIS = "/env/metrics/trace/dump/jolokia/info/configprops/trace/logfile/refresh/flyway/liquibase/heapdump/loggers/auditevents/hystrix.stream/";

    public static CommonResult defaultErrorHandler(HttpServletRequest req, HttpServletResponse resp, Exception e) {

        String uri = req.getRequestURI();
        if (END_POINTS_URIS.indexOf(uri) == -1) {
            logger.error("Exception while handler request: {}", e.getMessage(), e);
        }
        // 默认设置response.status = 500，只要status > 400,FeignClient才会认为此次请求异常
        resp.setStatus(500);
        CommonResult r = new CommonResult();
        r.setCode(BaseException.CODE);// 500 系统统一异常码
        r.setData(null);
        r.setSuccess(false);
        r.setMessage("系统内部异常"); // 系统的异常信息
        r.setExceptionClass(e.getClass().getCanonicalName()); // 设置异常类的包路径
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            r.setMessage("您请求的资源不存在");
        } else if (e instanceof org.springframework.web.method.annotation.MethodArgumentTypeMismatchException) {
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            r.setMessage("参数格式不正确");
        } else if (e instanceof org.springframework.web.bind.MethodArgumentNotValidException) {
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            r.setMessage("参数不合法");
        } else if (e instanceof org.springframework.web.bind.ServletRequestBindingException) {
            resp.setStatus(HttpStatus.BAD_REQUEST.value());
            r.setMessage("参数缺失");
        } else if (e instanceof BaseException) {
            // 自定义异常，比如serviceException等 默认resp.setStatus(500);
            r.setCode(((BaseException) e).getCode());
            r.setMessage(e.getMessage());
        } else {
            // 可能其他的系统请求异常码，系统异常信息不外传，统一“系统异常”
            resp.setStatus(getErrorStatus(req));
            // r.setMessage(getErrorMessage(req));
        }

        return r;
    }

    public static int getErrorStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public static String getErrorMessage(HttpServletRequest request) {
        final Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");
        return exc != null ? exc.getMessage() : "Unexpected error occurred";
    }

}
