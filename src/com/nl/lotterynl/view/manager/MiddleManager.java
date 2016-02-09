package com.nl.lotterynl.view.manager;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.nl.lotterynl.R;
import com.nl.lotterynl.util.MemoryManager;
import com.nl.lotterynl.util.PromptManager;
import com.nl.lotterynl.util.SoftMap;
import com.nl.lotterynl.view.Hall;
/**
 * �м������Ĺ�����
 * 
 * @author ׷��
 * 
 */
public class MiddleManager extends Observable {
	private static final String TAG = "MiddleManager";
	// ����ģʽ
	private static MiddleManager instance;

	private MiddleManager() {
	};

	public static MiddleManager getInstance() {
		if (instance == null) {
			instance = new MiddleManager();
		}
		return instance;
	}

	private RelativeLayout il_middle;

	public void setIl_middle(RelativeLayout il_middle) {
		this.il_middle = il_middle;
	}

	// K :Ψһ�ı�ʾBaseUI������
	private static Map<String, BaseUI> VIEWCACHE = new HashMap<String, BaseUI>();

	// ÿ����һ������150K����16M
	// �ڴ治��
	// ����ķ�����
	// ��һ�֣�����VIEWCACHE���ϵ�size
	// �ڶ��֣�Fragment���棬replace���������Ỻ�����
	// �����֣�����BaseUI��Ӧ�ü���
	// ǿ���ã���ǰ��GC�����׳�OOM���������BaseUI��
	// �����ã���OOM֮ǰ��GC���յ�
	// �����ã�һ����GC�����˾ͻ���
	// �����ã�һ�������˾ͱ�������

	// ��������ȱ��
	// ��һ�֣�����ʵ�ּ򵥣���Ӧ�Բ�ǿ
	// �ڶ��֣���һ��Fragment�������ˣ����ڴ�����ʱ�������ٶ���ʧ����
	// �����֣��ŵ㣬ȱ�㣺��Ȼ���ü��𽵵ͣ����Ǳ���ȴ�GCȥ���գ�����Ҫ�ṩ��GCһ�����յ�ʱ�䣬����һ�������ڴ��ٶȹ��죬������
	// �ر������ٲ����¡���Lrucache
	static{
		if(MemoryManager.hasAcailMemory()){//�ڴ����
			VIEWCACHE = new HashMap<String, BaseUI>();
		}else{
			VIEWCACHE = new SoftMap<String, BaseUI>();
		}
		
	}
	
	
	private BaseUI currentUI;

	public BaseUI getCurrentUI() {
		return currentUI;
	}

	public void changeUI(Class<? extends BaseUI> targetClazz, Bundle ssqBundle) {
		// �жϣ���ǰ����չʾ�Ľ�����л�Ŀ������Ƿ���ͬ
		if (currentUI != null && currentUI.getClass() == targetClazz) {
			return;
		}
		BaseUI targetUI;
		// һ��������������
		// �ж��Ƿ񴴽��ˡ��������������Ľ�����Ҫ�洢
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// �����ˣ�����
			targetUI = VIEWCACHE.get(key);
		} else {
			// ���򣬴���
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// ����
				targetUI = constructor.newInstance(getContext());// ����
				VIEWCACHE.put(key, targetUI);
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");
			}
		}

		// ��targetUI�����ˣ������������ݣ��ж�targetUI��Ϊ��
		if (targetUI != null) {
			targetUI.setSsqBundle(ssqBundle);
		}

		// ���������ǰ����չʾ�Ľ���֮ǰ����onPause����
		if (currentUI != null) {
			currentUI.onPause();
		}
		Log.i(TAG, targetUI.toString());
		il_middle.removeAllViews();
		View child = targetUI.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));
		// �ڼ��������֮�󡪡�onResume
		targetUI.onResume();

		currentUI = targetUI;
		// ����ǰ��ʾ�Ľ���ŵ�ջ��
		HISTORY.addFirst(key);
		changeTitleAndBottom();
	}

	/**
	 * �л�����:������⡰����������������
	 * 
	 * @param ui
	 */
	public void changeUI(Class<? extends BaseUI> targetClazz) {
		// �жϣ���ǰ����չʾ�Ľ�����л�Ŀ������Ƿ���ͬ
		if (currentUI != null && currentUI.getClass() == targetClazz) {
			return;
		}
		BaseUI targetUI;
		// һ��������������
		// �ж��Ƿ񴴽��ˡ��������������Ľ�����Ҫ�洢
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// �����ˣ�����
			targetUI = VIEWCACHE.get(key);
		} else {
			// ���򣬴���
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// ����
				targetUI = constructor.newInstance(getContext());// ����
				VIEWCACHE.put(key, targetUI);
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");
			}
		}
		// ���������ǰ����չʾ�Ľ���֮ǰ����onPause����
		if (currentUI != null) {
			currentUI.onPause();
		}
		Log.i(TAG, targetUI.toString());
		il_middle.removeAllViews();
		View child = targetUI.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));
		// �ڼ��������֮�󡪡�onResume
		targetUI.onResume();

		currentUI = targetUI;
		// ����ǰ��ʾ�Ľ���ŵ�ջ��
		HISTORY.addFirst(key);
		changeTitleAndBottom();
	}

	private void changeTitleAndBottom() {
		// 1������һ��Ӧδ��½�����ͨ�õ���
		// 2���������Ӧͨ�ñ�����淨����

		// ��ǰ����չʾ������ǵ�һ������
		// ����һ��
		// �������⣬�ȶԵ����ݣ����� ���� �ֽ���
		// �ڽ��洦����ڣ������еĽ�������ȷ��������Բ���ҹ�������
		// ������ֽ��룬�����еĽ��涼�Ĵ������
		// if (currentUI.getClass() == FirstUI.class) {
		// TitleManager.getInstance().showUnloginTitle();
		// BottomManager.getInstance().showCommonBottom();
		// }
		// if(currentUI.getClass().getSimpleName().equals("SecondUI")){
		// TitleManager.getInstance().showCommonTitle();
		// BottomManager.getInstance().showGameBottom();
		// }
		// �������������ȶ����ݣ���϶�̫�ߣ��м����������������ײ�����
		// switch (currentUI.getID()) {
		// case ConstantValues.VIEW_FIRST:
		// TitleManager.getInstance().showUnloginTitle();
		// BottomManager.getInstance().showCommonBottom();
		// break;
		// case ConstantValues.VIEW_SECOND:
		// TitleManager.getInstance().showCommonTitle();
		// BottomManager.getInstance().showGameBottom();
		// break;
		// default:
		// break;
		// }
		// ����������������϶ȣ�ʹ�����Լ��ܺ��Լ��Ĵ��루������
		// ���м������䶯��ʱ���м�������֪ͨ�����������������Ǹñ䶯�ˣ�Ψһ�ı�ʾ���ݣ�������������Ψһ��ʾ�����������ݵ��л�
		// ֪ͨ�������֪ͨ��Ϣ����
		// 1.�㲥�����Ӧ�ã���������ڣ�
		// 2.Ϊ�м������ı䶯�����˼��������۲������ģʽ������������������������������һ��Ӧ���ڲ������֮���֪ͨ��Ϣ��������϶ȣ�

		// �ٽ��м�������ɱ��۲�Ķ��� extends observable
		// �ڱ���͵ײ�������ɹ۲��� implememts observer
		// �۽����۲��ߺͱ��۲���֮��Ĺ�ϵ������͵ײ�������ӵ��۲��ߵ��������棩
		// ��һ���м������䶯���޸�booleanΪtrue��Ȼ��֪ͨ���еĹ۲���.updata()

		setChanged();
		notifyObservers(currentUI.getID());
	}

	/**
	 * �л�����:������⡰�м������У�ÿ���л�û���жϵ�ǰ����չʾ����Ҫ�л���Ŀ���ǲ���ͬһ����
	 * 
	 * @param ui
	 */
	public void changeUI3(Class<? extends BaseUI> targetClazz) {
		// �жϣ���ǰ����չʾ�Ľ�����л�Ŀ������Ƿ���ͬ
		if (currentUI != null && currentUI.getClass() == targetClazz) {
			return;
		}
		BaseUI targetUI;
		// һ��������������
		// �ж��Ƿ񴴽��ˡ��������������Ľ�����Ҫ�洢
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// �����ˣ�����
			targetUI = VIEWCACHE.get(key);
		} else {
			// ���򣬴���
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// ����
				targetUI = constructor.newInstance(getContext());// ����
				VIEWCACHE.put(key, targetUI);
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");
			}
		}

		Log.i(TAG, targetUI.toString());
		il_middle.removeAllViews();
		View child = targetUI.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));

		currentUI = targetUI;
		// ����ǰ��ʾ�Ľ���ŵ�ջ��
		HISTORY.addFirst(key);
	}

	/**
	 * �л�����:������⡰�ڱ���������ÿ�ε�����ڴ���һ��Ŀ����桱 Ƶ������������治�л�����ʾ���ݲ��ظ�����
	 * 
	 * @param ui
	 */
	public void changeUI2(Class<? extends BaseUI> targetClazz) {
		BaseUI targetUI;
		// һ��������������
		// �ж��Ƿ񴴽��ˡ��������������Ľ�����Ҫ�洢
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// �����ˣ�����
			targetUI = VIEWCACHE.get(key);
		} else {
			// ���򣬴���
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// ����
				targetUI = constructor.newInstance(getContext());// ����
				VIEWCACHE.put(key, targetUI);
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");
			}
		}

		Log.i(TAG, targetUI.toString());
		il_middle.removeAllViews();
		View child = targetUI.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));
	}

	/**
	 * �л�����
	 */
	public void changeUI1(BaseUI ui) {
		// 1���л�����ʱ������һ����ʾ����
		// �л�����ĺ��ķ���һ
		il_middle.removeAllViews();
		// FadeUtil.fadeOut(il_middle.getChildAt(0), 2000);
		View child = ui.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));
		// FadeUtil.fadeIn(child, 2000, 1000);
	}

	public Context getContext() {
		return il_middle.getContext();
	}

	private LinkedList<String> HISTORY = new LinkedList<String>();

	/**
	 * ���ؼ�����
	 * 
	 * false ������
	 */
	public boolean goBack() {
		// ��¼һ���û�������ʷ
		// Ƶ������ջ������ӣ������ڽ����л��ɹ�
		// ��ȡջ��
		// ɾ����ջ��
		// ���򼯺�
		if (HISTORY.size() > 0) {
			// ���û���������ؼ������˳�Ӧ�ã�
			if (HISTORY.size() == 1) {
				return false;// ֻ��һ������ʱ��remove������ֱ�������Ƿ��˳�Ӧ��
			}

			HISTORY.removeFirst();
			if (HISTORY.size() > 0) {
				String key = HISTORY.getFirst();
				BaseUI targetUI = VIEWCACHE.get(key);
				if (targetUI != null) {
					currentUI.onPause();// �������ǰ����
					il_middle.removeAllViews();
					il_middle.addView(targetUI.getChild());
					targetUI.getChild().setAnimation(
							AnimationUtils.loadAnimation(getContext(),
									R.anim.ia_view_change));
					targetUI.onResume();// �����µĽ���
					currentUI = targetUI;
					changeTitleAndBottom();
					return true;
				}else{
					changeUI(Hall.class);
					PromptManager.showToast(getContext(), "���ڴ�����");
				}
			}

		}
		return false;
	}

	public void clear() {
		HISTORY.clear();
	}

}
