package com.nl.lotterynl.domain;


public interface ConstantValues {
	String AGENTERID = "889931";
	String SOURCE = "ivr";
	String COMPRESS = "DES";
	String ENCODING = "UTF-8";
	
	/**
	 * des加密密钥
	 */
	String DES_PASSWORD = "9b2648fcdfbad80f";
	/**
	 * 子代理商的密钥(.so) JNI
	 */
	String AGENTER_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";
	
	/**
	 * 服务器地址
	 */
	String LOTTERY_URI = "http://10.0.2.2:8080/ZCWServer/Entrance";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1
	// String LOTTERY_URI = "http://192.168.1.100:8080/ZCWService/Entrance";// 10.0.2.2模拟器如果需要跟PC机通信127.0.0.1
	/**
	 * 
	 */
	
	int VIEW_FIRST = 1;
	int VIEW_SECOND = 2;
	/**
	 * 购彩大厅
	 */
	int VIEW_HALL = 10;
	/**
	 * 双色球选号界面
	 */
	int VIEW_SSQ = 15;
	/**
	 * 购物车
	 */
	int VIEW_SHOPPING = 20;
	/**
	 * 追期和倍投的设置界面
	 */
	int VIEW_PREBET = 25;
	/**
	 * 用户登录
	 */
	int VIEW_LOGIN = 30;
	/**
	 * 双色球的标识
	 */
	int SSQ = 118;
	/**
	 * 服务器返回成功状态码
	 */
	String SUCCESS = "0";
}
