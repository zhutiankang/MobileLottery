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

	// �Ż����֣������ֵĲ��ִ�LinearLayout��ΪListView
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
		 //categoryList.setFadingEdgeLength(0);//ɾ���ڱߣ����£�
		 viewpager = (ViewPager) findViewById(R.id.ii_viewpager);
		 initPagers();
		 pagerAdapter = new MyPagerAdapter();
		 viewpager.setAdapter(pagerAdapter);
		 //��ʼ��ѡ����»���
		 initTabStrip();
		

	}

	private TextView fcTitle;// ����
	private TextView tcTitle;// ���
	private TextView gpcTitle;// ��Ƶ��

	private void initTabStrip() {
		underLine = (ImageView) findViewById(R.id.ii_category_selector);
		fcTitle = (TextView) findViewById(R.id.ii_category_fc);
		tcTitle = (TextView) findViewById(R.id.ii_category_tc);
		gpcTitle = (TextView) findViewById(R.id.ii_category_gpc);
		// ��Ļ�Ŀ��
		fcTitle.setTextColor(Color.RED);
		// СͼƬ�Ŀ��
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.id_category_selector);//ͼƬdrawable������id
		int offset = (GlobalParams.WIN_WIDTH / 3 - bitmap.getWidth()) / 2;
		// ����ͼƬ��ʼλ��--����ƫ��

		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		underLine.setImageMatrix(matrix);

	}

	private void initPagers() {
		pagers = new ArrayList<View>();
		pagers.add(categoryList);
		TextView tv = new TextView(context);
		tv.setText("���");
		pagers.add(tv);

		tv = new TextView(context);
		tv.setText("��Ƶ��");
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

	// ��¼viewpager��һ�������position��Ϣ
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
				// ������ɺ�
				// position:0
				// fromXDelta toXDelta:�����ͼƬ��ʼλ����Ҫ���ӵ���
				TranslateAnimation animation = new TranslateAnimation(
						lastPosition * GlobalParams.WIN_WIDTH / 3, position
								* GlobalParams.WIN_WIDTH / 3, 0, 0);
				animation.setDuration(300);
				animation.setFillAfter(true);// �ƶ����ͣ�����յ�
				underLine.startAnimation(animation);
				lastPosition = position;
				/*
				 * switch (position) {//�����ܽᣬ��Ϊ��̬�����������󻬻������һ� case 1://
				 * ��position��0�ƶ���1 TranslateAnimation animation = new
				 * TranslateAnimation(0*GlobalParams.WIN_WIDTH/3,
				 * 1*GlobalParams.WIN_WIDTH/3, 0, 0);
				 * animation.setDuration(500); animation.setFillAfter(true);//
				 * �ƶ����ͣ�����յ� underLine.startAnimation(animation); break; case
				 * 2:// ��position��1�ƶ���2 animation = new
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
	 * ��ȡ��ǰ����������Ϣ��˫ɫ��
	 */
	private void getCurrentIssueInfo() {
		// new MyAsyncTask().executeProxy(ConstantValues.SSQ);//��execute�м���Ƿ�������
		// new MyThread().start();��start�м���Ƿ�������
		new MyAsyncTask<Integer>() {
			// �첽��͵͵������ȥ�ã�����Ҫ����ʲô��ʾ��
			@Override
			protected Message doInBackground(Integer... params) {
				// ��ȡ���ݣ�ҵ��ĵ���
				CommonInfoEngine engine = BeanFactory
						.getImpl(CommonInfoEngine.class);
				return engine.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				// ���½���
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
					// ����:���粻ͨ��Ȩ�ޣ������������Ƿ�����......
					// �����ʾ�û�
					PromptManager.showToast(context, "��������æ�����Ժ�����....");
				}
			}

		}.executeProxy(ConstantValues.SSQ);
	}

	private String text;

	// private List<View> needUpdate;
	/**
	 * �޸Ľ�����ʾ��Ϣ
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
		// TODO �Ż����½���
		// ��ʽһ��
		// adapter.notifyDataSetChanged();// ���е�item����

		// ��ʽ����������Ҫ�������ݣ�û�б�Ҫˢ�����е���Ϣ��
		// ��ȡ����Ҫ���¿ؼ���Ӧ��
		// TextView view = (TextView) needUpdate.get(0);
		// view.setText(text);

		// ��ʽ��������ά��needUpdate����λ�ȡ��Ҫ���µĿؼ�������
		// �����е�item��ӵ�ListView ���ǲ����з�ʽ���Ի�ȡ��ListView�ĺ���
		// categoryList.findViewById(R.id.ii_hall_lottery_summary);
		// tag The tag to search for, using "tag.equals(getTag())".
		
		ssqBundle = new Bundle();
		ssqBundle.putString("ssqBundle",issue);

		TextView view = (TextView) categoryList.findViewWithTag(0);// tag :Ψһ
		if (view != null) {
			view.setText(text);
		}
	}

	/**
	 * ����ʱ��ת������ʱ�ָ�ʽ
	 * 
	 * @param lasttime
	 * @return
	 */
	public String getLasttime(String lasttime) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNumericSpace(lasttime)) {
			int time = Integer.parseInt(lasttime);
			int day = time / (24 * 60 * 60);
			result.append(day).append("��");
			if (day > 0) {
				time = time - day * 24 * 60 * 60;
			}
			int hour = time / 3600;
			result.append(hour).append("ʱ");
			if (hour > 0) {
				time = time - hour * 60 * 60;
			}
			int minute = time / 60;
			result.append(minute).append("��");
		}
		return result.toString();
	}

	// ��Դ��Ϣ
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
			// ����һ
			// if(StringUtils.isNotBlank(text)&&position==0){
			// holder.summary.setText(text);
			// }
			holder.summary.setTag(position);// ���������棬���ã��Է�����ʱ����
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
