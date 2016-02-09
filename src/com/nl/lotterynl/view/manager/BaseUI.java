package com.nl.lotterynl.view.manager;

import com.nl.lotterynl.net.NetUtil;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.PromptManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;

public abstract class BaseUI implements OnClickListener {
	protected Context context;
	/**
	 * ��ʾ���м�����
	 */
	protected ViewGroup showInMiddle;
	//�����л�����֮�䣬�������ݿ�����bundle������1�������ݸ�������--ͨ���������л�������2��ͨ����ͬ������������--����2��ȡ����
	protected Bundle ssqBundle;
	public BaseUI(Context context) {
		this.context = context;
		init();
		setListener();
	}
	
	public void setSsqBundle(Bundle ssqBundle) {
		this.ssqBundle = ssqBundle;
	}

	/**
	 * ����ĳ�ʼ��
	 */
	public abstract void init();

	/**
	 * ���ü���
	 */
	public abstract void setListener();

	/**
	 * ��ȡ��Ҫ���м��������صĿؼ�
	 * 
	 * @return
	 */
	public View getChild() {
		// ����layout����
		// root=null
		// showInMiddle.getLayoutParams()=null;
		// root!=null
		// return root;
		LayoutParams params = showInMiddle.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			showInMiddle.setLayoutParams(params);
		}
		return showInMiddle;
	}

	/**
	 * ��ȡ��Ҫ���м��������صĿؼ�ID
	 * 
	 * @return
	 */
	public abstract int getID();

	@Override
	public void onClick(View v) {
	}

	public View findViewById(int id) {
		return showInMiddle.findViewById(id);
	}

	public abstract class MyAsyncTask<Params> extends
			AsyncTask<Params, Void, Message> {

		// final���������أ���������,ֻ���ƹ�ȥ
		/**
		 * ����Thread.start���� 
		 * ����final���Σ��޷�Override������������ 
		 * ʡ�Ե������ж�
		 * 
		 * @param params
		 * @return
		 */
		public final AsyncTask<Params, Void, Message> executeProxy(
				Params... params) {
			if (NetUtil.checkNet(context)) {
				return super.execute(params);
			} else {
				PromptManager.showNoNetWork(context);
			}
			return null;
		}

	}
	/**
	 * Ҫ��ȥ��ʱ�����
	 */
	public void onPause() {
		
	}
	/**
	 * ���뵽����֮��
	 */
	public void onResume() {
		
	}
}
