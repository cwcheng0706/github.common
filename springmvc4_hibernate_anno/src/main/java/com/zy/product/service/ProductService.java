/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:10:13
 */
package com.zy.product.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zy.entity.Product;
import com.zy.product.dao.ProductDao;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:10:13
 */
@Service
public class ProductService {

	private final static Logger logger = Logger.getLogger(ProductService.class);

	@Autowired
	private ProductDao productDao;

	@Value("${smtp.host}")
	private String smtphost;

	@Transactional(propagation = Propagation.REQUIRED)
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