package com.phantoms.framework.cloudbase.configuration;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.phantoms.framework.cloudbase.exception.BaseException;
import com.phantoms.framework.cloudbase.response.CommonResult;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

/**
 * 
 * feign异常捕抓后还原传递
 * 如果feign设置熔断器hystrix，这里的异常会被熔断器捕抓还没到rescontroller处理
 * 熔断器如果继续抛出异常，那么RestController得到的都是 HystrixException 的异常
 * 如果业务比较复杂的情况下，应当设置hystrix做统一处理，而不必每个feign都try catch
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月1日 	Created
 *
 * </pre>
 * @since 1.
 */
@Configuration
public class FeignExceptionHandler implements ErrorDecoder {

    private static final Logger logger       = LoggerFactory.getLogger(FeignExceptionHandler.class);

    @Override
    public Exception decode(String s, Response response) {
        try {
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader());
                logger.error(body);
                // 由于GlobalConvertFeignExceptionHandler重写了异常信息结构，所以使用CommonResult类进行接收异常信息,
                // JSONObject兼容性更好，避免升级导致问题
                CommonResult commonResult = JSONObject.parseObject(body.getBytes("UTF-8"),CommonResult.class);
//                CommonResult commonResult = this.objectMapper.readValue(body.getBytes("UTF-8"), CommonResult.class);
                @SuppressWarnings("unchecked")
                Class<? extends Exception> clazz = (Class<? extends Exception>) Class.forName(commonResult.getExceptionClass());
                Constructor<? extends Exception> constructor;
                try{
                    // 大部分异常都是有参数的构造方法
                    constructor = clazz.getConstructor(String.class);
                }catch(Exception e){
                    // 针对无参数构成方法的异常进行单独处理，封装成统一的Exception，后面可以使用Throwable进行判断。
                    Exception exceptionObj = clazz.newInstance();
                    String message = commonResult.getMessage();
                    return new Exception(message, exceptionObj);
                }
                Exception exceptionObj = constructor.newInstance(commonResult.getMessage());
                if (exceptionObj instanceof BaseException) {
                    BaseException baseException = (BaseException) exceptionObj;
                    baseException.setCode(commonResult.getCode());
                    return baseException;
                } else {
                    return exceptionObj;
                }
            }
        } catch (Exception var4) {
            logger.error(var4.getMessage());
            return new Exception("系统发生异常");
        }
        return new Exception("系统未知异常");
    }
}
