/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
package com.zy.product.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zy.entity.Product;
import com.zy.exception.MyException;
import com.zy.product.service.ProductService;
import com.zy.product.vo.ProductVo;

/**
 * @Project: servlet_spring4_hibernate_anno
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	
	private final static Logger logger = Logger.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	/**
	 * 
	 * @RequestParam 方式传值 如果没有带 ?userName= 则报404错误
	 * @ModelAttribute 传值方式不会因为没有传 ?name 而报名。可以进到controller类里面
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月15日 下午4:03:41
	 * @param userName
	 * @param vo
	 */
	@RequestMapping(value="/saveem",method = RequestMethod.POST)
	public void save(@RequestParam("userName") String userName,@ModelAttribute ProductVo vo,HttpServletRequest req) {
		

		logger.info("req: userName: " + req.getParameter("userName"));
		
//		try {
//			logger.info("userName : " + new String(userName.getBytes("ISO-8859-1"), "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
		
		Product product = new Product();
		product.setCreateDate(new Date());
		product.setName("测试111");
		
		productService.saveProduceByEntityManager(product );
		
	}
	
	@RequestMapping(value = "/saverep")
	public void saveByReponsitory() {
		Product product1 = new Product();
		product1.setCreateDate(new Date());
		product1.setName("再开一线程");
		productService.saveProduceByReponsitory(product1);
		
	}
	
	
	@ExceptionHandler(MyException.class)
	public void handlerMyException(MyException e) {
		logger.info("---------------处理MyException------------------");
	}

	/**
	 * 用来测试不同url报出的异常是不是会区别调用相应的Hander
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年10月15日 下午4:45:26
	 */
	@RequestMapping("/exception")
	public void testException() {
		
		throw new MyException("error");
	}
}
