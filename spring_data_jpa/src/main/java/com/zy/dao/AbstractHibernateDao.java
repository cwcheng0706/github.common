/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 下午1:48:31
 */
package com.zy.dao;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 下午1:48:31
 */
@Repository
public class AbstractHibernateDao {

	@Autowired
	protected EntityManagerFactory entityManagerFactory;

}
