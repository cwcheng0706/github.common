/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
package com.zy.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {

	@RequestMapping(value = "/findSignedAsset")
	@ResponseBody
	public ModelAndView save() {

		return new ModelAndView("", "", null);
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
