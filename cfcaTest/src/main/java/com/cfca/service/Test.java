package com.cfca.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import cfca.ra.common.vo.response.CertServiceResponseVO;

import com.cfca.service.ra.RACerService;

import sun.misc.BASE64Encoder;

public class Test {
	
	// 对字节数组Base64编码
	private static BASE64Encoder encoder = new BASE64Encoder();

	public static void main(String[] args) throws Exception {

//		System.out.println(GetImageStr());
		
		String p10csr = "MIICgTCCAWkCAQAwPjELMAkGA1UEBhMCQ04xFTATBgNVBAoMDENGQ0EgVEVTVCBDQTEYMBYGA1UEAwwPY2VydFJlcXVpc2l0aW9uMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzrBC9KZ3MdB3KcTdpJSIdOe3fYhnNEkgDGQBHLggmkZ4TR+tOJLUogtCkvBrun6EKGZeAzn1Bgydz/geaeFMIdOUjfGiul6J/yEBEhbkwT4kmDf2gHHE9mgQ182NuukF+J1NT57AHNUIvn0SM586POe9FkGBe3C4lcQ6cfytltxg+nTMUSz6joLz49xty9K3uM6LIgMEjls1JcfRjDYNhDi39K+Kl4cE5xWZ+ADYr+u0ohgXjh4pHLftrFwHt4l+4JpyPN3IO/0g58HXGgp/EneEhedrWpqEduBd90OvJ+E3Snp1nU+uomta2EdkJGx1eBUwASsDbUWpncUdM0+IYwIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQCT7qXUUpBN7TapEE9Z6uGc4f4DwXZsbSvcKAHg9RGZkXy47FJvXz/sJtrlN8FEJg5/Wj2C8H4HdDzLUV2jSh5vSvCRKT9R29hSzhykL7WdOd3M23YRkKagf7iT59+tAaIzZ/DeY5nLKT3Y9Ewe32RvOVnRdKQ4VCwTapoMNQGwNXtztRrBAcofbUddlGEyfKTE/lpLlR/XUQw7BIQovBznzImCc3e5uiK8xIBRdygXtRGKgMdhXBblFU4ooBNLn+M9BYmjA/1rC80TkhNLsJAeuNw0hUPP+ymvn+/FV2vbVeLMFYKYGY0w67ZWHS+W+7xhutaYomHKgS1/oCfB1kb0";
		
		CertServiceResponseVO raResp = RACerService.reqCer(p10csr);
		System.out.println(raResp);
	}

	public static String GetImageStr() {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream("c:\\psb4XAGBF0E.jpg");
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	
	
	public static String getImageBinary(){    
        File f = new File("D:\\company_JL\\workspace_JL\\cfcaTest\\src\\main\\java\\seal.bmp");           
        BufferedImage bi;    
        try {    
            bi = ImageIO.read(f);    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();    
            ImageIO.write(bi, "jpg", baos);    
            byte[] bytes = baos.toByteArray();    
                
            return encoder.encodeBuffer(bytes).trim();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
        return null;    
    }    
}
