/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午10:19:24
 */
package com.zy.springboot_1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午10:19:24
 */
@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping("/login")
	public String login() {
		logger.info("-------login-----");
		return "/login";
	}
	
	@RequestMapping("/index")
	public String index(){
		logger.info("-------index-----");
		return "/index";
	}
	
//	@RequestMapping("/error")
//	public String error(){
//		logger.info("-------error-----");
//		return "error";
//	}
	
	@RequestMapping("/logoutSuccess")
	public String logoutSuccess(){
		logger.info("-------logoutSuccess-----");
		return "/logoutSuccess";
	}
	
	@RequestMapping("/user")
	public String user(){
		logger.info("-------user-----");
		return "/user";
	}
	
}
