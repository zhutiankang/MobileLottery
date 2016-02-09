package com.nl.lotterynl.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.domain.ShoppingCart;
import com.nl.lotterynl.domain.Ticket;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.MiddleManager;

/**
 * 购物车展示
 * 
 */
public class Shopping extends BaseUI {
	// 通用三步 加载linearLayout findviewbyId 设置监听
	private Button addOptional;// 添加自选
	private Button addRandom;// 添加机选

	private ListView shoppingList;// 用户选择信息列表

	private ImageButton shoppingListClear;// 清空购物车
	private TextView notice;// 提示信息
	private Button buy;// 购买
	
	// ①填充购物车
	private ShoppingAdapter adapter;
	// ②添加自选+添加机选
	// ③清空购物车
	// ④高亮的提示信息处理
	// ⑤购买
	public Shopping(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_shopping, null);

		addOptional = (Button) findViewById(R.id.ii_add_optional);
		addRandom = (Button) findViewById(R.id.ii_add_random);
		shoppingListClear = (ImageButton) findViewById(R.id.ii_shopping_list_clear);
		notice = (TextView) findViewById(R.id.ii_shopping_lottery_notice);
		buy = (Button) findViewById(R.id.ii_lottery_shopping_buy);
		shoppingList = (ListView) findViewById(R.id.ii_shopping_list);
		
		adapter = new ShoppingAdapter();
		shoppingList.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		addOptional.setOnClickListener(this);
		addRandom.setOnClickListener(this);
		shoppingListClear.setOnClickListener(this);
		buy.setOnClickListener(this);
	}

	@Override
	public int getID() {
		return ConstantValues.VIEW_SHOPPING;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_add_optional:
			// 添加自选
			MiddleManager.getInstance().goBack();
			break;
		case R.id.ii_add_random:
			// 添加机选
			addRandom();
			changeNotice();
			break;
		case R.id.ii_shopping_list_clear:
			// 清空
			ShoppingCart.getInstance().getTickets().clear();
			adapter.notifyDataSetChanged();
			changeNotice();
			break;
		case R.id.ii_lottery_shopping_buy:
			// 购买
			//1，判断：购物车中是否有投注
			if(ShoppingCart.getInstance().getTickets().size()>=0){
				//2，判断：用户是否登录--被动登录
				if(GlobalParams.isLogin){
					//3，判断：用户的余额是否满足投注需求
					if(ShoppingCart.getInstance().getLotteryvalue()<=GlobalParams.MONEY){
						//4，界面跳转；跳转到追期和倍投的设置界面
						MiddleManager.getInstance().changeUI(PreBet.class, ssqBundle);
					}else{
						// 提示用户：充值去；界面跳转：用户充值界面
						PromptManager.showToast(context, "充值去");
					}
				}else{
					// 提示用户：登录去；界面跳转：用户登录界面
					PromptManager.showToast(context, "登录去");
					MiddleManager.getInstance().changeUI(UserLogin.class, ssqBundle);
				}
			}else{
				// 提示用户：需要选择一注
				PromptManager.showToast(context, "需要选择一注");
			}
			
			// 分支
			break;
		}
	}
	@Override
	public void onResume() {
		changeNotice();
		super.onResume();
	}
	private void changeNotice(){
		Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
		Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();
		String noticeInfo = context.getResources().getString(R.string.is_shopping_list_notice);
		noticeInfo = StringUtils.replaceEach(noticeInfo, new String[]{"NUM","MONEY"}, new String[]{lotterynumber.toString(),lotteryvalue.toString()});
//		Html.fromHtml(noticeInfo);将html里面的内容转换
		notice.setText(Html.fromHtml(noticeInfo));
	}
	private void addRandom() {
		Random random = new Random();
		List<Integer> redList = new ArrayList<Integer>();
		List<Integer> blueList = new ArrayList<Integer>();
		// 机选红球
		while (redList.size() < 6) {// 循环6次,产生6个球，开始为0
			int num = random.nextInt(33);// 33个球 下标0-32
			if (redList.contains(num)) {
				continue;
			}
			redList.add(num);
		}
		// 机选蓝球
		int num = random.nextInt(16);// 16个球 下标0-15
		blueList.add(num);
		
		//封装成Ticket
		Ticket ticket = new Ticket();
		StringBuffer redBuffer = new StringBuffer();
		DecimalFormat decimal = new DecimalFormat("00");
		for(Integer item : redList){//存取的是位置，比实际的值小1
			Integer value = item + 1;
			redBuffer.append(" ").append(decimal.format(value));
		}
		ticket.setRedList(redBuffer.substring(1));//第一个元素前的空格去掉
		
		StringBuffer blueBuffer = new StringBuffer();
		for(Integer item : blueList){//存取的是位置，比实际的值小1
			Integer value = item + 1;
			blueBuffer.append(" ").append(decimal.format(value));
		}
		ticket.setBlueList(blueBuffer.substring(1));//第一个元素前的空格去掉
		ticket.setNum(1);
		//添加到购物车中
		ShoppingCart.getInstance().getTickets().add(ticket);
		//更新界面
		adapter.notifyDataSetChanged();
	}

	private class ShoppingAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ShoppingCart.getInstance().getTickets().size();
		}

		@Override
		public Object getItem(int position) {
			return ShoppingCart.getInstance().getTickets().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				convertView = View.inflate(context, R.layout.il_shopping_row, null);
				holder = new ViewHolder();
				holder.delete = (ImageButton) convertView.findViewById(R.id.ii_shopping_item_delete);
				holder.redball = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
				holder.blueball = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
				holder.num = (TextView) convertView.findViewById(R.id.ii_shopping_item_money);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
			holder.redball.setText(ticket.getRedList());
			holder.blueball.setText(ticket.getBlueList());
			holder.num.setText(ticket.getNum()+"注");
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ShoppingCart.getInstance().getTickets().remove(position);
					notifyDataSetChanged();
					//一般清空，listView中没有设置增减项，做局部更新，不会调用notifyDataSetChanged()更新界面
					changeNotice();
				}
			});
			
			
			
			return convertView;
		}
		
	}
	static class ViewHolder{
		ImageButton delete;
		TextView redball;
		TextView blueball;
		TextView num;
	}

}
