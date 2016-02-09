package com.nl.lotterynl.test;

import android.test.AndroidTestCase;

import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.domain.User;
import com.nl.lotterynl.engine.UserEngine;
import com.nl.lotterynl.engine.impl.UserEngineImpl;
import com.nl.lotterynl.net.NetUtil;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.BeanFactory;

public class TestCase extends AndroidTestCase {
	
	private static final String TAG = "TestCase";

	public void createXML(){
		try {
			Message m = new Message();
			CurrentIssueElement element = new CurrentIssueElement();
			element.getLotteryid().setTagValue("118");
			String msg = m.getXml(element);
			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void checkNet(){
		boolean isNetworkConnection = NetUtil.checkNet(getContext());
		System.out.println(GlobalParams.PROXY+GlobalParams.PORT);
	}
	public void testUserLogin(){
//		UserEngineImpl userEngineImpl = new UserEngineImpl();
//		User user = new User();
//		user.setPassword("13200000000");
//		user.setUsername("kaler");
//		Message msg = userEngineImpl.login(user);
//		String errorcode = msg.getBody().getOelement().getErrorcode();
//		System.out.println("errorcode:"+errorcode);
		UserEngine engine = BeanFactory.getImpl(UserEngine.class);
		User user = new User();
		user.setPassword("13200000000");
		user.setUsername("kaler");
		Message msg = engine.login(user);
		String errorcode = msg.getBody().getOelement().getErrorcode();
		System.out.println("errorcode:"+errorcode);
		
	}
}
