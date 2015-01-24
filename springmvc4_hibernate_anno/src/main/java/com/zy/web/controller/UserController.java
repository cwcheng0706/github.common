package com.zy.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.core.controller.BaseContorller;
import com.zy.core.security.MessageDigestCoder;
import com.zy.entity.User;
import com.zy.product.service.UserService;
import com.zy.web.vo.UserVo;

@Controller
public class UserController extends BaseContorller{

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 登录页面
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2015年1月23日 上午9:15:24
	 * @return
	 */
	@RequestMapping(value="/login")
	public String login() {
		return "/admin/login";
	}
	
	/**
	 * 注册页面
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2015年1月23日 上午9:15:34
	 * @return
	 */
	@RequestMapping(value = "/registerin")
	public String registerin(){
		return "/admin/register";
	}
	
	@RequestMapping(value = "/registeron")
	public String registeron(Model model) {
		model.addAttribute("test", "test");
		return this.index(model);
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
