package com.nl.lotterynl.view.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

/**
 * ���������ļ���
 * 
 * @author ׷��
 * 
 */
public abstract class ShakeListener implements SensorEventListener {

	// ��ʽ������ȡ������ļ��ٶ�ֵ����¼������һ��ʱ��֮���ٴλ�ȡ������ļ��ٶ�ֵ��������������������������������л��ܣ����ﵽ���úõ���ֵ

	// �ټ�¼��һ�����ݣ�������ļ��ٶȣ�Ϊ�����ε���ͬ�ֻ�������ʱ��������¼��һ�����ʱ��
	// �ڵ����µĴ��������ݴ��ݺ��ж�ʱ�������õ�ǰʱ�����һ������ʱ����бȶԣ����������ʱ����Ҫ����Ϊ�Ǻϸ�ĵڶ����㣬�������������ݰ���
	//   ���������ļ��㣺��ȡ���µļ��ٶ�ֵ���һ�����ϴ洢�Ľ��в�ֵ���㣬��ȡ��һ��Ͷ���֮�������
	// ���Դ����ƣ���ȡ�������������������һ�λ���
	// ��ͨ������ֵ���趨�õ���ֵ�ȶԣ�������ڵ��ڣ��û�ҡ���ֻ������������¼��ǰ�����ݣ����ٶ�ֵ��ʱ�䣩
	
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
	
	//ʱ����
	private long duration = 100;
	//�ۼ�
	private float total;
	//�ж��ֻ�ҡ�ε���ֵ
	private float switchValue = 200;
	@Override
	public void onSensorChanged(SensorEvent event) {
		//��
		//�ж��Ƿ��ǵ�һ���㣬��ʼ����ǰ��ʱ��Ϊ�㣬֮���¼�ĵ��Ϊ��һ����
		if(lastTime == 0){
			lastX = event.values[SensorManager.DATA_X];
			lastY = event.values[SensorManager.DATA_Y];
			lastZ = event.values[SensorManager.DATA_Z];
			lastTime = System.currentTimeMillis();
		}else{
			//��
			//�ڶ����㼰�Ժ��
			long currentTime = System.currentTimeMillis();
			if((currentTime-lastTime)>=duration){
				float x = event.values[SensorManager.DATA_X];
				float y = event.values[SensorManager.DATA_Y];
				float z = event.values[SensorManager.DATA_Z];
				
				float dx = Math.abs(x-lastX);
				float dy = Math.abs(y-lastY);
				float dz = Math.abs(z-lastZ);
				//�Ż�������΢С������
				if(dx<1){
					dx = 0;
				}
				if(dy<1){
					dy = 0;
				}
				if(dz<1){
					dz = 0;
				}
				//�������ֻ�����ֹĳ�������������1��10���ϣ�100����
//				if(dx==0 || dy==0 || dz==0){
//					init();
//				}
				
				//��ȡ��һ��Ͷ���֮�������
				float shake = dx + dy + dz;
				
				if(shake==0){
					init();
				}
				
				
				//��
				//����������һ�λ���
				total += shake;
				//��ͨ������ֵ���趨�õ���ֵ�ȶ�
				if(total>=switchValue){
					//�ֻ�ҡ�δ���
					//1.��ѡһע��Ʊ
					randomCure();
					//2.��ʾ�û������������񶯣�
					vibrator.vibrate(100);
					//3.�������ݶ�Ҫ��ʼ������һע�Ѿ�ѡ���ˣ�������ճ�ʼ����
					init();
				}else{
					//���У�������������ĵ��
					lastX = event.values[SensorManager.DATA_X];
					lastY = event.values[SensorManager.DATA_Y];
					lastZ = event.values[SensorManager.DATA_Z];
					lastTime = System.currentTimeMillis();
				}
			}
		}
	}
	/**
	 * ��ѡһע��Ʊ
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
