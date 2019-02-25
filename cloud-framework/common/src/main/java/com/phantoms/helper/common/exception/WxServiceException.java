/*
* 文 件 名:  ServiceException.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  服务层异常
* 修 改 人:  zyj
* 修改时间:  2012-9-11
*/
package com.phantoms.helper.common.exception;

/**
* 服务层异常
* 
* @author  zyj
* @version  [1.0, 2012-9-11]
* @since  [base/1.0]
*/
public class WxServiceException extends Exception {
    
    /**
    * 注释内容
    */
    private static final long serialVersionUID = -4306611193031135336L;
    
    /**
     * 异常编码
     */
    private String code;
    
    /**
     * 构造函数
     * @param message 异常消息
     */
    public WxServiceException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     */
    public WxServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public WxServiceException(String message, Throwable ex){
    	super(message, ex);
    }
    
    /**
     * 构造函数
     * @param code 异常编码
     * @param message 异常消息
     * @param ex 异常
     */
    public WxServiceException(String code, String message, Throwable ex) {
        super(message, ex);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
