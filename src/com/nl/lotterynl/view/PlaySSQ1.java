package com.nl.lotterynl.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.nl.lotterynl.R;
import com.nl.lotterynl.domain.ConstantValues;
import com.nl.lotterynl.view.adapter.PoolAdapter;
import com.nl.lotterynl.view.manager.BaseUI;
import com.nl.lotterynl.view.manager.TitleManager;

/**
 * ˫ɫ��ѡ�Ž���
 * 
 * @author ׷��
 * 
 */
public class PlaySSQ1 extends BaseUI {
	// ͨ������ ����linearLayout findviewbyId ���ü���

	// �ٱ���
	// �жϹ��ʴ����Ƿ��ȡ���ڴ���Ϣ
	// �����ȡ����ƴװ����
	// ����Ĭ�ϵı���չʾ

	// �����ѡ������
	// ��ѡ�ţ�����+��ѡ������
	// ���ֻ�ҡ�δ���
	// ����ʾ��Ϣ+���+ѡ����
	// ��ѡ
	private Button randomRed;
	private Button randomBlue;
	// ѡ������
	private GridView redContainer;
	private GridView blueContainer;

	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;
	
	//ѡ������б�
	private List<Integer> redList;
	private List<Integer> blueList;
	public PlaySSQ1(Context context) {
		super(context);
	}

	@Override
	public void init() {
		showInMiddle = (ViewGroup) View.inflate(context, R.layout.il_playssq,
				null);
		redContainer = (GridView) findViewById(R.id.ii_ssq_red_number_container);
		blueContainer = (GridView) findViewById(R.id.ii_ssq_blue_number_container);
		randomRed = (Button) findViewById(R.id.ii_ssq_random_red);
		randomBlue = (Button) findViewById(R.id.ii_ssq_random_blue);
		redList = new ArrayList<Integer>();
		blueList = new ArrayList<Integer>();
		redAdapter = new PoolAdapter(context,33);
		blueAdapter = new PoolAdapter(context,16);
		redContainer.setAdapter(redAdapter);
		blueContainer.setAdapter(blueAdapter);
		
	}

	@Override
	public void setListener() {
		randomRed.setOnClickListener(this);
		randomBlue.setOnClickListener(this);
		redContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//�жϵ�ǰ�����item�Ƿ�ѡ����
				if(!redList.contains(position)){
				//���û�б�ѡ��
				//����ͼƬ�л�����ɫ
				view.setBackgroundResource(R.drawable.id_redball);
				//ҡ�εĶ���
				view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
				redList.add(position);
				}else{
				//�����ѡ��
				//��ԭ����ͼƬ
				view.setBackgroundResource(R.drawable.id_defalut_ball);
				redList.remove((Object)position);
				}
			}
		});
		//�����й���Ļ�������ֱ�Ӹ��ƴ��룬Ȼ���滻��������֣�ע��һ��Ҫ�Ǹ��Ʋ���
		blueContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//�жϵ�ǰ�����item�Ƿ�ѡ����
				if(!blueList.contains(position)){
				//���û�б�ѡ��
				//����ͼƬ�л�����ɫ
				view.setBackgroundResource(R.drawable.id_blueball);
				//ҡ�εĶ���
				view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.ia_ball_shake));
				blueList.add(position);
				}else{
				//�����ѡ��
				//��ԭ����ͼƬ
				view.setBackgroundResource(R.drawable.id_defalut_ball);
				blueList.remove((Object)position);
				}
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ii_ssq_random_red:
			
			break;
		case R.id.ii_ssq_random_blue:
			
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
		super.onResume();
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
}
