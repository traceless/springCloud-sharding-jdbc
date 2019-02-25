package com.phantoms.helper.zuulgateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZuulGatewayApplicationTests {

    private final static Logger logger = LoggerFactory.getLogger(ZuulGatewayApplication.class);
	@Test
	public void contextLoads() throws InterruptedException {
	    for(int i=0; i<1000;i++){
            Thread.sleep(500);
            logger.info("====wodood我的速度的的的的 ================od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 od我的速度的的的的 ============================================================================================cesi==================");
        }
	}

}
