package com.nl.lotterynl.util;

import java.io.IOException;
import java.util.Properties;

import com.nl.lotterynl.engine.UserEngine;

/**
 * ������
 * @author ׷��
 *
 */
public class BeanFactory {
	
	
	/********���������ļ�����ʵ��****************/
	private static Properties properties;
	static{
		properties = new Properties();
		//bean.properties����Ҫ��src��Ŀ¼��
		try {
			properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//������Ҫ�󣬽ӿ�
	//��󷵻�һ���ӿڵ�ʵ��ʵ��
	/**
	 * ������Ҫ��ʵ����
	 * @param clazz
	 * @return
	 */
//	public static UserEngine getImpl(Class clazz){
//		String key = clazz.getSimpleName();//getName��ȫ������getSimpleNameֻ�ǵõ�����
//		String className = properties.getProperty(key);
//		try {
//			return (UserEngine) Class.forName(className).newInstance();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	//�÷���ʹ֮����ͨ��
	public static<T> T getImpl(Class<T> clazz){
		String key = clazz.getSimpleName();//getName��ȫ������getSimpleNameֻ�ǵõ�����
		String className = properties.getProperty(key);
		try {
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
