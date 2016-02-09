package com.nl.lotterynl.net.protocal;

import org.xmlpull.v1.XmlSerializer;

public interface Element {
	//每一个请求都要序列化自己
	void serializerElement(XmlSerializer serializer);
	//每个请求都有自己的标识
	String getTransactionType();
}
