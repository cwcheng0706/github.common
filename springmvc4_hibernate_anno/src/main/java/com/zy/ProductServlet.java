/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年9月13日 上午10:13:41
 */
package com.zy;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zy.entity.Product;
import com.zy.product.service.ProductService;

/**
 * @Project: springdatajpa
 * @Author zy
 * @Company:
 * @Create Time: 2014年9月13日 上午10:13:41
 */
public class ProductServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7108521079923277538L;

	ProductService productService = null;
	
	@Override
	public void init() throws ServletException {
		super.init();
		ServletContext servletContext = this.getServletContext();
		
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		productService = ctx.getBean(ProductService.class);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		Product product = new Product();
		product.setCreateDate(new Date());
		product.setName("测试1");
		
		productService.save(product );
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
