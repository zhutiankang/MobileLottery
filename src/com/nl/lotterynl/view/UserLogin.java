package com.nl.lotterynl.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.domain.Oelement;
import com.nl.lotterynl.domain.User;
import com.nl.lotterynl.engine.UserEngine;
import com.nl.lotterynl.net.element.BalanceElement;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.BeanFactory;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.MiddleManager;
/**
 * 用户登录+余额获取
 * 两个登录：主动登录（购彩大厅）；被动登录（购物车）
 * @author 追梦
 *
 */
public class UserLogin extends BaseUI {

	private EditText username;
	private ImageView clear;
	private EditText password;
	private Button login;

	public UserLogin(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context,
				R.layout.il_user_login, null);
		username = (EditText) findViewById(R.id.ii_user_login_username);
		clear = (ImageView) findViewById(R.id.ii_clear);
		password = (EditText) findViewById(R.id.ii_user_login_password);
		login = (Button) findViewById(R.id.ii_user_login);
	}

	@Override
	public void setListener() {
		// 增加一个文本变化的监听
		username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			// 文本变动之后
			@Override
			public void afterTextChanged(Editable s) {
				if (username.getText().toString().trim().length() > 0) {
					clear.setVisibility(View.VISIBLE);
				}
			}
		});
		clear.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_clear:
			// 清除用户名
			username.setText("");
			clear.setVisibility(View.INVISIBLE);
			break;

		case R.id.ii_user_login:
			// 登录
			// 用户输入信息
			if (checkUserInfo()) {
				User user = new User();
				user.setUsername(username.getText().toString().trim());
				user.setPassword(password.getText().toString().trim());
				new MyAsyncTask<User>() {
					@Override
					protected void onPreExecute() {
						PromptManager.showProgressDialog(context);
						super.onPreExecute();
					}

					@Override
					protected Message doInBackground(User... params) {
						UserEngine engine = BeanFactory
								.getImpl(UserEngine.class);
						Message login = engine.login(params[0]);
						if (login != null) {//得到一个对象要判断一下是否为空
							Oelement oelement = login.getBody().getOelement();
							if(ConstantValues.SUCCESS.equals(oelement.getErrorcode())){
								// 登录成功了
								GlobalParams.isLogin = true;
								GlobalParams.USERNAME = params[0].getUsername();
								
								// 成功了获取余额
								Message balance = engine.getBalance(params[0]);
								if(balance!=null){
									oelement = balance.getBody().getOelement();
									if(ConstantValues.SUCCESS.equals(oelement.getErrorcode())){
										BalanceElement element = (BalanceElement) balance.getBody().getElements().get(0);
										GlobalParams.MONEY = Float.valueOf(element.getInvestvalues());
										return balance;
									}
								}
								
							}
						}
						
						return null;
					}

					@Override
					protected void onPostExecute(Message result) {
						PromptManager.closeProgressDialog();
						if(result !=null){
							// 界面跳转，返回购物车界面
							PromptManager.showToast(context, "登录成功");
							MiddleManager.getInstance().goBack();
						}else{
							PromptManager.showToast(context, "服务忙……");
						}
					}
				}.executeProxy(user);

			}
			break;
		}
	}

	/**
	 * 用户信息判断与服务器上的对比
	 * 
	 * @return
	 */
	private boolean checkUserInfo() {
		return true;
	}

	@Override
	public int getID() {
		return ConstantValues.VIEW_LOGIN;
	}

}
