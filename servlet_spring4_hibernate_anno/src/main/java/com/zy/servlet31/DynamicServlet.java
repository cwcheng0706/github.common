package com.zy.servlet31;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DynamicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7109376846338821082L;
	
	private static Log logger = LogFactory.getLog(DynamicServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		logger.info("--------DynamicServlet.class-------");
	}

}
