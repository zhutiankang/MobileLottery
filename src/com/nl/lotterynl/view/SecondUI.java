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
	 * ��ʼ����ֻ����һ�Σ�ֻ����һ�Σ�����ÿ�ζ��������˷���Դ
	 * ���ڹ��캯����
	 * ���� uiĿ�����ÿ�ζ��ڴ����µĿؼ���
	 */
	public void init(){
		tv = new TextView(context);
		LayoutParams layoutParams = tv.getLayoutParams();
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tv.setLayoutParams(layoutParams);
		tv.setBackgroundColor(Color.RED);
		tv.setText("���ǵڶ�������");
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
