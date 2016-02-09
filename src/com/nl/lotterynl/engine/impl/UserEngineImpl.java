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
		// 第一步：获取到登录用的xml
		// 创建登录用Element
		UserLoginElement element = new UserLoginElement();
		// 设置用户数据
		element.getActpassword().setTagValue(user.getPassword());
		// Message.getXml(element)
		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);
		// 如果第三步比对通过，返回result，否则返回空
		Message result = getResult(xml);

		if (result != null) {
			// 第四步：请求结果的数据处理
			// body部分的第二次解析，解析的是明文内容
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
	 * 用户登录
	 * 
	 * @param user
	 * @return
	 */

	public Message login1(User user) {
		// 第一步：获取到登录用的xml
		// 创建登录用Element
		UserLoginElement element = new UserLoginElement();
		// 设置用户数据
		element.getActpassword().setTagValue(user.getPassword());
		// Message.getXml(element)
		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);

		// 第二步：发送xml到服务器端，等待回复
		// HttpClientUtil.sendXml
		HttpClientUtil client = new HttpClientUtil();
		InputStream is = client.sendXml(ConstantValues.LOTTERY_URI, xml);
		// 判断输入流非空,

		// 第三步：数据的校验（MD5数据校验）
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

				// 第四步：请求结果的数据处理
				// body部分的第二次解析，解析的是明文内容
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
		// 如果第三步比对通过，返回result，否则返回空
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
		
		// 彩票的业务里面：
		// ①关于注数的计算
		// ②关于投注信息封装（用户投注号码）
		// 010203040506|01^01020304050607|01
		StringBuffer codeBuffer = new StringBuffer();
		for(Ticket item : ShoppingCart.getInstance().getTickets()){
			codeBuffer.append("^").append(item.getRedList().replaceAll(" ", "")).append("|").append(item.getBlueList().replaceAll(" ", ""));
		}
		element.getLotterycode().setTagValue(codeBuffer.substring(1));
		
		
		element.getIssue().setTagValue(ShoppingCart.getInstance().getIssue());
		element.getLotteryvalue().setTagValue((ShoppingCart.getInstance().getLotteryvalue() * 100) + "");//以分为单位 所以要乘以100
		element.getLotterynumber().setTagValue(ShoppingCart.getInstance().getLotterynumber().toString());
		element.getAppnumbers().setTagValue(ShoppingCart.getInstance().getAppnumbers().toString());
		element.getIssuesnumbers().setTagValue(ShoppingCart.getInstance().getIssuesnumbers().toString());
		element.getIssueflag().setTagValue(ShoppingCart.getInstance().getIssuesnumbers() > 1 ? "1" : "0");
		
		
		Message message = new Message();
		message.getHeader().getUsername().setTagValue(user.getUsername());
		String xml = message.getXml(element);
		// 如果第三步比对通过，返回result，否则返回空
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
