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
	 * 显示到中间容器
	 */
	protected ViewGroup showInMiddle;
	//两个切换界面之间，传递数据可以用bundle，界面1传递数据给控制器--通过控制器切换到界面2并通过共同父类设置数据--界面2获取数据
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
	 * 界面的初始化
	 */
	public abstract void init();

	/**
	 * 设置监听
	 */
	public abstract void setListener();

	/**
	 * 获取需要在中间容器加载的控件
	 * 
	 * @return
	 */
	public View getChild() {
		// 设置layout参数
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
	 * 获取需要在中间容器加载的控件ID
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

		// final不可以重载，还想重载,只能绕过去
		/**
		 * 类似Thread.start方法 
		 * 由于final修饰，无法Override，方法重命名 
		 * 省略掉网络判断
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
	 * 要出去的时候调用
	 */
	public void onPause() {
		
	}
	/**
	 * 进入到界面之后
	 */
	public void onResume() {
		
	}
}
