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
		// 1.��ָ����----��ʾ�Ŵ�ĺ���
		// 2.��ָ����----���£����������+������ʾ��λ��
		// 3.��ָ̧��----�޸���ָ������򱳾�
		pop = new PopupWindow(context);
		View contentView = View.inflate(context, R.layout.il_gridview_item_pop,
				null);
		ball = (TextView) contentView.findViewById(R.id.ii_pretextView);
		pop.setContentView(contentView);
		pop.setBackgroundDrawable(null);// ������Ϊ��
		// pop.setAnimationStyle(0);����ҪĬ�϶���
		// ����pop�Ĵ�С��Ϊ����ʾ
		pop.setWidth(DensityUtil.dip2px(context, 55));// dip--ת������Ӧ��Ļ��px
		pop.setHeight(DensityUtil.dip2px(context, 53));
	}
	// 3.
	private OnActionUpListener onActionUpListener;

	public void setOnActionUpListener(OnActionUpListener onActionUpListener) {
		this.onActionUpListener = onActionUpListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// ����ָ���µ�ʱ�򣬻�ȡ������Ǹ���
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		int position = pointToPosition(x, y);
		if (position == INVALID_POSITION) {// �Ƿ�λ��
			return false;
		}
		TextView child = (TextView) this.getChildAt(position);// ȷ��������ʾλ��
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//����ֻ��ˮƽ��������Ҫ��ֱ����������ָ���µ�ʱ�򣬽ӹ�ScrollView����
			this.getParent().getParent().requestDisallowInterceptTouchEvent(true);
			showPop(child);
			break;
		case MotionEvent.ACTION_MOVE:
			updatePop(child);
			break;
		case MotionEvent.ACTION_UP:
			//����ָ�뿪��ʱ�򣬷��У�ScrollView�Լ�����
			this.getParent().getParent().requestDisallowInterceptTouchEvent(false);
			hiddenPop();
			// ����һ������
			if (onActionUpListener != null) {// �����ָ���쳣
				onActionUpListener.onActionUp(child, position);
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ��ʾ����
	 * 
	 * @param child
	 *            :��ͷ����ʾ
	 */
	private void showPop(TextView child) {
		// ƫ����,ȷ��λ��
		int yOffset = -(pop.getHeight() + child.getHeight());
		int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
		ball.setText(child.getText());// ȷ����������
		// pop.showAsDropDown(child);
		pop.showAsDropDown(child, xOffset, yOffset);
	}

	private void updatePop(TextView child) {
		int yOffset = -(pop.getHeight() + child.getHeight());
		int xOffset = -(pop.getWidth() - child.getWidth()) / 2;
		ball.setText(child.getText());// ȷ����������
		pop.update(child, xOffset, yOffset, -1, -1);
	}

	private void hiddenPop() {
		pop.dismiss();
	}

	/**
	 * �����û���ָ̧��
	 * 
	 */
	public interface OnActionUpListener {
		void onActionUp(View view, int position);
	}
}
