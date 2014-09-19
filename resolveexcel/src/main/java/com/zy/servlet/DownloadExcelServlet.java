/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月8日 上午9:33:19
 */
package com.zy.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zy.util.Constant;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月8日 上午9:33:19
 */
public class DownloadExcelServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DownloadExcelServlet.class);

	private String contentType = "application/x-msdownload";
	private String enc = "utf-8";

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729022242344421445L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String tempFolderName = req.getParameter("tempFolderName");
		logger.debug("tempFolderName：" + tempFolderName);

		String filePath = this.getServletConfig().getServletContext().getRealPath("/");
		filePath += Constant.FOLDER + File.separator + tempFolderName + File.separator + tempFolderName + ".zip";

		File zip = new File(filePath);
		if (zip.exists()) {

			String filename = URLEncoder.encode(zip.getName(), enc);
			resp.reset();
			resp.setContentType(contentType);
			resp.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			int fileLength = (int) zip.length();
			resp.setContentLength(fileLength);

			if (fileLength != 0) {
				/* 创建输入流 */
				InputStream inStream = null;
				ServletOutputStream out = null;
				try {
					inStream = new FileInputStream(zip);
					byte[] buf = new byte[4096];
					/* 创建输出流 */
					out = resp.getOutputStream();
					int readLength;
					while (((readLength = inStream.read(buf)) != -1)) {
						out.write(buf, 0, readLength);
					}
					out.flush();
				} catch (Exception e) {
					logger.error(e);
				} finally {
					if (null != out) {
						out.close();
					}
					if (null != inStream) {
						inStream.close();
					}

				}

			}

		} else {
			logger.error("zip包【" + zip.getPath() + "】不存在！");
		}

		logger.debug("下载的文件名【" + filePath + "】");

		// req.getRequestDispatcher("/listExcelServlet").forward(req, resp);
	}

}
