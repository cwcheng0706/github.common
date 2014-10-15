/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:08:18
 */
package com.zy.product.dao.impl;


import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.zy.dao.AbstractHibernateDao;
import com.zy.entity.Product;
import com.zy.product.dao.ProductDao;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:08:18
 */
@Repository
public class ProductDaoImpl extends AbstractHibernateDao implements ProductDao {
	
	
	public void save(Product product) {
		EntityManager entityManager = this.entityManagerFactory.createEntityManager();
		
		entityManager.getTransaction().begin();
		entityManager.persist(product);
		entityManager.getTransaction().commit();
		
	}

}
