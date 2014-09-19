/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:10:13
 */
package com.zy.service;

import org.apache.log4j.Logger;

import com.zy.dao.ProductDao;
import com.zy.entity.Product;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:10:13
 */
public class ProductService {
	
	private final static Logger logger = Logger.getLogger(ProductService.class);
	
	private ProductDao productDao;
	private String smtphost;

	public boolean saveProduce(Product product) {
		boolean ret = false;
		
		productDao.save(product);
		
		logger.debug("smtp.host: " + this.smtphost);
		return ret;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}

	public String getSmtphost() {
		return smtphost;
	}
}
