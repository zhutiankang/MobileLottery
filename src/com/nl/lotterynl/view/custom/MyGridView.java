package com.nl.lotterynl.view.custom;

import com.nl.lotterynl.R;
import com.nl.lotterynl.util.DensityUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyGridView extends GridView {
	private PopupWindow pop;
	private TextView ball;

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 1.手指按下----显示放大的号码
		// 2.手指滑动----更新：号码的内容+号码显示的位置
		// 3.手指抬起----修改手指下面的球背景
		pop = new PopupWindow(context);
		View contentView = View.inflate(context, R.layout.il_gridview_item_pop,
				null);
		ball = (TextView) contentView.findViewById(R.id.ii_pretextView);
		pop.setContentView(contentView);
		pop.setBackgroundDrawable(null);// 背景设为空
		// pop.setAnimationStyle(0);不想要默认动画
		// 设置pop的大小，为了显示
		pop.setWidth(DensityUtil.dip2px(context, 55));// dip--转换成适应屏幕的px
		pop.setHeight(DensityUtil.dip2px(context, 53));
	}
	// 3.
	private OnActionUpListener onActionUpListener;

	public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
		this.onActionUpListener = onActionUpListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 当手指按下的时候，获取到点击那个球
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		int position = pointToPosition(x, y);
		if (position == INVALID_POSITION) {// 非法位置
			return false;
		}
		TextView child = (TextView) this.getChildAt(position);// 确定号码显示位置
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//这样只能水平滑动，想要竖直滑动，当手指按下的时候，接管ScrollView滑动
			this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
			showPop(child);
			break;
		case MotionEvent.ACTION_MOVE:
			updatePop(child);
			break;
		case MotionEvent.ACTION_UP:
			//当手指离开的时候，放行，ScrollView自己滑动
			this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
			hiddenPop();
			// 增加一个监听
			if (onActionUpListener != null) {// 避免空指针异常
				onActionUpListener.onActionUp(child, position);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 显示泡泡
	 * 
	 * @param child
	 *            :在头上显示
	 */
	private void showPop(TextView child) {
		// 偏移量,确定位置
		int yOffset = -(pop.getHeight() + child.getHeight());
		int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
		ball.setText(child.getText());// 确定号码内容
		// pop.showAsDropDown(child);
		pop.showAsDropDown(child, xOffset, yOffset);
	}

	private void updatePop(TextView child) {
		int yOffset = -(pop.getHeight() + child.getHeight());
		int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
		ball.setText(child.getText());// 确定号码内容
		pop.update(child, xOffset, yOffset, -1, -1);
	}

	private void hiddenPop() {
		pop.dismiss();
	}

	/**
	 * 监听用户手指抬起
	 * 
	 */
	public interface OnActionUpListener {
		void onActionUp(View view, int position);
	}
}
