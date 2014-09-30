package com.cfca.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cfca.ra.common.vo.response.CertServiceResponseVO;

import com.cfca.service.ra.RACerService;

@WebServlet(name = "geneP10Servlet", urlPatterns = "/generatep10")
public class GeneP10Servlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3511688687722958336L;

	private static final Log logger = LogFactory.getLog(GeneP10Servlet.class);

	private Map<String, String> db = new HashMap<String, String>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	private Map<String,String> getParams(String queryString) {
		Map<String, String> map = new HashMap<String, String>();
		if(null != queryString) {
			String[] arr1 = queryString.split("&");
			if(null != arr1 ) {
				for(int i = 0 ; i < arr1.length; i++ ) {
					String temp = arr1[i];
					String[] arr2 = temp.split("=");
					if(null!= arr2 && 2 == arr2.length) {
						for(int j = 0 ; j < arr2.length; j++ ) {
							map.put(arr2[0], arr2[1]);
						}
					}
				}
			}
			
		}
		return map;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("===根据p10证书请求生成p10证书..");
		String userId = null;
		String p10csr =  null;
		String p10 = "";
		
		String queryString = req.getQueryString();
		Map<String, String> map = getParams(queryString);
		
		userId = map.get("userId");
		p10csr = map.get("p10csr");
		
		if(null != userId && "admin".equals(userId)) {
			
			String value = db.get(userId);
			logger.debug("===老用户【" + userId + "】  p10公钥证书【" + value + "】");
			p10 = value;
		}else {
			logger.debug("===新用户【" + String.valueOf(userId) +"】 证书请求【" + String.valueOf(p10csr) +"】" );
			CertServiceResponseVO cerResp = RACerService.reqCer(p10csr);
			if (null != cerResp) {
				p10 = cerResp.getSignatureCert();
			}
			logger.debug("===新用户【" + String.valueOf(userId) +"】 p10公钥证书【" + p10 + "】");
		}
		resp.getWriter().print(p10);

	}



	@Override
	public void init() throws ServletException {
		super.init();
		db.put("admin", "MIIEPjCCAyagAwIBAgIFIAEBg2cwDQYJKoZIhvcNAQEFBQAwWTELMAkGA1UEBhMCQ04xMDAuBgNVBAoTJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEYMBYGA1UEAxMPQ0ZDQSBURVNUIE9DQTExMB4XDTE0MDkzMDA4MTYxNloXDTE2MDkzMDA4MTYxNlowezELMAkGA1UEBhMCQ04xFTATBgNVBAoTDENGQ0EgVEVTVCBDQTERMA8GA1UECxMITG9jYWwgUkExFTATBgNVBAsTDEluZGl2aWR1YWwtMTErMCkGA1UEAxQiMDUxQGt0VEVTVDAwM0BaMTIwMDA5MjMwOTYwODkwQDE1NjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAIMjgphJiSgLzNEKgm/wKRNIF4GZmDXAv6KBIfKe/6al4lXFuxUEAaUAZI+HrlpBvBUYx5OYWp5t1KZ1dFqBfb0MJDw1B8ZoH3Tn8Gc2CeuJLUipBo9koTxnQsSIXj1vScTZDLFYmWV50U7BYmeR+rXYZUWg4C6PSmg0XgsbcZ1ojYeGO/b3Uj8iWvil60bYDs07q0jp7qoQHaJ44x6lFohi7NM6mABCvslnrnOirPNB+Cdbjwe+A0GqaK0k3PK0aoNg3WpxXZytVDAhaLz48gt/tiOpQ7tMNU/UnsIJYCSciechyfcysBIhZdP8SKt8dcTcJFT6fTAylRWEJICDBJ0CAwEAAaOB6jCB5zAfBgNVHSMEGDAWgBT8C7xEmg4xoYOpgYcnHgVCxr9W+DBIBgNVHSAEQTA/MD0GCGCBHIbvKgECMDEwLwYIKwYBBQUHAgEWI2h0dHA6Ly93d3cuY2ZjYS5jb20uY24vdXMvdXMtMTUuaHRtMDkGA1UdHwQyMDAwLqAsoCqGKGh0dHA6Ly8yMTAuNzQuNDEuODcvb2NhMTEvUlNBL2NybDkxOS5jcmwwCwYDVR0PBAQDAgPoMB0GA1UdDgQWBBT37aDu8SgKYgOquUf0R3YZfcWz4TATBgNVHSUEDDAKBggrBgEFBQcDAjANBgkqhkiG9w0BAQUFAAOCAQEARWRyi0KyH4ubPXlSOAqdKUnjJl7DkXaFJ5D4XfKgScZ25PsZWbIoFaT4jhlpIrBqjQ0d1WJUbQym2otyyWrSTDlsHR7WCZtDt0l2eEyQDW0hUxCVVlZqgjbYBsa/ZmkAab4u4tQ90IzGNvYsdL1g46yp5XPqvVWzP4BHn7Clui//IQwu+2WtuBdiNDTXA72Kx34eghC7Nbgx1KN0fkrAtXIqG/hkmceozl7Y1dXeQs89wkbkdFEBAI7OlZ8y+Lb/RwNrqM1EpTgdI00XrbwvScCLUhoqtqesdQci4g/VE6GE1Jgo6fARpZhlAtPAeo0HCq/FCG8D9Q9A3diJUOsWgA==,{AB0604CA-F359-4F23-A6C9-68F3CA836DB9}");
	}

}
