package com.nl.lotterynl.net.protocal;

import org.xmlpull.v1.XmlSerializer;

public interface Element {
	//ÿһ������Ҫ���л��Լ�
	void serializerElement(XmlSerializer serializer);
	//ÿ���������Լ��ı�ʶ
	String getTransactionType();
}
