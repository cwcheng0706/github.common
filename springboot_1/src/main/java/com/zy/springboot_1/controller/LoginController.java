/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午10:19:24
 */
package com.zy.springboot_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午10:19:24
 */
@Controller
public class LoginController {

	@RequestMapping("/loginIndex")
	public String loginIndex(){
		
		return "/login";
	}
	
	@RequestMapping("/loginIndex1")
	public ModelAndView loginIndex1(){
		return new ModelAndView("/login");
	}
	
	@RequestMapping("/login")
	public String login() {
		
		
		return "/index";
	}
	
}
