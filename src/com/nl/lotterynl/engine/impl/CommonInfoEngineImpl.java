package com.nl.lotterynl.engine.impl;

import com.nl.lotterynl.engine.BaseEngine;
import com.nl.lotterynl.engine.CommonInfoEngine;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Message;

public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine {

	@Override
	public Message getCurrentIssueInfo(Integer lotteryid) {
		
		//��һ������ȡ����¼�õ�xml
		CurrentIssueElement element = new CurrentIssueElement();
		element.getLotteryid().setTagValue(lotteryid.toString());
		Message message = new Message();
		String xml = message.getXml(element);
		//�ڶ���������xml���������ˣ��ȴ��ظ�
		//�����������ݵ�У�飨MD5����У�飩
		//����������ȶ�ͨ��������result�����򷵻ؿ�
		Message result = getResult(xml);
		if(result != null){
			//���Ĳ��������������ݴ���
			parserBody2(result);
			return result;
		}
		return null;
	}

	

}
