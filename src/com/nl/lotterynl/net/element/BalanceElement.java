package com.nl.lotterynl.net.element;

import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.net.protocal.Element;
/**
 * ��ȡ���
 * @author ׷��
 *
 */
public class BalanceElement implements Element {
	/*************** �ظ���Ϣ **********************/
	// investvalues ��Ͷע���
	private String investvalues;
	public String getInvestvalues() {
		return investvalues;
	}
	
	public void setInvestvalues(String investvalues) {
		this.investvalues = investvalues;
	}
	/*************************************/
	@Override
	public void serializerElement(XmlSerializer serializer) {
	}

	@Override
	public String getTransactionType() {
		return "11007";
	}


}
