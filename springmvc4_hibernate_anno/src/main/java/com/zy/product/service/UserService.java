/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:10:13
 */
package com.zy.product.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zy.entity.User;
import com.zy.product.dao.UserDao;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:10:13
 */
@Service
public class UserService {

	private final static Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly = true)
	public User getUserByMail(String mail){
		return userDao.getUserByMail(mail);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean save(User user) {
		boolean ret = false;
		
		//根据路由规则分发
		for(int i = 0 ; i < 10000; i++) {
			User u = new User();
			u.setId(String.valueOf(i+1000));
			u.setName("朱勇user_" + i);
			u.setSex(String.valueOf(i));
			u.setCreateDate(user.getCreateDate());
			userDao.save(u);
		}
		
		//此语句会被分发到两个库中
		userDao.save(user);
		
		return ret;
	}

}
