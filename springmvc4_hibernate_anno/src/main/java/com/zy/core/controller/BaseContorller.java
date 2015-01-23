package com.zy.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public abstract class BaseContorller {
	
	@RequestMapping(value = "/index")
	public String index(Model model){
		
		return "index";
	}
	
	@RequestMapping(value = "/error/500")
	public String error(Model model){
		return "error/500";
	}

}
