package com.nl.lotterynl.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ﳵ �Թ��ﳵ��˵��get,set�����ɲ����������õ�
 */
public class ShoppingCart {
	// �ֻ���ֻ��һ��ʵ������֤���ݶ�����һ��ʵ����
	// ����ģʽ��ȷ��ÿ���õĶ���ͬһ�������ݴ���ͬһ�����ﳵ�У�ʹ����ͳһ���Ƿ���һ�����ﳵ�У�������ÿ�ζ��½�һ������ͬ��
	private static ShoppingCart instance;

	private ShoppingCart() {
	}

	public static ShoppingCart getInstance() {
		if (instance == null) {
			instance = new ShoppingCart();
		}
		return instance;
	}

	// Ͷע
	// lotteryid string * �淨���
	// issue string * �ںţ���ǰ�����ڣ�
	// lotterycode string * Ͷע���룬ע��ע֮��^�ָ�
	// lotterynumber string ע��
	// lotteryvalue string �������Է�Ϊ��λ

	// appnumbers string ����
	// issuesnumbers string ׷��
	// issueflag int * �Ƿ����׷�� 0��1����
	// bonusstop int * �н����Ƿ�ֹͣ��0��ͣ��1ͣ

	private Integer lotteryid;
	private String issue; // ˫ɫ�򣬸���3D���ֲ�Ʊע���嵥
	private List<Ticket> tickets = new ArrayList<Ticket>();// lotterycode string
															// * Ͷע���룬ע��ע֮��^�ָ�
	private Integer lotterynumber;// ��������ģ�����
	private Integer lotteryvalue;
	// ������׷��
	private Integer appnumbers = 1;
	private Integer issuesnumbers = 1;

	public Integer getAppnumbers() {
		return appnumbers;
	}

	public Integer getIssuesnumbers() {
		return issuesnumbers;
	}

	// ǰ��������get,set������
	public Integer getLotteryid() {
		return lotteryid;
	}

	public void setLotteryid(Integer lotteryid) {
		this.lotteryid = lotteryid;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	// ��Ʒ�Ѿ�new����Ҫ��set
	public List<Ticket> getTickets() {
		return tickets;
	}

	public Integer getLotterynumber() {
		lotterynumber = 0;
		for (Ticket item : tickets) {
			lotterynumber += item.getNum();
		}
		return lotterynumber;
	}

	public Integer getLotteryvalue() {
		lotteryvalue = 2 * lotterynumber * appnumbers * issuesnumbers;
		return lotteryvalue;
	}

	/**
	 * ��������(�����ӱ�������ٱ��������һ��)
	 */
	public boolean addAppnumbers(boolean isAdd) {
		if (isAdd) {
			appnumbers++;
			if (appnumbers > 99) {
				appnumbers--;
				return false;
			}
			if (getLotteryvalue() > GlobalParams.MONEY) {
				appnumbers--;
				return false;
			}
		} else {
			appnumbers--;
			if (appnumbers == 0) {
				appnumbers++;
				return false;
			}
		}
		return true;
	}
	/**
	 * ����(������׷�������׷�ڽ����һ��)
	 */
	public boolean addIssuesnumbers(boolean isAdd) {
		if (isAdd) {
			issuesnumbers++;
			if (issuesnumbers > 99) {//����������
				issuesnumbers--;
				return false;
			}
			if (getLotteryvalue() > GlobalParams.MONEY) {//Ǯ�����������
				issuesnumbers--;
				return false;
			}
		} else {
			issuesnumbers--;
			if (issuesnumbers == 0) {
				issuesnumbers++;
				return false;
			}
		}
		return true;
	}

	public void clear() {
		tickets.clear();
		lotterynumber = 0;
		lotteryvalue = 0;
		
		appnumbers = 1;
		issuesnumbers = 1;
	}
}
