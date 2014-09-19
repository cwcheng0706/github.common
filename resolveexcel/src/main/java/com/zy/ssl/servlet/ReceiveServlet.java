package com.zy.ssl.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ReceiveServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4427955884393132019L;

	private static Logger logger = Logger.getLogger(ReceiveServlet.class);

	@SuppressWarnings("rawtypes")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		logger.debug("--------------------------ReceiveServlet start -------------------------");

		try {
//			X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
//			System.out.println(certs.length);

			Enumeration e = req.getHeaderNames();
			while (e.hasMoreElements()) {
				String name = (String) e.nextElement();
				String value = req.getHeader(name);
				logger.info(name + " = " + value);
			}

			logger.info("==========================================================");
			logger.info("Forwarded IP【" + String.valueOf(req.getHeader("X-Forwarded-For")) + "】");
			logger.info("Real IP【" + String.valueOf(req.getHeader("X-Real-IP")) + "】");
			logger.info("验证状态【"  + String.valueOf(req.getHeader("X-CLIENT-VERIFY"))+ "】");
			logger.info("SSL_CERT【" + String.valueOf(req.getHeader("SSL_CERT"))+ "】") ;
			logger.info("X-SSL-Client-S-DN【" + String.valueOf(req.getHeader("X-SSL-Client-S-DN")) + "】");
		} catch (Exception e) {
			logger.error(e);
		}
		ServletInputStream in = req.getInputStream();
		// BufferedInputStream bis = new BufferedInputStream(in);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str = null;
		StringBuffer sb = new StringBuffer();
		while (null != (str = br.readLine())) {
			sb.append(str);
		}

		logger.info(sb.toString());

		String xmlContent = "<?xml version='1.0' encoding='utf-8'?><ABC>";
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter out = resp.getWriter();
		for (int i = 0; i < 2; i++) {
			xmlContent += "<Abc><abc>测试" + i + "</abc></Abc>";
		}
		xmlContent += "</ABC>";
		out.println(xmlContent);
		out.flush();
		out.close();

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
