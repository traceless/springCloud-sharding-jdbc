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
 * 服务层异常
 * 
 * @author zyj
 * @version [1.0, 2012-9-11]
 * @since [base/1.0]
 */

@Getter
@Setter
public class BaseException extends Exception {

    /**
     * 注释内容
     */
    private static final long  serialVersionUID = -4306611193031135336L;

    public static final String CODE             = "500";
    /**
     * 异常编码
     */
    private String             code;

    private String             message;

    /**
     * 提供一个无参数构造方法，用于Object exceptionObj = clazz.newInstance();
     * TODO Class constructors.
     */
    public BaseException(){
        this.code = CODE;
    }
    
    /**
     * 构造函数
     * 
     * @param message 异常消息
     */
    public BaseException(String message){
        super(message);
        this.code = CODE;
        this.message = message;
    }

    /**
     * 构造函数
     * 
     * @param code 异常编码
     * @param message 异常消息
     */
    public BaseException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
        
    }
 
    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
