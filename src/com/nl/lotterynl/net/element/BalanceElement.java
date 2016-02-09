package com.nl.lotterynl.net.element;

import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.net.protocal.Element;
/**
 * 获取余额
 * @author 追梦
 *
 */
public class BalanceElement implements Element {
	/*************** 回复信息 **********************/
	// investvalues 可投注金额
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
