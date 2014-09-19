package com.zy.execmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExeCmd {

	public static void process(String cmd,String charset) {
		try {
			System.out.println("cmd【" +cmd + "】");
			Process process = Runtime.getRuntime().exec(cmd);
 
			new Thread(new StreamDrainer(process.getInputStream(),charset)).start();
			new Thread(new StreamDrainer(process.getErrorStream(),charset)).start();

			process.getOutputStream().close();

			int exitValue = process.waitFor();
			System.out.println("返回值：" + exitValue);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

class StreamDrainer implements Runnable {
	private InputStream ins;
	private String charset;

	public StreamDrainer(InputStream ins,String charset) {
		this.ins = ins;
		this.charset = charset;
	}

	public void run() {
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins,charset));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
