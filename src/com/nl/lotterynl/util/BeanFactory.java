package com.nl.lotterynl.util;

import java.io.IOException;
import java.util.Properties;

import com.nl.lotterynl.engine.UserEngine;

/**
 * 工厂类
 * @author 追梦
 *
 */
public class BeanFactory {
	
	
	/********根据配置文件加载实例****************/
	private static Properties properties;
	static{
		properties = new Properties();
		//bean.properties必须要在src根目录下
		try {
			properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//参数，要求，接口
	//最后返回一个接口的实现实例
	/**
	 * 加载需要的实现类
	 * @param clazz
	 * @return
	 */
//	public static UserEngine getImpl(Class clazz){
//		String key = clazz.getSimpleName();//getName是全类名，getSimpleName只是得到类名
//		String className = properties.getProperty(key);
//		try {
//			return (UserEngine) Class.forName(className).newInstance();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	//用泛型使之更加通用
	public static<T> T getImpl(Class<T> clazz){
		String key = clazz.getSimpleName();//getName是全类名，getSimpleName只是得到类名
		String className = properties.getProperty(key);
		try {
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
