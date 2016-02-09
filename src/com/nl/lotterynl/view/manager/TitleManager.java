package com.nl.lotterynl.view.manager;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.view.SecondUI;
import com.nl.lotterynl.view.UserLogin;

/**
 * ������������Ĺ���
 * 
 * @author ׷��
 * 
 */
public class TitleManager implements OnClickListener, Observer {

	// ����ģʽ
	private TitleManager() {
	};

	private static TitleManager instance = new TitleManager();

	public static TitleManager getInstance() {
		return instance;
	}

	private RelativeLayout commonContainer;
	private RelativeLayout unloginContainer;
	private RelativeLayout loginContainer;

	private ImageView goback;
	private ImageView help;
	private ImageView regist;
	private ImageView login;

	private TextView titleContent;
	private TextView userInfo;

	private void initTitle() {
		commonContainer.setVisibility(View.GONE);
		unloginContainer.setVisibility(View.GONE);
		loginContainer.setVisibility(View.GONE);
	}

	public void init(Activity activity) {
		commonContainer = (RelativeLayout) activity
				.findViewById(R.id.ii_common_container);
		unloginContainer = (RelativeLayout) activity
				.findViewById(R.id.ii_unlogin_title);
		loginContainer = (RelativeLayout) activity
				.findViewById(R.id.ii_login_title);

		goback = (ImageView) activity.findViewById(R.id.ii_title_goback);
		help = (ImageView) activity.findViewById(R.id.ii_title_help);
		regist = (ImageView) activity.findViewById(R.id.ii_title_regist);
		login = (ImageView) activity.findViewById(R.id.ii_title_login);

		titleContent = (TextView) activity.findViewById(R.id.ii_title_content);
		userInfo = (TextView) activity.findViewById(R.id.ii_top_user_info);
		setListener();
	}

	private void setListener() {
		goback.setOnClickListener(this);
		help.setOnClickListener(this);
		regist.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	// ��ʾͨ�ñ���
	public void showCommonTitle() {
		initTitle();
		commonContainer.setVisibility(View.VISIBLE);
	}

	// ��ʾδ��¼�ı���
	public void showUnloginTitle() {
		initTitle();
		unloginContainer.setVisibility(View.VISIBLE);
	}

	// ��ʾ��¼�ı���
	public void showLoginTitle() {
		initTitle();
		loginContainer.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_title_goback:
			System.out.println("������");
			break;
		case R.id.ii_title_help:
			System.out.println("������");
			break;
		case R.id.ii_title_regist:
			System.out.println("ע����");
			break;
		case R.id.ii_title_login:
			System.out.println("��¼��");
			MiddleManager.getInstance().changeUI(UserLogin.class);
			break;
		}
	}

	public void changeTitle(String title) {
		titleContent.setText(title);
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data != null && StringUtils.isNumeric(data.toString())) {
			int id = Integer.parseInt(data.toString());
			switch (id) {
			case ConstantValues.VIEW_FIRST:
				showUnloginTitle();
				break;
			case ConstantValues.VIEW_SECOND:
			case ConstantValues.VIEW_SSQ:
			case ConstantValues.VIEW_SHOPPING:
			case ConstantValues.VIEW_LOGIN:
			case ConstantValues.VIEW_PREBET:
				showCommonTitle();
				break;
			case ConstantValues.VIEW_HALL:
				if(GlobalParams.isLogin){
					showLoginTitle();
					String info = "�û���:"+GlobalParams.USERNAME+"\r\n"+"���:"+GlobalParams.MONEY;
					userInfo.setText(info);
				}else{
					showUnloginTitle();
				}
				break;
			default:
				break;
			}
		}
	}

}
