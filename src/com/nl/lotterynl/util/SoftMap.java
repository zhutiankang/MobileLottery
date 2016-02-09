package com.nl.lotterynl.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * �����õ�map����
 * 
 * @author Administrator
 * 
 * @param <K>
 * @param <V>
 */
public class SoftMap<K, V> extends HashMap<K, V> {

	// ���Ͷ�������ý�𡪡�V
	private HashMap<K, SoftReference<V>> temp;// ��Ŵ��ӵļ���

	public SoftMap() {
		// ���ķ���
		// Object v=new Object();//ռ���ڴ�϶�
		// SoftReference sr=new SoftReference(v);//v�����ü��𱻽����ˣ������ã���������

		// ��һ������ռ���ڴ�϶���ֻ�����ӵ�������
		// �ڶ������ֻ���GC���գ�����մ���
		temp = new HashMap<K, SoftReference<V>>();
	}

	@Override
	public V put(K key, V value) {
		SoftReference<V> sr = new SoftReference<V>(value);// ���ֻ��ŵ�������
		temp.put(key, sr);
		return null;
	}

	@Override
	public V get(Object key) {
		SoftReference<V> sr = temp.get(key);
		if (sr != null) {// �жϴ����Ƿ�Ϊ��
			// �����������������˷��������� null��
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
