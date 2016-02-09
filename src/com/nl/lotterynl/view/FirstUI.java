package com.nl.lotterynl.view;

import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.view.manager.BaseUI;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class FirstUI extends BaseUI {
	private TextView tv;
	
	public FirstUI(Context context) {
		super(context);
		//init();
	}
	/**
	 * ��ʼ��������һ��
	 * ���ڹ��캯����
	 */
	public void init(){
		tv = new TextView(context);
		LayoutParams layoutParams = tv.getLayoutParams();
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tv.setLayoutParams(layoutParams);
		tv.setBackgroundColor(Color.BLUE);
		tv.setText("���ǵ�һ������");
	}
	public View getChild(){
		return tv;
	}
	@Override
	public int getID() {
		return ConstantValues.VIEW_FIRST;
	}
	@Override
	public void setListener() {
	}
}
