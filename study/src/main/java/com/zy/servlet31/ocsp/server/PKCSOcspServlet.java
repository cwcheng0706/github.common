package com.zy.servlet31.ocsp.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;

import com.zy.security.PKCSCertificateCoder;
import com.zy.security.jdk.CertificateCoder;

@WebServlet(name = "ocspServlet", urlPatterns = "/ocsp")
public class PKCSOcspServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8118425844082905919L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		
		// if (req.getContentType() == null ||
		// !req.getContentType().equals("application/ocsp-request")) {
		// resp.setContentType("text/html");
		// PrintWriter out = resp.getWriter();
		// out.println("<html><head><title>Novosec OCSPServer V 0.1</title></head>");
		// out.println("<body><font color=\"red\"><h1><u>The POST method is reserved only for OCSP - Clients!<br>(ContentType: application/ocsp-request)</u></h1></font><br>Please use the GET method to obtain human readable status information.</body></html>");
		// return;
		// }

		ServletInputStream is = req.getInputStream();
		byte[] reqBytes = IOUtils.toByteArray(is);
//		String str = new String(bytes,"UTF-8");
//		System.out.println(str);
		
		
//		OCSPResp oscpResp = new OCSPResp(is);
//		try {
//			BasicOCSPResp basicOCSPResp = (BasicOCSPResp)oscpResp.getResponseObject();
//		} catch (OCSPException e) {
//			e.printStackTrace();
//		}
		
		OCSPReq ocspReq = null;
		X509CertificateHolder[] clientcertCertificateHolders = null;
		try {
			ocspReq  = new OCSPReq(reqBytes);
			clientcertCertificateHolders = ocspReq.getCerts();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		

		
		try {
			//解析root CA 证书
			String rootcaCer = FileUtils.readFileToString(new File("d:\\bc\\" + "ocsp" + "RootCAcer.pem"), "UTF-8");
			X509Certificate rootcaCertificate = CertificateCoder.getX509CertificateFromPem(rootcaCer);
			//解析root CA 私钥
			String rootcaKey = FileUtils.readFileToString(new File("d:\\bc\\" + "ocsp" + "RootCAkey.pem"), "UTF-8");
			PrivateKey rootcaPrivateKey = PKCSCertificateCoder.getPrivateKeyFromPem(rootcaKey,"123456");
			
			
			X509Certificate certificate = CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\servercer.pem")));
			X509Certificate issuerCertificate = CertificateCoder.getX509CertificateFromPem(FileUtils.readFileToString(new File("d:\\bc\\serverRootCAcer.pem")));
			OCSPResp ocspResp = PKCSCertificateCoder.createOcspResp(certificate, false, issuerCertificate, rootcaCertificate, rootcaPrivateKey, Long.valueOf("2"));
			
			
			writeData(ocspResp.getEncoded(), resp);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
//		byte[] badresponses = new byte[1];
//		badresponses[0] = 0;
//		try {
//			ocspResp = PKCSCertificateCoder.testCreateOCSP("ocsp");
//			writeData(badresponses, resp);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	private void writeData(byte[] respdata, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/ocsp-response");
		resp.setContentLength(respdata.length);
		ServletOutputStream so = resp.getOutputStream();
		so.write(respdata);
	}
}
