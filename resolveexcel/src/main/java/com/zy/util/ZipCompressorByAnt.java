/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月17日 下午12:42:01
 */
package com.zy.util;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company: 
 * @Create Time: 2014年1月17日 下午12:42:01
 */

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

public class ZipCompressorByAnt {
	
	public static void main(String[] args) {
		
		
		ZipCompressorByAnt zip = new ZipCompressorByAnt(
				"E:\\workspace-zy\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\resolveexcel\\excelFiles\\2014-01-17_091518_940\\2014-01-17_091518_940.zip");
		zip.compress("E:\\workspace-zy\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\resolveexcel\\excelFiles\\2014-01-17_091518_940\\workspace");
		
	}
	
	private static Logger logger = Logger.getLogger(ZipCompressorByAnt.class);

	private File zipFile;

	public ZipCompressorByAnt(String zipFilePathName) {
		zipFile = new File(zipFilePathName);
	}
	
	public void compress(String packageDir) {
		File srcdir = new File(packageDir);
		if (!srcdir.exists()) {
			logger.error(packageDir + "不存在！");
		}
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setDir(srcdir);
		//fileSet.setIncludes("**/*.java"); 包括哪些文件或文件夹 eg:zip.setIncludes("*.java");
		//fileSet.setExcludes(...); 排除哪些文件或文件夹
		zip.addFileset(fileSet);
		
		zip.execute();
	}
}
