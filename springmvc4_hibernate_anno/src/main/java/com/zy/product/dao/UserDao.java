/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:07:18
 */
package com.zy.product.dao;

import com.zy.entity.User;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:07:18
 */
public interface UserDao {

	public void save(User user);
	
	public User getUserByMail(String mail);
	
	
}
