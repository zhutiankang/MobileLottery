package com.nl.lotterynl.net.protocal;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.Oelement;
import com.nl.lotterynl.util.DES;

import android.util.Xml;

public class Body {
	
	private List<Element> elements = new ArrayList<Element>();
	/**********************����������ظ�**********************/
	private String serviceBodyInsideDESInfo;//���ܵ�body����
	private Oelement oelement = new Oelement();
	
	public Oelement getOelement() {
		return oelement;
	}
	public String getServiceBodyInsideDESInfo() {
		return serviceBodyInsideDESInfo;
	}
	public void setServiceBodyInsideDESInfo(String serviceBodyInsideDESInfo) {
		this.serviceBodyInsideDESInfo = serviceBodyInsideDESInfo;
	}
	private String serviceBodyInfo;//������body����
	public String getServiceBodyInfo() {
		return serviceBodyInfo;
	}
	public void setServiceBodyInfo(String serviceBodyInfo) {
		this.serviceBodyInfo = serviceBodyInfo;
	}
	/**********************����������ظ�**********************/
	public void serializerBody(XmlSerializer serializer){
		
		try {
			serializer.startTag(null, "body");
			serializer.startTag(null, "elements");
			for(Element e : getElements()){
				e.serializerElement(serializer);
			}
			serializer.endTag(null, "elements");
			serializer.endTag(null, "body");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡ������body
	 * @return
	 */
	public String getWholeBody(){
		StringWriter writer = new StringWriter();
		XmlSerializer temp = Xml.newSerializer();//��ʱ���л������<body>.......</body>
		try {
			temp.setOutput(writer);
			this.serializerBody(temp);
			temp.flush();                       //û��endDocument,����flush
			return writer.toString();           //���Ϊ<body>.......</body>
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * ��ȡ  body�����DES��������
	 * @return
	 */
	public String getBodyInsideDESInfo(){
		//��ȡҪ���ܵ�����
		String wholeBody = this.getWholeBody();
		String orgDESInfo = StringUtils.substringBetween(wholeBody, "<body>", "</body>");
		//����
		DES des = new DES();
		return des.authcode(orgDESInfo, "DECODE", ConstantValues.DES_PASSWORD);
	}
	public List<Element> getElements() {
		return elements;
	}
	
	
	
}
