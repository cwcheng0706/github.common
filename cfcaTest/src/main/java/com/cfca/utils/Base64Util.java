package com.cfca.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Encoder;

/**
 * @Project: cfcaTest
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年9月24日 下午2:22:26
 */
public class Base64Util {
	
	private static final Log logger = LogFactory.getLog(Base64Util.class);
	
	private static BASE64Encoder base64Encoder = new BASE64Encoder();
	
	public static String getBase64StrByImageFile(File file) {
		String ret = null;
		
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			ret = getBase64StrByImageFile(fis);
		}catch(Exception e) {
			ret = null;
        	logger.error("根据文件转换base64编码字串异常." + e);
		}finally {
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					ret = null;
		        	logger.error("根据文件转换base64编码字串后关闭文件流出现异常." + e);
				}
			}
		}
		
		return ret;
	}
	
	public static String getBase64StrByImageFile(InputStream in) {
		String ret = null;
		
		BufferedImage bi = null;
		ByteArrayOutputStream baos = null;
        try {    
            bi = ImageIO.read(in);    
            baos = new ByteArrayOutputStream();    
            
            ImageIO.write(bi, "jpg", baos);    
            byte[] bytes = baos.toByteArray();    
                
            ret = base64Encoder.encodeBuffer(bytes).trim();    
        } catch (IOException e) {    
        	ret = null;
        	logger.error("根据文件流转换base64编码字串异常." + e);
        }  finally {
        	if(null != baos) {
        		try {
					baos.close();
				} catch (IOException e) {
					ret = null;
					logger.error("根据文件流转换base64编码字串后关闭流出现异常." + e);
				}
        	}
        }
		return ret;
	}
	
			
	/**
	 * 返回 null 表示失败
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月24日 下午2:22:30
	 * @param in
	 * @return
	 */
	public static String getBase64StrByJPGFile(InputStream in) {
		String ret = null;
		
		ByteArrayOutputStream outStream = null;
		try{
			outStream = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1024];
			int readLength= 0;
			while(-1 != (readLength = in.read(buffer))) {
				outStream.write(buffer, 0, readLength);
			}
			
			outStream.flush();
			
			byte[] imgBuffer = outStream.toByteArray();
			
			ret = base64Encoder.encode(imgBuffer);
			
		}catch(Exception e) {
			ret = null;
			logger.error("根据文件流转换base64编码字串异常." + e);
		}finally {
			if(null != outStream) {
				try {
					outStream.close();
				} catch (IOException e) {
					logger.error("根据文件流转换base64编码字串后关闭流出现异常." + e);
				}
			}
		}
		
		return ret;
	}

	/**
	 * 返回 null 表示失败
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月24日 下午2:22:37
	 * @param file
	 * @return
	 */
	public static String getBase64StrByJPGFile(String file) {
		String ret = null;
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(file);
			
			ret = getBase64StrByJPGFile(fis);
			
		}catch(Exception e) {
			ret = null;
			logger.error("根据文件转换base64编码字串异常." + e);
		}finally {
			if(null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					ret = null;
					logger.error("根据文件转换base64编码字串后关闭文件流出现异常." + e);
				}
			}
		}
		
		return ret;
	}
}
