package com.jl.ssl;

import java.io.File;
import java.io.FileInputStream;

import com.jl.ssl.util.HttpClientUtils;

public class MonitorMain {

	
	/**
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年8月25日 下午4:27:06
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			System.out.println("========================");
			
			if(null == args || 5 > args.length) {
				System.out.println("传入的参数个数不对！");
				return;
			}
			
			
			String ksFilePath = args[0]; //"C:\\Users\\Administrator\\Desktop\\client.p12";
			String clientStroePass = args[1]; //"client123456";

			String tsFilePath = args[2]; //"C:\\Users\\Administrator\\Desktop\\client.truststore";
			String serverTrusStorePass = args[3] ; //"server123456";
			String url = args[4]  ;
			
			
			url = url.replace(" ", "%20");
			
			String method = "get";
			if(6 == args.length && null != args[5] && !"".equals(args[5].trim())) {
				method = args[5];
			}
			
			FileInputStream ksIn = new FileInputStream(new File(ksFilePath));
			FileInputStream tsIn = new FileInputStream(new File(tsFilePath));
			
			String respStr = HttpClientUtils.ssl(ksIn, clientStroePass, tsIn, serverTrusStorePass,url,method,443);
			System.out.println("===================响应字串===================");
			System.out.println("【" + respStr + "】");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
