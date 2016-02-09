package com.nl.lotterynl.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**
 * ���뵭�����л�
 * 
 * @author ׷��
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
	// ��ǰ����ĵ�����������ִ��ʱ��
	// �����ִ�й����У��ڶ������洦�ڵȴ�״̬
	// �ڶ������浭����������ִ��ʱ��
	/**
	 * ����
	 * 
	 * @param view
	 *            ִ�ж����Ľ���
	 * @param duration
	 *            ִ�е�ʱ��
	 */
	public static void fadeOut(final View view, long duration) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(duration);
		//����ִ�����֮����ɾ��view�Ĳ���
		//���Ӷ���ִ�����֮��ļ���
		alphaAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//ֱ���õĻ�          2.3ģ�������쳣��4.0���������������handler�ƹ�ȥ
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
	 * ����
	 * 
	 * @param view
	 *            ִ�ж����Ľ���
	 * @param delay
	 *            �ȴ�ʱ�䣨�뵭���Ľ���ִ�ж�����ʱ����ͬ��
	 * @param duration
	 *            ִ�е�ʱ��
	 */
	public static void fadeIn(View view, long delay, long duration) {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		//������ʱʱ��
		alphaAnimation.setStartOffset(delay);
		alphaAnimation.setDuration(duration);
		view.setAnimation(alphaAnimation);
	}
}
