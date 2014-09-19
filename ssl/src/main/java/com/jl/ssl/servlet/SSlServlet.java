package com.jl.ssl.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.jl.ssl.Ssl;
import com.jl.ssl.util.HttpClientUtils;

public class SSlServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(SSlServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 6420666763733070720L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int yourMaxMemorySize = 2048;
		File yourTempDirectory = new File("");
		long yourMaxRequestSize = 1024000;

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory(yourMaxMemorySize, yourTempDirectory);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Set overall request size constraint
		upload.setSizeMax(yourMaxRequestSize);
		
		Ssl ssl = new Ssl();

		// Parse the request
		try {
			List<FileItem> items = upload.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();
				if (item.isFormField()) {
					
					processFormField(item,ssl);
				} else {
					processUploadedFile(item,ssl);
				}
			}
			
			HttpClientUtils.ssl(ssl.getServerIs(),ssl.getServerPasswd(), ssl.getClientIs(),ssl.getClientPasswd(),"","",8443);
			

		} catch (Exception e) {
			logger.error(e);
		}
		
		
	}

	private void processUploadedFile(FileItem item, Ssl ssl) throws IOException {
		if (!item.isFormField()) {
		    String fieldName = item.getFieldName();
//		    String fileName = item.getName();
		    logger.debug("UploadedFile ---> fieldName: " + fieldName);
		    if("clientCerFile".equals(fieldName)) {
		    	ssl.setClientIs(item.getInputStream());
		    }else if("serverCerFile".equals(fieldName)) {
		    	ssl.setServerIs(item.getInputStream());
		    }
		}
	}

	private void processFormField(FileItem item, Ssl ssl) {
		if (item.isFormField()) {
		    String name = item.getFieldName();
		    String value = item.getString();
		    if("url".equals(name)) {
		    	ssl.setUrl(value);
		    }else if("clientCerPasswd".equals(name)) {
		    	ssl.setClientPasswd(value);
		    }else if("serverCerPasswd".equals(name)) {
		    	ssl.setServerPasswd(value);
		    }
		    
		    logger.debug("FormField ---> name: " + name + "  value: " + value);
		}
		
	}

}
