package com.zy.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import org.apache.tools.zip.ZipEntry;
//import org.apache.tools.zip.ZipOutputStream;

public class ZipTest {

	/**
	 * 
	 * @param inputFileName
	 *            输入一个文件夹
	 * @param zipFileName
	 *            输出一个压缩文件夹，打包后文件名字
	 * @throws Exception
	 */
	public void zip(String inputFileName, String zipFileName) throws Exception {
		System.out.println(zipFileName);
		zip(zipFileName, new File(inputFileName));
	}

	private void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
		zip(out, inputFile, "");
		System.out.println("zip done");
		out.close();
	}

	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) { // 判断是否为目录
			File[] fl = f.listFiles();
			// out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
			System.out.println(base + "-------------");
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else { // 压缩目录中的所有文件
			// out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

	public static void main(String[] temp) {
		String inputFileName = "D:\\bc"; // 你要压缩的文件夹
		String zipFileName = "D:\\bc.rar"; // 压缩后的zip文件

		ZipTest book = new ZipTest();
		try {
			book.zip(inputFileName, zipFileName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
