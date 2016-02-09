package com.nl.lotterynl.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.domain.GlobalParams;
import com.nl.lotterynl.domain.ShoppingCart;
import com.nl.lotterynl.domain.Ticket;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.MiddleManager;

/**
 * ���ﳵչʾ
 * 
 */
public class Shopping extends BaseUI {
	// ͨ������ ����linearLayout findviewbyId ���ü���
	private Button addOptional;// �����ѡ
	private Button addRandom;// ��ӻ�ѡ

	private ListView shoppingList;// �û�ѡ����Ϣ�б�

	private ImageButton shoppingListClear;// ��չ��ﳵ
	private TextView notice;// ��ʾ��Ϣ
	private Button buy;// ����
	
	// ����乺�ﳵ
	private ShoppingAdapter adapter;
	// �������ѡ+��ӻ�ѡ
	// ����չ��ﳵ
	// �ܸ�������ʾ��Ϣ����
	// �ݹ���
	public Shopping(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_shopping, null);

		addOptional = (Button) findViewById(R.id.ii_add_optional);
		addRandom = (Button) findViewById(R.id.ii_add_random);
		shoppingListClear = (ImageButton) findViewById(R.id.ii_shopping_list_clear);
		notice = (TextView) findViewById(R.id.ii_shopping_lottery_notice);
		buy = (Button) findViewById(R.id.ii_lottery_shopping_buy);
		shoppingList = (ListView) findViewById(R.id.ii_shopping_list);
		
		adapter = new ShoppingAdapter();
		shoppingList.setAdapter(adapter);
	}

	@Override
	public void setListener() {
		addOptional.setOnClickListener(this);
		addRandom.setOnClickListener(this);
		shoppingListClear.setOnClickListener(this);
		buy.setOnClickListener(this);
	}

	@Override
	public int getID() {
		return ConstantValues.VIEW_SHOPPING;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_add_optional:
			// �����ѡ
			MiddleManager.getInstance().goBack();
			break;
		case R.id.ii_add_random:
			// ��ӻ�ѡ
			addRandom();
			changeNotice();
			break;
		case R.id.ii_shopping_list_clear:
			// ���
			ShoppingCart.getInstance().getTickets().clear();
			adapter.notifyDataSetChanged();
			changeNotice();
			break;
		case R.id.ii_lottery_shopping_buy:
			// ����
			//1���жϣ����ﳵ���Ƿ���Ͷע
			if(ShoppingCart.getInstance().getTickets().size()>=0){
				//2���жϣ��û��Ƿ��¼--������¼
				if(GlobalParams.isLogin){
					//3���жϣ��û�������Ƿ�����Ͷע����
					if(ShoppingCart.getInstance().getLotteryvalue()<=GlobalParams.MONEY){
						//4��������ת����ת��׷�ںͱ�Ͷ�����ý���
						MiddleManager.getInstance().changeUI(PreBet.class, ssqBundle);
					}else{
						// ��ʾ�û�����ֵȥ��������ת���û���ֵ����
						PromptManager.showToast(context, "��ֵȥ");
					}
				}else{
					// ��ʾ�û�����¼ȥ��������ת���û���¼����
					PromptManager.showToast(context, "��¼ȥ");
					MiddleManager.getInstance().changeUI(UserLogin.class, ssqBundle);
				}
			}else{
				// ��ʾ�û�����Ҫѡ��һע
				PromptManager.showToast(context, "��Ҫѡ��һע");
			}
			
			// ��֧
			break;
		}
	}
	@Override
	public void onResume() {
		changeNotice();
		super.onResume();
	}
	private void changeNotice(){
		Integer lotterynumber = ShoppingCart.getInstance().getLotterynumber();
		Integer lotteryvalue = ShoppingCart.getInstance().getLotteryvalue();
		String noticeInfo = context.getResources().getString(R.string.is_shopping_list_notice);
		noticeInfo = StringUtils.replaceEach(noticeInfo, new String[]{"NUM","MONEY"}, new String[]{lotterynumber.toString(),lotteryvalue.toString()});
//		Html.fromHtml(noticeInfo);��html���������ת��
		notice.setText(Html.fromHtml(noticeInfo));
	}
	private void addRandom() {
		Random random = new Random();
		List<Integer> redList = new ArrayList<Integer>();
		List<Integer> blueList = new ArrayList<Integer>();
		// ��ѡ����
		while (redList.size() < 6) {// ѭ��6��,����6���򣬿�ʼΪ0
			int num = random.nextInt(33);// 33���� �±�0-32
			if (redList.contains(num)) {
				continue;
			}
			redList.add(num);
		}
		// ��ѡ����
		int num = random.nextInt(16);// 16���� �±�0-15
		blueList.add(num);
		
		//��װ��Ticket
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
		ticket.setNum(1);
		//��ӵ����ﳵ��
		ShoppingCart.getInstance().getTickets().add(ticket);
		//���½���
		adapter.notifyDataSetChanged();
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
				convertView = View.inflate(context, R.layout.il_shopping_row, null);
				holder = new ViewHolder();
				holder.delete = (ImageButton) convertView.findViewById(R.id.ii_shopping_item_delete);
				holder.redball = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
				holder.blueball = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
				holder.num = (TextView) convertView.findViewById(R.id.ii_shopping_item_money);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			Ticket ticket = ShoppingCart.getInstance().getTickets().get(position);
			holder.redball.setText(ticket.getRedList());
			holder.blueball.setText(ticket.getBlueList());
			holder.num.setText(ticket.getNum()+"ע");
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ShoppingCart.getInstance().getTickets().remove(position);
					notifyDataSetChanged();
					//һ����գ�listView��û��������������ֲ����£��������notifyDataSetChanged()���½���
					changeNotice();
				}
			});
			
			
			
			return convertView;
		}
		
	}
	static class ViewHolder{
		ImageButton delete;
		TextView redball;
		TextView blueball;
		TextView num;
	}

}
