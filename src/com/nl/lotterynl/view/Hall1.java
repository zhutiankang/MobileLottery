package com.nl.lotterynl.view;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.Oelement;
import com.nl.lotterynl.engine.CommonInfoEngine;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Element;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.BeanFactory;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.manager.BaseUI;

public class Hall1 extends BaseUI  {
	// 第一步：加载layout（布局参数设置）
	// 第二步：初始化layout中控件
	// 第三步：设置监听
	private TextView ssqIssue;
	private ImageView ssqBet;
	public Hall1(Context context) {
		super(context);
	}
	public void init() {
		showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall1,
				null);
		ssqIssue = (TextView)findViewById(R.id.ii_hall_ssq_summary);
		ssqBet = (ImageView)findViewById(R.id.ii_hall_ssq_bet);
		//只会调用一次
		/**
		 * 每次进入购彩大厅界面，获取最新的数据——>已进入到某个界面，
		      想去修改界面信息（存储的）——>当进入到某个界面的时候，
		      开启耗费资源的操作，当要离开界面，清理耗费资源的操作
		 */
		//方法一
		// 进入界面做些工作，出去的时候还需要完成一些工作
		// 模拟生命周期方法
		// onResume（当界面被加载了：add（View））
	    // onPause（当界面要被删除：removeAllView）——Activity抄了两个方法
		//方法二
		//倒计时
	}
	@Override
	public void onResume() {
		getCurrentIssueInfo();
	}
	@Override
	public int getID() {
		return ConstantValues.VIEW_HALL;
	}

	public void setListener() {
		ssqBet.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	/**
	 * 获取当前的销售期信息（双色球）
	 */
	private void getCurrentIssueInfo(){
		//new MyAsyncTask().executeProxy(ConstantValues.SSQ);//在execute中检测是否有网络
		//new MyThread().start();在start中检测是否有网络
		new MyAsyncTask<Integer>() {
			//异步，偷偷摸摸的去拿，不需要弹出什么提示框
			@Override
			protected Message doInBackground(Integer... params) {
				//获取数据，业务的调用
				CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);
				return engine.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				//更新界面
				super.onPostExecute(result);
				if(result!=null){
					Oelement oelement = result.getBody().getOelement();
					if(ConstantValues.SUCCESS.equals(oelement.getErrorcode())){
						Element element = result.getBody().getElements().get(0);
						changeNotice(element);
					}else{
						PromptManager.showToast(context,oelement.getErrormsg());
					}
				}else{
					//可能:网络不通，权限，服务器出错，非法数据......
					//如何提示用户
					PromptManager.showToast(context, "服务器繁忙，请稍后重试....");
				}
			}
			
		}.executeProxy(ConstantValues.SSQ);
	}
	/**
	 * 修改界面提示信息
	 * @param element
	 */
	protected void changeNotice(Element element) {
		String text = context.getResources().getString(R.string.is_hall_common_summary);
		CurrentIssueElement currentElement = (CurrentIssueElement) element;
		String issue = currentElement.getIssue();
		String lasttime = getLasttime(currentElement.getLasttime());
		text = StringUtils.replaceEach(text, new String[]{"ISSUE","TIME"}, new String[]{issue,lasttime});
		ssqIssue.setText(text);
	}
	/**
	 * 将秒时间转换成日时分格式
	 * 
	 * @param lasttime
	 * @return
	 */
	public String getLasttime(String lasttime) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNumericSpace(lasttime)) {
			int time = Integer.parseInt(lasttime);
			int day = time / (24 * 60 * 60);
			result.append(day).append("天");
			if (day > 0) {
				time = time - day * 24 * 60 * 60;
			}
			int hour = time / 3600;
			result.append(hour).append("时");
			if (hour > 0) {
				time = time - hour * 60 * 60;
			}
			int minute = time / 60;
			result.append(minute).append("分");
		}
		return result.toString();
	}
	/**异步访问网络的工具
	 * 
	 * AsyncTask异步任务
	 * onPreExecute();1弹出一框，提示用户正在访问网络
	 * doInBackground();2类似run，处理访问网络的耗时操作
	 * onPostExecute();3更新界面
	 * Params:传输的参数
	 * Progress:下载相关的 进度提示（Integer Float）
	 * Result:服务器回复数据的封装
	 */
	/*private class MyAsyncTask extends AsyncTask<Integer, Void, Message>{
		
		@Override
		protected void onPreExecute() {
			//显示滚动条
			super.onPreExecute();
		}
		*//**
		 * 同run方法，在子线程运行
		 * @param params
		 * @return
		 *//*
		@Override
		protected Message doInBackground(Integer... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Message result) {
			//修改界面的提示信息
			super.onPostExecute(result);
		}
		//final不可以重载，还想重载,只能绕过去
		*//**
		 * 类似Thread.start方法
		 * 由于final修饰，无法Override，方法重命名
		 * 省略掉网络判断
		 * @param params
		 * @return
		 *//*
		public final AsyncTask<Integer, Void, Message> executeProxy(Integer... params) {
			if(NetUtil.checkNet(context)){
				return super.execute(params);
				}else{
					PromptManager.showNoNetWork(context);
				}
	        return null;
	    }
	}*/
	/*private class MyThread extends Thread{
		
		@Override
		public void run() {
			// TODO 访问网络获取数据
			super.run();
		}
		@Override
		public synchronized void start() {
			if(NetUtil.checkNet(context)){
			super.start();
			}else{
				PromptManager.showNoNetWork(context);
			}
		}
	}*/
}
