/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午12:53:00
 */
package com.zy.springboot_1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zy.springboot_1.service.TestService;
import com.zy.springboot_1.config.SpringbootConfig;
import com.zy.springboot_1.entity.User;;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月7日 下午12:53:00
 */
@RestController
public class TestController {
	
	@Autowired
	private SpringbootConfig cfg;
	@Autowired
	private TestService testService;
	
	@RequestMapping("/test")
	public User rest() {
		
		System.out.println(cfg.getName());
		System.out.println(testService.getUserName());
		System.out.println(cfg.getPassword());
		
		User u = new User();
		u.setAccount("account");
		u.setPasswork("123456");
		return u;
	}
	
	@RequestMapping("/testList")
	public List<User> listUser(){
		List<User> userList = new ArrayList<User>();
		for(int i = 0 ; i < 5; i++ ) {
			User u = new User();
			u.setAccount("account" + i);
			u.setPasswork("password" + i);
			userList.add(u);
		}
		
		return userList;
	}
	
	@RequestMapping("/testList1")
	public List<User> listUser1(){
		List<User> userList = new ArrayList<User>();
		return userList;
	}
	
	@RequestMapping("/testList2")
	public List<User> listUser2(){
		List<User> userList = null;
		return userList;
	}
	
	@RequestMapping("/err")
	public String error(){
		throw new RuntimeException("测试异常");
	}
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(){
		return "error";
	}
	
	@RequestMapping("/err1")
	public String error1(){
		return "error";
	}
	
	@RequestMapping("/")
	public String index(){
		return "/index";
	}
	
	@RequestMapping("/userList")
	public List<User> userList(){
		List<User> userList = new ArrayList<User>();
		for(int i = 0 ; i < 5; i++ ) {
			User u = new User();
			u.setAccount("account" + i);
			u.setPasswork("password" + i);
			userList.add(u);
		}
		
		return userList;
	}
	
}
