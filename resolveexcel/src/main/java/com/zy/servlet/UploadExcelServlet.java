/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月8日 上午9:33:19
 */
package com.zy.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.zy.util.Constant;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月8日 上午9:33:19
 */
public class UploadExcelServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(UploadExcelServlet.class);
	
	private static final int BUFFERLENGTH = 2048;

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729022242344421445L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		boolean isMultipart = ServletFileUpload.isMultipartContent(req);// 检查输入请求是否
		logger.debug(isMultipart);

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repository);

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// upload.setSizeMax(10);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss_SSS");
		String seq = sdf.format(new Date());

		try {
			List<FileItem> items = upload.parseRequest(req);

			// Process the uploaded items
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = iter.next();

				if (item.isFormField()) {
					processFormField(item);
				} else {
					//文件上传
					processUploadedFile(item,seq);
				}
				
				//处理Excel
				processExcel(item,seq);
			}

		} catch (FileUploadException e) {
			logger.error(e);
		}
		
//		Map<String,String> fileMap = new HashMap<String, String>();
//		List<FileBean> files = getFiles(fileMap);
//		
//		req.setAttribute("files", files);
		
		req.getRequestDispatcher("/listExcelServlet").forward(req, resp);
	}
	
	
	/**
	 * @Author zy
	 * @Company: 
	 * @Create Time: 2014年1月10日 下午5:00:00
	 * @param item
	 */
	private void processExcel(FileItem item,String seq) {
//		String name = item.getName();
		String folderPath = geneTemFolder(seq);
		logger.debug("folderPath:" + folderPath);
		
		
		
	}


	private String geneTemFolder(String seq) {
		String filePath = this.getServletConfig().getServletContext().getRealPath("/");
		filePath += Constant.FOLDER + File.separator + seq + File.separator;
		return filePath;
	}
	
	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月9日 下午5:09:13
	 * @param item
	 * @throws IOException
	 */
	private void processUploadedFile(FileItem item,String seq) {

		String folderPath = geneTemFolder(seq);
		
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		String name = item.getName();
		logger.debug("folderPath:" + folderPath);
		logger.debug("fileName:" + name);

		InputStream is = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		byte[] buffer = new byte[BUFFERLENGTH];
		try {
			is = item.getInputStream();
			bis = new BufferedInputStream(is);
			fos = new FileOutputStream(new File(folderPath + File.separator + name));
			@SuppressWarnings("unused")
			int len = 0;
			while((len = bis.read(buffer)) != -1 ) {
				fos.write(buffer);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if(null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("关闭写入流错误【" + e + "】");
					fos = null;
				}finally {
					fos = null;
				}
			}
			if(null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					logger.error("关闭输入流错误【" + e + "】");
					bis = null;
				}finally {
					bis = null;
				}
			}

		}

	}

	/**
	 * @Author zy
	 * @Company:
	 * @Create Time: 2014年1月9日 下午5:09:10
	 * @param item
	 */
	private void processFormField(FileItem item) {
//		String fieldName = item.getFieldName();
//		String fileName = item.getName();
//		String contentType = item.getContentType();
//		boolean isInMemory = item.isInMemory();
//		long sizeInBytes = item.getSize();
		
		logger.debug("");

	}

}
