package com.nl.lotterynl.domain;
/**
 * 
 *封装用户的投注信息
 */
public class Ticket {
	private String redList;
	private String blueList;
	private int num;//注数
	public String getRedList() {
		return redList;
	}
	public void setRedList(String redList) {
		this.redList = redList;
	}
	public String getBlueList() {
		return blueList;
	}
	public void setBlueList(String blueList) {
		this.blueList = blueList;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
}
