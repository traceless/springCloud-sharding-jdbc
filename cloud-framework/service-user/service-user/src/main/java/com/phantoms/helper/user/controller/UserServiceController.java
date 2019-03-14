package com.phantoms.helper.user.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.phantoms.framework.cloudbase.exception.CloudBaseException;
import com.phantoms.framework.cloudbase.response.CommonResult;
import com.phantoms.helper.user.mapper.UserMapper;
import com.phantoms.helper.user.model.User;
import com.phantoms.helper.user.serviceapi.feign.UserServiceFeign;

/**
 * @version
 * 
 *          <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年11月30日 	Created
 *
 *          </pre>
 * 
 * @since 1.
 */
// @RequestMapping不能同时在UserServiceFeign注解
// 因为@EnableFeignClients 和 @ComponentScan 同时扫描后会发生Ambiguous mapping（模棱两可的映射）问题，
@RestController
@RequestMapping("/service-user")
public class UserServiceController implements UserServiceFeign {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceController.class);

	@Autowired
	private UserMapper userMapper;

	@PostMapping("user/getUserInfo")
	@ResponseBody
	@Override
	public CommonResult getUserInfo(@RequestBody String userName) throws CloudBaseException {
		logger.info("-------come in getUserInfo------");
		if (StringUtils.isEmpty(userName)) {
			throw new NullPointerException("参数为空");
		}
		if (userName.length() < 6) {
			throw new CloudBaseException("-201", "参数太短");
		}
		User user = userMapper.selectOne();
		CommonResult res = new CommonResult();
		res.setData(user);
		return res;
	}

}
