package com.phantoms.helper.serviceuser;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.phantoms.framework.cloudbase.dbconfig.sharding.SourceShardingAlgorithm;
import com.phantoms.framework.cloudbase.util.SnowflakeIdFactory;
import com.phantoms.helper.user.ServiceUserApplication;
import com.phantoms.helper.user.mapper.OrderMapper;
import com.phantoms.helper.user.mapper.UserMapper;
import com.phantoms.helper.user.model.Order;
import com.phantoms.helper.user.model.User;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ServiceUserApplication.class})
@Slf4j
public class ServiceUserApplicationTests {

	private SnowflakeIdFactory snowflakeIdFactory = new SnowflakeIdFactory(12);
	@Autowired
    private UserMapper userMapper;
	
	@Autowired
    private OrderMapper orderMapper;
	
	/**
	 * 插入数据，订单数据
	 */
//	@Test
	public void insertUser() {
		for(int i=0;i<10;i++) {
			User user = createUser(generateString(6, false));
			userMapper.insert(user);
			orderMapper.insert(createOrder(user));
			orderMapper.insert(createOrder(user));
			log.info("-----finish-----" );
		}
	}
	
	/**
	 * 利用 userName 的hashVal 来做模糊查询，这一点设计优秀
	 * 也可以直接使用 userName = ”具体的值“ 完全匹配查询
	 */
	@Test
	public void selectUser() {
		String userName = "5"; // 5 开头的名字 
		Long hashVal =  SourceShardingAlgorithm.stringPreciseAlgorithm(userName);
		List<User> list  = userMapper.selectByLikeName(userName, hashVal.intValue());
		list.forEach(user -> log.info("----userName= "  + user.getUserName()));
	}
	
	/**
	 * 根据订单ID得到 手机号，使用left join 方式
	 */
	@Test
	public void selectOrderListByName() {
		long orderId = orderMapper.selectOne().getOrderId();
		User user = orderMapper.selectUserByOrderId(orderId);
		log.info("----mobile= "  + user.getMobile());
	}
	
	
	
	private User createUser(String userName) {
		Long hashVal = SourceShardingAlgorithm.stringPreciseAlgorithm(userName);
		User user = new User();
		user.setUserName(userName);
		user.setMobile("134" + generateString(8, true));
		user.setHashVal(hashVal.intValue());
		user.setUserId(snowflakeIdFactory.nextId(hashVal));
		return user;
	}
	
	private Order createOrder(User user) {
		Order order = new Order();
		order.setAmount(132);
		order.setHashVal(user.getHashVal());
		order.setUserId(user.getUserId());
		order.setUserName(user.getUserName());
		order.setOrderId(snowflakeIdFactory.nextId(user.getHashVal()));
		return order;
	}
	
 
 
 
	public static String generateString(int length, boolean number) //参数为返回随机数的长度 
	{ 
		String allChar = number ? "1234567890" : "123456789012345678901234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length; i++) 
		{ 
			sb.append(allChar.charAt(random.nextInt(allChar.length()))); 
		} 
		return sb.toString(); 
	} 

}
