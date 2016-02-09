package com.nl.lotterynl;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.util.FadeUtil;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.FirstUI;
import com.nl.lotterynl.view.Hall;
import com.nl.lotterynl.view.Hall2;
import com.nl.lotterynl.view.SecondUI;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.BottomManager;
import com.nl.lotterynl.view.manager.MiddleManager;
import com.nl.lotterynl.view.manager.TitleManager;

public class MainActivity extends Activity {
	private RelativeLayout il_middle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nl_main);
		
		DisplayMetrics outMetrics = new DisplayMetrics();//����Ϊ��
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		GlobalParams.WIN_WIDTH = outMetrics.widthPixels;
		init();
	}
	private void init() {
		TitleManager manager = TitleManager.getInstance();
		manager.init(this);
		manager.showUnloginTitle();
		
		BottomManager bm = BottomManager.getInstance();
		bm.init(this);
		bm.showCommonBottom();
		il_middle = (RelativeLayout) findViewById(R.id.il_middle);
		MiddleManager.getInstance().setIl_middle(il_middle);
		// �����۲��ߺͱ��۲���֮��Ĺ�ϵ������͵ײ�������ӵ��۲��ߵ��������棩
		MiddleManager.getInstance().addObserver(TitleManager.getInstance());
		MiddleManager.getInstance().addObserver(BottomManager.getInstance());
		//loadFirstUI();
		MiddleManager.getInstance().changeUI(Hall.class);
		//����һ�����������ɣ�2���Ӻ�ڶ���������ʾ
		//handler.sendEmptyMessageDelayed(10, 2000);
		
		
	}
	private void loadFirstUI() {
		FirstUI firstUI = new FirstUI(this);
		View child = firstUI.getChild();
		il_middle.addView(child);
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//changeUI();
			changeUI(new SecondUI(MainActivity.this));
		}
		
	};
	protected void loadSecondUI() {
		SecondUI secondUI = new SecondUI(this);
		View child = secondUI.getChild();
		//�л�����ĺ��ķ�����
		il_middle.addView(child);
//		2�������л��������򵥶���(ƽ��)�������Ӷ���(���뵭��)
		//child.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ia_view_change));
		FadeUtil.fadeIn(child, 2000, 1000);
	}


//	3���л�����ͨ�ô���--����һ����������ȷ�л���Ŀ����棬ͨ�ã�
	/**
	 * �л�����
	 */
	private void changeUI(BaseUI ui){
//		1���л�����ʱ������һ����ʾ����
		//�л�����ĺ��ķ���һ
		il_middle.removeAllViews();
		//FadeUtil.fadeOut(il_middle.getChildAt(0), 2000);
		View child = ui.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ia_view_change));
		//FadeUtil.fadeIn(child, 2000, 1000);
	}
//	4����ʹ��Handler����������ť�л�
	/**
	 * �л�����
	 */
	private void changeUI(){
//		1���л�����ʱ������һ����ʾ����
		//�л�����ĺ��ķ���һ
		//il_middle.removeAllViews();
		//il_middle.getChildAt(0);
		FadeUtil.fadeOut(il_middle.getChildAt(0), 2000);
		loadSecondUI();
	}
	// 1���л�����ʱ������һ����ʾ����
		// 2�������л��������򵥶����������Ӷ��������뵭����
		// 3���л�����ͨ�ô���������һ����������ȷ�л���Ŀ�����,ͨ�ã�
		// 4����ʹ��Handler����������ť�л�

		// a���û����ؼ�������׽
		// b����Ӧ���ؼ������л�����ʷ����

		//
		// LinkedList<String>����AndroidStack
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			boolean result = MiddleManager.getInstance().goBack();
			//���ؼ�����ʧ��
			if(!result){
				//Toast.makeText(MainActivity.this, "�Ƿ��˳�ϵͳ", 0).show();
				PromptManager.showExitSystem(this);
			}
			return false;//��Ҫ�����������
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
