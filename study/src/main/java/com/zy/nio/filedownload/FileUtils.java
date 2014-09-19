package com.zy.nio.filedownload;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtils {

	private static final int capacity = 4096;

	public static void main(String[] args) throws Exception {
		long t = System.currentTimeMillis();
		nioCcopyFileBy("D:\\company_JL\\新人手册.docx", "c:\\新人手册.docx");

		System.out.println((System.currentTimeMillis() - t));
	}

	// 普通I/O文件复制功能
	public static void normalCopy(String infile, String outfile) {
		FileInputStream fin = null;
		FileOutputStream fout = null;

		try {
			fin = new FileInputStream(infile);
			fout = new FileOutputStream(outfile);
			byte[] block = new byte[capacity];
			while (fin.read(block) != -1) {
				fout.write(block);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fin) {
					fin.close();
				}
				if (null != fout) {
					fout.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void nioCcopyFileBy(String srcFileName, String destFileName) {

		FileInputStream srcFis = null;
		FileOutputStream destFis = null;

		try {
			srcFis = new FileInputStream(srcFileName);
			destFis = new FileOutputStream(destFileName);

			// 获取输入输出通道
			FileChannel fcin = srcFis.getChannel();
			FileChannel fcout = destFis.getChannel();

			// 创建缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(capacity);

			while (true) {
				// clear方法重设缓冲区，使它可以接受读入的数据
				buffer.clear();

				// 从输入通道中将数据读到缓冲区
				int r = fcin.read(buffer);

				// read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
				if (r == -1) {
					break;
				}

				// flip方法让缓冲区可以将新读入的数据写入另一个通道
				buffer.flip();

				// 从输出通道中将数据写入缓冲区
				fcout.write(buffer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != srcFis) {
				try {
					srcFis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != destFis) {
				try {
					destFis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
