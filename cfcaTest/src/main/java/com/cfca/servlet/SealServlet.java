package com.cfca.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.cfca.utils.Base64Util;


@WebServlet(name="sealServlet",urlPatterns="/seal")
public class SealServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3511688687722958336L;
	
	private static final Log logger = LogFactory.getLog(SealServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("");
		
		InputStream in = null;
		String base64Img = "";
		
		try{
			in = this.getClass().getClassLoader().getResourceAsStream("seal.bmp");
			
			base64Img = Base64Util.getBase64StrByImageFile(in);
			
			
			logger.debug(base64Img);
			
		
		}catch(Exception e) {
			throw new RuntimeException(e);
		}finally {
			if(null != in) {
				in.close();
			}
		}
		
		req.setAttribute("sealImg", base64Img);
		
		req.getRequestDispatcher("/api.jsp").forward(req,resp);
	}
	
	

}
