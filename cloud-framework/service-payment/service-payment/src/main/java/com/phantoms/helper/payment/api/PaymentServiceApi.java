package com.phantoms.helper.payment.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.phantoms.framework.cloudbase.response.CommonResult;
import com.phantoms.helper.user.serviceapi.feign.UserServiceFeign;

@RestController
@RequestMapping("/service-payment")
public class PaymentServiceApi {
    
    private final static Logger logger = LoggerFactory.getLogger(PaymentServiceApi.class); 
    
    @Autowired
    UserServiceFeign userServiceFeign;
    
    @ResponseBody
    @GetMapping("/user/getUserInfo")
    public CommonResult getUserInfo(String productIdList) throws Exception {
        // TODO Auto-generated method stub
        try{
            CommonResult test = userServiceFeign.getUserInfo(null);
            logger.info("======test={}", test);
        }catch(Exception e){
            if(e.getCause() instanceof NullPointerException)
                throw e;
        }
        CommonResult res = userServiceFeign.getUserInfo("3"); 
        return res;
    }

}
