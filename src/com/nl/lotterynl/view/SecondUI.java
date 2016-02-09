package com.nl.lotterynl.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.view.manager.BaseUI;

public class SecondUI extends BaseUI{
	private TextView tv;
	
	public SecondUI(Context context) {
		super(context);
		//init();
	}
	/**
	 * 初始化：只调用一次，只创建一次，不再每次都创建，浪费资源
	 * 放在构造函数中
	 * 避免 ui目标界面每次都在创建新的控件，
	 */
	public void init(){
		tv = new TextView(context);
		LayoutParams layoutParams = tv.getLayoutParams();
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tv.setLayoutParams(layoutParams);
		tv.setBackgroundColor(Color.RED);
		tv.setText("这是第二个界面");
	}

	public View getChild(){
		return tv;
	}
	@Override
	public int getID() {
		return ConstantValues.VIEW_SECOND;
	}
	
	@Override
	public void setListener() {
	}
}
