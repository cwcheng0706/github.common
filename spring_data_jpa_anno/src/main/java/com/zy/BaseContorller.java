package com.zy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BaseContorller {
	
	@RequestMapping(value = "/index")
	public ModelAndView index(Model model){
		return new ModelAndView("index");
	}
	
	@RequestMapping(value = "/error/500")
	public String error(Model model){
		return "error/500";
	}

}
