package com.nl.lotterynl.net.protocal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import com.nl.lotterynl.domain.ConstantValues;

public class Header {
	
	private Leaf agenterid = new Leaf("agenterid", ConstantValues.AGENTERID);
	private Leaf source = new Leaf("source", ConstantValues.SOURCE);
	private Leaf compress = new Leaf("compress",ConstantValues.COMPRESS);
	
	private Leaf timestamp = new Leaf("timestamp");
	private Leaf messengerid = new Leaf("messengerid");
	private Leaf digest = new Leaf("digest");
	
	private Leaf transactiontype = new Leaf("transactiontype");
	public Leaf getTransactiontype() {
		return transactiontype;
	}

	private Leaf username = new Leaf("username");
	
	public Leaf getUsername() {
		return username;
	}

	public void serializerHeader(XmlSerializer serializer,String body){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time = dateFormat.format(date);
		timestamp.setTagValue(time);
		
		Random random = new Random();
		int rand = random.nextInt(999999)+1;
		DecimalFormat format = new DecimalFormat("000000");
		String randFormat= format.format(rand);
		messengerid.setTagValue(time+randFormat);
		
		String md5Hex = DigestUtils.md5Hex(time+ConstantValues.AGENTER_PASSWORD+body);
		digest.setTagValue(md5Hex);
		try {
			serializer.startTag(null, "header");
			agenterid.serializerLeaf(serializer);
			source.serializerLeaf(serializer);
			compress.serializerLeaf(serializer);
			timestamp.serializerLeaf(serializer);
			messengerid.serializerLeaf(serializer);
			digest.serializerLeaf(serializer);
			transactiontype.serializerLeaf(serializer);
			username.serializerLeaf(serializer);
			serializer.endTag(null, "header");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**********************处理服务器回复**********************/

	public Leaf getTimestamp() {
		return timestamp;
	}

	public Leaf getDigest() {
		return digest;
	}
	/**********************处理服务器回复**********************/
}
