package com.zy.servlet31;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Hex;

import com.zy.security.jdk.CertificateCoder;
import com.zy.security.jdk.MessageDigestCoder;

public class TestCRLLoad {

	
	
	public static void main(String[] args) throws Exception {
		
//		ExecutorService threadPool = Executors.newFixedThreadPool(200);
//		for(int i = 0 ; i < 200; i++) {
//			Runnable task = new Worker();
//			threadPool.submit(task);
//		}
		
		
		while(true) {
			InputStream in = null;
			URL url = new URL("http://ssl.jl.com/crl/ca-crl.pem");
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(5 * 1000);
	        
	        in = conn.getInputStream();
			byte[] bytes = MessageDigestCoder.encrypt(in , MessageDigestCoder.KEY_SHA);
			System.out.println(Hex.encodeHexString(bytes));
			
			Thread.sleep(1000);
		}
	}
	
	
	
}

class Worker implements Runnable{
	
	String clientCert = "MIID6TCCAtGgAwIBAgIBAzANBgkqhkiG9w0BAQUFADBnMQswCQYDVQQGEwJDTjELMAkGA1UECBMCU0gxFzAVBgNVBAoTDkpMIENvcnBvcmF0aW9uMRcwFQYDVQQLEw5TZWN1cml0eUNlbnRlcjEZMBcGA1UEAxMQSkwgVHJ1c3QgTmV0V29yazAeFw0xNDExMTEwNDEyNTZaFw0yNDExMDgwNDEyNTZaMFUxCzAJBgNVBAYTAkNOMQswCQYDVQQIEwJTSDEXMBUGA1UEChMOSkwgQ29ycG9yYXRpb24xCzAJBgNVBAsTAkpMMRMwEQYDVQQDEwp6aHV5b25nMDAyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs8x7glAAuVXGflm81pn2S7cwOVTec7bKvf1rijiIRAA3YjMBNfl3RV95KDIjQ7EDszWuKU5x/+RmhfedJeYDUWxbhIlezp+oHhkqe5h3t8oiXT0d1SxgteHhNUKP5PQVJswlx2qw0ZIPVYoxsC0+krRxoRXwG/JmkE7yFhPdddrr9o/0yhR/bOlN1mOb3FwBC6B0NgcejQ6Ip96Nm2wCWKesfbTZcPIFvElULT0IHgPGu+2jEZmdnAqWuXxTTPAm9B8C9PU76jen2YBNXcpBNfkesl+G//9addZ18o9GSg2lG1ikaTTvzYfEM0Avo2PsZMv4QH0i3v3Z9nTOEu2L9QIDAQABo4GxMIGuMAkGA1UdEwQCMAAwLAYJYIZIAYb4QgENBB8WHU9wZW5TU0wgR2VuZXJhdGVkIENlcnRpZmljYXRlMB0GA1UdDgQWBBQ6x5x237QK6l2CY2ldhjcTHArdbzAfBgNVHSMEGDAWgBRC/DXP3Db7/SNphBSKtkNRVkZTnjAzBggrBgEFBQcBAQQnMCUwIwYIKwYBBQUHMAGGF2h0dHA6Ly9zc2wuamwuY29tOjEyMzQ1MA0GCSqGSIb3DQEBBQUAA4IBAQCMOx3ivt2w9U7xpTrTQ7w66oXQWHhqWLGOmGNBZMdspSB8ZdNv64vu+vrHiZqCM5j/Gwc+TmsQasjAFQOxLK4vMHaoi67wZ1f439TqkF19l+ZSno0NRiySubqpkbctV3McSLIhiGQHgs1eiT9vbFIuLpTE	+ZNC1ojobWY9sLWMlp3dmaWIkMrYaKD6MKtue8MNUKezrm640sc9qHspdW7IGckJyaJBqjHlvXYr2W1jwvkSXw2CoQaNp6sgEx/eK3ptmBBgZSKMqqF2MGz0Vid/mSysCXNnZS7jvN1om+1V5+RoN5yKpN4APHdHm2rrXnmgYtBuKhizRzalSYGr20Im";

	@Override
	public void run() {
		while(true) {
			
			boolean isRevoked = false;
			if(null != clientCert) {
				X509Certificate cert = CertificateCoder.getX509Certificate(clientCert);
				X509CRL crl = CertificateCoder.loadX509CRL("http://ssl.jl.com/crl/ca-crl.pem");
				if(null != crl) {
					isRevoked = crl.isRevoked(cert);
					System.out.println("【" + Thread.currentThread().getName() + "】-【" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()) +"】  -当前用户证书吊销状态【" + isRevoked + "】");
				}
				
			}
			
			try {
				Thread.sleep(600);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
