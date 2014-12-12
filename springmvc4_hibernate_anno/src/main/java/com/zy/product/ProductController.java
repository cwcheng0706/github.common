/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
package com.zy.product;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.zy.entity.Product;
import com.zy.entity.User;
import com.zy.product.service.ProductService;
import com.zy.product.service.UserService;


@Controller
@RequestMapping("/test")
public class ProductController {
	
	private final static Logger logger = Logger.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/saveProduct",method = RequestMethod.GET)
	public void saveProdcut(HttpServletRequest req) {

		logger.info("==========save product========");
		
		for(int i = 0 ; i < 100; i++) {
			Product product = new Product();
			product.setCreateDate(new Date());
			product.setName("朱勇product_" + i);
			
			productService.save(product);
		}
		
	}
	
	@RequestMapping(value="/saveUser",method = RequestMethod.GET)
	public void saveUser(HttpServletRequest req) {

		logger.info("=========save user=========");
	
		User u = new User();
		u.setName("朱勇user_");
		u.setSex("1");
		u.setCreateDate(new Date());
		userService.save(u);
	
	}
	
	
	
	
	


	@RequestMapping(value = "/helloworld.do")
	public ModelAndView hello() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hello_jsp");
		modelAndView.addObject("message", "Hello,SpringMvc");
		return modelAndView;
	}

	@RequestMapping(value = "/welcome.do")
	public ModelAndView helloVm() {
		// ModelAndView modelAndView=new ModelAndView();
		// modelAndView.setViewName("freemarker_ftl");
		// modelAndView.addObject("message", "Hello,Velocity");
		// return modelAndView;
		Map map = new HashMap();
		return new ModelAndView("freemarker_ftl", map); // 根据之前的解释这里就是去寻找
														// user.ftl资源

	}

	@RequestMapping(value = "/free.do")
	public ModelAndView helloFreeMarker() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("welcome");
		modelAndView.addObject("message", "Hello,Freemarker");
		return modelAndView;
	}
}
