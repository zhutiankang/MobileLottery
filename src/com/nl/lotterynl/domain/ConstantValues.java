package com.nl.lotterynl.domain;


public interface ConstantValues {
	String AGENTERID = "889931";
	String SOURCE = "ivr";
	String COMPRESS = "DES";
	String ENCODING = "UTF-8";
	
	/**
	 * des������Կ
	 */
	String DES_PASSWORD = "9b2648fcdfbad80f";
	/**
	 * �Ӵ����̵���Կ(.so) JNI
	 */
	String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";
	
	/**
	 * ��������ַ
	 */
	String LOTTERY_URI = "http://10.0.2.2:8080/ZCWServer/Entrance";// 10.0.2.2ģ���������Ҫ��PC��ͨ��127.0.0.1
	// String LOTTERY_URI = "http://192.168.1.100:8080/ZCWService/Entrance";// 10.0.2.2ģ���������Ҫ��PC��ͨ��127.0.0.1
	/**
	 * 
	 */
	
	int VIEW_FIRST = 1;
	int VIEW_SECOND = 2;
	/**
	 * ���ʴ���
	 */
	int VIEW_HALL = 10;
	/**
	 * ˫ɫ��ѡ�Ž���
	 */
	int VIEW_SSQ = 15;
	/**
	 * ���ﳵ
	 */
	int VIEW_SHOPPING = 20;
	/**
	 * ׷�ںͱ�Ͷ�����ý���
	 */
	int VIEW_PREBET = 25;
	/**
	 * �û���¼
	 */
	int VIEW_LOGIN = 30;
	/**
	 * ˫ɫ��ı�ʶ
	 */
	int SSQ = 118;
	/**
	 * ���������سɹ�״̬��
	 */
	String SUCCESS = "0";
}
