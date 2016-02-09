package com.nl.lotterynl.net.element;

import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.net.protocal.Element;
import com.nl.lotterynl.net.protocal.Leaf;

public class CurrentIssueElement implements Element {
	
	private Leaf lotteryid = new Leaf("lotteryid");
	private Leaf issues = new Leaf("issues","1");
	/*****************处理服务器回复*********************/
	private String issue;
	private String lasttime;
	
	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	/*************************************************/
	public Leaf getLotteryid() {
		return lotteryid;
	}
	
	public void serializerElement(XmlSerializer serializer){
		
		try {
			serializer.startTag(null, "element");
			lotteryid.serializerLeaf(serializer);
			issues.serializerLeaf(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getTransactionType(){
		return "12002";
	}
}
