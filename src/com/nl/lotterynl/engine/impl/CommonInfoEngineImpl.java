package com.nl.lotterynl.engine.impl;

import com.nl.lotterynl.engine.BaseEngine;
import com.nl.lotterynl.engine.CommonInfoEngine;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Message;

public class CommonInfoEngineImpl extends BaseEngine implements CommonInfoEngine {

	@Override
	public Message getCurrentIssueInfo(Integer lotteryid) {
		
		//第一步：获取到登录用的xml
		CurrentIssueElement element = new CurrentIssueElement();
		element.getLotteryid().setTagValue(lotteryid.toString());
		Message message = new Message();
		String xml = message.getXml(element);
		//第二步：发送xml到服务器端，等待回复
		//第三步：数据的校验（MD5数据校验）
		//如果第三步比对通过，返回result，否则返回空
		Message result = getResult(xml);
		if(result != null){
			//第四步：请求结果的数据处理
			parserBody2(result);
			return result;
		}
		return null;
	}

	

}
