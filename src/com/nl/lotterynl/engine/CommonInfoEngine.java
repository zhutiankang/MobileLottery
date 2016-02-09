package com.nl.lotterynl.engine;

import com.nl.lotterynl.net.protocal.Message;
/**
 * 公共数据处理
 * @author 追梦
 *
 */
public interface CommonInfoEngine {
	/**
	 * 获取当前销售期
	 * @param integer 彩种标识
	 * @return
	 */
	Message getCurrentIssueInfo(Integer integer);
}
