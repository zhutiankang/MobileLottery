package com.nl.lotterynl.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车 对购物车来说，get,set方法可不是随意设置的
 */
public class ShoppingCart {
	// 手机中只有一个实例，保证数据都存在一个实例中
	// 单例模式，确保每次拿的都是同一个，数据存在同一个购物车中，使数据统一，是放在一个购物车中，而不是每次都新建一个，不同的
	private static ShoppingCart instance;

	private ShoppingCart() {
	}

	public static ShoppingCart getInstance() {
		if (instance == null) {
			instance = new ShoppingCart();
		}
		return instance;
	}

	// 投注
	// lotteryid string * 玩法编号
	// issue string * 期号（当前销售期）
	// lotterycode string * 投注号码，注与注之间^分割
	// lotterynumber string 注数
	// lotteryvalue string 方案金额，以分为单位

	// appnumbers string 倍数
	// issuesnumbers string 追期
	// issueflag int * 是否多期追号 0否，1多期
	// bonusstop int * 中奖后是否停止：0不停，1停

	private Integer lotteryid;
	private String issue; // 双色球，福彩3D各种彩票注数清单
	private List<Ticket> tickets = new ArrayList<Ticket>();// lotterycode string
															// * 投注号码，注与注之间^分割
	private Integer lotterynumber;// 迭代上面的，计算
	private Integer lotteryvalue;
	// 倍数与追期
	private Integer appnumbers = 1;
	private Integer issuesnumbers = 1;

	public Integer getAppnumbers() {
		return appnumbers;
	}

	public Integer getIssuesnumbers() {
		return issuesnumbers;
	}

	// 前两个随意get,set均可以
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

	// 物品已经new不需要在set
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
	 * 操作倍数(将增加倍数与减少倍数结合在一起)
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
	 * 操作(将增加追期与减少追期结合在一起)
	 */
	public boolean addIssuesnumbers(boolean isAdd) {
		if (isAdd) {
			issuesnumbers++;
			if (issuesnumbers > 99) {//期数超过了
				issuesnumbers--;
				return false;
			}
			if (getLotteryvalue() > GlobalParams.MONEY) {//钱数超过余额了
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
