package com.cfca.service.kt;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PrivateKey;

import sun.misc.BASE64Decoder;
import cfca.internal.tool.Mechanism_Inside;
import cfca.kt.toolkit.ClientContext;
import cfca.kt.vo.KeyPairRequestVO;
import cfca.kt.vo.KeyPairResponseVO;
import cfca.kt.vo.PFXRequestVO;
import cfca.kt.vo.PFXResponseVO;
import cfca.kt.vo.util.XmlUtil2;
import cfca.util.CertUtil;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil2;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;

public class KTCerService {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("========生成密钥对,返回证书申请请求========");
		produceCerKey();
		
		
		System.out.println("========证书pfx生成========");
		producePfxFile("140922142447182213","MIIEPTCCAyWgAwIBAgIFIAEAaEYwDQYJKoZIhvcNAQEFBQAwWTELMAkGA1UEBhMCQ04xMDAuBgNVBAoTJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEYMBYGA1UEAxMPQ0ZDQSBURVNUIE9DQTExMB4XDTE0MDkyMjA2MzUxMloXDTE2MDkyMjA2MzUxMlowejELMAkGA1UEBhMCQ04xFTATBgNVBAoTDENGQ0EgVEVTVCBDQTERMA8GA1UECxMITG9jYWwgUkExFTATBgNVBAsTDEluZGl2aWR1YWwtMTEqMCgGA1UEAxQhMDUxQGt0VEVTVDAwM0BaMTIwMDA5MjMwOTYwODkwQDE0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkDfK5v2kY7n4eXpRKoOKTMh3GlxY2gJ06h0VqPC40jUC/8LZa0K9aJG4fJw+a9RB5yF3u2xxZ+dDXHlwOQjIGltU8rNet3Orp0Y65UZIbB/wbsOUVnmlRGQaywdPu+Hb5wJa9Pef2t4jQo45OUsK1dwmC2/xiHB6GDp6VrOSDKoyBkeZadgbs5m+RvoqtssUypzpB4Lii8HA+UBsU6KIf3W0MS1I3rmEyUhzVtnTsFVeH+M/wwLP1ZXZY7YygrdakLe9XAWqTb4QqLcPdp2sA56bUZAZPQYYF8u/ggFKvg+056lh+Aq6XRYL8gaitTjEELuCoY9iy0hN70LU9cgXMQIDAQABo4HqMIHnMB8GA1UdIwQYMBaAFPwLvESaDjGhg6mBhyceBULGv1b4MEgGA1UdIARBMD8wPQYIYIEchu8qAQIwMTAvBggrBgEFBQcCARYjaHR0cDovL3d3dy5jZmNhLmNvbS5jbi91cy91cy0xNS5odG0wOQYDVR0fBDIwMDAuoCygKoYoaHR0cDovLzIxMC43NC40MS44Ny9vY2ExMS9SU0EvY3JsOTA4LmNybDALBgNVHQ8EBAMCA+gwHQYDVR0OBBYEFLboJPeEbKRDAtBJSia9ZW0v3UutMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqGSIb3DQEBBQUAA4IBAQBl+nI+d0HxV0BM8EXxfys8G0I4f+6mhfh7n0MXiDEW2sAxw95jwB7hhP+Ey/78jji/AGGbB0It2ulcxUkaYv+0gSz/HPi4LFtEZdQU5/PPHOrHexPaemep59w8UkdL4V1yfb6Q5K/+z7aWQSPwj/Pln/EcznsVnv/3Pfl//VP2LNPxryce5LkYIwSTJkVeIDkTpKRCe7P5KaDHS5+IXoT/LhwQ+35nddQZEn1dt8imH3/2iVWTfyPfT/4HRGctlXZFkJHOA0QyioII5nCXbDMNl87yTC28SZEhZPxo79FkJ78b24Y/1DNurw7MuER5qSGGSGqv3HhXHq23PiZRF9QL");
		
		
		
	}

	private static ClientContext createCFCARACLientContext() {
		// 服务器地址
		String ip = "172.16.230.173";
		// 服务器端口
		int port = 9040;

		// 连接超时 时间（ms）
		int connectTimeout = 6000;
		// 读取超时 时间（ms）
		int readTimeout = 6000;

		ClientContext client = null;
		// 初始化连接。
		ClientContext.initSocket(ip, port, connectTimeout, readTimeout);

		client = ClientContext.getInstance();
		return client;
	}

	/**
	 * 生成密钥对
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午2:37:01
	 */
	public static KeyPairResponseVO produceCerKey() {
		ClientContext context = createCFCARACLientContext();
		String requestXml = null;
		String responseXml = null;
		KeyPairResponseVO responseVO = null;

		try {
			KeyPairRequestVO requestVO = new KeyPairRequestVO();
			requestVO.setTxType("1001");
			requestXml = XmlUtil2.vo2xml(requestVO, "Request");
			responseVO = context.tx1001(requestVO);
			responseXml = XmlUtil2.vo2xml(responseVO, "Response");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("请求报文" + "\n" + requestXml);
		System.out.println("响应报文" + "\n" + responseXml);
		
		return responseVO;
	}
	
	/**
	 * 生成pfx数据
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午2:37:26
	 * @param keyIdentifier
	 * @param signatureCert
	 */
	public static PFXResponseVO producePfxFile(String keyIdentifier,String signatureCert) {
        ClientContext context = createCFCARACLientContext();
        String requestXml = null;
        String responseXml = null;

        PFXResponseVO responseVO = null;
        try {
            PFXRequestVO requestVO = new PFXRequestVO();
            requestVO.setTxType("1002");
            requestVO.setKeyIdentifier(keyIdentifier);
            requestVO.setSignatureCert(signatureCert);
//            requestVO.setKeyIdentifier("140918161924138202");
//            requestVO.setSignatureCert("MIIEPDCCAySgAwIBAgIFIAEAYEgwDQYJKoZIhvcNAQEFBQAwWTELMAkGA1UEBhMCQ04xMDAuBgNVBAoTJ0NoaW5hIEZpbmFuY2lhbCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEYMBYGA1UEAxMPQ0ZDQSBURVNUIE9DQTExMB4XDTE0MDkxODA4MjIyOVoXDTE2MDkxODA4MjIyOVoweTELMAkGA1UEBhMCQ04xFTATBgNVBAoTDENGQ0EgVEVTVCBDQTERMA8GA1UECxMITG9jYWwgUkExFTATBgNVBAsTDEluZGl2aWR1YWwtMTEpMCcGA1UEAxQgMDUxQGt0VEVTVDAwM0BaMTIwMDA5MjMwOTYwODkwQDEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCTuuJ3o+r+TF721A0340UHbcmA3M5LBXj8/Avmntk9nfLy74k8J2lkT0ygVjqmlT35AbL50QgGZVLshdbvBWKqTLplTBB19FA13Acb7csvPPkW8V9dF4bnhcoPchRto9WKZGphh8mXQc3db/Mfo0cWdiT/NoeRvSjMzCkXtlwnSlggM5TRtdUeaKR7V5cB+jm+ITISDvdxAK2sY9KIPujrS7jzJ2NmSGgyQtflgNL+5dIxjecv5VlmJ+E/JCfK/WJ+Iay7R0qbeGnsVgBTwtPshSwVCGJw43y8jdCMm5TbJ52Fpp43t2L8s2lcF2BrXSFo0fpHzCtw0WfzGQj/Z2OhAgMBAAGjgeowgecwHwYDVR0jBBgwFoAU/Au8RJoOMaGDqYGHJx4FQsa/VvgwSAYDVR0gBEEwPzA9BghggRyG7yoBAjAxMC8GCCsGAQUFBwIBFiNodHRwOi8vd3d3LmNmY2EuY29tLmNuL3VzL3VzLTE1Lmh0bTA5BgNVHR8EMjAwMC6gLKAqhihodHRwOi8vMjEwLjc0LjQxLjg3L29jYTExL1JTQS9jcmw5MDcuY3JsMAsGA1UdDwQEAwID6DAdBgNVHQ4EFgQUU8/wqhgM3VblCWo7pGkILZv4U7IwEwYDVR0lBAwwCgYIKwYBBQUHAwIwDQYJKoZIhvcNAQEFBQADggEBALeZTFhZC4iHyGR3BUdPXyO5ykvzkaqoqs/qTsk9Lt10gVK/K5rvnj3MFuYMplUqi/R6oM0RJxI/0NAxAMnk6cQwGiNf8X51+1JSbA+0paBUGYdhmfA0wICuQedP0HB9/5OReNa/544tNMQbYMw8TyWcO0KqkDjXw1BE+Yng1VpbUOjT1b1GDVVKjGgJaD6SzSeY5a8Lt9SDYcXrsNH1fKtb9DfKDn+C5G6Vamv512PNl6Xs0l8nGe9sW7AqWUOJrNZHg0qFykq0LLB0fWTBVGqLBS5oQe7RxutPDeUM6r1/m6x6ouWBuWzIdm8LVcxQxLsQhV7oWt71kxGl+5bP0gw=");
            requestXml = XmlUtil2.vo2xml(requestVO, "Request");
            responseVO = context.tx1002(requestVO);
            responseXml = XmlUtil2.vo2xml(responseVO, "Response");
            
            System.out.println("请求报文" + "\n" + requestXml);
            System.out.println("响应报文" + "\n" + responseXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseVO;
	}
	
	/**
	 * 查询pfx数据及验签
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午2:37:39
	 * @param keyIdentifier
	 */
	public static void queryPfxByKeyIdentifier(String keyIdentifier) {
        ClientContext context = createCFCARACLientContext();
        String requestXml = null;
        String responseXml = null;

        try {
            PFXRequestVO requestVO = new PFXRequestVO();
            requestVO.setTxType("1003");
//            requestVO.setKeyIdentifier("140918135838825616");
            requestVO.setKeyIdentifier(keyIdentifier);
            PFXResponseVO responseVO = null;
            requestXml = XmlUtil2.vo2xml(requestVO, "Request");
            responseVO = context.tx1003(requestVO);
            responseXml = XmlUtil2.vo2xml(responseVO, "Response");

            System.out.println("请求报文" + "\n" + requestXml);
            System.out.println("响应报文" + "\n" + responseXml);
            
            boolean doSignDemo=true;
            if(doSignDemo){
                // 签名验签示例（所用工具包为CFCA的SADK）
                JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
                Session session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
                
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] pfxData = decoder.decodeBuffer(responseVO.getPfxData());
                X509Cert cert=CertUtil.getCertFromPfx(pfxData,"cfca1234");
                System.out.println("序列号：" + cert.getStringSerialNumber());
                System.out.println("主题（DN）:"  + cert.getSubject());
                System.out.println("颁发者："  + cert.getIssuer());
                System.out.println("有效期："  + cert.getNotAfter()+" - "+cert.getNotBefore());
                PrivateKey privateKey=KeyUtil.getPrivateKeyFromPFX(pfxData, "cfca1234");
                SignatureUtil2 signUtil = new SignatureUtil2();
                String srcData="ffff";
                System.out.println("原文：" + srcData);
                byte[] signature = signUtil.p7SignMessageAttach(Mechanism_Inside.SHA1_RSA, srcData.getBytes("UTF8"), privateKey, cert, session);
                boolean result=signUtil.p7VerifyMessageAttach(signature, session);
                System.out.println("验签结果：" + result);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
