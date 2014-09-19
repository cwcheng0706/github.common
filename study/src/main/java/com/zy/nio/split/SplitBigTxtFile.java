package com.zy.nio.split;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;


public class SplitBigTxtFile{
	
	private String splitFile = "c:/split/a.log";
	private long splitFileSize = 1024 * 1024 * 37; //37会有乱码 
	private String folder = "c:/split";
	

	public void process() {
		
		long contentLength = getContentLength();
		int chineseTakeByteSize = 0;
		
		int threadCount = (int) ((contentLength - 1) / splitFileSize + 1);
		
		//判断文件的编码格式 计算出汉字编码字节的位数
		String encoding = getEncoding(splitFile);
		
		encoding = "UTF-8";//目前写死
		
		if("UTF-8".equals(encoding)) {
			chineseTakeByteSize = 3;
		}
		
		long offset = 0 ;
		long startIndex = 0;
		long length = 0;
		long step = this.splitFileSize;
		
		System.out.println("分割文件的个数：" +threadCount + "  文件总大小：" + contentLength);
		
		for(int i = 0 ; i < threadCount ; i++) { 
			
			if (i == 0 && threadCount != 1) {//第一次 但不至一次
				
				startIndex = 0;
				length = step;
				
			}else if(i == 0 && threadCount == 1) {//第一次 有却只有一次
				
				startIndex = 0;
				length = contentLength;
				
			}else if(i == (threadCount - 1)) { //最后一次 
				
				length = contentLength - startIndex;
				
			}else {
				
				length = step;
				
			}
			
			
			Worker worker = new Worker(folder,splitFile,startIndex,length,chineseTakeByteSize,String.valueOf(i));
			offset = worker.work();
			

			
			startIndex += step;
			
			if(0 < offset) {
				System.out.println(i + " 偏移前：" + startIndex);
				startIndex = startIndex -(chineseTakeByteSize-offset);
				System.out.println(i+ " 偏移后：" + startIndex);
			}
		}
		
		
		
	}
	
	/**
	 * 测试 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 下午1:25:37
	 * @param args
	 */
	public static void main(String[] args) {
		SplitBigTxtFile bigTxtFile = new SplitBigTxtFile();
		
		bigTxtFile.process();
	}
	
	private long getContentLength() {
		File file = new File(splitFile);
		return file.length();
	}
	
	private String getEncoding(String fileName) {
		String encoding = "GBK";
		File file = new File(fileName);
		
		InputStream in = null;  
		byte[] b = new byte[3];  
		try {
			in= new java.io.FileInputStream(file);  
			in.read(b);
			
			if (b[0] == -17 && b[1] == -69 && b[2] == -65)  {
			    System.out.println(file.getName() + "：编码为UTF-8");  
			    encoding = "UTF-8";
			}else  {
				System.out.println(file.getName() + "：编码为GBK");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if(null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}
		
		return encoding;
	}
	
}

class Worker {
	
	private String index;

	private String folder;

	// 需要分割的文件路径
	private String splitFile;
		
	// 文件分割的开始的位置
	private long startIndex;

	// 文件分割后的大小 即分割成多少大小的文件
	private long length;
	
	//汉字编码的字节数
	private int chineseTakeByteSize;


	public Worker(String folder,String splitFile,long startIndex,long length,int chineseTakeByteSize,String index) {
		this.folder = folder;
		this.splitFile = splitFile;
		this.startIndex = startIndex;
		this.length = length;
		this.chineseTakeByteSize = chineseTakeByteSize;
		
		this.index = index;
	}
	
	public int work() {
		int offset = 0;
		System.out.println(this + " ..start");
		
//		offset = splitByMappBuffer();
		
		offset = splitByByteArr();
		
		System.out.println(this + " ..end");
		
		return offset;
	}

	/**
	 * 采用内存映射的方式写分割后的文件
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 下午1:13:05
	 */
	private int splitByMappBuffer() {
		int offset = 0;
		
		RandomAccessFile localRandomFileRead = null;
		MappedByteBuffer mapBufferRead = null;
		
		RandomAccessFile localRandomFileWriter = null;
		MappedByteBuffer mapBufferWriter = null;
		
		int start = 0;
		int chineseByteCount = 0;
		
		try{
			localRandomFileRead = new RandomAccessFile(splitFile, "rw");
			mapBufferRead = localRandomFileRead.getChannel().map(MapMode.READ_ONLY, startIndex, length);
			
			localRandomFileWriter = new RandomAccessFile(new File(folder + File.separator + System.currentTimeMillis() + "_" + Thread.currentThread().getName() + "_" + index + ".log"), "rw");
			mapBufferWriter = localRandomFileWriter.getChannel().map(MapMode.READ_WRITE, startIndex, length);
			
			
			//1 以下写法会导致写文件的时候 有很多NUL字符写到文件里面中去。 并导致后面生成的文件以递增的方式增加
			while (start < length) {
				
				byte b = mapBufferRead.get();
//				byte b = localRandomFileRead.readByte();
				
				if( ((int)b) < 0 ) {
					chineseByteCount++;
				}
				
				mapBufferWriter.put(b);
				
				start++;
			}
			
			
			//2 采用读取到byte[] 中再写这个byte[] 
			/*int step = 1024;
			int len = 0;
			byte[] bytes = new byte[step];
			int lastLen = Integer.valueOf(String.valueOf(length)).intValue() % step; // 计算最后一次的长度 用以后面判断是否为最后一次读取数据
			while (start < length) {
				
				if (length < step) { // 当缓冲数组的长度可以装得下文件映射的大小（length）时
					
					len = Integer.valueOf(String.valueOf(length)).intValue();
					
				} else if (lastLen == (length - start)) { //最后一次
					
					len = Integer.valueOf(String.valueOf(length)).intValue() - start;
					
				}else{
					
					len = step;
				}
				
				//读数据
				mapBufferRead.get(bytes, 0, len);
				
				//写数据
				mapBufferWriter.put(bytes, 0, len);
				
				start += len;
			}*/
			
			
			
			offset = chineseByteCount % chineseTakeByteSize;
				
			System.out.println(this.index + " 汉字字节数：" + chineseByteCount + "%" + chineseTakeByteSize + "=" + offset);
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(this.index  + "【" + e + "】");
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
		
		return offset;
	}
	
	/**
	 * 采用缓冲字节数组方式写分割后的文件
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月28日 下午7:00:28
	 * @return
	 */
	private int splitByByteArr() {
		
		int offset = 0;
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;

		RandomAccessFile localRandomFile = null;
		MappedByteBuffer mapBuffer = null;
		
		int chineseByteCount = 0;
		
		try {

			localRandomFile = new RandomAccessFile(splitFile, "rw");
			mapBuffer = localRandomFile.getChannel().map(MapMode.READ_WRITE, startIndex, length);

			int start = 0;

			// 跳过多少字节。以便分片作文件映射
			// localRandomFile.skipBytes(Integer.valueOf(String.valueOf(startIndex)));

			// localRandomFile.s

			// 分割后需要输出的文件
			fos = new FileOutputStream(new File(folder + File.separator + this.index + "_" + System.currentTimeMillis() + ".log"));
			bos = new BufferedOutputStream(fos);

			int step = 1024; // 步长。也是缓冲区字节数组的长度
			byte[] bytes = new byte[step]; // 缓冲数组
			int lastLen = Integer.valueOf(String.valueOf(length)).intValue() % step; // 计算最后一次的长度 用以后面判断是否为最后一次读取数据

			int total = 0;

			int len = 0;
			int startIndex = 0;
			while (start < length) {

				if (length < step) { // 当缓冲数组的长度可以装得下文件映射的大小（length）时
					
					startIndex = 0 ;
					len = Integer.valueOf(String.valueOf(length)).intValue();

//					mapBuffer.get(bytes, 0, Integer.valueOf(String.valueOf(length)).intValue());
//					bos.write(bytes, 0, Integer.valueOf(String.valueOf(length)).intValue());
					
//					total += Integer.valueOf(String.valueOf(length)).intValue();
					
				} else if (lastLen == (length - start)) { //最后一次
					
					startIndex = 0 ;
					len = Integer.valueOf(String.valueOf(length)).intValue() - start;

//					mapBuffer.get(bytes, 0, (Integer.valueOf(String.valueOf(length)).intValue() - start));
//					bos.write(bytes, 0, (Integer.valueOf(String.valueOf(length)).intValue() - start));

//					total += Integer.valueOf(String.valueOf(length)).intValue() - start;
					
				} else {
					
					startIndex = 0 ;
					len = step;

//					mapBuffer.get(bytes, 0, step);
//					bos.write(bytes, 0, step);
					
//					total += step;
					
				}
				
				//计算此次总共处理的字节数 供打印日志 核对
				total += len;
				
				//读数据
				mapBuffer.get(bytes, startIndex, len);
				
				//计算汉字的字节数
				chineseByteCount += geChineseByteCount(bytes, len);
				
				//最后一次 的时候 去掉多读取中半个汉字的字节
				if ((start+step) >= length) { 
					offset = chineseByteCount % chineseTakeByteSize;
					len = len - (chineseTakeByteSize - offset);
				}
				
				//写数据
				bos.write(bytes,  startIndex, len);
				bos.flush();
				start += step;

			}
			
//			offset = chineseByteCount % chineseTakeByteSize;
			System.out.println(this.index + " 汉字字节数：" + chineseByteCount + "%" + chineseTakeByteSize + "=" + offset);

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
		
		return offset;
	}
	
	private int geChineseByteCount (byte[] bytes,int length){
		int total = 0;
		
		byte b;
		if(null != bytes && 0 < bytes.length) {
			for(int i = 0 ; i < length ; i++) {
				b = bytes[i];
				if( ((int)b) < 0 ) {
					total++;
				}
			}
		}
		
		return total;
	}

	
	public String toString() {
		return "Worker " + this.index  + " 【startIndex=" + startIndex + ", length=" + length + "】";
	}

}
