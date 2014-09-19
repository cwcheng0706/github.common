package com.zy.nio.split;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

public final class SplitBigFile implements Runnable {

	final static String folder = "c:\\split";

	// 文件分割的数量 即线程数
	private long THREADS;

	// 文件分割的开始的位置
	private long startIndex;

	// 文件分割后的大小 即分割成多少大小的文件
	private long length;

	// 需要分割的文件路径
	private String localFile;
	

	public SplitBigFile(String localFIle, long splitFileSize) {
		this.localFile = localFIle;

		long contentLength = getContentLength();

		this.THREADS = (contentLength - 1) / splitFileSize + 1;

		// long step = contentLength/THREADS;
		long step = splitFileSize;

		long index = 0;
		for (int i = 0; i < THREADS; i++) { // 第一次
			if (i == 0) {

				this.startIndex = 0;
				this.length = step;
				Thread t = new Thread(this);
				t.start();

			} else if (i == (THREADS - 1)) { // 最后一次

				SplitBigFile worker = new SplitBigFile(localFIle, index, contentLength - index);
				new Thread(worker).start();

			} else {

				SplitBigFile worker = new SplitBigFile(localFIle, index, step);
				new Thread(worker).start();

			}

			index = index + step;
		}

	}

	public SplitBigFile(String localFile, long startIndex, long length) {
		this.startIndex = startIndex;
		this.length = length;
		this.localFile = localFile;
	}

	public long getContentLength() {
		File file = new File(localFile);
		
		return file.length();
	}
	
	/**
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 上午10:03:11
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		String fileName = "catalina.out";
		fileName = "a.log";
		
		// new SplitBigFile(folder + File.separator + "big.log",10);
		
		SplitBigFile s = new SplitBigFile(folder + File.separator + fileName, 1024 * 1024 * 37);
		System.out.println("contentLength " + s.getContentLength());
	}

	@Override
	public void run() {

//		splitByByteArr();
		
		splitByByte();

	}
	
	/**
	 * 分割后 写单个文件时采用内存映射方式写文件
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 上午11:56:49
	 */
	private void splitByByte() {
//		System.out.println(this + " ..start");
		
		
		RandomAccessFile localRandomFileRead = null;
		MappedByteBuffer mapBufferRead = null;
		RandomAccessFile localRandomFileWriter = null;
		MappedByteBuffer mapBufferWriter = null;
		
		
		int start = 0;
		
		try{
			localRandomFileRead = new RandomAccessFile(localFile, "rw");
			mapBufferRead = localRandomFileRead.getChannel().map(MapMode.READ_WRITE, startIndex, length);
			
			// 分割后需要输出的文件
//			FileOutputStream fos = new FileOutputStream(new File(folder + File.separator + Thread.currentThread().getName()) + "_" + System.currentTimeMillis() + ".log");
			localRandomFileWriter = new RandomAccessFile(new File(folder + File.separator + Thread.currentThread().getName() + ".log"), "rw");
			mapBufferWriter = localRandomFileWriter.getChannel().map(MapMode.READ_WRITE, startIndex, length);
			
			
			while (start < length) {
				
				byte b = mapBufferRead.get(start);
				
//				fos.write(b);
				mapBufferWriter.put(b);
				
				start++;
			}
			
//			mapBufferRead.force();
//			mapBufferWriter.force();
			
			mapBufferRead.clear();
			mapBufferWriter.clear();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(Thread.currentThread().getName() + "【" + e + "】");
		}finally {
			// 资源释放
			try {
				if (null != localRandomFileRead) {
					localRandomFileRead.close();
				}
				if (null != localRandomFileWriter) {
					localRandomFileWriter.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(this + " ..end + total : " + start);
		
	}

	/**
	 * 分割文件后  采用缓冲数组写文件以减少频繁写硬盘操作
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 上午9:28:40
	 */
	private void splitByByteArr() {
		System.out.println(this + " ..start");
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		RandomAccessFile localRandomFile = null;
		MappedByteBuffer mapBuffer = null;
		try {

			localRandomFile = new RandomAccessFile(localFile, "rw");
			mapBuffer = localRandomFile.getChannel().map(MapMode.READ_WRITE, startIndex, length);

			int start = 0;

			// 跳过多少字节。以便分片作文件映射
			// localRandomFile.skipBytes(Integer.valueOf(String.valueOf(startIndex)));

			// localRandomFile.s

			// 分割后需要输出的文件
			fos = new FileOutputStream(new File(folder + File.separator + Thread.currentThread().getName()) + "_" + System.currentTimeMillis() + ".log");
			bos = new BufferedOutputStream(fos);

			int step = 10240; // 步长。也是缓冲区字节数组的长度
			byte[] bytes = new byte[step]; // 缓冲数组
			int lastLen = Integer.valueOf(String.valueOf(length)).intValue() % step; // 计算最后一次的长度
																						// 用以后面判断是否为最后一次读取数据

			int total = 0;

			while (start < length) {

				if (length < step) { // 当缓冲数组的长度可以装得下文件映射的大小（length）时

					mapBuffer.get(bytes, 0, Integer.valueOf(String.valueOf(length)).intValue());
					bos.write(bytes, 0, Integer.valueOf(String.valueOf(length)).intValue());
					
//					System.out.println(Thread.currentThread().getName() + " first: i " + start + " len:" + Integer.valueOf(String.valueOf(length)).intValue());
					total += Integer.valueOf(String.valueOf(length)).intValue();
					
				} else if (lastLen == (length - start)) { //最后一次
					
					mapBuffer.get(bytes, 0, (Integer.valueOf(String.valueOf(length)).intValue() - start));
					bos.write(bytes, 0, (Integer.valueOf(String.valueOf(length)).intValue() - start));

//					System.out.println(Thread.currentThread().getName() + " last: i " + start + " len:" + (Integer.valueOf(String.valueOf(length)).intValue() - start) );
					total += Integer.valueOf(String.valueOf(length)).intValue() - start;
					
					
				} else {

					mapBuffer.get(bytes, 0, step);
					bos.write(bytes, 0, step);
					
//					System.out.println(Thread.currentThread().getName() + " middle: i " + start + " len:" + step);
					
					total += step;
					
				}
				

				bos.flush();
				start += step;

			}

			System.out.println(this + " ..end + total : " + total);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Thread.currentThread().getName() + "【" + e + "】");
		} finally {
			// 资源释放
			try {
				if (null != fos) {
					fos.close();
				}
				if (null != bos) {
					bos.close();
				}
				if (null != localRandomFile) {
					localRandomFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	public String toString() {
		return "Worker " + Thread.currentThread().getName() + " [startIndex=" + startIndex + ", length=" + length + "]";
	}

}
