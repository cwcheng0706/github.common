package com.zy.http;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URLEncoder;


public class ReflectUtils {

	/**
	 * 将实体bean中的name及value拼装成 ?name1=value1&name2=value2
	 * @Author 
	 * @Company: 
	 * @Create Time: 2015年8月21日 上午9:46:11
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static String beanToQueryString(Object bean) throws Exception {
		StringBuilder sb = new StringBuilder();
		Class<?> type = bean.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		if(null != propertyDescriptors && 0 < propertyDescriptors.length) {
			sb.append(C.Symbol.QUESTION_MARK);
		}
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals(C.Clazz.CLAZZ)) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if(null != result && !"".equals(String.valueOf(result))) {
					sb.append(propertyName).append(C.Symbol.EQUAL_MARK).append((null != result ? URLEncoder.encode(String.valueOf(result), C.Charset.UTF8) : "")).append(C.Symbol.AND_MARK);
				}
				
			}
		}
		if(-1 != sb.lastIndexOf(C.Symbol.AND_MARK)) {
			sb.substring(0, sb.lastIndexOf(C.Symbol.AND_MARK));
		}
		return sb.toString();
	}
}
