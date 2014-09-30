package com.cfca.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@WebServlet(name="verifyServlet",urlPatterns="/verify")
public class VerifyServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3511688687722958336L;
	
	private static final Log logger = LogFactory.getLog(VerifyServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("验证用户是否能安装证书..");
		try{
		
		}catch(Exception e) {
			throw new RuntimeException(e);
		}finally {
		}
		
		String userId = req.getParameter("userId");
		if(null != userId) {
			req.setAttribute("userId", userId);
		}else {
			req.setAttribute("userId", "0");
		}
		req.getRequestDispatcher("/api.jsp").forward(req,resp);
	}
	
	

}
