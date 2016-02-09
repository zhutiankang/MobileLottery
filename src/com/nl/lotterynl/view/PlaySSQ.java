package com.nl.lotterynl.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.Oelement;
import com.nl.lotterynl.domain.ShoppingCart;
import com.nl.lotterynl.domain.Ticket;
import com.nl.lotterynl.engine.CommonInfoEngine;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.BeanFactory;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.adapter.PoolAdapter;
import com.nl.lotterynl.view.custom.MyGridView;
import com.nl.lotterynl.view.custom.MyGridView.OnActionUpListener;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.BottomManager;
import com.nl.lotterynl.view.manager.MiddleManager;
import com.nl.lotterynl.view.manager.PlayGame;
import com.nl.lotterynl.view.manager.TitleManager;
import com.nl.lotterynl.view.sensor.ShakeListener;

/**
 * 双色球选号界面
 * 
 * @author 追梦
 * 
 */
public class PlaySSQ extends BaseUI implements PlayGame {
	// 通用三步 加载linearLayout findviewbyId 设置监听

	// ①标题
	// 判断购彩大厅是否获取到期次信息
	// 如果获取到：拼装标题
	// 否则默认的标题展示

	// ②填充选号容器
	// ③选号：单击+机选红篮球
	// 机选红蓝球：一注的要求
	// 红：6+蓝：1
	// ④手机摇晃处理
	// 加速度传感器：
	// 方式一：任意一个轴的加速度值在单位时间内（1秒），变动的速率达到设置好的阈值
	// 方式二：获取三个轴的加速度值，记录，当过一段时间之后再次获取三个轴的加速度值，计算增量，将相邻两个点的增量进行汇总，当达到设置好的阈值

	// ①记录第一个数据：三个轴的加速度，为了屏蔽掉不同手机采样的时间间隔，记录第一个点的时间
	// ②当有新的传感器数据传递后，判断时间间隔（用当前时间与第一个采样时间进行比对，如果满足了时间间隔要求，认为是合格的第二个点，否则舍弃该数据包）
	// 进行增量的计算：获取到新的加速度值与第一个点上存储的进行差值运算，获取到一点和二点之间的增量
	// ③以此类推，获取到相邻两个点的增量，一次汇总
	// ④通过汇总值与设定好的阈值比对，如果大于等于，用户摇晃手机，否则继续记录当前的数据（加速度值和时间）
	// ⑤提示信息+清空+选好了
	// 机选
	private Button randomRed;
	private Button randomBlue;
	// 选号容器
	private MyGridView redContainer;
	private GridView blueContainer;

	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;

	// 选中球的列表
	private List<Integer> redList;
	private List<Integer> blueList;
	// 加速度传感器：
	private SensorManager manager;
	private ShakeListener listener;

	public PlaySSQ(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq,
				null);
		redContainer = (MyGridView) findViewById(R.id.ii_ssq_red_number_container);
		blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
		randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
		randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);
		redList = new ArrayList<Integer>();
		blueList = new ArrayList<Integer>();
		redAdapter = new PoolAdapter(context, 33);
		blueAdapter = new PoolAdapter(context, 16);
		redContainer.setAdapter(redAdapter);
		blueContainer.setAdapter(blueAdapter);

		manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
	}

	@Override
	public void setListener() {
		randomRed.setOnClickListener(this);
		randomBlue.setOnClickListener(this);
		/*
		 * redContainer.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { //判断当前点击的item是否被选中了
		 * if(!redList.contains(position)){ //如果没有被选中 //背景图片切换到红色
		 * view.setBackgroundResource(R.drawable.id_redball); //摇晃的动画
		 * view.startAnimation(AnimationUtils.loadAnimation(context,
		 * R.anim.ia_ball_shake)); redList.add(position); }else{ //如果被选中
		 * //还原背景图片 view.setBackgroundResource(R.drawable.id_defalut_ball);
		 * redList.remove((Object)position); } } });
		 */
		redContainer.setOnActionUpListener(new OnActionUpListener() {

			@Override
			public void onActionUp(View view, int position) {
				// 判断当前点击的item是否被选中了
				if (!redList.contains(position)) {
					// 如果没有被选中
					// 背景图片切换到红色
					view.setBackgroundResource(R.drawable.id_redball);
					// 摇晃的动画
					// view.startAnimation(AnimationUtils.loadAnimation(context,
					// R.anim.ia_ball_shake));
					redList.add(position);
				} else {
					// 如果被选中,点一下，由红色变为灰色
					// 还原背景图片
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					redList.remove((Object) position);
				}
				changeNotice();
			}
		});
		// 命名有规则的话，可以直接复制代码，然后替换里面的名字，注意一定要是复制部分
		blueContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 判断当前点击的item是否被选中了
				if (!blueList.contains(position)) {
					// 如果没有被选中
					// 背景图片切换到红色
					view.setBackgroundResource(R.drawable.id_blueball);
					// 摇晃的动画
					view.startAnimation(AnimationUtils.loadAnimation(context,
							R.anim.ia_ball_shake));
					blueList.add(position);
				} else {
					// 如果被选中
					// 还原背景图片
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					blueList.remove((Object) position);
				}
				changeNotice();
			}
		});

	}

	@Override
	public void onClick(View v) {
		Random random = new Random();
		switch (v.getId()) {
		case R.id.ii_ssq_random_red:
			// 机选红球
			redList.clear();
			while (redList.size() < 6) {// 循环6次,产生6个球，开始为0
				int num = random.nextInt(33);// 33个球 下标0-32
				if (redList.contains(num)) {
					continue;
				}
				redList.add(num);
			}
			redAdapter.setSelectedList(redList);
			redAdapter.notifyDataSetChanged();
			changeNotice();
			break;
		case R.id.ii_ssq_random_blue:
			// 机选蓝球
			blueList.clear();
			int num = random.nextInt(16);// 16个球 下标0-15
			blueList.add(num);
			blueAdapter.setSelectedList(blueList);
			blueAdapter.notifyDataSetChanged();
			changeNotice();
			break;
		}
	}

	@Override
	public int getID() {
		return ConstantValues.VIEW_SSQ;
	}

	@Override
	public void onResume() {
		changeTitle();
		changeNotice();
		clear();
		// 注册
		listener = new ShakeListener(context) {
			@Override
			public void randomCure() {
				randomSSQ();
			}
		};
		manager.registerListener(listener,
				manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_FASTEST);
		super.onResume();
	}

	/**
	 * 机选一注
	 */
	private void randomSSQ() {
		Random random = new Random();
		// 机选红球
		redList.clear();
		while (redList.size() < 6) {// 循环6次,产生6个球，开始为0
			int num = random.nextInt(33);// 33个球 下标0-32
			if (redList.contains(num)) {
				continue;
			}
			redList.add(num);
		}
		redAdapter.setSelectedList(redList);
		redAdapter.notifyDataSetChanged();

		// 机选蓝球
		blueList.clear();
		int num = random.nextInt(16);// 16个球 下标0-15
		blueList.add(num);
		blueAdapter.setSelectedList(blueList);
		blueAdapter.notifyDataSetChanged();
		changeNotice();
	}

	@Override
	public void onPause() {
		// 注销
		manager.unregisterListener(listener);
		listener = null;
		super.onPause();
	}

	private void changeTitle() {
		// ①标题——界面之间的数据传递(Bundle)
		// 判断购彩大厅是否获取到期次信息
		String titleInfo = "";
		if (ssqBundle != null) {
			titleInfo = "双色球第" + ssqBundle.getString("ssqBundle") + "期";
		} else {
			titleInfo = "双色球选号";
		}
		TitleManager.getInstance().changeTitle(titleInfo);
	}

	/**
	 * 改变底部导航的提示信息
	 */
	private void changeNotice() {
		String notice = "";
		// 以一注为分割
		if (redList.size() < 6) {
			notice = "还需要选择" + (6 - redList.size()) + "注";
		} else if (blueList.size() == 0) {
			notice = "您还需要选择" + 1 + "个蓝球";
		} else {
			notice = "共 " + calc() + " 注 " + calc() * 2 + " 元";
		}
		BottomManager.getInstance().changeBottomNotice(notice);
	}

	/**
	 * 计算双色球的注数
	 * 
	 * @return
	 */
	private int calc() {
		int redC = (int) (factorial(redList.size()) / (factorial(6) * factorial(redList
				.size() - 6)));
		int blueC = blueList.size();
		return redC * blueC;
	}

	/**
	 * 计算一个数的阶乘
	 * 
	 * @param num
	 * @return
	 */
	private long factorial(int num) {
		// num=7 7*6*5...*1
		if (num > 1) {
			return num * factorial(num - 1);
		} else if (num == 1 || num == 0) {
			return 1;
		} else {
			throw new IllegalArgumentException("num >= 0");
		}
	}

	/**
	 * 清空
	 */
	public void clear() {
		redList.clear();
		blueList.clear();
		changeNotice();
		redAdapter.notifyDataSetChanged();
		blueAdapter.notifyDataSetChanged();
	}

	@Override
	public void done() {
		// ①判断：用户是否选择了一注投注
		if (redList.size() >= 6 && blueList.size() >= 1) {
			// 一个购物车中，只能放置一个彩种，当前期的投注信息
			// ②判断：是否获取到了当前销售期的信息
			if (ssqBundle != null) {
				// ③封装用户的投注信息：红球、蓝球、注数
				Ticket ticket = new Ticket();
				StringBuffer redBuffer = new StringBuffer();
				DecimalFormat decimal = new DecimalFormat("00");
				for(Integer item : redList){//存取的是位置，比实际的值小1
					Integer value = item + 1;
					redBuffer.append(" ").append(decimal.format(value));
				}
				ticket.setRedList(redBuffer.substring(1));//第一个元素前的空格去掉
				
				StringBuffer blueBuffer = new StringBuffer();
				for(Integer item : blueList){//存取的是位置，比实际的值小1
					Integer value = item + 1;
					blueBuffer.append(" ").append(decimal.format(value));
				}
				ticket.setBlueList(blueBuffer.substring(1));//第一个元素前的空格去掉
				ticket.setNum(calc());
				// ④创建彩票购物车，将投注信息添加到购物车中
				ShoppingCart.getInstance().getTickets().add(ticket);
				// ⑤设置彩种的标示，设置彩种期次
				ShoppingCart.getInstance().setIssue(ssqBundle.getString("ssqBundle"));
				ShoppingCart.getInstance().setLotteryid(ConstantValues.SSQ);
				// ⑥界面跳转——购物车展示
				MiddleManager.getInstance().changeUI(Shopping.class, ssqBundle);
			} else {
				// 重新获取期次信息
				getCurrentIssueInfo();
			}
		} else {
			// 分支
			// 提示:需要选择一注
			PromptManager.showToast(context, "需要选择一注");
			// 重新获取期次信息
		}
	}
	
	/**
	 * 重新获取当前的销售期信息（双色球）
	 */
	private void getCurrentIssueInfo() {
		// new MyAsyncTask().executeProxy(ConstantValues.SSQ);//在execute中检测是否有网络
		// new MyThread().start();在start中检测是否有网络
		new MyAsyncTask<Integer>() {
			// 异步，偷偷摸摸的去拿，不需要弹出什么提示框
			protected void onPreExecute() {
				//显示滚动条
				PromptManager.showProgressDialog(context);
			};
			@Override
			protected Message doInBackground(Integer... params) {
				// 获取数据，业务的调用
				CommonInfoEngine engine = BeanFactory
						.getImpl(CommonInfoEngine.class);
				return engine.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				PromptManager.closeProgressDialog();
				// 更新界面
				super.onPostExecute(result);
				if (result != null) {
					Oelement oelement = result.getBody().getOelement();
					if (ConstantValues.SUCCESS.equals(oelement.getErrorcode())) {
						CurrentIssueElement element = (CurrentIssueElement)result.getBody().getElements().get(0);
						String issue = element.getIssue();
						//创建ssqbundle
						ssqBundle = new Bundle();
						ssqBundle.putString("ssqBundle",issue);
						changeTitle();
					} else {
						PromptManager
								.showToast(context, oelement.getErrormsg());
					}
				} else {
					// 可能:网络不通，权限，服务器出错，非法数据......
					// 如何提示用户
					PromptManager.showToast(context, "服务器繁忙，请稍后重试....");
				}
			}

		}.executeProxy(ConstantValues.SSQ);
	}


}
