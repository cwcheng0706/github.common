package com.zy.nio.filedownload.zy.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;


public class DownloadServer {

	private static final int BUFFER_SIZE = 4096;

	private Selector selector;
	private CharsetDecoder decoder;
	
	private ByteBuffer clientBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	
	protected String fileName = "D:\\maven-repos.rar"; // a big file

	public DownloadServer(int port,String charsetName) throws Exception {
		ServerSocketChannel server = ServerSocketChannel.open();
		selector = Selector.open();
		server.socket().bind(new InetSocketAddress(port));
		server.configureBlocking(false);
		server.register(selector, SelectionKey.OP_ACCEPT);
		
		Charset charset = Charset.forName(charsetName);
		decoder = charset.newDecoder();
	}

	// 监听端口
	public void listen() {
		try {
			while (true) {
				selector.select();
				Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					iter.remove();
					handleKey(key);
//					multiHandleKey(key);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void multiHandleKey(final SelectionKey key) throws IOException {
		
		new Thread(new Runnable() {
			public void run() {
				try {
					handleKey(key);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
			
	}
	

	// 处理事件
	protected void handleKey(final SelectionKey key) throws IOException {
		try {
			if (key.isAcceptable()) { // 接收事件
				ServerSocketChannel server = (ServerSocketChannel) key.channel();

				// 获得和客户端连接的通道
				SocketChannel channel = server.accept();

				System.out.println("channel  " + channel);
				
				// 设置成非阻塞
				channel.configureBlocking(false);

				// 注册读事件 交由下载的读事件处理
				channel.register(selector, SelectionKey.OP_READ);

			} else if (key.isReadable()) { // 读事件
				
				SocketChannel channel = (SocketChannel) key.channel();
				
				clientBuffer.clear();
				
				int count = channel.read(clientBuffer);
				
				if (count > 0) {
					
					clientBuffer.flip();
					CharBuffer charBuffer = decoder.decode(clientBuffer);
					System.out.println("Hello  【" + charBuffer.toString() + "】");
					SelectionKey wKey = channel.register(selector,SelectionKey.OP_WRITE);
					wKey.attach(new DownloadHandler(fileName, charBuffer.toString()));
					
				}else {
					System.out.println("客户端传入为空！！通道关闭！");
					channel.close();
				}
				
			} else if (key.isWritable()) { // 写事件
				
				SocketChannel channel = (SocketChannel) key.channel();
				DownloadHandler handler = (DownloadHandler) key.attachment();
				
				ByteBuffer block = handler.readBlock();
				if (block != null) {
					channel.write(block);
					
				} else {
					handler.close();
					channel.close();
				}
				
//				final SocketChannel channel = (SocketChannel) key.channel();
//				final DownloadHandler handler = (DownloadHandler) key.attachment();
//				new Thread(new Runnable() {
//					
//					@Override
//					public void run() {
//						
//						
//						ByteBuffer block = handler.readBlock();
//						try {
//							if (block != null) {
//								channel.write(block);
//								
//							} else {
//								handler.close();
//								channel.close();
//							}
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//						
//					}
//				}).start();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8888;
		try {
			DownloadServer server = new DownloadServer(port,"GB2312");
			System.out.println("Listernint on " + port);
			
			while (true) {
				server.listen();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
