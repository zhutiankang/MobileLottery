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
	/**********************处理服务器回复**********************/
	private String serviceBodyInsideDESInfo;//加密的body密文
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
	private String serviceBodyInfo;//完整的body明文
	public String getServiceBodyInfo() {
		return serviceBodyInfo;
	}
	public void setServiceBodyInfo(String serviceBodyInfo) {
		this.serviceBodyInfo = serviceBodyInfo;
	}
	/**********************处理服务器回复**********************/
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
	 * 获取完整的body
	 * @return
	 */
	public String getWholeBody(){
		StringWriter writer = new StringWriter();
		XmlSerializer temp = Xml.newSerializer();//临时序列化，获得<body>.......</body>
		try {
			temp.setOutput(writer);
			this.serializerBody(temp);
			temp.flush();                       //没有endDocument,别忘flush
			return writer.toString();           //结果为<body>.......</body>
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取  body里面的DES加密数据
	 * @return
	 */
	public String getBodyInsideDESInfo(){
		//获取要加密的数据
		String wholeBody = this.getWholeBody();
		String orgDESInfo = StringUtils.substringBetween(wholeBody, "<body>", "</body>");
		//加密
		DES des = new DES();
		return des.authcode(orgDESInfo, "DECODE", ConstantValues.DES_PASSWORD);
	}
	public List<Element> getElements() {
		return elements;
	}
	
	
	
}
