package com.phantoms.helper.user.serviceapi.feign.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.phantoms.framework.cloudbase.response.CommonResult;
import com.phantoms.helper.user.serviceapi.feign.UserServiceFeign;

import feign.RetryableException;
import feign.hystrix.FallbackFactory;

/**
 * 熔断支持（工厂模式）
 * 工厂模式的熔断支持，可捕获调用中发生的异常信息（包含目标服务产生的异常）
 * 因此可以针对不同的异常,采用不同的服务降级措施。
 * 而且熔断器集中了已知异常的处理，避免每个使用fegin的客户端都单独处理这些客观异常的，比如网络异常，超时异常等
 * 业务异常一般不在熔断器捕抓处理
 * 
 * @author zyj
 */
@Component
public class UserServiceHystrixFallbackFactory implements FallbackFactory<UserServiceFeign> {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceHystrixFallbackFactory.class);
  
    /**
     * 创建熔断支持
     * 可在内部针对不同的异常信息类型采用不同的服务降级措施，返回对应的UserServiceFeign实现
     *
     * @param cause 异常信息
     * @return
     */
    @Override
    public UserServiceFeign create(Throwable cause) {
        // 可以做成单例, 如果策略不是很多的情况下可以做成单例
        return new UserServiceFeign() {
            @Override
            public CommonResult getUserInfo(String userName) throws Exception {
                // 如果是链接超时异常，即可考虑服务降级处理
                if(cause instanceof RetryableException){
                    return null;
                }
                // 把异常（可能是业务异常）往客户端抛
                throw new Exception(cause.getMessage(), cause);
            }
        };
        
    }
}
