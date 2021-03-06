/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月7日 下午10:19:24
 */
package com.zy.sec1.controll;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping("")
	public String index(HttpServletRequest req) {
		LOGGER.info("-------index-----");
		HttpSession session = req.getSession();
		session.setAttribute("user", "user");
		
		// 登陆用户的权限对象
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	
		List<GrantedAuthority> authorList = (List<GrantedAuthority>) auth.getAuthorities();
		for (Iterator<GrantedAuthority> iterator = authorList.iterator(); iterator.hasNext();) {
			GrantedAuthority grantedAuthority = (GrantedAuthority) iterator.next();
			LOGGER.info("ROLE【" + grantedAuthority.getAuthority() + "】");
		}
		
		return "/index";
	}
	
	@RequestMapping("/login")
	public String login() {
		LOGGER.info("-------login-----");
		return "/login";
	}
	
}
