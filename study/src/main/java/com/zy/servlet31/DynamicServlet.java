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
		String clientCert = req.getHeader("X-SSL-CLIENT-CERT");
		
		//返回建立的SSL连接中那些使用的密码字段
		String cipher = req.getHeader("X-SSL-SSL_CIPHER");
		
		//返回建立的SSL连接中客户端证书的序列号
		String serial = req.getHeader("X-SSL-SSL_CLIENT_SERIAL");
		
		//返回建立的SSL连接中客户端证书的DN主题字段
		String sDn = req.getHeader("X-SSL-CLIENT-S-DN");
		
		//返回建立的SSL连接中客户端证书的DN发行者字段
		String iDn = req.getHeader("X-SSL-SSL_CLIENT_I_DN");
		
		//返回建立的SSL连接中使用的协议
		String protocol = req.getHeader("X-SSL-SSL_PROTOCOL");
		
		String sslSessionId = req.getHeader("X-SSL-SSL_SESSION_ID");
		
		String rawCert = req.getHeader("X-SSL-SSL_CLIENT_RAW_CERT");
		
		logger.debug("服务器检验结果【" + cert + "】");
		logger.debug("密码:【" + cipher + "】");
		logger.debug("客户端证书的序列号【" + serial + "】");
		logger.debug("客户端证书的DN主题【" + sDn + "】");
		logger.debug("客户端证书的DN发行者字段【" + iDn + "】");
		logger.debug("使用的协议【" + protocol + "】");
		logger.debug("SSL会话ID【" + sslSessionId + "】");
		logger.debug("SSL_CLIENT_RAW_CERT【" + rawCert + "】");
		logger.debug("证书" + clientCert + "】");
		
		req.getRequestDispatcher("/ca.jsp").forward(req, resp);
	}

}


