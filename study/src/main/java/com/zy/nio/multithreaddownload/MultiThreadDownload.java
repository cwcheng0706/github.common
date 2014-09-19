package com.zy.nio.multithreaddownload;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * 多线程 下载服务器的文件
 * 
 * @Project: study
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年8月26日 下午1:53:47
 */
public final class MultiThreadDownload implements Runnable {

	// 多线程下载的数量
	private static int THREADS = 5;

	// 每个线程下载开始的位置
	private int startIndex;

	// 每个线程下载内容的长度
	private int length;

	// 文件保存位置
	private String localFile;

	// 远程文件的流
	InputStream in;

	private MultiThreadDownload(String urlFile, String localFile, int startIndex, int length) throws IOException {
		this.startIndex = startIndex;
		this.length = length;
		this.localFile = localFile;
		init(urlFile);
	}

	/**
	 * 
	 * 主线程打开网络文件,先分割为指定的大小，然后开启多线程下载
	 */
	public MultiThreadDownload(String urlFile, String localFile) throws IOException {

		this.localFile = localFile;
		int contentLength = init(urlFile); //101
		int step = contentLength / THREADS; //10
		int index = 0;

		for (int i = 0; i < THREADS; i++) {
			if (i == 0) {
				this.startIndex = 0;
				this.length = step;
				new Thread(this).start();
			} else if (i == THREADS - 1) {
				MultiThreadDownload worker = new MultiThreadDownload(urlFile, localFile, index, contentLength - index);
				new Thread(worker).start();
			} else {
				MultiThreadDownload worker = new MultiThreadDownload(urlFile, localFile, index, step);
				new Thread(worker).start();
			}
			index = index + step;
		}

	}

	private int init(String urlFile) throws IOException {
		URL url;
		url = new URL(urlFile);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5 * 1000);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, "
				+ "application/x-shockwave-flash, application/xaml+xml, "
				+ "application/vnd.ms-xpsdocument, application/x-ms-xbap, "
				+ "application/x-ms-application, application/vnd.ms-excel, "
				+ "application/vnd.ms-powerpoint, application/msword, */*");
		connection.setRequestProperty("Accept-Language", "zh-CN");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Connection", "Keep-Alive");
		InputStream in = connection.getInputStream();
		this.in = in;
		int total = connection.getContentLength();
		System.out.println("total:" + total);
		return total;
	}

	@Override
	public void run() {
		System.out.println(this + "start");
		try {
			RandomAccessFile localRandomFile = new RandomAccessFile(localFile, "rw");
			MappedByteBuffer buffer = localRandomFile.getChannel().map(MapMode.READ_WRITE, startIndex, length);
			int i = 0;
			
			in.skip(startIndex);
			while (i < length) {
				buffer.put((byte) in.read());
				i++;
			}
			buffer.force();
			in.close();
			localRandomFile.close();
			
			
//			int readLeng = 0;
//			BufferedInputStream bis = new BufferedInputStream(in);
//			in.skip(startIndex);
//			byte[] bytes = new byte[1];
//			while(i < length && (-1 !=(readLeng = in.read(bytes)))) {//length 4756922
//				if("Thread-0".equals(Thread.currentThread().getName())) {
//					System.out.println(Thread.currentThread().getName() + "===i " + i +"  readLeng: " + readLeng);
//				}
//				buffer.put(bytes,0,readLeng);
//				i = i + readLeng;
//				
//			}
//			buffer.force();
//			in.close();
//			localRandomFile.close();		
			
			
			
			System.out.println(this + " end");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Thread.currentThread().getName() + "【" + e + "】");
		}

	}

	@Override
	public String toString() {
		return "Worker " + Thread.currentThread().getName() + " [localFile=" + localFile + ", startIndex=" + startIndex + ", length=" + length + "]";
	}

	public static void main(String[] args) throws IOException {
		new MultiThreadDownload("http://mirror.hust.edu.cn/eclipse//technology/epp/downloads/release/luna/R/eclipse-jee-luna-R-win32-x86_64.zip",
				"eclipse-jee-luna-R-win32-x86_64.zip");
		
//		new MultiThreadDownload("http://mirror.bit.edu.cn/apache/tomcat/tomcat-7/v7.0.55/bin/apache-tomcat-7.0.55.zip",
//				"apache-tomcat-7.0.55.zip");
		
		
		
	}

}
