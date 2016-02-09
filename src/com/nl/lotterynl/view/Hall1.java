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
	// ��һ��������layout�����ֲ������ã�
	// �ڶ�������ʼ��layout�пؼ�
	// �����������ü���
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
		//ֻ�����һ��
		/**
		 * ÿ�ν��빺�ʴ������棬��ȡ���µ����ݡ���>�ѽ��뵽ĳ�����棬
		      ��ȥ�޸Ľ�����Ϣ���洢�ģ�����>�����뵽ĳ�������ʱ��
		      �����ķ���Դ�Ĳ�������Ҫ�뿪���棬����ķ���Դ�Ĳ���
		 */
		//����һ
		// ���������Щ��������ȥ��ʱ����Ҫ���һЩ����
		// ģ���������ڷ���
		// onResume�������汻�����ˣ�add��View����
	    // onPause��������Ҫ��ɾ����removeAllView������Activity������������
		//������
		//����ʱ
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
	 * ��ȡ��ǰ����������Ϣ��˫ɫ��
	 */
	private void getCurrentIssueInfo(){
		//new MyAsyncTask().executeProxy(ConstantValues.SSQ);//��execute�м���Ƿ�������
		//new MyThread().start();��start�м���Ƿ�������
		new MyAsyncTask<Integer>() {
			//�첽��͵͵������ȥ�ã�����Ҫ����ʲô��ʾ��
			@Override
			protected Message doInBackground(Integer... params) {
				//��ȡ���ݣ�ҵ��ĵ���
				CommonInfoEngine engine = BeanFactory.getImpl(CommonInfoEngine.class);
				return engine.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				//���½���
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
					//����:���粻ͨ��Ȩ�ޣ������������Ƿ�����......
					//�����ʾ�û�
					PromptManager.showToast(context, "��������æ�����Ժ�����....");
				}
			}
			
		}.executeProxy(ConstantValues.SSQ);
	}
	/**
	 * �޸Ľ�����ʾ��Ϣ
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
	/**�첽��������Ĺ���
	 * 
	 * AsyncTask�첽����
	 * onPreExecute();1����һ����ʾ�û����ڷ�������
	 * doInBackground();2����run�������������ĺ�ʱ����
	 * onPostExecute();3���½���
	 * Params:����Ĳ���
	 * Progress:������ص� ������ʾ��Integer Float��
	 * Result:�������ظ����ݵķ�װ
	 */
	/*private class MyAsyncTask extends AsyncTask<Integer, Void, Message>{
		
		@Override
		protected void onPreExecute() {
			//��ʾ������
			super.onPreExecute();
		}
		*//**
		 * ͬrun�����������߳�����
		 * @param params
		 * @return
		 *//*
		@Override
		protected Message doInBackground(Integer... params) {
			return null;
		}
		
		@Override
		protected void onPostExecute(Message result) {
			//�޸Ľ������ʾ��Ϣ
			super.onPostExecute(result);
		}
		//final���������أ���������,ֻ���ƹ�ȥ
		*//**
		 * ����Thread.start����
		 * ����final���Σ��޷�Override������������
		 * ʡ�Ե������ж�
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
			// TODO ���������ȡ����
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
