package com.nl.lotterynl.engine;

import com.nl.lotterynl.domain.User;
import com.nl.lotterynl.net.protocal.Message;
/**
 * �û���ص�ҵ������Ľӿ�
 * @author ׷��
 *
 */
public interface UserEngine {
	/*******�û���¼*********/
	Message login(User user);
	
	/*******��ȡ�û����*********/
	Message getBalance(User user);
    /**********�û�Ͷע**************/
	Message bet(User user);
}
