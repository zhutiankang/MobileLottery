package com.nl.lotterynl.view;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class Hall2 extends BaseUI {

	// �Ż����֣������ֵĲ��ִ�LinearLayout��ΪListView
	private ListView categoryList;
	private BaseAdapter adapter;

	public Hall2(Context context) {
		super(context);
	}

	public void init() {
		showInMiddle = (LinearLayout) View.inflate(context, R.layout.il_hall2,
				null);
		categoryList = (ListView) findViewById(R.id.ii_hall_lottery_list);

		adapter = new CategoryAdapter();
		categoryList.setAdapter(adapter);
		//needUpdate = new ArrayList<View>();
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
	//private List<View> needUpdate;
	/**
	 * �޸Ľ�����ʾ��Ϣ
	 * 
	 * @param element
	 */
	protected void changeNotice(Element element) {
		text = context.getResources().getString(
				R.string.is_hall_common_summary);
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
		
		TextView view = (TextView) categoryList.findViewWithTag(0);// tag :Ψһ
		if(view!=null){
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
				//needUpdate.add(holder.summary);
				holder.bet = (ImageView) convertView
						.findViewById(R.id.ii_hall_lottery_bet);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.logo.setImageResource(logoResIds[position]);
			holder.title.setText(titleResIds[position]);
			//����һ
//			if(StringUtils.isNotBlank(text)&&position==0){
//				holder.summary.setText(text);
//			}
			holder.summary.setTag(position);//���������棬���ã��Է�����ʱ����
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView logo;
		TextView title;
		TextView summary;
		ImageView bet;
	}
}
