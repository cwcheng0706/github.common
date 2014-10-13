/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 下午1:48:31
 */
package com.zy.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月13日 下午1:48:31
 */
@Repository
public class AbstractHibernateDao{
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
}
