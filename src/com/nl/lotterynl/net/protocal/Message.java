package com.nl.lotterynl.net.protocal;

import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

import com.nl.lotterynl.domain.ConstantValues;

public class Message {
	private Header header = new Header();
	private Body body = new Body();
	
	public Header getHeader() {
		return header;
	}
	public void serializerMessage(XmlSerializer serializer){
		try {
			serializer.startTag(null, "message");
			serializer.attribute(null, "version", "1.0");
			header.serializerHeader(serializer,body.getWholeBody());
			//body序列化
			//body.serializerBody(serializer);
			//body DES加密
			serializer.startTag(null, "body");
			serializer.text(body.getBodyInsideDESInfo());
			serializer.endTag(null, "body");
			serializer.endTag(null, "message");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取请求的xml文件
	 * @return
	 */
	public String getXml(Element element){
		if(element==null){
			throw new IllegalArgumentException("element is null");
		}
		//请求标识需要设置，
		header.getTransactiontype().setTagValue(element.getTransactionType());
		//请求内容需要设置
		body.getElements().add(element);
		
		
		
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument(ConstantValues.ENCODING, null);
			this.serializerMessage(serializer);
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**********************处理服务器回复**********************/
	public Body getBody() {
		return body;
	}
	/**********************处理服务器回复**********************/
}
