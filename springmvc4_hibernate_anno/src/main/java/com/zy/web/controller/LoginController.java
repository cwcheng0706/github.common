package com.zy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.core.security.MessageDigestCoder;
import com.zy.entity.User;
import com.zy.product.service.UserService;
import com.zy.web.vo.UserVo;

@Controller
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login")
	public String login() {
		return "/admin/login";
	}
	
	@RequestMapping(value="/logon")
	public String logon(UserVo vo) {
		logger.debug("用户登录");
		
		boolean logionSuccess = false;
		String _pass = null;
		User u = userService.getUserByMail(String.valueOf(vo.getName()));
		
		if(null != u && null != vo.getPassword() && !"".equals(vo.getPassword()) ){
			try {
				_pass = MessageDigestCoder.encryptHex(MessageDigestCoder.encryptSHA(vo.getPassword().getBytes(MessageDigestCoder.CHARSET_UTF8)));
			} catch (Exception e) {
				logger.error("密码加密异常" + e);
			}
			
			if(_pass.equals(u.getPassword())) {
				logger.debug("用户登录成功");
				logionSuccess = true;
			}
			
		}
		
		if(logionSuccess) {
			return "redirect:/index";
		}else {
			return "redirect:/login";
		}
	}
}
