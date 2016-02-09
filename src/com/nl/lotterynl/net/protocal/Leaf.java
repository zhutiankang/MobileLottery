package com.nl.lotterynl.net.protocal;

import org.xmlpull.v1.XmlSerializer;

public class Leaf {

	private String tagName;
	private String tagValue;

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public String getTagValue() {
		return tagValue;
	}

	public Leaf(String tagName) {
		super();
		this.tagName = tagName;
	}

	public Leaf(String tagName, String tagValue) {
		super();
		this.tagName = tagName;
		this.tagValue = tagValue;
	}

	public void serializerLeaf(XmlSerializer serializer) {

		try {
			serializer.startTag(null, tagName);
			if (tagValue == null) {
				tagValue = "";//±‹√‚”–ø’÷∏’Î“Ï≥£
			}
			serializer.text(tagValue);
			serializer.endTag(null, tagName);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
