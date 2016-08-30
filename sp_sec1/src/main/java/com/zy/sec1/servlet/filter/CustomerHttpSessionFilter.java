/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2016年8月28日 下午5:17:03
 */
package com.zy.sec1.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.sec1.entity.User;

/**
 * @Project: sp_sec1
 * @Author zy
 * @Company: 
 * @Create Time: 2016年8月28日 下午5:17:03
 */
public class CustomerHttpSessionFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHttpSessionFilter.class);
			
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LOGGER.info("--------HttpSessionFilter--------");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		User u = (User) session.getAttribute("user");
		if(null != u) {
			resp.sendRedirect("/");
		}else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
	}

}
