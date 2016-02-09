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
	// ��ȡ�ڶ�����������
	public Message getResult(String xml) {
		// �ڶ���������xml���������ˣ��ȴ��ظ�
		// HttpClientUtil.sendXml
		HttpClientUtil client = new HttpClientUtil();
		InputStream is = client.sendXml(ConstantValues.LOTTERY_URI, xml);

		// �ж��������ǿ�,

		// �����������ݵ�У�飨MD5����У�飩���Է������ʱ�����
		// �����ظ���xml �õ�timestamp+digest+body
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
							// Ҳ���ü�ֵ�Դ洢
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
			// ԭʼ���ݻ�ԭ��ʱ�����������+���루������+body���ģ�����+����DES��
			String orgInfo = result.getHeader().getTimestamp().getTagValue()
					+ ConstantValues.AGENTER_PASSWORD;
			// body����,����
			DES des = new DES();
			String body = "<body>"
					+ des.authcode(result.getBody()
							.getServiceBodyInsideDESInfo(), "ENCODE",
							ConstantValues.DES_PASSWORD) + "</body>";

			// ���ù��������ֻ��˵�md5
			String md5Hex = DigestUtils.md5Hex(orgInfo + body);
			// ���ֻ��˵ĺͷ������˵Ľ��бȶԣ���ͨ����ִ����һ��
			if (md5Hex.equals(result.getHeader().getDigest().getTagValue())) {
				// �ȶ�ͨ��,����ʱ�����漰bodyû�г���Ϊ��Ҫ����ȷ��
				result.getBody().setServiceBodyInfo(body);//������body�ŵ�ServiceBodyInfo��
				return result;
			}

		}
		return null;
	}

	// ��ȡ���Ĳ�
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
					// �ж��Ƿ���element��ǩ������еĻ�����resultElement
					if ("element".equals(tagName)) {
						resultElement = new CurrentIssueElement();
						// onPostExecute���½���ʱ��ֻ��result��Ҫ��resultElement
						// �󶨵�result�ϣ��ſ�������
						result.getBody().getElements().add(resultElement);
					}

					// ������������
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

	// ��ȡ���Ĳ�
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
