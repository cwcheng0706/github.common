/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月8日 上午9:33:19
 */
package com.zy.ssl.servlet;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月8日 上午9:33:19
 */
public class SSLListServlet extends HttpServlet {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SSLListServlet.class);
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729022242344421445L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.getRequestDispatcher("/ssllist.jsp").forward(req, resp);
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}


}
