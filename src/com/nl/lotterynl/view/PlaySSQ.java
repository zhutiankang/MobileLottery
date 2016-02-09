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
 * ˫ɫ��ѡ�Ž���
 * 
 * @author ׷��
 * 
 */
public class PlaySSQ extends BaseUI implements PlayGame {
	// ͨ������ ����linearLayout findviewbyId ���ü���

	// �ٱ���
	// �жϹ��ʴ����Ƿ��ȡ���ڴ���Ϣ
	// �����ȡ����ƴװ����
	// ����Ĭ�ϵı���չʾ

	// �����ѡ������
	// ��ѡ�ţ�����+��ѡ������
	// ��ѡ������һע��Ҫ��
	// �죺6+����1
	// ���ֻ�ҡ�δ���
	// ���ٶȴ�������
	// ��ʽһ������һ����ļ��ٶ�ֵ�ڵ�λʱ���ڣ�1�룩���䶯�����ʴﵽ���úõ���ֵ
	// ��ʽ������ȡ������ļ��ٶ�ֵ����¼������һ��ʱ��֮���ٴλ�ȡ������ļ��ٶ�ֵ��������������������������������л��ܣ����ﵽ���úõ���ֵ

	// �ټ�¼��һ�����ݣ�������ļ��ٶȣ�Ϊ�����ε���ͬ�ֻ�������ʱ��������¼��һ�����ʱ��
	// �ڵ����µĴ��������ݴ��ݺ��ж�ʱ�������õ�ǰʱ�����һ������ʱ����бȶԣ����������ʱ����Ҫ����Ϊ�Ǻϸ�ĵڶ����㣬�������������ݰ���
	// ���������ļ��㣺��ȡ���µļ��ٶ�ֵ���һ�����ϴ洢�Ľ��в�ֵ���㣬��ȡ��һ��Ͷ���֮�������
	// ���Դ����ƣ���ȡ�������������������һ�λ���
	// ��ͨ������ֵ���趨�õ���ֵ�ȶԣ�������ڵ��ڣ��û�ҡ���ֻ������������¼��ǰ�����ݣ����ٶ�ֵ��ʱ�䣩
	// ����ʾ��Ϣ+���+ѡ����
	// ��ѡ
	private Button randomRed;
	private Button randomBlue;
	// ѡ������
	private MyGridView redContainer;
	private GridView blueContainer;

	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;

	// ѡ������б�
	private List<Integer> redList;
	private List<Integer> blueList;
	// ���ٶȴ�������
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
		 * int position, long id) { //�жϵ�ǰ�����item�Ƿ�ѡ����
		 * if(!redList.contains(position)){ //���û�б�ѡ�� //����ͼƬ�л�����ɫ
		 * view.setBackgroundResource(R.drawable.id_redball); //ҡ�εĶ���
		 * view.startAnimation(AnimationUtils.loadAnimation(context,
		 * R.anim.ia_ball_shake)); redList.add(position); }else{ //�����ѡ��
		 * //��ԭ����ͼƬ view.setBackgroundResource(R.drawable.id_defalut_ball);
		 * redList.remove((Object)position); } } });
		 */
		redContainer.setOnActionUpListener(new OnActionUpListener() {

			@Override
			public void onActionUp(View view, int position) {
				// �жϵ�ǰ�����item�Ƿ�ѡ����
				if (!redList.contains(position)) {
					// ���û�б�ѡ��
					// ����ͼƬ�л�����ɫ
					view.setBackgroundResource(R.drawable.id_redball);
					// ҡ�εĶ���
					// view.startAnimation(AnimationUtils.loadAnimation(context,
					// R.anim.ia_ball_shake));
					redList.add(position);
				} else {
					// �����ѡ��,��һ�£��ɺ�ɫ��Ϊ��ɫ
					// ��ԭ����ͼƬ
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					redList.remove((Object) position);
				}
				changeNotice();
			}
		});
		// �����й���Ļ�������ֱ�Ӹ��ƴ��룬Ȼ���滻��������֣�ע��һ��Ҫ�Ǹ��Ʋ���
		blueContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �жϵ�ǰ�����item�Ƿ�ѡ����
				if (!blueList.contains(position)) {
					// ���û�б�ѡ��
					// ����ͼƬ�л�����ɫ
					view.setBackgroundResource(R.drawable.id_blueball);
					// ҡ�εĶ���
					view.startAnimation(AnimationUtils.loadAnimation(context,
							R.anim.ia_ball_shake));
					blueList.add(position);
				} else {
					// �����ѡ��
					// ��ԭ����ͼƬ
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
			// ��ѡ����
			redList.clear();
			while (redList.size() < 6) {// ѭ��6��,����6���򣬿�ʼΪ0
				int num = random.nextInt(33);// 33���� �±�0-32
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
			// ��ѡ����
			blueList.clear();
			int num = random.nextInt(16);// 16���� �±�0-15
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
		// ע��
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
	 * ��ѡһע
	 */
	private void randomSSQ() {
		Random random = new Random();
		// ��ѡ����
		redList.clear();
		while (redList.size() < 6) {// ѭ��6��,����6���򣬿�ʼΪ0
			int num = random.nextInt(33);// 33���� �±�0-32
			if (redList.contains(num)) {
				continue;
			}
			redList.add(num);
		}
		redAdapter.setSelectedList(redList);
		redAdapter.notifyDataSetChanged();

		// ��ѡ����
		blueList.clear();
		int num = random.nextInt(16);// 16���� �±�0-15
		blueList.add(num);
		blueAdapter.setSelectedList(blueList);
		blueAdapter.notifyDataSetChanged();
		changeNotice();
	}

	@Override
	public void onPause() {
		// ע��
		manager.unregisterListener(listener);
		listener = null;
		super.onPause();
	}

	private void changeTitle() {
		// �ٱ��⡪������֮������ݴ���(Bundle)
		// �жϹ��ʴ����Ƿ��ȡ���ڴ���Ϣ
		String titleInfo = "";
		if (ssqBundle != null) {
			titleInfo = "˫ɫ���" + ssqBundle.getString("ssqBundle") + "��";
		} else {
			titleInfo = "˫ɫ��ѡ��";
		}
		TitleManager.getInstance().changeTitle(titleInfo);
	}

	/**
	 * �ı�ײ���������ʾ��Ϣ
	 */
	private void changeNotice() {
		String notice = "";
		// ��һעΪ�ָ�
		if (redList.size() < 6) {
			notice = "����Ҫѡ��" + (6 - redList.size()) + "ע";
		} else if (blueList.size() == 0) {
			notice = "������Ҫѡ��" + 1 + "������";
		} else {
			notice = "�� " + calc() + " ע " + calc() * 2 + " Ԫ";
		}
		BottomManager.getInstance().changeBottomNotice(notice);
	}

	/**
	 * ����˫ɫ���ע��
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
	 * ����һ�����Ľ׳�
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
	 * ���
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
		// ���жϣ��û��Ƿ�ѡ����һעͶע
		if (redList.size() >= 6 && blueList.size() >= 1) {
			// һ�����ﳵ�У�ֻ�ܷ���һ�����֣���ǰ�ڵ�Ͷע��Ϣ
			// ���жϣ��Ƿ��ȡ���˵�ǰ�����ڵ���Ϣ
			if (ssqBundle != null) {
				// �۷�װ�û���Ͷע��Ϣ����������ע��
				Ticket ticket = new Ticket();
				StringBuffer redBuffer = new StringBuffer();
				DecimalFormat decimal = new DecimalFormat("00");
				for(Integer item : redList){//��ȡ����λ�ã���ʵ�ʵ�ֵС1
					Integer value = item + 1;
					redBuffer.append(" ").append(decimal.format(value));
				}
				ticket.setRedList(redBuffer.substring(1));//��һ��Ԫ��ǰ�Ŀո�ȥ��
				
				StringBuffer blueBuffer = new StringBuffer();
				for(Integer item : blueList){//��ȡ����λ�ã���ʵ�ʵ�ֵС1
					Integer value = item + 1;
					blueBuffer.append(" ").append(decimal.format(value));
				}
				ticket.setBlueList(blueBuffer.substring(1));//��һ��Ԫ��ǰ�Ŀո�ȥ��
				ticket.setNum(calc());
				// �ܴ�����Ʊ���ﳵ����Ͷע��Ϣ��ӵ����ﳵ��
				ShoppingCart.getInstance().getTickets().add(ticket);
				// �����ò��ֵı�ʾ�����ò����ڴ�
				ShoppingCart.getInstance().setIssue(ssqBundle.getString("ssqBundle"));
				ShoppingCart.getInstance().setLotteryid(ConstantValues.SSQ);
				// �޽�����ת�������ﳵչʾ
				MiddleManager.getInstance().changeUI(Shopping.class, ssqBundle);
			} else {
				// ���»�ȡ�ڴ���Ϣ
				getCurrentIssueInfo();
			}
		} else {
			// ��֧
			// ��ʾ:��Ҫѡ��һע
			PromptManager.showToast(context, "��Ҫѡ��һע");
			// ���»�ȡ�ڴ���Ϣ
		}
	}
	
	/**
	 * ���»�ȡ��ǰ����������Ϣ��˫ɫ��
	 */
	private void getCurrentIssueInfo() {
		// new MyAsyncTask().executeProxy(ConstantValues.SSQ);//��execute�м���Ƿ�������
		// new MyThread().start();��start�м���Ƿ�������
		new MyAsyncTask<Integer>() {
			// �첽��͵͵������ȥ�ã�����Ҫ����ʲô��ʾ��
			protected void onPreExecute() {
				//��ʾ������
				PromptManager.showProgressDialog(context);
			};
			@Override
			protected Message doInBackground(Integer... params) {
				// ��ȡ���ݣ�ҵ��ĵ���
				CommonInfoEngine engine = BeanFactory
						.getImpl(CommonInfoEngine.class);
				return engine.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				PromptManager.closeProgressDialog();
				// ���½���
				super.onPostExecute(result);
				if (result != null) {
					Oelement oelement = result.getBody().getOelement();
					if (ConstantValues.SUCCESS.equals(oelement.getErrorcode())) {
						CurrentIssueElement element = (CurrentIssueElement)result.getBody().getElements().get(0);
						String issue = element.getIssue();
						//����ssqbundle
						ssqBundle = new Bundle();
						ssqBundle.putString("ssqBundle",issue);
						changeTitle();
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


}
