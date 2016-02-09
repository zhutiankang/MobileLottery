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
		
		DisplayMetrics outMetrics = new DisplayMetrics();//不能为空
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
		// 建立观察者和被观察者之间的关系（标题和底部导航添加到观察者的容器里面）
		MiddleManager.getInstance().addObserver(TitleManager.getInstance());
		MiddleManager.getInstance().addObserver(BottomManager.getInstance());
		//loadFirstUI();
		MiddleManager.getInstance().changeUI(Hall.class);
		//当第一个界面加载完成，2秒钟后第二个界面显示
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
		//切换界面的核心方法二
		il_middle.addView(child);
//		2、处理切换动画：简单动画(平移)——复杂动画(淡入淡出)
		//child.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ia_view_change));
		FadeUtil.fadeIn(child, 2000, 1000);
	}


//	3、切换界面通用处理--增加一个参数（明确切换的目标界面，通用）
	/**
	 * 切换界面
	 */
	private void changeUI(BaseUI ui){
//		1、切换界面时清理上一个显示内容
		//切换界面的核心方法一
		il_middle.removeAllViews();
		//FadeUtil.fadeOut(il_middle.getChildAt(0), 2000);
		View child = ui.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(this, R.anim.ia_view_change));
		//FadeUtil.fadeIn(child, 2000, 1000);
	}
//	4、不使用Handler，任意点击按钮切换
	/**
	 * 切换界面
	 */
	private void changeUI(){
//		1、切换界面时清理上一个显示内容
		//切换界面的核心方法一
		//il_middle.removeAllViews();
		//il_middle.getChildAt(0);
		FadeUtil.fadeOut(il_middle.getChildAt(0), 2000);
		loadSecondUI();
	}
	// 1、切换界面时清理上一个显示内容
		// 2、处理切换动画：简单动画——复杂动画（淡入淡出）
		// 3、切换界面通用处理——增加一个参数（明确切换的目标界面,通用）
		// 4、不使用Handler，任意点击按钮切换

		// a、用户返回键操作捕捉
		// b、响应返回键——切换到历史界面

		//
		// LinkedList<String>——AndroidStack
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			boolean result = MiddleManager.getInstance().goBack();
			//返回键操作失败
			if(!result){
				//Toast.makeText(MainActivity.this, "是否退出系统", 0).show();
				PromptManager.showExitSystem(this);
			}
			return false;//不要向下面操作了
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
