package com.phantoms.framework.cloudbase.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 通用信息结构
 */
@Getter
@Setter
public class CommonRequet implements Serializable {
    private static final long serialVersionUID = 5968641933428383078L;

    @JSONField
    private String traceId;
    
}
