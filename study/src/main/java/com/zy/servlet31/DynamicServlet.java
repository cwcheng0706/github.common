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
		
		//返回SUCCESS
		String cert = req.getHeader("X-SSL-CLIENT-VERIFY");
		
		//返回证书字符串base64编码里面还有一些其它多余的标识行
		String dn = req.getHeader("X-SSL-CLIENT-CERT");
		
		//返回建立的SSL连接中那些使用的密码字段
		req.getHeader("X-SSL-SSL_CIPHER");
		
		//返回建立的SSL连接中客户端证书的序列号
		req.getHeader("X-SSL-SSL_CLIENT_SERIAL");
		
		//返回建立的SSL连接中客户端证书的DN主题字段
		req.getHeader("X-SSL-CLIENT-S-DN");
		
		//返回建立的SSL连接中客户端证书的DN发行者字段
		req.getHeader("X-SSL-SSL_CLIENT_I_DN");
		
		//返回建立的SSL连接中使用的协议
		req.getHeader("X-SSL-SSL_PROTOCOL");
		
		req.getHeader("X-SSL-SSL_SESSION_ID");
		
		req.getHeader("X-SSL-SSL_CLIENT_RAW_CERT");
		
		logger.debug("证书【" + cert + "】");
		logger.debug("DN【" + dn + "】");
		
		req.getRequestDispatcher("/ca.jsp").forward(req, resp);
	}

}


