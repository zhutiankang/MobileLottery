package com.nl.lotterynl.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.domain.Oelement;
import com.nl.lotterynl.engine.CommonInfoEngine;
import com.nl.lotterynl.net.element.CurrentIssueElement;
import com.nl.lotterynl.net.protocal.Element;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.BeanFactory;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.MiddleManager;

public class Hall extends BaseUI {

	// 优化布局，将彩种的布局从LinearLayout改为ListView
	private ListView categoryList;
	private BaseAdapter adapter;

	private ViewPager viewpager;
	private List<View> pagers;
	private PagerAdapter pagerAdapter;

	private ImageView underLine;

	public Hall(Context context) {
		super(context);
	}

	public void init() {
		showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall,
				null);
		 categoryList = new ListView(context);
		 adapter = new CategoryAdapter();
		 categoryList.setAdapter(adapter);
		 //categoryList.setFadingEdgeLength(0);//删除黑边（上下）
		 viewpager = (ViewPager) findViewById(R.id.ii_viewpager);
		 initPagers();
		 pagerAdapter = new MyPagerAdapter();
		 viewpager.setAdapter(pagerAdapter);
		 //初始化选项卡的下划线
		 initTabStrip();
		

	}

	private TextView fcTitle;// 福彩
	private TextView tcTitle;// 体彩
	private TextView gpcTitle;// 高频彩

	private void initTabStrip() {
		underLine = (ImageView) findViewById(R.id.ii_category_selector);
		fcTitle = (TextView) findViewById(R.id.ii_category_fc);
		tcTitle = (TextView) findViewById(R.id.ii_category_tc);
		gpcTitle = (TextView) findViewById(R.id.ii_category_gpc);
		// 屏幕的宽度
		fcTitle.setTextColor(Color.RED);
		// 小图片的宽度
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.id_category_selector);//图片drawable而不是id
		int offset = (GlobalParams.WIN_WIDTH / 3 - bitmap.getWidth()) / 2;
		// 设置图片初始位置--向右偏移

		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		underLine.setImageMatrix(matrix);

	}

	private void initPagers() {
		pagers = new ArrayList<View>();
		pagers.add(categoryList);
		TextView tv = new TextView(context);
		tv.setText("体彩");
		pagers.add(tv);

		tv = new TextView(context);
		tv.setText("高频彩");
		pagers.add(tv);
	}

	@Override
	public void onResume() {
		getCurrentIssueInfo();
	}

	@Override
	public int getID() {
		return ConstantValues.VIEW_HALL;
	}

	// 记录viewpager上一个界面的position信息
	private int lastPosition = 0;

	public void setListener() {

		fcTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(0);
			}
		});

		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 滑动完成后
				// position:0
				// fromXDelta toXDelta:相对于图片初始位置需要增加的量
				TranslateAnimation animation = new TranslateAnimation(
						lastPosition * GlobalParams.WIN_WIDTH / 3, position
								* GlobalParams.WIN_WIDTH / 3, 0, 0);
				animation.setDuration(300);
				animation.setFillAfter(true);// 移动完后停留到终点
				underLine.startAnimation(animation);
				lastPosition = position;
				/*
				 * switch (position) {//规律总结，变为动态，不仅可以左滑还可以右滑 case 1://
				 * 当position从0移动到1 TranslateAnimation animation = new
				 * TranslateAnimation(0*GlobalParams.WIN_WIDTH/3,
				 * 1*GlobalParams.WIN_WIDTH/3, 0, 0);
				 * animation.setDuration(500); animation.setFillAfter(true);//
				 * 移动完后停留到终点 underLine.startAnimation(animation); break; case
				 * 2:// 当position从1移动到2 animation = new
				 * TranslateAnimation(1*GlobalParams.WIN_WIDTH/3,
				 * 2*GlobalParams.WIN_WIDTH/3, 0, 0);
				 * animation.setDuration(500); animation.setFillAfter(true);
				 * underLine.startAnimation(animation); break; }
				 */
				fcTitle.setTextColor(Color.BLACK);
				tcTitle.setTextColor(Color.BLACK);
				gpcTitle.setTextColor(Color.BLACK);
				switch (position) {
				case 0:
					fcTitle.setTextColor(Color.RED);
					break;
				case 1:
					tcTitle.setTextColor(Color.RED);
					break;
				case 2:
					gpcTitle.setTextColor(Color.RED);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	/**
	 * 获取当前的销售期信息（双色球）
	 */
	private void getCurrentIssueInfo() {
		// new MyAsyncTask().executeProxy(ConstantValues.SSQ);//在execute中检测是否有网络
		// new MyThread().start();在start中检测是否有网络
		new MyAsyncTask<Integer>() {
			// 异步，偷偷摸摸的去拿，不需要弹出什么提示框
			@Override
			protected Message doInBackground(Integer... params) {
				// 获取数据，业务的调用
				CommonInfoEngine engine = BeanFactory
						.getImpl(CommonInfoEngine.class);
				return engine.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				// 更新界面
				super.onPostExecute(result);
				if (result != null) {
					Oelement oelement = result.getBody().getOelement();
					if (ConstantValues.SUCCESS.equals(oelement.getErrorcode())) {
						Element element = result.getBody().getElements().get(0);
						changeNotice(element);
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

	private String text;

	// private List<View> needUpdate;
	/**
	 * 修改界面提示信息
	 * 
	 * @param element
	 */
	protected void changeNotice(Element element) {
		text = context.getResources()
				.getString(R.string.is_hall_common_summary);
		CurrentIssueElement currentElement = (CurrentIssueElement) element;
		String issue = currentElement.getIssue();
		String lasttime = getLasttime(currentElement.getLasttime());
		text = StringUtils.replaceEach(text, new String[] { "ISSUE", "TIME" },
				new String[] { issue, lasttime });
		// TODO 优化更新界面
		// 方式一：
		// adapter.notifyDataSetChanged();// 所有的item更新

		// 方式二：更新需要更新内容（没有必要刷新所有的信息）
		// 获取到需要更新控件的应用
		// TextView view = (TextView) needUpdate.get(0);
		// view.setText(text);

		// 方式三：不想维护needUpdate，如何获取需要更新的控件的引用
		// 将所有的item添加到ListView ，是不是有方式可以获取到ListView的孩子
		// categoryList.findViewById(R.id.ii_hall_lottery_summary);
		// tag The tag to search for, using "tag.equals(getTag())".
		
		ssqBundle = new Bundle();
		ssqBundle.putString("ssqBundle",issue);

		TextView view = (TextView) categoryList.findViewWithTag(0);// tag :唯一
		if (view != null) {
			view.setText(text);
		}
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

	// 资源信息
	private int[] logoResIds = new int[] { R.drawable.id_ssq, R.drawable.id_3d,
			R.drawable.id_qlc };
	private int[] titleResIds = new int[] { R.string.is_hall_ssq_title,
			R.string.is_hall_3d_title, R.string.is_hall_qlc_title };

	private class CategoryAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 3;
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
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(context,
						R.layout.il_hall_lottery_item, null);
				holder = new ViewHolder();
				holder.logo = (ImageView) convertView
						.findViewById(R.id.ii_hall_lottery_logo);
				holder.title = (TextView) convertView
						.findViewById(R.id.ii_hall_lottery_title);
				holder.summary = (TextView) convertView
						.findViewById(R.id.ii_hall_lottery_summary);
				// needUpdate.add(holder.summary);
				holder.bet = (ImageView) convertView
						.findViewById(R.id.ii_hall_lottery_bet);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.logo.setImageResource(logoResIds[position]);
			holder.title.setText(titleResIds[position]);
			// 方法一
			// if(StringUtils.isNotBlank(text)&&position==0){
			// holder.summary.setText(text);
			// }
			holder.summary.setTag(position);// 必须在外面，设置，以防重用时出错
			holder.bet.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MiddleManager.getInstance().changeUI(PlaySSQ.class,ssqBundle);
				}
			});
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView logo;
		TextView title;
		TextView summary;
		ImageView bet;
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(pagers.get(position));
			object = null;
		}

		@Override
		public int getCount() {
			return pagers.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = pagers.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
