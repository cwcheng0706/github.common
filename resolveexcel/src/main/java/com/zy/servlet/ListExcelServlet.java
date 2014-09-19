/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月8日 上午9:33:19
 */
package com.zy.servlet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zy.entity.FileBean;
import com.zy.util.Constant;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company:
 * @Create Time: 2014年1月8日 上午9:33:19
 */
public class ListExcelServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(ListExcelServlet.class);
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729022242344421445L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Map<String,String> fileMap = new HashMap<String, String>();
		List<FileBean> files = getFiles(fileMap);
		
		req.setAttribute("files", files);
		
		req.getRequestDispatcher("/list.jsp").forward(req, resp);
	}
	
	
	private List<FileBean> getFiles(Map<String,String> fileMap) {
		List<FileBean> files = new ArrayList<FileBean>();
		String filePath = this.getServletConfig().getServletContext().getRealPath("/");
		filePath += Constant.FOLDER;
		File file = new File(filePath);
		File[] children = file.listFiles();
		int len = 0;
		int len1 = 0;
		File tempZipFile = null;
		String key = "";
		if(null != children && 0 < (len = children.length)) {
			for(int i = 0 ; i < len; i++) {
				File temFolder = children[i];
				key = temFolder.getName();
				FileBean fileBean = new FileBean();
				
				tempZipFile = new File(temFolder.getPath() + File.separator + key + ".zip");
				if(tempZipFile.exists()) {
					logger.debug("目录【" + temFolder.getPath() + "】对应zip【"+ tempZipFile.getPath() +"】,存在！");
					fileBean.setDownload(true);
				}else {
//					logger.warn("目录【" + temFolder.getPath() + "】对应zip【"+ tempZipFile.getPath() +"】,不存在！");
				}
				
				if(temFolder.isDirectory()) {
					File[] excelFiles = temFolder.listFiles(new FilenameFilter() {
						
						public boolean accept(File dir, String name) {
							if(-1 != name.lastIndexOf(".xls") || -1 != name.lastIndexOf(".xlsx")) {
								return true;
							}
							return false;
						}
					});
					
					
					fileBean.setTempFolderName(key);
					if(null != excelFiles & 0 < (len1 = excelFiles.length) ) {
						for(int j = 0 ; j < len1 ; j++) {
							File excelFile = excelFiles[j];
							logger.debug(key + "===" + excelFile.getPath());
							
							fileMap.put(key, excelFile.getPath());
							fileBean.setFileName(excelFile.getName());
							fileBean.setFullFileName(excelFile.getPath());
							files.add(fileBean);
						}
					}
				}
			}
		}
		
		return files;
	}


	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}


}
