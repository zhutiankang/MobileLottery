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
 * ͨ�ã�ѡ�������õ�adapter(˫ɫ��ĺ���)
 * 
 * @author ׷��
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
		return endNum;// ����һ�� �ͱ����˸�������
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
		// ����Ҫ���ǽ��������Ż�
		// �����Խ���ͼƬ�Ż�����С���ͼƬ��ΪTextView����ͼ������Ҫÿ�ζ���ImageView
		TextView ball = new TextView(context);
		DecimalFormat decimal = new DecimalFormat("00");
		ball.setText(decimal.format(position + 1));
		ball.setTextSize(16);
		// ����
		ball.setGravity(Gravity.CENTER);

		// ��ȡ���û���ѡ����ļ��ϣ��жϼ������У�����ͼƬ�޸�Ϊ��ɫ
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
