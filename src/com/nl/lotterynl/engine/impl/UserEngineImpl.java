package com.nl.lotterynl.engine.impl;

import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.ShoppingCart;
import com.nl.lotterynl.domain.Ticket;
import com.nl.lotterynl.domain.User;
import com.nl.lotterynl.engine.BaseEngine;
import com.nl.lotterynl.engine.UserEngine;
import com.nl.lotterynl.net.HttpClientUtil;
import com.nl.lotterynl.net.element.BalanceElement;
import com.nl.lotterynl.net.element.BetElement;
import com.nl.lotterynl.net.element.UserLoginElement;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.DES;

public class UserEngineImpl extends BaseEngine implements UserEngine {

	public Message login(User user) {
		// ��һ������ȡ����¼�õ�xml
		// ������¼��Element
		UserLoginElement element = new UserLoginElement();
		// �����û�����
		element.getActpassword().setTagValue(user.getPassword());
		// Message.getXml(element)
		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);
		// ����������ȶ�ͨ��������result�����򷵻ؿ�
		Message result = getResult(xml);

		if (result != null) {
			// ���Ĳ��������������ݴ���
			// body���ֵĵڶ��ν���������������������
			parserBody(result);
			return result;
			// XmlPullParser parser = Xml.newPullParser();
			// try {
			// String body = result.getBody().getServiceBodyInfo();
			// parser.setInput(new StringReader(body));
			// int eventType = parser.getEventType();
			// while (eventType != XmlPullParser.END_DOCUMENT) {
			// String tagName = parser.getName();
			// switch (eventType) {
			// case XmlPullParser.START_TAG:
			// if ("errorcode".equals(tagName)) {
			// result.getBody().getOelement()
			// .setErrorcode(parser.nextText());
			// }
			// if ("errormsg".equals(tagName)) {
			// result.getBody().getOelement()
			// .setErrormsg(parser.nextText());
			// }
			// break;
			// }
			// eventType = parser.next();
			// }
			// return result;
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

		}
		return null;
	}

	/**
	 * �û���¼
	 * 
	 * @param user
	 * @return
	 */

	public Message login1(User user) {
		// ��һ������ȡ����¼�õ�xml
		// ������¼��Element
		UserLoginElement element = new UserLoginElement();
		// �����û�����
		element.getActpassword().setTagValue(user.getPassword());
		// Message.getXml(element)
		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);

		// �ڶ���������xml���������ˣ��ȴ��ظ�
		// HttpClientUtil.sendXml
		HttpClientUtil client = new HttpClientUtil();
		InputStream is = client.sendXml(ConstantValues.LOTTERY_URI, xml);
		// �ж��������ǿ�,

		// �����������ݵ�У�飨MD5����У�飩
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

				// ���Ĳ��������������ݴ���
				// body���ֵĵڶ��ν���������������������
				parser = Xml.newPullParser();
				try {
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
					return result;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	@Override
	public Message getBalance(User user) {
		BalanceElement element = new BalanceElement();

		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);
		// ����������ȶ�ͨ��������result�����򷵻ؿ�
		Message result = getResult(xml);
		if (result != null) {
			XmlPullParser parser = Xml.newPullParser();
			try {
				String body = result.getBody().getServiceBodyInfo();
				parser.setInput(new StringReader(body));
				int eventType = parser.getEventType();
				BalanceElement resultElement = null;
				String tagName;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					tagName = parser.getName();
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
						if ("element".equals(tagName)) {
							resultElement = new BalanceElement();
							result.getBody().getElements().add(resultElement);
						}
						if ("investvalues".equals(tagName)) {
							if (resultElement != null) {
								resultElement
										.setInvestvalues(parser.nextText());
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
		return result;
	}

	@Override
	public Message bet(User user) {
		BetElement element = new BetElement();
		element.getLotteryid().setTagValue(
				ShoppingCart.getInstance().getLotteryid().toString());
		
		// ��Ʊ��ҵ�����棺
		// �ٹ���ע���ļ���
		// �ڹ���Ͷע��Ϣ��װ���û�Ͷע���룩
		// 010203040506|01^01020304050607|01
		StringBuffer codeBuffer = new StringBuffer();
		for(Ticket item : ShoppingCart.getInstance().getTickets()){
			codeBuffer.append("^").append(item.getRedList().replaceAll(" ", "")).append("|").append(item.getBlueList().replaceAll(" ", ""));
		}
		element.getLotterycode().setTagValue(codeBuffer.substring(1));
		
		
		element.getIssue().setTagValue(ShoppingCart.getInstance().getIssue());
		element.getLotteryvalue().setTagValue((ShoppingCart.getInstance().getLotteryvalue() * 100) + "");//�Է�Ϊ��λ ����Ҫ����100
		element.getLotterynumber().setTagValue(ShoppingCart.getInstance().getLotterynumber().toString());
		element.getAppnumbers().setTagValue(ShoppingCart.getInstance().getAppnumbers().toString());
		element.getIssuesnumbers().setTagValue(ShoppingCart.getInstance().getIssuesnumbers().toString());
		element.getIssueflag().setTagValue(ShoppingCart.getInstance().getIssuesnumbers() > 1 ? "1" : "0");
		
		
		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);
		// ����������ȶ�ͨ��������result�����򷵻ؿ�
		Message result = getResult(xml);
		if (result != null) {
			XmlPullParser parser = Xml.newPullParser();
			try {
				String body = result.getBody().getServiceBodyInfo();
				parser.setInput(new StringReader(body));
				int eventType = parser.getEventType();
				BetElement resultElement = null;
				String tagName;
				while (eventType != XmlPullParser.END_DOCUMENT) {
					tagName = parser.getName();
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
						if ("element".equals(tagName)) {
							resultElement = new BetElement();
							result.getBody().getElements().add(resultElement);
						}
						if ("actvalue".equals(tagName)) {
							if (resultElement != null) {
								resultElement.setActvalue(parser.nextText());
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
		return result;
	}
}
