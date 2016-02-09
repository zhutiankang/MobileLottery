package com.nl.lotterynl.net.element;

import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.net.protocal.Element;
import com.nl.lotterynl.net.protocal.Leaf;

public class UserLoginElement implements Element{
	private Leaf actpassword = new Leaf("actpassword");
	public Leaf getActpassword() {
		return actpassword;
	}

	@Override
	public void serializerElement(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			actpassword.serializerLeaf(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getTransactionType() {
		return "14001";
	}

}
