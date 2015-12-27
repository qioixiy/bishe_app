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

	// ����null
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Service onBind--->");
		return null;
	}

	// Service����ʱ����
	public void onCreate() {
		Log.i(TAG, "Service onCreate--->");
		startUpdateCheckTimer();
	}

	// ���ͻ��˵���startService()��������Serviceʱ���÷���������
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "Service onStart--->");
	}

	// ��Service����ʹ��ʱ����
	public void onDestroy() {
		Log.i(TAG, "Service onDestroy--->");
	}

	// �������ʱ����
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "Service onUnbind--->");
		return super.onUnbind(intent);
	}
	

	@SuppressWarnings("deprecation")
	private void startUpdateCheckTimer() {
		// ����֪ͨ����Ϣ��������
		NotificationManager m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// ���֪ͨʱת������
		Intent m_Intent = new Intent(ServiceUpdate.this, MainActivity.class);

		// ��Ҫ�����õ��֪ͨʱ��ʾ���ݵ���
		PendingIntent m_PendingIntent = PendingIntent.getActivity(
				ServiceUpdate.this, 0, m_Intent, 0); // ����D�ƃ��݄t��m_Intent();

		// ��������Notification����
		Notification m_Notification = new Notification();
		// ����֪ͨ��״̬����ʾ��ͼ��
		m_Notification.icon = R.drawable.icon;
		// �����ǵ��֪ͨʱ��ʾ������
		m_Notification.tickerText = "����֪ͨ";
		// ֪ͨʱ����Ĭ�ϵ�����
		m_Notification.defaults = Notification.DEFAULT_SOUND;
		// ����֪ͨ��ʾ�Ĳ���
		m_Notification.setLatestEventInfo(ServiceUpdate.this, "ecu", "�����°汾",
				m_PendingIntent);
		// �������Ϊִ�����֪ͨ
		m_NotificationManager.notify(0, m_Notification);
	}
}