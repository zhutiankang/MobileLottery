package com.nl.lotterynl.engine;

import com.nl.lotterynl.net.protocal.Message;
/**
 * �������ݴ���
 * @author ׷��
 *
 */
public interface CommonInfoEngine {
	/**
	 * ��ȡ��ǰ������
	 * @param integer ���ֱ�ʶ
	 * @return
	 */
	Message getCurrentIssueInfo(Integer integer);
}
