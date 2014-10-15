package com.zy.product.controller;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.exception.MyException;

@Controller
@RequestMapping("/product1")
public class Product1Contorller {
	
	private final static Logger logger = Logger.getLogger(Product1Contorller.class);

	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月15日 下午4:41:35
	 * @param e
	 */
	@ExceptionHandler(MyException.class)
	public void handlerMyException(MyException e) {
		logger.info("---------------处理MyException------------------");
	}
	
	
	/**
	 * 用来测试不同url报出的异常是不是会区别调用相应的Hander
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月15日 下午4:45:18
	 */
	@RequestMapping(value="/exception1")
	public void testException() {
		
		throw new MyException("error");
	}
}
