package com.nl.lotterynl.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * 淡入淡出的切换
 * 
 * @author 追梦
 * 
 */
public class FadeUtil {
	
	private static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			View view = (View) msg.obj;
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
	};
	// 当前界面的淡出，动画的执行时间
	// 在这个执行过程中，第二个界面处于等待状态
	// 第二个界面淡出，动画的执行时间
	/**
	 * 淡出
	 * 
	 * @param view
	 *            执行动画的界面
	 * @param duration
	 *            执行的时间
	 */
	public static void fadeOut(final View view, long duration) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(duration);
		//动画执行完成之后，做删除view的操作
		//增加动画执行完成之后的监听
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//直接用的话          2.3模拟器抛异常，4.0正常，避免出错，用handler绕过去
//				ViewGroup parent = (ViewGroup) view.getParent();
//				parent.removeView(view);
				Message msg = Message.obtain();
				msg.obj = view;
				handler.sendMessage(msg);
			}
		});
		view.setAnimation(alphaAnimation);
	}

	/**
	 * 淡入
	 * 
	 * @param view
	 *            执行动画的界面
	 * @param delay
	 *            等待时间（与淡出的界面执行动画的时间相同）
	 * @param duration
	 *            执行的时间
	 */
	public static void fadeIn(View view, long delay, long duration) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		//设置延时时间
		alphaAnimation.setStartOffset(delay);
		alphaAnimation.setDuration(duration);
		view.setAnimation(alphaAnimation);
	}
}
