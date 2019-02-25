package com.phantoms.framework.cloudbase.response;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 通用返回信息结构
 */
@Getter
@Setter
public class CommonResult implements Serializable {

    private static final long serialVersionUID = 5968641933428383078L;

    private String            traceId;
    private String            message;                                // 业务信息
    private Object            data;
    private String            code;                                    // 业务异常代码
    /** 
     * 让FeignExceptionHandler 
     * 知道是哪种类型异常，方便生成异常对象进行传递
     */
    private String            exceptionClass;
    private Date              timestamp;
    private boolean           success;

}
