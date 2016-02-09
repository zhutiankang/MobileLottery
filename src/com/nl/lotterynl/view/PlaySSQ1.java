package com.nl.lotterynl.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.view.adapter.PoolAdapter;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.TitleManager;

/**
 * 双色球选号界面
 * 
 * @author 追梦
 * 
 */
public class PlaySSQ1 extends BaseUI {
	// 通用三步 加载linearLayout findviewbyId 设置监听

	// ①标题
	// 判断购彩大厅是否获取到期次信息
	// 如果获取到：拼装标题
	// 否则默认的标题展示

	// ②填充选号容器
	// ③选号：单机+机选红篮球
	// ④手机摇晃处理
	// ⑤提示信息+清空+选好了
	// 机选
	private Button randomRed;
	private Button randomBlue;
	// 选号容器
	private GridView redContainer;
	private GridView blueContainer;

	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;
	
	//选中球的列表
	private List<Integer> redList;
	private List<Integer> blueList;
	public PlaySSQ1(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq,
				null);
		redContainer = (GridView) findViewById(R.id.ii_ssq_red_number_container);
		blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
		randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
		randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);
		redList = new ArrayList<Integer>();
		blueList = new ArrayList<Integer>();
		redAdapter = new PoolAdapter(context,33);
		blueAdapter = new PoolAdapter(context,16);
		redContainer.setAdapter(redAdapter);
		blueContainer.setAdapter(blueAdapter);
		
	}

	@Override
	public void setListener() {
		randomRed.setOnClickListener(this);
		randomBlue.setOnClickListener(this);
		redContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//判断当前点击的item是否被选中了
				if(!redList.contains(position)){
				//如果没有被选中
				//背景图片切换到红色
				view.setBackgroundResource(R.drawable.id_redball);
				//摇晃的动画
				view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
				redList.add(position);
				}else{
				//如果被选中
				//还原背景图片
				view.setBackgroundResource(R.drawable.id_defalut_ball);
				redList.remove((Object)position);
				}
			}
		});
		//命名有规则的话，可以直接复制代码，然后替换里面的名字，注意一定要是复制部分
		blueContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//判断当前点击的item是否被选中了
				if(!blueList.contains(position)){
				//如果没有被选中
				//背景图片切换到红色
				view.setBackgroundResource(R.drawable.id_blueball);
				//摇晃的动画
				view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
				blueList.add(position);
				}else{
				//如果被选中
				//还原背景图片
				view.setBackgroundResource(R.drawable.id_defalut_ball);
				blueList.remove((Object)position);
				}
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_ssq_random_red:
			
			break;
		case R.id.ii_ssq_random_blue:
			
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
		super.onResume();
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
}
