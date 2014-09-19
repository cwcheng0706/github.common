package com.zy.nio.filedownload.simple.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 文件下载客户端
 * 
 * @author tenyears.cn
 */

public class NIOClient {

	static int SIZE = 5;
	static InetSocketAddress ip = new InetSocketAddress("localhost", 12345);
	static CharsetEncoder encoder = Charset.forName("GB2312").newEncoder();
	
	
	static class Download implements Runnable {
		protected int index;
		protected boolean downFlag = false;
		
		public Download(int index) {
			this.index = index;
		}

		public void run() {
			long start = System.currentTimeMillis();
			int total = 0;
			
			try {
				
				FileOutputStream fos = new FileOutputStream("c:\\VMware安装CentOS6.2安装CentOS_6.2超级详细_" + index + ".pdf");
				
				FileChannel c = fos.getChannel();
				
				
				SocketChannel client = SocketChannel.open();
				client.configureBlocking(false);
				Selector selector = Selector.open();
				client.register(selector, SelectionKey.OP_CONNECT);
				client.connect(ip);
				ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
				
//				FOR: for (;;) {
				while(true && !downFlag) {
					selector.select();
					Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
					while (iter.hasNext()) {
						SelectionKey key = iter.next();
						iter.remove();
						if (key.isConnectable()) {
							SocketChannel channel = (SocketChannel) key.channel();
							if (channel.isConnectionPending()) {
								channel.finishConnect();
							}
							
							String title = "client_" + index;
							channel.write(encoder.encode(CharBuffer.wrap(title)));
							
							channel.register(selector, SelectionKey.OP_READ);
						} else if (key.isReadable()) {
							SocketChannel channel = (SocketChannel) key.channel();
							int count = channel.read(buffer);
							
							
							buffer.flip();
							
//							bos.write(buffer.array());
//							bos.flush();
							
							c.write(buffer);
							
							if (count > 0) {
								total += count;
								buffer.clear();
							} else {
								client.close();
								downFlag = true;
//								break FOR;
							}
						}
					}
				}

				double last = (System.currentTimeMillis() - start) * 1.0 / 1000;
				System.out.println("Thread " + index + " downloaded " + total + "bytes in " + last + "s.");

			} catch (IOException e) {
				e.printStackTrace();
			}finally {
//				double last = (System.currentTimeMillis() - start) * 1.0 / 1000;
//				System.out.println("Thread " + index + " downloaded " + total + "bytes in " + last + "s.");
			}
		}
	}

	public static void main(String[] args) throws IOException {

		ExecutorService exec = Executors.newFixedThreadPool(SIZE);
		for (int index = 1; index <= SIZE; index++) {
			exec.execute(new Download(index));
			
		}
		exec.shutdown();
	}

}
