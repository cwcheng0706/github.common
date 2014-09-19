package com.zy.nio.filedownload.zy.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;

public class MultiDownloadHandler implements Runnable {
	
	private ByteBuffer clientBuffer ;
	private Selector selector;
	private SelectionKey key;
	private CharsetDecoder decoder;
	
	private String fileName;

	public MultiDownloadHandler(ByteBuffer clientBuffer,Selector selector,SelectionKey key,CharsetDecoder decoder, String fileName) throws IOException {
//		clientBuffer = clientBuffer;
//		selector = selector;
//		key = key;
//		decoder = decoder;
//		fileName = fileName;
	}

	@Override
	public void run() {
		try{
			if (key.isAcceptable()) { // 接收事件
	
				ServerSocketChannel server = (ServerSocketChannel) key.channel();
	
				// 获得和客户端连接的通道
				SocketChannel channel = server.accept();
	
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
					System.out.println(Thread.currentThread().getName() + ": Hello  【" + charBuffer.toString() + "】");
					SelectionKey wKey = channel.register(selector,SelectionKey.OP_WRITE);
					wKey.attach(new DownloadHandler(fileName, charBuffer.toString()));
					
				}else {
					System.out.println("客户端传入为空！！通道关闭！");
					channel.close();
				}
				
			} else if (key.isWritable()) { // 写事件
				
				SocketChannel channel = (SocketChannel) key.channel();
				DownloadHandler handler = (DownloadHandler) key.attachment();
				
				System.out.println(Thread.currentThread().getName() + ": 【" + handler.getName() + "】-->" + handler);
				
				ByteBuffer block = handler.readBlock();
				if (block != null) {
					channel.write(block);
					
				} else {
					handler.close();
					channel.close();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			
		}

	}

}
