package com.nl.lotterynl.engine;

import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.net.HttpClientUtil;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.DES;

public abstract class BaseEngine {
	// 抽取第二步，第三步
	public Message getResult(String xml) {
		// 第二步：发送xml到服务器端，等待回复
		// HttpClientUtil.sendXml
		HttpClientUtil client = new HttpClientUtil();
		InputStream is = client.sendXml(ConstantValues.LOTTERY_URI, xml);

		// 判断输入流非空,

		// 第三步：数据的校验（MD5数据校验），以防传输的时候出错
		// 解析回复的xml 得到timestamp+digest+body
		if (is != null) {
			Message result = new Message();
			XmlPullParser parser = Xml.newPullParser();
			try {
				parser.setInput(is, ConstantValues.ENCODING);
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String tagName = parser.getName();
					switch (eventType) {
					case XmlPullParser.START_TAG:
						if ("timestamp".equals(tagName)) {
							// 也可用键值对存储
							result.getHeader().getTimestamp()
									.setTagValue(parser.nextText());
						}
						if ("digest".equals(tagName)) {
							result.getHeader().getDigest()
									.setTagValue(parser.nextText());
						}
						if ("body".equals(tagName)) {
							result.getBody().setServiceBodyInsideDESInfo(
									parser.nextText());
						}
						break;
					}
					eventType = parser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 原始数据还原：时间戳（解析）+密码（常量）+body明文（解析+解密DES）
			String orgInfo = result.getHeader().getTimestamp().getTagValue()
					+ ConstantValues.AGENTER_PASSWORD;
			// body明文,解密
			DES des = new DES();
			String body = "<body>"
					+ des.authcode(result.getBody()
							.getServiceBodyInsideDESInfo(), "ENCODE",
							ConstantValues.DES_PASSWORD) + "</body>";

			// 利用工具生成手机端的md5
			String md5Hex = DigestUtils.md5Hex(orgInfo + body);
			// 将手机端的和服务器端的进行比对，若通过，执行下一步
			if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
				// 比对通过,传输时各方面及body没有出错，为需要的正确的
				result.getBody().setServiceBodyInfo(body);//将明文body放到ServiceBodyInfo中
				return result;
			}

		}
		return null;
	}

	// 抽取第四步
	public void parserBody2(Message result) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			String body = result.getBody().getServiceBodyInfo();
			parser.setInput(new StringReader(body));
			int eventType = parser.getEventType();
			CurrentIssueElement resultElement = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if ("errorcode".equals(tagName)) {
						result.getBody().getOelement()
								.setErrorcode(parser.nextText());
					}
					if ("errormsg".equals(tagName)) {
						result.getBody().getOelement()
								.setErrormsg(parser.nextText());
					}
					// 判断是否含有element标签，如果有的话创建resultElement
					if ("element".equals(tagName)) {
						resultElement = new CurrentIssueElement();
						// onPostExecute更新界面时，只有result，要将resultElement
						// 绑定到result上，才可以运用
						result.getBody().getElements().add(resultElement);
					}

					// 解析特殊数据
					if ("issue".equals(tagName)) {
						if (resultElement != null) {
							resultElement.setIssue(parser.nextText());
						}
					}
					if ("lasttime".equals(tagName)) {
						if (resultElement != null) {
							resultElement.setLasttime(parser.nextText());
						}
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 抽取第四步
	public void parserBody(Message result) {
		XmlPullParser parser = Xml.newPullParser();
		try {
			String body = result.getBody().getServiceBodyInfo();
			parser.setInput(new StringReader(body));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if ("errorcode".equals(tagName)) {
						result.getBody().getOelement()
								.setErrorcode(parser.nextText());
					}
					if ("errormsg".equals(tagName)) {
						result.getBody().getOelement()
								.setErrormsg(parser.nextText());
					}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
