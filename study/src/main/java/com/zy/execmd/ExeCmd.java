package com.zy.execmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExeCmd {
	
	public static void process(String cmd,String charset) {
		
		try {
			System.out.println("cmd【" +cmd + "】");
			Process process = Runtime.getRuntime().exec(new String[]{"sh","-c",cmd});
 
			Thread t1 = new Thread(new StreamDrainer(process.getInputStream(),charset));
			Thread t2 = new Thread(new StreamDrainer(process.getErrorStream(),charset));
			t1.start();
			t2.start();
			
			t1.join();
			t2.join();
			
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
			System.out.println("============");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
