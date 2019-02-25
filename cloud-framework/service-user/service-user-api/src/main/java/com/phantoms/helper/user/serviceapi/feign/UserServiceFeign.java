package com.phantoms.helper.user.serviceapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.phantoms.framework.cloudbase.response.CommonResult;

/**
 * 定义feign客户端，name指定调用的
 * <Change to the actual description of this class>
 * 
 * @version 
 * <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年11月29日 	Created
 *
 * </pre>
 * @since 1.
 */
@FeignClient(name="user-server" )
public interface UserServiceFeign {
    
    @PostMapping("/service-user/user/getUserInfo")
    CommonResult getUserInfo(@RequestBody String userName) throws Exception;
 
}
