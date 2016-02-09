package com.nl.lotterynl.view;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.domain.Oelement;
import com.nl.lotterynl.domain.ShoppingCart;
import com.nl.lotterynl.domain.Ticket;
import com.nl.lotterynl.domain.User;
import com.nl.lotterynl.engine.UserEngine;
import com.nl.lotterynl.net.element.BetElement;
import com.nl.lotterynl.net.protocal.Message;
import com.nl.lotterynl.util.BeanFactory;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.MiddleManager;

/**
 * ׷�ںͱ�Ͷ�����ý���
 * 
 * @author ׷��
 * 
 */
public class PreBet extends BaseUI {
	// ͨ������
	// �����ListView
	// ����ʾ��Ϣ����
	// �۱�Ͷ��׷�ڵ�����
	// ����������
	
	private TextView bettingNum;// ע��
	private TextView bettingMoney;// ���

	private Button subAppnumbers;// ���ٱ�Ͷ
	private TextView appnumbersInfo;// ����
	private Button addAppnumbers;// ���ӱ�Ͷ

	private Button subIssueflagNum;// ����׷��
	private TextView issueflagNumInfo;// ׷��
	private Button addIssueflagNum;// ����׷��

	private ImageButton lotteryPurchase;// Ͷע
	private ListView shoppingList;// ���ﳵչʾ

	private ShoppingAdapter adapter;
	public PreBet(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context,
				R.layout.il_play_prefectbetting, null);
		bettingNum = (TextView) findViewById(R.id.ii_shopping_list_betting_num);
		bettingMoney = (TextView) findViewById(R.id.ii_shopping_list_betting_money);

		subAppnumbers = (Button) findViewById(R.id.ii_sub_appnumbers);
		appnumbersInfo = (TextView) findViewById(R.id.ii_appnumbers);
		addAppnumbers = (Button) findViewById(R.id.ii_add_appnumbers);

		subIssueflagNum = (Button) findViewById(R.id.ii_sub_issueflagNum);
		issueflagNumInfo = (TextView) findViewById(R.id.ii_issueflagNum);
		addIssueflagNum = (Button) findViewById(R.id.ii_add_issueflagNum);

		lotteryPurchase = (ImageButton) findViewById(R.id.ii_lottery_purchase);
		shoppingList = (ListView) findViewById(R.id.ii_lottery_shopping_list);
		
		adapter = new ShoppingAdapter();
		shoppingList.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		// ����
		addAppnumbers.setOnClickListener(this);
		subAppnumbers.setOnClickListener(this);
		// ׷��
		addIssueflagNum.setOnClickListener(this);
		subIssueflagNum.setOnClickListener(this);
		// Ͷע
		lotteryPurchase.setOnClickListener(this);
	}
	@Override
	public void onResume() {
		changeNotice();
		super.onResume();
	}
	private void changeNotice(){
		Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
		Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();
		String number = context.getResources().getString(R.string.is_shopping_list_betting_num);
		String money = context.getResources().getString(R.string.is_shopping_list_betting_money);
		number = StringUtils.replaceEach(number, new String[]{"NUM"}, new String[]{lotterynumber.toString()});
		bettingNum.setText(Html.fromHtml(number));
//		Html.fromHtml(noticeInfo);��html���������ת��
		
		money = StringUtils.replaceEach(money, new String[]{"MONEY1","MONEY2"}, new String[]{lotteryvalue.toString(),String.valueOf(GlobalParams.MONEY)});
		bettingMoney.setText(Html.fromHtml(money));
		
		//�޸ı�����׷�ڵ���ʾ��Ϣ
		appnumbersInfo.setText(ShoppingCart.getInstance().getAppnumbers().toString());
		issueflagNumInfo.setText(ShoppingCart.getInstance().getIssuesnumbers().toString());
		
	}
	@Override
	public void onClick(View v) {
		boolean result;
		switch (v.getId()) {
		case R.id.ii_add_appnumbers:
			// ���ӱ���
			result = ShoppingCart.getInstance().addAppnumbers(true);
			if(result){
				changeNotice();
			}
			break;
		case R.id.ii_sub_appnumbers:
			// ���ٱ���
			result = ShoppingCart.getInstance().addAppnumbers(false);
			if(result){
				changeNotice();
			}
			break;
		case R.id.ii_add_issueflagNum:
			// ����׷��
			result = ShoppingCart.getInstance().addIssuesnumbers(true);
			if(result){
				changeNotice();
			}
			break;
		case R.id.ii_sub_issueflagNum:
			// ����׷��
			result = ShoppingCart.getInstance().addIssuesnumbers(false);
			if(result){
				changeNotice();
			}
			break;

		case R.id.ii_lottery_purchase:
			// Ͷע����
			//��Ҫ�û�����֪����˭��ģ���Ϊ�Ѿ���¼�����Բ���Ҫ����
			User user = new User();
			user.setUsername(GlobalParams.USERNAME);
			new MyAsyncTask<User>() {

				@Override
				protected Message doInBackground(User... params) {
					UserEngine engine = BeanFactory.getImpl(UserEngine.class);
					Message bet = engine.bet(params[0]);
					if(bet!=null){
						Oelement oelement = bet.getBody().getOelement();
						if(ConstantValues.SUCCESS.equals(oelement.getErrorcode())){
							BetElement element = (BetElement) bet.getBody().getElements().get(0);
							//�޸��û��������Ϣ
							GlobalParams.MONEY = Float.valueOf(element.getActvalue());
							return bet;
						}
					}
					return null;
				}
				protected void onPostExecute(Message result) {
					if(result!=null){
						//�����ؼ�
						MiddleManager.getInstance().clear();
						//��ת�����ʴ�������ʾ�Ի���
						MiddleManager.getInstance().changeUI(Hall.class);
						PromptManager.showToast(context, "Ͷע�ɹ�");
						//��չ��ﳵ
						ShoppingCart.getInstance().clear();
					}else{
						PromptManager.showToast(context, "������æ....");
					}
				};
			}.executeProxy(user);
			
			break;
		}
	}

	@Override
	public int getID() {
		return ConstantValues.VIEW_PREBET;
	}
	private class ShoppingAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return ShoppingCart.getInstance().getTickets().size();
		}

		@Override
		public Object getItem(int position) {
			return ShoppingCart.getInstance().getTickets().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				convertView = View.inflate(context, R.layout.il_play_prefectbetting_row, null);
				holder = new ViewHolder();
				holder.redball = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
				holder.blueball = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
			holder.redball.setText(ticket.getRedList());
			holder.blueball.setText(ticket.getBlueList());
			return convertView;
		}
		
	}
	static class ViewHolder{
		TextView redball;
		TextView blueball;
	}
}
