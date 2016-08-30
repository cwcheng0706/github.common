/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月27日 下午8:20:29
 */
package com.zy.sec1.controll;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zy.sec1.cfg.SpringMvcConfig;

/**
 * @Project: springboot_1
 * @Author zy
 * @Company:
 * @Create Time: 2016年8月27日 下午8:20:29
 */
@RestController
@RequestMapping("/redis")
public class RedisSessionController {
	
	@Autowired
	private SessionRegistry sessionRegistry;

	@RequestMapping(value = "session/first", method = RequestMethod.GET)
	public Map<String, Object> firstResp(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		request.getSession().setAttribute("requestUrl", request.getRequestURL());
		map.put("requestUrl", request.getRequestURL());
		
		return map;
	}

	@RequestMapping(value = "session/list", method = RequestMethod.GET)
	public Object sessions(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sessionId", request.getSession().getId());
		map.put("message", request.getSession().getAttribute("requestUrl"));
		return map;
	}
	
	@RequestMapping(value = "session/online", method = RequestMethod.GET)
	public Object online(HttpServletRequest request) {
		System.out.println(request.getSession().getMaxInactiveInterval());
		return SpringMvcConfig.OnlineUserList;
	}
	
	@RequestMapping(value = "session/online1", method = RequestMethod.GET)
	public Object online1(HttpServletRequest request) {
		return sessionRegistry.getAllPrincipals();
	}
	
	
}
