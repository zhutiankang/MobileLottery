package com.nl.lotterynl.view.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.nl.lotterynl.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 通用，选好容器用的adapter(双色球的红球)
 * 
 * @author 追梦
 * 
 */
public class PoolAdapter extends BaseAdapter {
	private Context context;
	private int endNum;

	private List<Integer> selectedList;

	public void setSelectedList(List<Integer> selectedList) {
		this.selectedList = selectedList;
	}

	public PoolAdapter(Context context, int endNum) {
		super();
		this.context = context;
		this.endNum = endNum;
	}

	@Override
	public int getCount() {
		return endNum;// 个数一变 就别忘了更改这里
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 不需要考虑进行重用优化
		// 但可以进行图片优化，将小球的图片作为TextView背景图，不需要每次都画ImageView
		TextView ball = new TextView(context);
		DecimalFormat decimal = new DecimalFormat("00");
		ball.setText(decimal.format(position + 1));
		ball.setTextSize(16);
		// 居中
		ball.setGravity(Gravity.CENTER);

		// 获取到用户已选号码的集合，判断集合中有，背景图片修改为红色
		if (selectedList != null && selectedList.contains(position)) {
			if (selectedList.size() > 1) {
				ball.setBackgroundResource(R.drawable.id_redball);
			} else {
				ball.setBackgroundResource(R.drawable.id_blueball);
			}
		} else {
			ball.setBackgroundResource(R.drawable.id_defalut_ball);
		}
		return ball;
	}

}
