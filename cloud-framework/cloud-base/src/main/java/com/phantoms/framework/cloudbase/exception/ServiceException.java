/*
 * 文 件 名:  ServiceException.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
 * 描    述:  服务层异常
 * 修 改 人:  zyj
 * 修改时间:  2012-9-11
 */
package com.phantoms.framework.cloudbase.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常类
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月1日 	Created
 *
 * </pre>
 * @since 1.
 */
@Getter
@Setter
public class ServiceException extends BaseException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 提供一个无参数构造方法，用于Object exceptionObj = clazz.newInstance(); TODO Class
     * constructors.
     */
    public ServiceException(){
        super(BaseException.CODE);
    }

    /**
     * 构造函数
     * 
     * @param message 异常消息
     */
    public ServiceException(String message){
        super(BaseException.CODE, message);
    }

    /**
     * 构造函数
     * 
     * @param code 异常编码
     * @param message 异常消息
     */
    public ServiceException(String code, String message){
        super(code, message);
    }

    public ServiceException(String code, String message, Throwable ex){
        super(code, message, ex);
    }
}
