package com.cfca.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Enumeration;

import cfca.internal.tool.Mechanism_Inside;
import cfca.org.bouncycastle.asn1.ASN1ObjectIdentifier;
import cfca.org.bouncycastle.asn1.x509.Extensions;
import cfca.sm2rsa.common.CBCParam;
import cfca.sm2rsa.common.Mechanism;
import cfca.system.SecurityContext;
import cfca.util.CertUtil;
import cfca.util.EncryptUtil;
import cfca.util.EnvelopeUtil;
import cfca.util.KeyUtil;
import cfca.util.SignatureUtil;
import cfca.util.SignatureUtil2;
import cfca.util.cipher.lib.JCrypto;
import cfca.util.cipher.lib.Session;
import cfca.x509.certificate.X509Cert;
import cfca.x509.certificate.X509CertHelper;
import cfca.x509.certificate.X509CertValidator;

@SuppressWarnings("deprecation")
public class InterChangeSADKTestAndDemo{
    
    @SuppressWarnings("unused")
	private static final String LICENSE_FILE = null;
    public static Session session = null;
    
    private static String showOptions(){
        System.out.println("**********************************************************");
        System.out.println("  CFCA工具包_服务器端(JAVA版)功能测试及调用demo程序。                                             ");
        System.out.println("    输入编号后(如:3.1)按回车执行各功能测试。                                                                    ");
        System.out.println("**********************************************************");
        
        System.out.println("3.1        获得PFX中的证书");
        System.out.println("3.1.1      获得JKS中的证书");
        System.out.println("3.1.2      获得SM2中的证书");
        System.out.println("3.2        获得PFX中的私钥");
        System.out.println("3.2.1      获得JKS中的私钥");
        System.out.println("3.2.2      获得SM2中的私钥");
        System.out.println("3.3        解析P7B证书链");
        System.out.println("3.3.1      生成X509Cert对象");
        System.out.println("3.3.2      获取证书单个拓展域对象");
        System.out.println("3.3.3      获取证书所有拓展域对象");
        System.out.println("3.4        验证RSA/SM2证书有效性(签名、有效期、CRL)");
        
        System.out.println("4.1        产生RSA公钥、私钥");
        System.out.println("5.1        产生SM2公钥、私钥");
        
        System.out.println("6.1        RSA消息签名(PKCS#1)");
        System.out.println("6.1.1      RSA消息验签(PKCS#1)");
        System.out.println("6.2        SM2签名(裸签/PKCS#1)");
        System.out.println("6.2.1      SM2验签(裸签/PKCS#1)");
        System.out.println("6.3        RSA消息签名(PKCS#7,含原文)");
        System.out.println("6.3.1      RSA消息验签(PKCS#7,含原文)");
        System.out.println("6.4        RSA消息签名(PKCS#7,不含原文)");
        System.out.println("6.4.1      RSA消息验签(PKCS#7,不含原文)");
        System.out.println("6.5        SM2消息签名(PKCS#7,含原文)");
        System.out.println("6.5.1      SM2消息验签(PKCS#7,含原文)");
        System.out.println("6.6        SM2消息签名(PKCS#7,不含原文)");
        System.out.println("6.6.1      SM2消息验签(PKCS#7,不含原文)");
        
        System.out.println("7.1        RSA消息加密(数字信封)");
        System.out.println("7.1.1      RSA消息解密(数字信封)");
        System.out.println("7.2        RSA文件加密(数字信封)");
        System.out.println("7.2.1      RSA文件解密(数字信封)");
        
        System.out.println("9.1        RSA文件签名(PKCS#1)");
        System.out.println("9.1.1      RSA文件验签(PKCS#1)");
        System.out.println("10.1       SM2文件签名(裸签)");
        System.out.println("10.1.1     SM2文件验签(裸签)");
        System.out.println("11.1       RSA文件签名(PKCS#7，含原文)");
        System.out.println("11.1.1     RSA文件验签(PKCS#7，含原文)");
        System.out.println("12.1       RSA文件签名(PKCS#7，不含原文)");
        System.out.println("12.1.1     RSA文件验签(PKCS#7，不含原文)");
        System.out.println("13.1       SM2文件签名(PKCS#7,含原文)");
        System.out.println("13.1.1     SM2文件验签(PKCS#7,含原文)");
        System.out.println("14.1       SM2文件签名(PKCS#7,不含原文)");
        System.out.println("14.1.1     SM2文件验签(PKCS#7,不含原文)");
        
        System.out.println("15.1       SM2消息加密(数字信封)");
        System.out.println("15.1.1     SM2消息解密(数字信封)");
        System.out.println("15.2       SM2文件加密(数字信封)");
        System.out.println("15.2.1     SM2文件解密(数字信封)");
        
        System.out.println("16.1       3DES消息加密");
        System.out.println("16.1.1     3DES消息解密");
        System.out.println("16.2       3DES文件加密");
        System.out.println("16.2.1     3DES文件解密-输出到文件");
        System.out.println("16.2.2     3DES文件解密-输出到内存");
        System.out.println("16.2.3     SM4文件加密");
        System.out.println("16.2.4     SM4文件解密-输出到文件");
        System.out.println("16.2.5     SM4文件解密-输出到内存");
        
        System.out.println("17.1       消息加密解密(用对称密钥)");
        System.out.println("18.1       文件加密解密(用对称密钥)");
        
        System.out.println("version    显示版本号");
        System.out.println("quit       退出测试");
        System.out.println("*****************************************");
        
        System.out.println("请输入选项:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String choose = null;
        
        try{
            choose = reader.readLine().trim();
        }catch(Exception ex){
            
        }
        return choose;
    }
    
    static  {
    	try{
    		// 软库初始化
    		JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
    		session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) throws Exception{
        System.out.println("*******************************************************************************");
        System.out.println("  功能测试开始，对应<<CFCA工具包_服务器端(JAVA版)接口定义>>文档各章节描述功能。");
        System.out.println("            测试过程中输入r则终止测试，返回到主菜单。");
        System.out.println("*******************************************************************************");
        
        // 软库初始化
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        
        String opt = showOptions();
        
        // 硬库初始化,一般只用来做签名
        // 第二个参数为硬件厂家实现的Provider这个类的全路径，如三味的这个值为：“com.sansec.jce.provider.SwxaProvider”
        // 若设置为null，则默认使用三味的
        // JCrypto.getInstance().initialize(JCrypto.JHARD_LIB,
        // "com.sansec.jce.provider.SwxaProvider");
        // Session hard_session =
        // JCrypto.getInstance().openSession(JCrypto.JHARD_LIB);
        
        // JNI初始化,适用于在使用SM2签名验签时对效率要求较高场合
        // JCrypto.getInstance().initialize(JCrypto.JNI_LIB, null);
        // Session jni_session =
        // JCrypto.getInstance().openSession(JCrypto.JNI_LIB);
        
        while(true){
            try{
                if(opt.equals("3.1")){
                    getCertFromPfx();
                }else if(opt.equals("3.1.1")){
                    getCertFromJks();
                }else if(opt.equals("3.1.2")){
                    getCertFromSM2();
                }else if(opt.equals("3.2")){
                    getPrivateKeyFromPfx();
                }else if(opt.equals("3.2.1")){
                    getPrivateKeyFromJks();
                }else if(opt.equals("3.2.2")){
                    getPrivateKeyFromSM2();
                }else if(opt.equals("3.3")){
                    getP7bChainCerts();
                }else if(opt.equals("3.3.1")){
                    generateX509Cert();
                }else if(opt.equals("3.3.2")){
                    getCertExtensionData();
                }else if(opt.equals("3.3.3")){
                    getCertExtensionsData();
                }else if(opt.equals("3.4")){
                    verifyRSACertAll();
                }else if(opt.equals("4.1")){
                    generateRSAKeyPair();
                }else if(opt.equals("5.1")){
                    generateSM2KeyPair();
                }else if(opt.equals("6.1")){
                    rsaP1Sign();
                }else if(opt.equals("6.1.1")){
                    rsaP1Verify();
                }else if(opt.equals("6.2")){
                    sm2RawSign();
                }else if(opt.equals("6.2.1")){
                    sm2RawVerify();
                }else if(opt.equals("6.3")){
                    rsaP7SignAttach();
                }else if(opt.equals("6.3.1")){
                    rsaP7VerifyAttach();
                }else if(opt.equals("6.4")){
                    rsaP7SignDettach();
                }else if(opt.equals("6.4.1")){
                    rsaP7VerifyDettach();
                }else if(opt.equals("6.5")){
                    sm2SignAttach();
                }else if(opt.equals("6.5.1")){
                    sm2VerifyAttach();
                }else if(opt.equals("6.6")){
                    sm2SignDettach();
                }else if(opt.equals("6.6.1")){
                    sm2VerifyDettach();
                }else if(opt.equals("7.1")){
                    envelopMessage();
                }else if(opt.equals("7.1.1")){
                    openMessage();
                }else if(opt.equals("7.2")){
                    envelopFile();
                }else if(opt.equals("7.2.1")){
                    openFile();
                }else if(opt.equals("9.1")){
                    rsaFileP1Sign();
                }else if(opt.equals("9.1.1")){
                    rsaFileP1Verify();
                }else if(opt.equals("10.1")){
                    sm2RawFileSign();
                }else if(opt.equals("10.1.1")){
                    sm2RawFileVerify();
                }else if(opt.equals("11.1")){
                    rsaP7FileSignAttach();
                }else if(opt.equals("11.1.1")){
                    rsaP7FileVerifyAttach();
                }else if(opt.equals("12.1")){
                    rsaP7FileSignDettach();
                }else if(opt.equals("12.1.1")){
                    rsaP7FileVerifyDettach();
                }else if(opt.equals("13.1")){
                    sm2FileSignAttach();
                }else if(opt.equals("13.1.1")){
                    sm2FileVerifyAttach();
                }else if(opt.equals("14.1")){
                    sm2FileSignDettach();
                }else if(opt.equals("14.1.1")){
                    sm2FileVerifyDettach();
                }else if(opt.equals("15.1")){
                    sm2EnvelopMessage();
                }else if(opt.equals("15.1.1")){
                    openSM2EnvelopMessage();
                }else if(opt.equals("15.2")){
                    sm2EnvelopFile();
                }else if(opt.equals("15.2.1")){
                    openSM2EnvelopFile();
                }else if(opt.equals("16.1")){
                    encryptMessage_3DES();
                }else if(opt.equals("16.1.1")){
                    decryptMessage_3DES();
                }else if(opt.equals("16.2")){
                    encryptFile_3DES();
                }else if(opt.equals("16.2.1")){
                    decryptFile_3DES();
                }else if(opt.equals("16.2.2")){
                    decryptFiletoMemBy3DES();
                }else if(opt.equals("16.2.3")){
                    encryptFileBySM4();
                }else if(opt.equals("16.2.4")){
                    decryptFiletoFileBySM4();
                }else if(opt.equals("16.2.5")){
                    decryptFiletoMemBySM4();
                }else if(opt.equals("17.1")){
                    cithperMessageByKey();
                }else if(opt.equals("18.1")){
                    cithperFileByKey();
                }else if(opt.equals("version")){
                    System.out.println(SecurityContext.getVersion());
                }else if(opt.equals("quit")){
                    break;
                }else{
                    System.out.println("输入错误，请重新输入");
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }finally{
                opt = showOptions();
            }
        }
        
        // System.out.println("Then will run all test cases!!!");
        // testAll();
    }
    
    /**
     * 获得界面输入
     * 
     * @return
     * @throws Exception
     */
    private static String getInput() throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        if("r".equals(input)){
            throw new Exception("终止测试");
        }
        return input;
    }
    
    /**
     * 16.1 3DES消息加密
     * 
     * @throws Exception
     */
    private static void encryptMessage_3DES() throws Exception{
        System.out.println("输入原文:");
        String content = getInput();
        System.out.println("source data:" + content);
        System.out.println("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        String encryptData = EncryptUtil.encryptMessageByPwd_3DES(content, pwd);
        System.out.println("密文为:" + encryptData);
    }
    
    /**
     * 16.1.1 3DES消息解密
     * 
     * @throws Exception
     */
    private static void decryptMessage_3DES() throws Exception{
        System.out.println("输入密文:");
        String content = getInput();
        System.out.println("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        String sourcetData = EncryptUtil.decryptMessageByPwd_3DES(content, pwd);
        System.out.println("原文为:" + sourcetData);
    }
    
    /**
     * 16.2 3DES文件加密
     * 
     * @throws Exception
     */
    private static void encryptFile_3DES() throws Exception{
        System.out.println("输入原文件路径:");
        String srcFilePath = getInput();
        System.out.println("输入加密后的文件保存路径:");
        String saveFilePath = getInput();
        System.out.println("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        EncryptUtil.encryptFileByPwd_3DES(srcFilePath, saveFilePath, pwd);
        System.out.println("密文文件路径为:" + saveFilePath);
    }
    
    /**
     * 16.2.1 3DES文件解密
     * 
     * @throws Exception
     */
    private static void decryptFile_3DES() throws Exception{
        System.out.println("输入加密后的文件路径:");
        String encryptFilePath = getInput();
        System.out.println("输入解密后文件保存路径:");
        String plainTextFilePath = getInput();
        System.out.println("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        EncryptUtil.decryptFileByPwd_3DES(encryptFilePath, plainTextFilePath, pwd);
        System.out.println("密文文件路径为:" + plainTextFilePath);
    }
    
    /**
     * @description TODO
     * @author zhangrui
     * @throws Exception
     * @codereviewer
     * @amendment records
     * @date 2014-5-23 上午10:03:31
     * 
     */
    private static void decryptFiletoMemBy3DES() throws Exception{
        System.out.println("输入加密后的文件路径:");
        String encryptFilePath = getInput();
        System.out.println("输入密钥，比如:0282010100ea6226b38463db95bf4f0cae06b5c8ffa73bc3bdb83c193082010a");
        String pwd = getInput();
        ByteArrayOutputStream plainTextByteArrayOutputStream = new ByteArrayOutputStream();
        EncryptUtil.decryptFileBy3DES(encryptFilePath, plainTextByteArrayOutputStream, pwd);
        byte[] plainTextBytes = plainTextByteArrayOutputStream.toByteArray();
        FileOutputStream fos = new FileOutputStream("./TestData/decryptFiletoMem.txt");
        fos.write(plainTextBytes);
        fos.close();
    }
    
    /**
     * @description TODO
     * @author zhangrui
     * @throws Exception
     * @codereviewer
     * @amendment records
     * @date 2014-5-28 上午11:25:57
     * 
     */
    private static void decryptFiletoMemBySM4() throws Exception{
        System.out.println("输入待解密的原文件路径:");
        String encryptFilePath = getInput();
        System.out.println("输入解密后的文件路径:");
        String plainTextFilePath = getInput();
        System.out.println("输入解密密码");
        String password = getInput();
        ByteArrayOutputStream plainTextByteArrayOutputStream = new ByteArrayOutputStream();
        EncryptUtil.decryptFileBySM4(encryptFilePath, plainTextByteArrayOutputStream, password);
        byte[] bytes = plainTextByteArrayOutputStream.toByteArray();
        FileOutputStream fos = new FileOutputStream(plainTextFilePath);
        fos.write(bytes);
        fos.close();
        
    }
    
    /**
     * @description TODO
     * @author zhangrui
     * @throws Exception
     * @codereviewer
     * @amendment records
     * @date 2014-5-28 上午11:25:05
     * 
     */
    private static void decryptFiletoFileBySM4() throws Exception{
        System.out.println("输入待解密的原文件路径:");
        String encryptFilePath = getInput();
        System.out.println("输入解密后的文件路径:");
        String plainTextFilePath = getInput();
        System.out.println("输入解密密码");
        String password = getInput();
        EncryptUtil.decryptFileBySM4(encryptFilePath, plainTextFilePath, password);
    }
    
    /**
     * @description TODO
     * @author zhangrui
     * @throws Exception
     * @codereviewer
     * @amendment records
     * @date 2014-5-28 上午11:23:32
     * 
     */
    private static void encryptFileBySM4() throws Exception{
        System.out.println("输入待加密的原文件路径:");
        String sourceFilePath = getInput();
        System.out.println("输入加密后的文件路径:");
        String encryptFilePath = getInput();
        System.out.println("输入加密密码");
        String password = getInput();
        EncryptUtil.encryptFileBySM4(sourceFilePath, encryptFilePath, password);
        
    }
    
    private static void cithperMessageByKey() throws Exception{
        System.out.println("输入待加密的原文:");
        String src = getInput();
        
        // 选择产生密钥算法
        String keyType = getKeyType();
        Key key = KeyUtil.generateKey(new Mechanism(keyType), session);
        
        Mechanism mechanism = null;
        if(keyType.equals(Mechanism.DES3_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[8];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.DES3_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.SM4_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.SM4_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.RC4_KEY)){
            mechanism = new Mechanism(Mechanism.RC4);
        }
        
        byte[] encryptMessage = EncryptUtil.encrypt(mechanism, key, src.getBytes("UTF-8"), session);
        System.out.println("加密后的密文为：" + new String(encryptMessage, "UTF-8"));
        byte[] plainText = EncryptUtil.decrypt(mechanism, key, encryptMessage, session);
        System.out.println("解密后的明文为：" + new String(plainText, "UTF-8"));
    }
    
    private static String getKeyType() throws Exception{
        System.out.println("选择对称密钥算法:");
        System.out.println("****************");
        System.out.println("1 DESede");
        System.out.println("2 SM4");
        System.out.println("3 RC4");
        System.out.println("****************");
        System.out.println("请输入选项:");
        String keyTypeFlag = "";
        String keyType = "";
        while(true){
            keyTypeFlag = getInput();
            if("1".equals(keyTypeFlag)){
                keyType = Mechanism.DES3_KEY;
                break;
            }else if("2".equals(keyTypeFlag)){
                keyType = Mechanism.SM4_KEY;
                break;
            }else if("3".equals(keyTypeFlag)){
                keyType = Mechanism.RC4_KEY;
                break;
            }else{
                System.out.println("输入错误。请重新输入:");
            }
        }
        return keyType;
    }
    
    private static void cithperFileByKey() throws Exception{
        System.out.println("输入待加密的原文件路径:");
        String srcFilePath = getInput();
        
        // 选择产生密钥算法
        String keyType = getKeyType();
        Key key = KeyUtil.generateKey(new Mechanism(keyType), session);
        
        Mechanism mechanism = null;
        if(keyType.equals(Mechanism.DES3_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[8];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.DES3_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.SM4_KEY)){
            CBCParam param = new CBCParam();
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            param.setIv(iv);
            mechanism = new Mechanism(Mechanism.SM4_CBC);
            mechanism.setParam(param);
        }else if(keyType.equals(Mechanism.RC4_KEY)){
            mechanism = new Mechanism(Mechanism.RC4);
        }
        
        EncryptUtil.encrypt(mechanism, key, srcFilePath, srcFilePath + ".enc", session);
        System.out.println("加密后的文件为：" + srcFilePath + ".enc");
        EncryptUtil.decrypt(mechanism, key, srcFilePath + ".enc", srcFilePath + ".dec", session);
        System.out.println("解密后的文件为：" + srcFilePath + ".dec");
    }
    
    /**
     * 3.1 获得pfx中的证书
     * 
     * @throws Exception
     */
    private static void getCertFromPfx() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        X509Cert x509Cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        System.out.println("证书主题为:" + x509Cert.getSubject());
        System.out.println("获得证书序列号:" + x509Cert.getStringSerialNumber());
    }
    
    /**
     * 3.1.1 获得jks中的证书
     * 
     * @throws Exception
     */
    private static void getCertFromJks() throws Exception{
        System.out.println("输入jks证书路径:");
        String jksPath = getInput();
        System.out.println("输入jks证书密码:");
        String pwd = getInput();
        System.out.println("输入jks证书别名:");
        String alias = getInput();
        X509Cert x509Cert = CertUtil.getCertFromJks(jksPath, pwd, alias);
        System.out.println("证书主题为:" + x509Cert.getSubject());
        System.out.println("获得证书序列号:" + x509Cert.getStringSerialNumber());
    }
    
    /**
     * 3.1.2 获得SM2中的证书
     * 
     * @throws Exception
     */
    private static void getCertFromSM2() throws Exception{
        System.out.println("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        X509Cert sm2Cert = CertUtil.getCertFromSM2(sm2pfxPath);
        System.out.println("证书主题为:" + sm2Cert.getSubject());
        System.out.println("获得证书序列号:" + sm2Cert.getStringSerialNumber());
    }
    
    /**
     * 3.2 获得pfx中的私钥
     * 
     * @throws Exception
     */
    private static void getPrivateKeyFromPfx() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        System.out.println("私钥为:" + priKey.toString());
    }
    
    /**
     * 3.2.1 获得jks中的私钥
     * 
     * @throws Exception
     */
    private static void getPrivateKeyFromJks() throws Exception{
        System.out.println("输入jks证书路径:");
        String jksPath = getInput();
        System.out.println("输入jks证书密码:");
        String pwd = getInput();
        System.out.println("输入jks证书别名:");
        String alias = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromJKS(jksPath, pwd, alias);
        System.out.println("私钥为:" + priKey.toString());
    }
    
    /**
     * 3.2.2 获得SM2中的私钥
     * 
     * @throws Exception
     */
    private static void getPrivateKeyFromSM2() throws Exception{
        System.out.println("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        System.out.println("私钥为:" + priKey.toString());
    }
    
    /**
     * 3.3 获得p7b证书链
     * 
     * @throws Exception
     */
    private static void getP7bChainCerts() throws Exception{
        System.out.println("输入p7b文件路径:");
        String p7bFilePath = getInput();
        X509Cert[] certs = CertUtil.parseP7b(p7bFilePath);
        System.out.println("证书张数：" + certs.length);
        for(int i = 0; i < certs.length; i++){
            System.out.println("证书" + i + " serial number：" + certs[i].getStringSerialNumber());
        }
    }
    
    /**
     * 3.3.1 产生X509Cert对象
     * 
     * @throws Exception
     */
    private static void generateX509Cert() throws Exception{
        System.out.println("输入cer文件路径:");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        System.out.println("证书 serial number：" + cert.getStringSerialNumber());
    }
    
    /**
     * 3.3.2 获取拓展域对象
     * 
     * @throws Exception
     */
    private static void getCertExtensionData() throws Exception{
        System.out.println("输入cer文件路径,例如./TestData/extension.cer");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        
        System.out.println("输入oid,例如:1.2.86.1");
        String oidStr = getInput();
        
        byte[] extension = CertUtil.getCertExtensionData(cert, oidStr);
        System.out.println("拓展数据为：" + new String(extension, "GBK"));
    }
    
    /**
     * 3.3.3
     * 
     * @description get all extensions according to certificate
     * @throws Exception
     * @author zhangrui
     * @codereviewer
     * @date 2014-5-15 上午10:23:49
     */
    @SuppressWarnings("rawtypes")
	private static void getCertExtensionsData() throws Exception{
        System.out.println("输入cer文件路径,例如./TestData/extension.cer");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        Extensions extensions = CertUtil.getCertExtensionsData(cert);
        Enumeration enumeration = extensions.oids();
        System.out.println("拓展OID      |        拓展数据 ");
        while(enumeration.hasMoreElements()){
            ASN1ObjectIdentifier asn1ObjectIdentifier = (ASN1ObjectIdentifier)enumeration.nextElement();
            String oid = asn1ObjectIdentifier.getId();
            byte[] extension = CertUtil.getCertExtensionData(cert, oid);
            System.out.println(oid + "    |         " + Arrays.toString(extension));
        }
    }
    
    /**
     * 3.4 验证证书签名 有效期 CRL
     * 
     * @throws Exception
     */
    private static void verifyRSACertAll() throws Exception{
        System.out.println("输入要测试的的cer文件路径:");
        String cerPath = getInput();
        FileInputStream bais = new FileInputStream(cerPath);
        byte[] derData = new byte[bais.available()];
        bais.read(derData);
        bais.close();
        X509Cert userCert = new X509Cert(derData);
        
        if(X509CertValidator.verifyCertDate(userCert)){
            System.out.println("date is valid:" + userCert.getNotBefore() + "---" + userCert.getNotAfter());
        }else{
            System.out.println("out of date:" + userCert.getNotBefore() + "---" + userCert.getNotAfter());
        }
        
        System.out.println("即将验证证书的签名，请配置相应的受信任的证书路径!!!");
        String trustCertPath = getInput();
        X509CertValidator.updateTrustCertsMap(trustCertPath);
        if(X509CertValidator.validateCertSign(userCert)){
            System.out.println("signature is right!");
        }else{
            System.out.println("signature is wrong!");
        }
        
        System.out.println("输入要测试的crl文件路径:");
        String clrPath = getInput();
        
        if(X509CertValidator.verifyCertByCRLOutLine(userCert, clrPath)){
            System.out.println("is valid in crl");
        }else{
            System.out.println("is revoked in crl");
        }
    }
    
    /**
     * 4.1 产生RSA密钥对
     * 
     * @throws Exception
     */
    private static void generateRSAKeyPair() throws Exception{
        System.out.println("输入要产生的密钥长度:1.1024  2.2048  3.4096");
        String choose = getInput();
        int i = Integer.parseInt(choose);
        int len;
        switch(i){
            case 1:
                len = 1024;
                break;
            case 2:
                len = 2048;
                break;
            case 3:
                len = 4096;
                break;
            default:
                System.out.println("your choose is not right,will use 2048");
                len = 2048;
        }
        KeyPair pair = KeyUtil.generateRSAKeyPair(new Mechanism_Inside(Mechanism_Inside.RSA), len, session);
        System.out.println("RSA私钥：" + pair.getPrivate().toString());
        System.out.println("RSA公钥：" + pair.getPublic().toString());
    }
    
    /**
     * 5.1 产生SM2密钥对
     */
    private static void generateSM2KeyPair(){
        KeyPair pair = KeyUtil.generateSM2KeyPair(new Mechanism_Inside(Mechanism_Inside.SM2), session);
        System.out.println("SM2私钥：" + pair.getPrivate().toString());
        System.out.println("SM2公钥：" + pair.getPublic().toString());
    }
    
    private static String getSignAlgChoose() throws Exception{
        System.out.println("输入要使用的签名算法：1.SHA1_RSA 2.SHA256_RSA 3.SHA512_RSA 4.MD5_RSA");
        String choose = getInput();
        int i = Integer.parseInt(choose);
        String alg;
        switch(i){
            case 1:
                alg = Mechanism_Inside.SHA1_RSA;
                break;
            case 2:
                alg = Mechanism_Inside.SHA256_RSA;
                break;
            case 3:
                alg = Mechanism_Inside.SHA512_RSA;
                break;
            case 4:
                alg = Mechanism_Inside.MD5_RSA;
                break;
            default:
                System.out.println("your choose is not right,will use SHA1_RSA");
                alg = Mechanism_Inside.SHA1_RSA;
        }
        return alg;
    }
    
    /**
     * 6.1 RSA消息签名(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaP1Sign() throws Exception{
        PrivateKey priKey = (PrivateKey)getRSAPriKey();
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        
        String alg = getSignAlgChoose();
        byte[] signature = new SignatureUtil().p1SignMessage(alg, srcData.getBytes("UTF8"), priKey, session);
        System.out.println("p1 signatrue is:" + new String(signature));
        
    }
    
    private static Key getRSAPriKey() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        Key priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        return priKey;
    }
    
    private static Key getRSAPubKey() throws Exception{
        System.out.println("输入cer证书路径:");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        Key pubKey = cert.getPublicKey();
        return pubKey;
    }
    
    /**
     * 9.1 RSA文件签名(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaFileP1Sign() throws Exception{
        PrivateKey priKey = (PrivateKey)getRSAPriKey();
        
        System.out.println("请输入要签名的文件名：");
        String fileName = getInput();
        
        String alg = getSignAlgChoose();
        byte[] signData = new SignatureUtil().p1SignFile(alg, fileName, priKey, session);
        System.out.println("签名结果：" + new String(signData));
    }
    
    /**
     * 9.1.1 RSA文件验签(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaFileP1Verify() throws Exception{
        System.out.println("请输入签名数据：");
        String signData = getInput();
        System.out.println("请输入原文文件：");
        String srcDataFileName = getInput();
        
        String alg = getSignAlgChoose();
        PublicKey pubKey = (PublicKey)getRSAPubKey();
        
        if(new SignatureUtil().p1VerifyFile(alg, srcDataFileName, signData.getBytes(), pubKey, session)){
            System.out.println("p1 verify file pass");
        }else{
            System.out.println("p1 verify file failed");
        }
    }
    
    /**
     * 6.1.1 RSA消息验签(PKCS#1)
     * 
     * @throws Exception
     */
    private static void rsaP1Verify() throws Exception{
        System.out.println("请输入签名：");
        String signData = getInput();
        System.out.println("请输入原文：");
        String srcData = getInput();
        
        String alg = getSignAlgChoose();
        PublicKey pubKey = (PublicKey)getRSAPubKey();
        if(new SignatureUtil().p1VerifyMessage(alg, srcData.getBytes("UTF8"), signData.getBytes(), pubKey, session)){
            System.out.println("p1 verify pass");
        }else{
            System.out.println("p1 verify failed");
        }
    }
    
    /**
     * 6.2 SM2签名(裸签)"
     * 
     * @throws Exception
     */
    private static void sm2RawSign() throws Exception{
        System.out.println("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        byte[] signature = null;
        signature = new SignatureUtil2().p1SignMessage(Mechanism_Inside.SM3_SM2, srcData.getBytes("UTF8"), priKey, session);
        System.out.println("sm2 raw signatrue is:" + new String(signature));
    }
    
    /**
     * 10.1 SM2文件签名(裸签)"
     * 
     * @throws Exception
     */
    private static void sm2RawFileSign() throws Exception{
        /*
         * KeyPair pair = KeyUtil.generateSM2KeyPair(); PrivateKey priKey = pair.getPrivate(); PublicKey pubKey = pair.getPublic();
         */

        System.out.println("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        System.out.println("请输入原文文件名：");
        String srcDataFileName = getInput();
        
        byte[] signDataFileName = new SignatureUtil2().p1SignFile(Mechanism_Inside.SM3_SM2, srcDataFileName, priKey, session);
        System.out.println("sm2 raw sign file is:" + new String(signDataFileName));
    }
    
    /**
     * 10.1.1 SM2文件验签(裸签)
     * 
     * @throws Exception
     */
    private static void sm2RawFileVerify() throws Exception{
        System.out.println("请输入cer证书路径,如./TestData/sm2pfx.cer：");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        PublicKey pubKey = cert.getPublicKey();
        
        System.out.println("请输入签名数据：");
        String signData = getInput();
        
        System.out.println("请输入原文文件：");
        String srcDataFilePath = getInput();
        
        boolean ret = new SignatureUtil2().p1VerifyFile(Mechanism_Inside.SM3_SM2, srcDataFilePath, signData.getBytes(), pubKey, session);
        System.out.println("验签结果：" + ret);
    }
    
    /**
     * 6.2.1 SM2验签(裸签)
     * 
     * @throws Exception
     */
    private static void sm2RawVerify() throws Exception{
        System.out.println("请输入cer证书路径：");
        String cerPath = getInput();
        X509Cert cert = X509CertHelper.parse(cerPath);
        PublicKey pubKey = cert.getPublicKey();
        
        System.out.println("请输入签名：");
        String signData = getInput();
        byte[] signature = signData.getBytes();
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        boolean ret = new SignatureUtil2().p1VerifyMessage(Mechanism_Inside.SM3_SM2, srcData.getBytes("UTF8"), signature, pubKey, session);
        System.out.println("验签结果：" + ret);
    }
    
    /**
     * 6.3 RSA消息签名(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7SignAttach() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        String alg = getSignAlgChoose();
        SignatureUtil2 signUtil = new SignatureUtil2();
        byte[] signature = signUtil.p7SignMessageAttach(alg, srcData.getBytes("UTF8"), priKey, cert, session);
        if(signature != null){
            System.out.println("the signature is:" + new String(signature));
        }else{
            System.out.println("no");
        }
    }
    
    /**
     * 11.1 RSA文件签名(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileSignAttach() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        
        System.out.println("请输入原文路径：");
        String srcDataFilePath = getInput();
        System.out.println("请输入签名文件路径：");
        String signDataFilePath = getInput();
        String alg = getSignAlgChoose();
        SignatureUtil2 signUtil = new SignatureUtil2();
        signUtil.p7SignFileAttach(alg, srcDataFilePath, signDataFilePath, priKey, cert, session);
        System.out.println("the signature file is:" + signDataFilePath);
    }
    
    /**
     * 11.1.1 RSA文件验签(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileVerifyAttach() throws Exception{
        System.out.println("请输入签名文件：");
        String signDataFilePath = getInput();
        System.out.println("请输入原文保存路径,不想保存原文，请直接输入null：");
        String sourceFilePath = getInput();
        if(sourceFilePath.equals("null"))
            sourceFilePath = null;
        SignatureUtil2 signUtil = new SignatureUtil2();
        if(signUtil.p7VerifyFileAttach(signDataFilePath, sourceFilePath, session)){
            System.out.println("yes,p7 file attach verify pass");
            X509Cert signCert = signUtil.getSignerCert();
            System.out.println("签名者证书DN:" + signCert.getSubject());
        }else{
            System.out.println("no,p7 file attch verify failed");
        }
    }
    
    /**
     * 6.3.1 RSA消息验签(PKCS#7,含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7VerifyAttach() throws Exception{
        System.out.println("请输入签名：");
        String signData = getInput();
        if(new SignatureUtil2().p7VerifyMessageAttach(signData.getBytes(), session)){
            System.out.println("yes,p7 attach verify pass");
        }else{
            System.out.println("no,p7 attch verify failed");
        }
    }
    
    /**
     * 12.1 RSA文件签名(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileSignDettach() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        
        System.out.println("请输入原文文件名：");
        String srcDataFilePath = getInput();
        String alg = getSignAlgChoose();
        byte[] signData = new SignatureUtil2().p7SignFileDetach(alg, srcDataFilePath, priKey, cert, session);
        System.out.println("the signature file is：" + new String(signData));
    }
    
    /**
     * 6.4 RSA消息签名(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7SignDettach() throws Exception{
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        String alg = getSignAlgChoose();
        byte[] signature = new SignatureUtil2().p7SignMessageDetach(alg, srcData.getBytes("UTF8"), priKey, cert, session);
        System.out.println("the signature is：" + new String(signature));
    }
    
    /**
     * 12.1.1 RSA文件验签(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7FileVerifyDettach() throws Exception{
        System.out.println("请输入签名数据：");
        String signData = getInput();
        
        System.out.println("请输入原文文件：");
        String srcDataFilePath = getInput();
        
        if(new SignatureUtil2().p7VerifyFileDetach(srcDataFilePath, signData.getBytes(), session)){
            System.out.println("yes,p7 file dettach verify pass");
        }else{
            System.out.println("no,p7 file dettach verify failed");
        }
    }
    
    /**
     * 6.4.1 RSA消息验签(PKCS#7,不含原文)
     * 
     * @throws Exception
     */
    private static void rsaP7VerifyDettach() throws Exception{
        System.out.println("请输入签名：");
        String signData = getInput();
        // byte[] signature = Base64.decode(signData);
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        
        if(new SignatureUtil2().p7VerifyMessageDetach(srcData.getBytes("UTF8"), signData.getBytes(), session)){
            System.out.println("yes,p7 dettach verify pass");
        }else{
            System.out.println("no,p7 dettach verify failed");
        }
    }
    
    /**
     * 13.1 sm2文件签名(含原文)
     * 
     * @throws Exception
     * @throws CertificateEncodingException
     */
    private static void sm2FileSignAttach() throws Exception{
        System.out.println("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);
        
        System.out.println("请输入原文文件路径：");
        String srcDataFilePath = getInput();
        System.out.println("请输入签名文件路径：");
        String signDataFilePath = getInput();
        
        new SignatureUtil2().p7SignFileAttach(Mechanism_Inside.SM3_SM2, srcDataFilePath, signDataFilePath, priKey, cert, session);
        System.out.println("the signature file is:" + signDataFilePath);
    }
    
    /**
     * 6.5 sm2消息签名(含原文)
     * 
     * @throws Exception
     * @throws CertificateEncodingException
     */
    private static void sm2SignAttach() throws Exception{
        System.out.println("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        byte[] signature = null;
        
        signature = new SignatureUtil2().p7SignMessageAttach(Mechanism_Inside.SM3_SM2, srcData.getBytes("UTF8"), priKey, cert, session);
        System.out.println("the signature is:" + new String(signature));
    }
    
    /**
     * 13.1.1 sm2文件验签(含原文)
     * 
     * @throws Exception
     */
    private static void sm2FileVerifyAttach() throws Exception{
        System.out.println("请输入待验证签名文件路径：");
        String signDataFilePath = getInput();
        System.out.println("请输入原文保存路径,不想保存原文，请直接输入null：");
        String sourceFilePath = getInput();
        if(sourceFilePath.equals("null"))
            sourceFilePath = null;
        if(new SignatureUtil2().p7VerifyFileAttach(signDataFilePath, sourceFilePath, session)){
            System.out.println("yes,sm2 file attach with z value verify pass");
        }else{
            System.out.println("no,sm2 file attach with z value verify failed");
        }
        
    }
    
    /**
     * 6.5.1 sm2消息验签(含原文)
     * 
     * @throws Exception
     */
    private static void sm2VerifyAttach() throws Exception{
        System.out.println("请输入待验证签名：");
        String signData = getInput();
        
        if(new SignatureUtil2().p7VerifyMessageAttach(signData.getBytes(), session)){
            System.out.println("yes,sm2 attach with z value verify pass");
        }else{
            System.out.println("no,sm2 attach with z value verify failed");
        }
    }
    
    /**
     * 14.1 sm2文件签名(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2FileSignDettach() throws Exception{
        System.out.println("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);
        
        // sign with default z value
        // byte[] signature = null;
        
        System.out.println("请输入原文文件路径：");
        String srcDataFilePath = getInput();
        
        byte[] signData = new SignatureUtil2().p7SignFileDetach(Mechanism_Inside.SM3_SM2, srcDataFilePath, priKey, cert, session);
        System.out.println("the signature data is:" + new String(signData));
        
    }
    
    /**
     * 6.6 sm2消息签名(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2SignDettach() throws Exception{
        System.out.println("输入SM2证书路径,如./TestData/sm2pfx.SM2:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);
        
        // sign with default z value
        byte[] signature = null;
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        
        signature = new SignatureUtil().p7SignMessageDetach(Mechanism_Inside.SM3_SM2, srcData.getBytes("UTF8"), priKey, cert, session);
        System.out.println("the signature is:" + new String(signature));
        
    }
    
    /**
     * 14.1.1 sm2文件验签(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2FileVerifyDettach() throws Exception{
        System.out.println("请输入待验证签名数据：");
        String signData = getInput();
        
        System.out.println("请输入原文文件：");
        String srcDataFilePath = getInput();
        
        if(new SignatureUtil2().p7VerifyFileDetach(srcDataFilePath, signData.getBytes(), session)){
            System.out.println("yes,sm2 detach with z value verify pass");
        }else{
            System.out.println("no,sm2 detach with z value verify failed");
        }
        
    }
    
    /**
     * 6.6.1 sm2消息验签(不含原文)
     * 
     * @throws Exception
     */
    private static void sm2VerifyDettach() throws Exception{
        System.out.println("请输入待验证签名：");
        String signData = getInput();
        
        System.out.println("请输入原文：");
        String srcData = getInput();
        
        if(new SignatureUtil2().p7VerifyMessageDetach(srcData.getBytes("UTF8"), signData.getBytes(), session)){
            System.out.println("yes,sm2 detach with z value verify pass");
        }else{
            System.out.println("no,sm2 detach with z value verify failed");
        }
        
    }
    
    private static String getSymmetricAlgorithm() throws Exception{
        System.out.println("选择 加密/解密 算法:");
        System.out.println("****************");
        System.out.println("1 RC4");
        System.out.println("2 DESede/ECB/PKCS7Padding");
        System.out.println("3 DESede/CBC/PKCS7Padding");
        System.out.println("****************");
        System.out.println("请输入选项:");
        String encryptTypeFlag = "";
        String symmetricAlgorithm = null;
        while(true){
            encryptTypeFlag = getInput();
            if("1".equals(encryptTypeFlag)){
                symmetricAlgorithm = Mechanism_Inside.RC4;
                break;
            }else if("2".equals(encryptTypeFlag)){
                symmetricAlgorithm = Mechanism_Inside.DES3_ECB;
                break;
            }else if("3".equals(encryptTypeFlag)){
                symmetricAlgorithm = Mechanism_Inside.DES3_CBC;
                break;
            }else{
                System.out.println("输入错误。请重新输入:");
            }
        }
        return symmetricAlgorithm;
    }
    
    /**
     * 7.1 PKCS#7消息加密(数字信封)
     * 
     * @throws Exception
     */
    private static void envelopMessage() throws Exception{
        System.out.println("输入接收者证书路径，多张证书用；分开");
        String cerPath = getInput();
        String[] certPaths = cerPath.split(";");
        X509Cert[] certs = new X509Cert[certPaths.length];
        for(int i = 0; i < certPaths.length; i++){
            X509Cert cert = new X509Cert(new FileInputStream(certPaths[i]));
            certs[i] = cert;
        }
        System.out.println("输入原文:");
        String content = getInput();
        System.out.println("source data:" + content);
        String symmetricAlgorithm = getSymmetricAlgorithm();
        
        byte[] encryptedData = EnvelopeUtil.envelopeMessage(content.getBytes("UTF8"), symmetricAlgorithm, certs);
        
        System.out.println("envelope successed:" + new String(encryptedData));
    }
    
    private static String getSM2SymmetricAlgorithm() throws Exception{
        System.out.println("选择 加密/解密 算法:");
        System.out.println("****************");
        System.out.println("1 SM4/ECB/PKCS7Padding");
        System.out.println("2 SM4/CBC/PKCS7Padding");
        System.out.println("****************");
        System.out.println("请输入选项:");
        String encryptTypeFlag = "";
        String symmetricAlgorithm = null;
        while(true){
            encryptTypeFlag = getInput();
            if("1".equals(encryptTypeFlag)){
                symmetricAlgorithm = Mechanism_Inside.SM4_ECB;
                break;
            }else if("2".equals(encryptTypeFlag)){
                symmetricAlgorithm = Mechanism_Inside.SM4_CBC;
                break;
            }else{
                System.out.println("输入错误。请重新输入:");
            }
        }
        return symmetricAlgorithm;
    }
    
    /**
     * 15.1 SM2消息加密(数字信封)
     * 
     * @throws Exception
     */
    private static void sm2EnvelopMessage() throws Exception{
        System.out.println("输入接收者证书路径，多张证书用；分开");
        String cerPath = getInput();
        String[] certPaths = cerPath.split(";");
        X509Cert[] certs = new X509Cert[certPaths.length];
        for(int i = 0; i < certPaths.length; i++){
            X509Cert cert = new X509Cert(new FileInputStream(certPaths[i]));
            certs[i] = cert;
        }
        System.out.println("输入原文:");
        String content = getInput();
        System.out.println("source data:" + content);
        String symmetricAlgorithm = getSM2SymmetricAlgorithm();
        
        byte[] encryptedData = EnvelopeUtil.envelopeMessage(content.getBytes("UTF8"), symmetricAlgorithm, certs);
        
        System.out.println("envelope successed:" + new String(encryptedData));
    }
    
    /**
     * 15.1.1 SM2消息解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openSM2EnvelopMessage() throws Exception{
        
        System.out.println("输入接收到的数字信封数据：");
        String encryptedData = getInput();
        System.out.println("输入SM2证书路径:");
        String sm2Path = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey)KeyUtil.getPrivateKeyFromSM2(sm2Path, pwd);
        X509Cert cert = CertUtil.getCertFromSM2(sm2Path);
        
        byte[] sourceData = EnvelopeUtil.openEvelopedMessage(encryptedData.getBytes(), priKey, cert, session);
        System.out.println("get the source data:" + new String(sourceData, "UTF8"));
    }
    
    /**
     * 15.2 SM2文件加密(数字信封)
     * 
     * @throws Exception
     */
    private static void sm2EnvelopFile() throws Exception{
        System.out.println("输入一个接收者的cer证书：");
        String tt = getInput();
        FileInputStream fis = new FileInputStream(tt);
        X509Cert cert = new X509Cert(fis);
        X509Cert[] certs = new X509Cert[1];
        certs[0] = cert;
        
        System.out.println("请输入source文件路径：");
        String sourceFilePath = getInput();
        
        String symmetricAlgorithm = getSM2SymmetricAlgorithm();
        
        System.out.println("请输入加密后文件路径：");
        String encryptFilePath = getInput();
        
        EnvelopeUtil.envelopeFile(sourceFilePath, encryptFilePath, symmetricAlgorithm, certs);
        System.out.println("the envelop file is:" + encryptFilePath);
    }
    
    /**
     * 15.2.1 SM2文件解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openSM2EnvelopFile() throws Exception{
        System.out.println("请输入加密后文件路径：");
        String encryptFilePath = getInput();
        System.out.println("请输入解密后文件路径：");
        String plainTextFilePath = getInput();
        
        System.out.println("输入SM2证书路径:");
        String sm2pfxPath = getInput();
        System.out.println("输入SM2证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey)KeyUtil.getPrivateKeyFromSM2(sm2pfxPath, pwd);
        
        X509Cert cert = CertUtil.getCertFromSM2(sm2pfxPath);
        EnvelopeUtil.openEnvelopedFile(encryptFilePath, plainTextFilePath, priKey, cert, session);
        System.out.println("the plain text file is:" + plainTextFilePath);
    }
    
    /**
     * 7.1.1 消息解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openMessage() throws Exception{
        
        System.out.println("输入接收到的数字信封数据：");
        String encryptedData = getInput();
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey)KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        
        byte[] sourceData = EnvelopeUtil.openEvelopedMessage(encryptedData.getBytes(), priKey, cert, session);
        System.out.println("get the source data:" + new String(sourceData, "UTF8"));
    }
    
    /**
     * 7.2 文件加密(数字信封)
     * 
     * @throws Exception
     */
    private static void envelopFile() throws Exception{
        System.out.println("输入一个接收者的证书：");
        String tt = getInput();
        FileInputStream fis = new FileInputStream(tt);
        X509Cert cert = new X509Cert(fis);
        X509Cert[] certs = new X509Cert[1];
        certs[0] = cert;
        String symmetricAlgorithm = getSymmetricAlgorithm();
        
        System.out.println("请输入source文件路径：");
        String sourceFilePath = getInput();
        
        System.out.println("请输入加密后文件路径：");
        String encryptFilePath = getInput();
        
        EnvelopeUtil.envelopeFile(sourceFilePath, encryptFilePath, symmetricAlgorithm, certs);
        
        fis = new FileInputStream(encryptFilePath);
        byte[] encryptData = new byte[fis.available()];
        fis.read(encryptData);
        System.out.println("the encrypt file path is:" + encryptFilePath);
        fis.close();
    }
    
    /**
     * 7.2.1 文件解密(数字信封)
     * 
     * @throws Exception
     */
    private static void openFile() throws Exception{
        System.out.println("请输入加密后文件路径：");
        String encryptFilePath = getInput();
        System.out.println("请输入解密后文件路径：");
        String plainTextFilePath = getInput();
        
        System.out.println("输入pfx证书路径:");
        String pfxPath = getInput();
        System.out.println("输入pfx证书密码:");
        String pwd = getInput();
        PrivateKey priKey = (PrivateKey)KeyUtil.getPrivateKeyFromPFX(pfxPath, pwd);
        
        X509Cert cert = CertUtil.getCertFromPfx(pfxPath, pwd);
        
        EnvelopeUtil.openEnvelopedFile(encryptFilePath, plainTextFilePath, priKey, cert, session);
        System.out.println("the plainText file is:" + plainTextFilePath);
    }
}
