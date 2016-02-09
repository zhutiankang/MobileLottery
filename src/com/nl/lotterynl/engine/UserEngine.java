package com.nl.lotterynl.engine;

import com.nl.lotterynl.domain.User;
import com.nl.lotterynl.net.protocal.Message;
/**
 * 用户相关的业务操作的接口
 * @author 追梦
 *
 */
public interface UserEngine {
	/*******用户登录*********/
	Message login(User user);
	
	/*******获取用户余额*********/
	Message getBalance(User user);
    /**********用户投注**************/
	Message bet(User user);
}
