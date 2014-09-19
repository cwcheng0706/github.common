/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月10日 下午2:51:15
 */
package com.zy.entity;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company: 
 * @Create Time: 2014年1月10日 下午2:51:15
 */
public class FileBean {
	
	private String tempFolderName;
	
	private String fileName;
	
	private String fullFileName;
	
	private boolean download = false;;

	public String getTempFolderName() {
		return tempFolderName;
	}

	public void setTempFolderName(String tempFolderName) {
		this.tempFolderName = tempFolderName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}

	public boolean getDownload() {
		return download;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}


}
