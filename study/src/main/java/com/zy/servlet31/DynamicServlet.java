package com.zy.servlet31;

import java.io.IOException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zy.security.jdk.CertificateCoder;

public class DynamicServlet extends HttpServlet {
	
	private static Map<String, String> sessionMap = new HashMap<String, String>();

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
		String clientVerify = req.getHeader("X_SSL_CLIENT_VERIFY");
		
		//返回证书字符串base64编码里面还有一些其它多余的标识行
		String clientCert = req.getHeader("X_SSL_CLIENT_CERT");
		
		//返回建立的SSL连接中那些使用的密码字段
		String cipher = req.getHeader("X_SSL_CIPHER");
		
		//返回建立的SSL连接中客户端证书的序列号
		String serial = req.getHeader("X_SSL_CLIENT_SERIAL");
		
		//返回建立的SSL连接中客户端证书的DN主题字段
		String sDn = req.getHeader("X_SSL_CLIENT_S_DN");
		
		//返回建立的SSL连接中客户端证书的DN发行者字段
		String iDn = req.getHeader("X_SSL_CLIENT_I_DN");
		
		//返回建立的SSL连接中使用的协议
		String protocol = req.getHeader("X_SSL_PROTOCOL");
		
		String sslSessionId = req.getHeader("X_SSL_SESSION_ID");
		
		String rawCert = req.getHeader("X_SSL_CLIENT_RAW_CERT");
		
		logger.info("============" + String.valueOf(req.getParameter("method")));
		
		logger.debug("服务器检验结果【" + String.valueOf(clientVerify) + "】");
		logger.debug("密码【" + String.valueOf(cipher) + "】");
		logger.debug("客户端证书的序列号【" + String.valueOf(serial) + "】");
		logger.debug("客户端证书的DN主题【" + String.valueOf(sDn) + "】");
		logger.debug("客户端证书的DN发行者的主题【" + String.valueOf(iDn) + "】");
		logger.debug("使用的协议【" + String.valueOf(protocol) + "】");
		logger.debug("SSL会话ID【" + String.valueOf(sslSessionId) + "】");
		logger.debug("SSL_CLIENT_RAW_CERT【" + String.valueOf(rawCert) + "】");
		logger.debug("证书【" + String.valueOf(clientCert) + "】");
		
		boolean isRevoked = false;
		if(null != clientCert) {
			X509Certificate cert = CertificateCoder.getX509Certificate(clientCert);
			X509CRL crl = CertificateCoder.loadX509CRL("http://ssl.jl.com/crl/ca-crl.pem");
			if(null != crl) {
				isRevoked = crl.isRevoked(cert);
				logger.debug("当前用户证书吊销状态【" + isRevoked + "】");
			}
			
		}
		if(null != sslSessionId) {
			if(!sessionMap.containsKey(sslSessionId)) {
				sessionMap.put(sslSessionId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		}
		

		req.setAttribute("clientVerify", String.valueOf(clientVerify));
		req.setAttribute("cipher",String.valueOf(cipher));
		req.setAttribute("serial", String.valueOf(serial));
		req.setAttribute("sDn",String.valueOf(sDn));
		req.setAttribute("iDn",String.valueOf(iDn));
		req.setAttribute("protocol",String.valueOf(protocol));
		req.setAttribute("sslSessionId",String.valueOf(sslSessionId));
		req.setAttribute("rawCert",String.valueOf(rawCert));
		req.setAttribute("clientCert",String.valueOf(clientCert));
		req.setAttribute("isRevoked", isRevoked);
		req.setAttribute("sessionMap", sessionMap);
		
		req.getRequestDispatcher("/ca.jsp").forward(req, resp);
	}
	

}


