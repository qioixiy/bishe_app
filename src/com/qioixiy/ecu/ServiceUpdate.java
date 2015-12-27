package com.qioixiy.ecu;

import com.qioixiy.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceUpdate extends Service {

	private static final String TAG = "ServiceUpdate";

	// 返回null
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Service onBind--->");
		return null;
	}

	// Service创建时调用
	public void onCreate() {
		Log.i(TAG, "Service onCreate--->");
		startUpdateCheckTimer();
	}

	// 当客户端调用startService()方法启动Service时，该方法被调用
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Service onStart--->");
	}

	// 当Service不再使用时调用
	public void onDestroy() {
		Log.i(TAG, "Service onDestroy--->");
	}

	// 当解除绑定时调用
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "Service onUnbind--->");
		return super.onUnbind(intent);
	}
	

	@SuppressWarnings("deprecation")
	private void startUpdateCheckTimer() {
		// 声明通知（消息）管理器
		NotificationManager m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// 点击通知时转移内容
		Intent m_Intent = new Intent(ServiceUpdate.this, MainActivity.class);

		// 主要是设置点击通知时显示内容的类
		PendingIntent m_PendingIntent = PendingIntent.getActivity(
				ServiceUpdate.this, 0, m_Intent, 0); // 如果轉移內容則用m_Intent();

		// 声明构造Notification对象
		Notification m_Notification = new Notification();
		// 设置通知在状态栏显示的图标
		m_Notification.icon = R.drawable.icon;
		// 当我们点击通知时显示的内容
		m_Notification.tickerText = "更新通知";
		// 通知时发出默认的声音
		m_Notification.defaults = Notification.DEFAULT_SOUND;
		// 设置通知显示的参数
		m_Notification.setLatestEventInfo(ServiceUpdate.this, "ecu", "发现新版本",
				m_PendingIntent);
		// 可以理解为执行这个通知
		m_NotificationManager.notify(0, m_Notification);
	}
}