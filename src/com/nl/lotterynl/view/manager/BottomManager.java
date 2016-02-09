package com.nl.lotterynl.view.manager;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;

/**
 * ����ײ������Ĺ���
 * 
 * @author ׷��
 * 
 */
public class BottomManager implements OnClickListener,Observer {
	/******************* ��һ�����������Ĵ���(����ģʽ) ***************************************************/
	// ����,��ֹ���⹹�죬ֻ��һ��ʵ��������Ҫÿ�����½��������¼���
	// ����˽��
	private BottomManager() {
	};
	// ����һ����̬ʵ��
	private static BottomManager instance;
	public static BottomManager getInstance() {
		if(instance==null){
			instance = new BottomManager();
		}
		return instance;
	}
	/*********************************************************************************************/

	/******************* �ڶ�������ʼ������������������ؿؼ����ü��� *********************************/
	private RelativeLayout il_bottom;
	private LinearLayout commonBottom;
	private LinearLayout gameBottom;

	private ImageButton homeButton;
	private ImageButton hallButton;
	private ImageButton rechargeButton;
	private ImageButton myselfButton;

	private ImageButton chooseCleanButton;
	private ImageButton chooseOkButton;

	private TextView chooseNotice;
	public void initBottom() {
		commonBottom.setVisibility(View.GONE);
		gameBottom.setVisibility(View.GONE);
	}

	public void init(Activity activity) {
		il_bottom = (RelativeLayout) activity.findViewById(R.id.il_bottom);
		commonBottom = (LinearLayout) activity
				.findViewById(R.id.ii_bottom_common);
		gameBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);

		homeButton = (ImageButton) activity.findViewById(R.id.ii_bottom_home);
		hallButton = (ImageButton) activity
				.findViewById(R.id.ii_bottom_lottery_hall);
		rechargeButton = (ImageButton) activity
				.findViewById(R.id.ii_bottom_lottery_recharge);
		myselfButton = (ImageButton) activity
				.findViewById(R.id.ii_bottom_lottery_myself);
		chooseCleanButton = (ImageButton) activity
				.findViewById(R.id.ii_bottom_game_choose_clean);
		chooseOkButton = (ImageButton) activity
				.findViewById(R.id.ii_bottom_game_choose_ok);
		chooseNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
		setListener();
	}

	private void setListener() {
		homeButton.setOnClickListener(this);
		hallButton.setOnClickListener(this);
		rechargeButton.setOnClickListener(this);
		myselfButton.setOnClickListener(this);
		chooseCleanButton.setOnClickListener(this);
		chooseOkButton.setOnClickListener(this);

	}
	/*********************************************************************************************/
	
	/****************** �����������Ƹ���������������ʾ������ *****************************************/
	/**
	 * ת����ͨ�õ���
	 */
	public void showCommonBottom() {
		initBottom();
		if(il_bottom.getVisibility()==View.INVISIBLE||il_bottom.getVisibility()==View.GONE){
			il_bottom.setVisibility(View.VISIBLE);
		}
		commonBottom.setVisibility(View.VISIBLE);
		
	}
	/**
	 * ת��������
	 */
	public void showGameBottom() {
		initBottom();
		if(il_bottom.getVisibility()==View.INVISIBLE||il_bottom.getVisibility()==View.GONE){
			il_bottom.setVisibility(View.VISIBLE);
		}
		gameBottom.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_bottom_home:
			System.out.println("home");
			break;
		case R.id.ii_bottom_lottery_hall:
			System.out.println("hall");
			break;
		case R.id.ii_bottom_lottery_recharge:
			System.out.println("recharge");
			break;
		case R.id.ii_bottom_lottery_myself:
			System.out.println("myself");
			break;
		case R.id.ii_bottom_game_choose_clean:
			//�ӿڵ����ã����ʵ�����һ�廯��ʵ������ܽᣬ����Ҫ����дʵ������
			//�����ͬ���һ�廯
			//ֻ��Ҫд�ӿھ���
			BaseUI currentUI = MiddleManager.getInstance().getCurrentUI();
			if(currentUI instanceof PlayGame){
				((PlayGame)currentUI).clear();//��һ��������
			}
			break;
		case R.id.ii_bottom_game_choose_ok:
			System.out.println("choose_ok");
			BaseUI currentUI2 = MiddleManager.getInstance().getCurrentUI();
			if(currentUI2 instanceof PlayGame){
				((PlayGame)currentUI2).done();
			}
			break;
		
		}
	}
	/**
	 * �ı�ײ�����������ʾ���
	 */
	public void changeBottomVisibility(int type) {
		if (il_bottom.getVisibility() != type) {
			il_bottom.setVisibility(type);
		}
	}
	/*********************************************************************************************/
	
	/*********************** ���Ĳ��������淨����������ʾ ********************************************/
	/**
	 * �����淨�ײ���ʾ��Ϣ
	 * 
	 * @param notice
	 */
	public void changeBottomNotice(String notice){
		chooseNotice.setText(notice);
	}
	/*********************************************************************************************/

	@Override
	public void update(Observable observable, Object data) {
		if (data != null && StringUtils.isNumeric(data.toString())) {
			int id = Integer.parseInt(data.toString());
			switch (id) {
			case ConstantValues.VIEW_FIRST:
			case ConstantValues.VIEW_HALL:
			case ConstantValues.VIEW_LOGIN:
				showCommonBottom();
				break;
			case ConstantValues.VIEW_SECOND:
			case ConstantValues.VIEW_SSQ:
				showGameBottom();
				break;
			case ConstantValues.VIEW_SHOPPING:
			case ConstantValues.VIEW_PREBET:
				changeBottomVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	}
}
