package com.zy.nio.filedownload.simple.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

// 处理与客户端的交互
public class HandleClient {

	static int BLOCK = 4096;

	private String name;
	private FileInputStream fis;
	protected FileChannel channel;
	protected ByteBuffer buffer;

	public HandleClient(String fileName, String name) throws IOException {
		fis = new FileInputStream(fileName);
		this.channel = fis.getChannel();
		this.buffer = ByteBuffer.allocate(BLOCK);
		this.name = name;
	}

	public ByteBuffer readBlock() {
		try {
			buffer.clear();
			int count = channel.read(buffer);
			buffer.flip();
			if (count <= 0) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public void close() {
		try {
			if (null != fis) {
				fis.close();
			}
			if (null != channel) {
				channel.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
