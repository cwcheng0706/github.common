/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:08:18
 */
package com.zy.dao.impl;

import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.zy.dao.ProductDao;
import com.zy.entity.Product;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:08:18
 */
public class ProductDaoImpl extends HibernateDaoSupport implements ProductDao {

	public void save(Product product) {
		this.getHibernateTemplate().save(product);
	}

}
