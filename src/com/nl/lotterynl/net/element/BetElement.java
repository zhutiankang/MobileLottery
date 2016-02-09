package com.nl.lotterynl.net.element;

import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.net.protocal.Element;
import com.nl.lotterynl.net.protocal.Leaf;
/**
 * Ͷע����
 * @author ׷��
 *
 */
public class BetElement implements Element {
	// lotteryid string * �淨���
		private Leaf lotteryid = new Leaf("lotteryid");
		// issue string * �ںţ���ǰ�����ڣ�
		// lotterycode string * Ͷע���룬ע��ע֮��^�ָ�
		private Leaf lotterycode = new Leaf("lotterycode");

		// issue string * �ںţ���ǰ�����ڣ�
		private Leaf issue = new Leaf("issue");

		// lotterynumber string ע��
		private Leaf lotterynumber = new Leaf("lotterynumber");
		// lotteryvalue string �������Է�Ϊ��λ
		private Leaf lotteryvalue = new Leaf("lotteryvalue");
		// appnumbers string ����
		private Leaf appnumbers = new Leaf("appnumbers");
		// issuesnumbers string ׷��
		private Leaf issuesnumbers = new Leaf("issuesnumbers");
		// issueflag int * �Ƿ����׷�� 0��1����
		private Leaf issueflag = new Leaf("issueflag");

		private Leaf bonusstop = new Leaf("bonusstop", "1");
		/*********************����������ֵ**************************/
		// actvalue int * �û��ʺ����
		private String actvalue;//�û��ʺ����
		/***********************************************/
	@Override
	public void serializerElement(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			lotteryid.serializerLeaf(serializer);
			issue.serializerLeaf(serializer);
			lotteryvalue.serializerLeaf(serializer);
			lotterynumber.serializerLeaf(serializer);
			appnumbers.serializerLeaf(serializer);
			issuesnumbers.serializerLeaf(serializer);
			lotterycode.serializerLeaf(serializer);
			issueflag.serializerLeaf(serializer);
			bonusstop.serializerLeaf(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTransactionType() {
		return "12006";
	}

	public Leaf getLotteryid() {
		return lotteryid;
	}

	public Leaf getLotterycode() {
		return lotterycode;
	}

	public Leaf getIssue() {
		return issue;
	}

	public Leaf getLotterynumber() {
		return lotterynumber;
	}

	public Leaf getLotteryvalue() {
		return lotteryvalue;
	}

	public Leaf getAppnumbers() {
		return appnumbers;
	}

	public Leaf getIssuesnumbers() {
		return issuesnumbers;
	}

	public Leaf getIssueflag() {
		return issueflag;
	}

	public String getActvalue() {
		return actvalue;
	}

	public void setActvalue(String actvalue) {
		this.actvalue = actvalue;
	}

	
	
}
