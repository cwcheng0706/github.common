/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月8日 上午9:33:19
 */
package com.zy.ssl.keytool.servlet;


import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zy.ssl.keytool.JMXService;
import com.zy.ssl.keytool.KeytoolUtils;
import com.zy.ssl.keytool.entity.Certificate;
import com.zy.ssl.keytool.entity.CertificateBody;
import com.zy.ssl.keytool.entity.StoreType;


/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月8日 上午9:33:19
 */
public class SSLGenServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(SSLGenServlet.class);
	

	private static final long serialVersionUID = -4729022242344421445L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("============start=========");
		try{
			String alias = req.getParameter("alias");
			String cerFolder = req.getParameter("folder");
			String validity = req.getParameter("validity");
			String storepass= req.getParameter("storepass");
			
			String cn = "jl.ssl.com";
			String ou = "JL";
			String o = "SH_o";
			String l = "SH_l";
			String st = "SH_st";
			String c = "CN";
			
			KeytoolUtils keytoolUtils = new KeytoolUtils();
			
			CertificateBody certificateBody = new CertificateBody();
			certificateBody.setCn(cn);
			certificateBody.setOu(ou);
			certificateBody.setO(o);
			certificateBody.setL(l);
			certificateBody.setSt(st);
			certificateBody.setC(c);
			
			String clientKeyFolder = cerFolder + File.separator + alias;
			File clientKeyFolderFile = new File(clientKeyFolder);
			if(!clientKeyFolderFile.exists()) {
				clientKeyFolderFile.mkdirs();
			}else {
				logger.warn("目录【" + clientKeyFolder + "】已存在");
			}
			
			//1.生成客户 证书库  生成.p12文件
			Certificate p12Certificate = new Certificate();
			p12Certificate.setAlias(alias);
			p12Certificate.setStoreType(StoreType.PKCS12.getName());
			p12Certificate.setKeyStore(p12Certificate.getAlias());
			p12Certificate.setValidity(validity);
			p12Certificate.setStorePass(storepass);
			p12Certificate.setKeyPass(storepass);
			p12Certificate.setBody(certificateBody);
			
			keytoolUtils.geneClientCertificate(p12Certificate,clientKeyFolder);
			
			JMXService jmxService = new JMXService();
			jmxService.activeSSLKeyStore();
			
			
			logger.info("============end=========");
		}catch(Exception e) {
			logger.error(e);
		}
		req.getRequestDispatcher("/ssllist.jsp").forward(req, resp);
		
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}


}
