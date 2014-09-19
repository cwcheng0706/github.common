/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月8日 上午9:33:19
 */
package com.zy.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
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
public class DelExcelServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(DelExcelServlet.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4729022242344421445L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String tempFolderName = req.getParameter("tempFolderName");
		
		if(null != tempFolderName && !"".equals(tempFolderName.trim()) ) {
			tempFolderName = geneTemFolder(tempFolderName);
			
			try{
				File file = new File(tempFolderName);
				if(file.exists()) {
					file.delete();
				}
				logger.debug("删除的目录:" + tempFolderName + " 成功！");
			}catch(Exception e) {
				logger.debug("删除的目录:" + tempFolderName + " 异常！");
				logger.error(e);
			}
			
		}else {
			logger.error("传入要删除的目录为空!");
		}
		
		req.getRequestDispatcher("/listExcelServlet").forward(req, resp);
	}
	
	
	private String geneTemFolder(String seq) {
		String filePath = this.getServletConfig().getServletContext().getRealPath("/");
		filePath += Constant.FOLDER + File.separator + seq + File.separator;
		return filePath;
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}


}
