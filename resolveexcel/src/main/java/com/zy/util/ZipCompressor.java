/**
 * @Author: zy
 * @Company: 
 * @Create Time: 2014年1月17日 上午11:05:25
 */
package com.zy.util;

/**
 * @Project: resolveexcel
 * @Author zy
 * @Company: 
 * @Create Time: 2014年1月17日 上午11:05:25
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

/**
 * 此类只支持英文文件名
 * @Project: resolveexcel
 * @Author zy
 * @Company: 
 * @Create Time: 2014年1月17日 下午12:17:30
 */
public class ZipCompressor {

	private static Logger logger = Logger.getLogger(ZipCompressor.class);

	static final int BUFFER = 8192;

	private File zipFile;

	public static void main(String[] args) {
		ZipCompressor zip = new ZipCompressor(
				"E:\\workspace-zy\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\resolveexcel\\excelFiles\\2014-01-17_091518_940\\2014-01-17_091518_940.zip");
		zip.compress("E:\\workspace-zy\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\resolveexcel\\excelFiles\\2014-01-17_091518_940\\workspace");
	}

	public ZipCompressor(String zipFilePathName) {
		zipFile = new File(zipFilePathName);
	}

	public void compress(String packageDir) {
		File file = new File(packageDir);
		if (!file.exists())
			throw new RuntimeException(packageDir + "不存在！");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			System.out.println();
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
			compress(file, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void compress(File file, ZipOutputStream out, String basedir) {
		/* 判断是目录还是文件 */
		if (file.isDirectory()) {
			logger.debug("压缩：" + basedir + file.getName());
			this.compressDirectory(file, out, basedir);
		} else {
			logger.debug("压缩：" + basedir + file.getName());
			this.compressFile(file, out, basedir);
		}
	}

	/** 压缩一个目录 */
	private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
		if (!dir.exists()) {
			logger.error("目录【" + dir.getPath() + "】不存在！");
			return;
		}

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			/* 递归 */
			compress(files[i], out, basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			logger.error("文件【" + file.getPath() + "】存在！");
			return;
		}

		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));

			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);

			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
}

