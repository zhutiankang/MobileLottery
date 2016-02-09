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
 * 中间容器的管理工具
 * 
 * @author 追梦
 * 
 */
public class MiddleManager extends Observable {
	private static final String TAG = "MiddleManager";
	// 单例模式
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

	// K :唯一的标示BaseUI的子类
	private static Map<String, BaseUI> VIEWCACHE = new HashMap<String, BaseUI>();

	// 每增加一个界面150K——16M
	// 内存不足
	// 处理的方案：
	// 第一种：控制VIEWCACHE集合的size
	// 第二种：Fragment代替，replace方法，不会缓存界面
	// 第三种：降低BaseUI的应用级别
	// 强引用：当前（GC宁可抛出OOM，不会回收BaseUI）
	// 软引用：在OOM之前被GC回收掉
	// 弱引用：一旦被GC发现了就回收
	// 虚引用：一旦创建了就被回收了

	// 都存在优缺点
	// 第一种：代码实现简单，适应性不强
	// 第二种：上一个Fragment被回收了，当内存充足的时候，运行速度损失过多
	// 第三种：优点，缺点：虽然引用级别降低，但是必须等待GC去回收，必须要提供给GC一个回收的时间，所以一旦申请内存速度过快，不适用
	// 特别是在瀑布流下——Lrucache
	static{
		if(MemoryManager.hasAcailMemory()){//内存充足
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
		// 判断：当前正在展示的界面和切换目标界面是否相同
		if (currentUI != null && currentUI.getClass() == targetClazz) {
			return;
		}
		BaseUI targetUI;
		// 一旦创建过，重用
		// 判断是否创建了——曾经创建过的界面需要存储
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// 创建了，重用
			targetUI = VIEWCACHE.get(key);
		} else {
			// 否则，创建
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// 类型
				targetUI = constructor.newInstance(getContext());// 参数
				VIEWCACHE.put(key, targetUI);
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");
			}
		}

		// 当targetUI创建了，才能设置数据，判断targetUI不为空
		if (targetUI != null) {
			targetUI.setSsqBundle(ssqBundle);
		}

		// 在清理掉当前正在展示的界面之前——onPause方法
		if (currentUI != null) {
			currentUI.onPause();
		}
		Log.i(TAG, targetUI.toString());
		il_middle.removeAllViews();
		View child = targetUI.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));
		// 在加载完界面之后——onResume
		targetUI.onResume();

		currentUI = targetUI;
		// 将当前显示的界面放到栈顶
		HISTORY.addFirst(key);
		changeTitleAndBottom();
	}

	/**
	 * 切换界面:解决问题“三个容器的联动”
	 * 
	 * @param ui
	 */
	public void changeUI(Class<? extends BaseUI> targetClazz) {
		// 判断：当前正在展示的界面和切换目标界面是否相同
		if (currentUI != null && currentUI.getClass() == targetClazz) {
			return;
		}
		BaseUI targetUI;
		// 一旦创建过，重用
		// 判断是否创建了——曾经创建过的界面需要存储
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// 创建了，重用
			targetUI = VIEWCACHE.get(key);
		} else {
			// 否则，创建
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// 类型
				targetUI = constructor.newInstance(getContext());// 参数
				VIEWCACHE.put(key, targetUI);
			} catch (Exception e) {
				throw new RuntimeException("constructor new instance error");
			}
		}
		// 在清理掉当前正在展示的界面之前——onPause方法
		if (currentUI != null) {
			currentUI.onPause();
		}
		Log.i(TAG, targetUI.toString());
		il_middle.removeAllViews();
		View child = targetUI.getChild();
		il_middle.addView(child);
		child.setAnimation(AnimationUtils.loadAnimation(getContext(),
				R.anim.ia_view_change));
		// 在加载完界面之后——onResume
		targetUI.onResume();

		currentUI = targetUI;
		// 将当前显示的界面放到栈顶
		HISTORY.addFirst(key);
		changeTitleAndBottom();
	}

	private void changeTitleAndBottom() {
		// 1、界面一对应未登陆标题和通用导航
		// 2、界面二对应通用标题和玩法导航

		// 当前正在展示的如果是第一个界面
		// 方案一：
		// 存在问题，比对的依据：名称 或者 字节码
		// 在界面处理初期，将所有的界面名称确定，灵活性差，并且工作量大
		// 如果是字节码，将所有的界面都的创建完成
		// if (currentUI.getClass() == FirstUI.class) {
		// TitleManager.getInstance().showUnloginTitle();
		// BottomManager.getInstance().showCommonBottom();
		// }
		// if(currentUI.getClass().getSimpleName().equals("SecondUI")){
		// TitleManager.getInstance().showCommonTitle();
		// BottomManager.getInstance().showGameBottom();
		// }
		// 方案二：更换比对依据，耦合度太高，中间容器与标题容器与底部容器
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
		// 降低三个容器的耦合度，使容器自己管好自己的代码（容器）
		// 当中间容器变动的时候，中间容器“通知”其他的容器，你们该变动了，唯一的标示传递，其他容器依据唯一标示进行容器内容的切换
		// 通知（多个类通知信息）：
		// 1.广播：多个应用（大多适用于）
		// 2.为中间容器的变动增加了监听——观察者设计模式（（多个容器互相联动）大多适用于一个应用内部多个类之间的通知信息，降低耦合度）

		// ①将中间容器变成被观察的对象 extends observable
		// ②标题和底部导航变成观察者 implememts observer
		// ③建立观察者和被观察者之间的关系（标题和底部导航添加到观察者的容器里面）
		// ④一旦中间容器变动，修改boolean为true，然后通知所有的观察者.updata()

		setChanged();
		notifyObservers(currentUI.getID());
	}

	/**
	 * 切换界面:解决问题“中间容器中，每次切换没有判断当前正在展示和需要切换的目标是不是同一个”
	 * 
	 * @param ui
	 */
	public void changeUI3(Class<? extends BaseUI> targetClazz) {
		// 判断：当前正在展示的界面和切换目标界面是否相同
		if (currentUI != null && currentUI.getClass() == targetClazz) {
			return;
		}
		BaseUI targetUI;
		// 一旦创建过，重用
		// 判断是否创建了——曾经创建过的界面需要存储
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// 创建了，重用
			targetUI = VIEWCACHE.get(key);
		} else {
			// 否则，创建
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// 类型
				targetUI = constructor.newInstance(getContext());// 参数
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
		// 将当前显示的界面放到栈顶
		HISTORY.addFirst(key);
	}

	/**
	 * 切换界面:解决问题“在标题容器中每次点击都在创建一个目标界面” 频繁点击——界面不切换，显示内容不重复创建
	 * 
	 * @param ui
	 */
	public void changeUI2(Class<? extends BaseUI> targetClazz) {
		BaseUI targetUI;
		// 一旦创建过，重用
		// 判断是否创建了——曾经创建过的界面需要存储
		String key = targetClazz.getSimpleName();
		if (VIEWCACHE.containsKey(key)) {
			// 创建了，重用
			targetUI = VIEWCACHE.get(key);
		} else {
			// 否则，创建
			try {
				Constructor<? extends BaseUI> constructor = targetClazz
						.getConstructor(Context.class);// 类型
				targetUI = constructor.newInstance(getContext());// 参数
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
	 * 切换界面
	 */
	public void changeUI1(BaseUI ui) {
		// 1、切换界面时清理上一个显示内容
		// 切换界面的核心方法一
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
	 * 返回键处理
	 * 
	 * false 不返回
	 */
	public boolean goBack() {
		// 记录一下用户操作历史
		// 频繁操作栈顶（添加）——在界面切换成功
		// 获取栈顶
		// 删除了栈顶
		// 有序集合
		if (HISTORY.size() > 0) {
			// 当用户误操作返回键（不退出应用）
			if (HISTORY.size() == 1) {
				return false;// 只有一个界面时不remove，而是直接提醒是否退出应用
			}

			HISTORY.removeFirst();
			if (HISTORY.size() > 0) {
				String key = HISTORY.getFirst();
				BaseUI targetUI = VIEWCACHE.get(key);
				if (targetUI != null) {
					currentUI.onPause();// 清理掉当前界面
					il_middle.removeAllViews();
					il_middle.addView(targetUI.getChild());
					targetUI.getChild().setAnimation(
							AnimationUtils.loadAnimation(getContext(),
									R.anim.ia_view_change));
					targetUI.onResume();// 加载新的界面
					currentUI = targetUI;
					changeTitleAndBottom();
					return true;
				}else{
					changeUI(Hall.class);
					PromptManager.showToast(getContext(), "低内存运行");
				}
			}

		}
		return false;
	}

	public void clear() {
		HISTORY.clear();
	}

}
