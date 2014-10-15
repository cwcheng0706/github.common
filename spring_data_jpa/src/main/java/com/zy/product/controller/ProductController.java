/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
package com.zy.product.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zy.entity.Product;
import com.zy.product.service.ProductService;
import com.zy.product.vo.ProductVo;

/**
 * @Project: servlet_spring4_hibernate_anno
 * @Author zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
@Controller
public class ProductController {
	
	private final static Logger logger = Logger.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(Model model){
		return new ModelAndView("/index");
	}
	
	@RequestMapping(value = "/error/500")
	public String error(Model model){
		return "/error/500";
	}

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
	@RequestMapping(value="productem/save",method=RequestMethod.GET)
	@ResponseBody
	public void save(@RequestParam("userName") String userName,@ModelAttribute ProductVo vo) {

		try {
			logger.info("userName : " + new String(userName.getBytes("ISO-8859-1"), "GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Product product = new Product();
		product.setCreateDate(new Date());
		product.setName("测试111");
		
		productService.saveProduceByEntityManager(product );
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		logger.info("==============再开一线程==============");
//		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				
//				Product product1 = new Product();
//				product1.setCreateDate(new Date());
//				product1.setName("再开一线程");
//				productService.saveProduceByReponsitory(product1);
//				
//			}
//		}).start();
		
	}
	
	@RequestMapping(value = "/productr/save")
	public void saveByReponsitory() {
		Product product1 = new Product();
		product1.setCreateDate(new Date());
		product1.setName("再开一线程");
		productService.saveProduceByReponsitory(product1);
		
	}
}
