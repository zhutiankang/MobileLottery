package com.nl.lotterynl.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * 软引用的map集合
 * 
 * @author Administrator
 * 
 * @param <K>
 * @param <V>
 */
public class SoftMap<K, V> extends HashMap<K, V> {

	// 降低对象的引用界别——V
	private HashMap<K, SoftReference<V>> temp;// 存放袋子的集合

	public SoftMap() {
		// 核心方法
		// Object v=new Object();//占用内存较多
		// SoftReference sr=new SoftReference(v);//v的引用级别被降低了，软引用，单个袋子

		// 第一步：将占用内存较多的手机，添加到袋子中
		// 第二步：手机被GC回收，清理空袋子
		temp = new HashMap<K, SoftReference<V>>();
	}

	@Override
	public V put(K key, V value) {
		SoftReference<V> sr = new SoftReference<V>(value);// 将手机放到袋子里
		temp.put(key, sr);
		return null;
	}

	@Override
	public V get(Object key) {
		SoftReference<V> sr = temp.get(key);
		if (sr != null) {// 判断袋子是否为空
			// 垃圾回收器清除，则此方法将返回 null。
			return sr.get();
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		if (get(key) != null) {
			return true;
		} else {
			return false;
		}
		
		//return get(key)!=null;
	}

}
