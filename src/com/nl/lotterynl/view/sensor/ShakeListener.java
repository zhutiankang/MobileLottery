package com.nl.lotterynl.view.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * 处理传感器的监听
 * 
 * @author 追梦
 * 
 */
public abstract class ShakeListener implements SensorEventListener {

	// 方式二：获取三个轴的加速度值，记录，当过一段时间之后再次获取三个轴的加速度值，计算增量，将相邻两个点的增量进行汇总，当达到设置好的阈值

	// ①记录第一个数据：三个轴的加速度，为了屏蔽掉不同手机采样的时间间隔，记录第一个点的时间
	// ②当有新的传感器数据传递后，判断时间间隔（用当前时间与第一个采样时间进行比对，如果满足了时间间隔要求，认为是合格的第二个点，否则舍弃该数据包）
	//   进行增量的计算：获取到新的加速度值与第一个点上存储的进行差值运算，获取到一点和二点之间的增量
	// ③以此类推，获取到相邻两个点的增量，一次汇总
	// ④通过汇总值与设定好的阈值比对，如果大于等于，用户摇晃手机，否则继续记录当前的数据（加速度值和时间）
	
	private Context context;
	private Vibrator vibrator;
	
	public ShakeListener(Context context) {
		super();
		this.context = context;
		this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	private float lastX;
	private float lastY;
	private float lastZ;
	private long lastTime;
	
	//时间间隔
	private long duration = 100;
	//累加
	private float total;
	//判断手机摇晃的阈值
	private float switchValue = 200;
	@Override
	public void onSensorChanged(SensorEvent event) {
		//①
		//判断是否是第一个点，开始采样前，时间为零，之后记录的点就为第一个点
		if(lastTime == 0){
			lastX = event.values[SensorManager.DATA_X];
			lastY = event.values[SensorManager.DATA_Y];
			lastZ = event.values[SensorManager.DATA_Z];
			lastTime = System.currentTimeMillis();
		}else{
			//②
			//第二个点及以后的
			long currentTime = System.currentTimeMillis();
			if((currentTime-lastTime)>=duration){
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];
				
				float dx = Math.abs(x-lastX);
				float dy = Math.abs(y-lastY);
				float dz = Math.abs(z-lastZ);
				//优化，避免微小的增量
				if(dx<1){
					dx = 0;
				}
				if(dy<1){
					dy = 0;
				}
				if(dz<1){
					dz = 0;
				}
				//极个别手机，静止某个轴的增量大于1，10以上，100以上
//				if(dx==0 || dy==0 || dz==0){
//					init();
//				}
				
				//获取到一点和二点之间的增量
				float shake = dx + dy + dz;
				
				if(shake==0){
					init();
				}
				
				
				//③
				//所有增量，一次汇总
				total += shake;
				//④通过汇总值与设定好的阈值比对
				if(total>=switchValue){
					//手机摇晃处理
					//1.机选一注彩票
					randomCure();
					//2.提示用户（声音或者振动）
					vibrator.vibrate(100);
					//3.所有数据都要初始化（这一注已经选完了，数据清空初始化）
					init();
				}else{
					//不行，接着下面的三四点等
					lastX = event.values[SensorManager.DATA_X];
					lastY = event.values[SensorManager.DATA_Y];
					lastZ = event.values[SensorManager.DATA_Z];
					lastTime = System.currentTimeMillis();
				}
			}
		}
	}
	/**
	 * 机选一注彩票
	 */
	public abstract void randomCure();

	private void init() {
		 lastX = 0;
		 lastY = 0;
		 lastZ = 0;
		 lastTime = 0;
		 total = 0;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

}
