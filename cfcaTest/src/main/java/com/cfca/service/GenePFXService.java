package com.cfca.service;

import java.io.FileOutputStream;
import java.security.PrivateKey;

import cfca.internal.tool.Mechanism_Inside;
import cfca.kt.vo.KeyPairResponseVO;
import cfca.kt.vo.PFXResponseVO;
import cfca.ra.common.vo.response.CertServiceResponseVO;
import cfca.util.Base64;
import cfca.util.CertUtil;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil2;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;

import com.cfca.service.kt.KTCerService;
import com.cfca.service.ra.RACerService;

public class GenePFXService {

	public static void main(String[] args) throws Exception {

		System.out.println("========1.生成密钥对,返回证书申请请求========");
		KeyPairResponseVO ktResp = KTCerService.produceCerKey();

		System.out.println("========2.证书申请========");
		String p10csr = ktResp.getCsr();
		CertServiceResponseVO raResp = RACerService.reqCer(p10csr);
		
		System.out.println(raResp);

		System.out.println("========3.证书pfx生成========");
		String keyIdentifier = ktResp.getKeyIdentifier();
		String signatureCert = raResp.getSignatureCert();

		PFXResponseVO pfxResponseVO = KTCerService.producePfxFile(keyIdentifier, signatureCert);

		System.out.println("================================================");
		System.out.println("==================生成pfx文件====================");
		System.out.println("================================================");

		String base64Pfx = pfxResponseVO.getPfxData();
		byte[] pfx = Base64.decode(base64Pfx);
		
		FileOutputStream fos = new FileOutputStream("c:\\zhuyong.pfx");

		fos.write(pfx);
		fos.close();
		
		// 签名验签示例（所用工具包为CFCA的SADK）
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        Session session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        
		
        X509Cert cert=CertUtil.getCertFromPfx(pfx,"cfca1234");
        System.out.println("序列号：" + cert.getStringSerialNumber());
        System.out.println("主题（DN）:"  + cert.getSubject());
        System.out.println("颁发者："  + cert.getIssuer());
        System.out.println("有效期："  + cert.getNotAfter()+" - "+cert.getNotBefore());
        PrivateKey privateKey=KeyUtil.getPrivateKeyFromPFX(pfx, "cfca1234");
        
        String privateStr = new String(Base64.encode(privateKey.getEncoded()),"UTF-8");
        System.out.println("私钥【" + privateStr +"】");
//        System.out.println("公钥【" + publicStr +"】");
        
        SignatureUtil2 signUtil = new SignatureUtil2();
        String srcData="ffff";
        System.out.println("原文：" + srcData);
        byte[] signature = signUtil.p7SignMessageAttach(Mechanism_Inside.SHA1_RSA, srcData.getBytes("UTF8"), privateKey, cert, session);
        boolean result=signUtil.p7VerifyMessageAttach(signature, session);
        System.out.println("验签结果：" + result);

		

	}

}
