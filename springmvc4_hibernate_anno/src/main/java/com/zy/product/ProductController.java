/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月14日 下午6:47:32
 */
package com.zy.product;

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
}
