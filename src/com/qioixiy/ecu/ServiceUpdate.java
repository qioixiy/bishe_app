package com.qioixiy.ecu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.Common;
import com.qioixiy.R;
import com.qioixiy.service.DBMisc;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ServiceUpdate extends Service {
	Context context = this;
	private static final String TAG = "ServiceUpdate";
	private Intent intent = new Intent("com.example.communication.RECEIVER");
	
	Timer timer = null;
	TimerTask task = new TimerTask() {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					HttpsAsyncPostRequest mHttpsAsyncRequest = new HttpsAsyncPostRequest(
							new Handler() {
								public void handleMessage(Message msg) {
									switch (msg.what) {
									case 0:
										DBMisc dbMisc = new DBMisc(context);
										SQLiteDatabase db = dbMisc.getWritableDatabase();
										Cursor cursor = db.query("updateTable",
												null, null, null, null, null,
												null);
										String db_filename = "";
										if (cursor.moveToFirst()) {
											for (int i = 0; i < cursor
													.getCount(); i++) {
												cursor.move(i);
												db_filename = cursor
														.getString(1);
											}
										}

										String objStr = msg.getData()
												.getString("data");
										String filename = null;
										String size = null;
										String time = null;
										String version = null;
										String md5 = null;
										Date date = null;
										try {
											JSONObject jsonObj = new JSONObject(
													objStr);
											filename = jsonObj
													.getString("filename");
											size = jsonObj.getString("size");
											time = jsonObj.getString("time");
											version = jsonObj
													.getString("version");
											md5 = jsonObj.getString("md5");
											SimpleDateFormat df = new SimpleDateFormat(
													"yyyy-MM-dd HH:mm:ss");
											date = df.parse(time);
											boolean newer = !db_filename
													.equals(filename);
											// update
											if (newer) {
												intent.putExtra("msg", "true");
												sendBroadcast(intent);
												updateVerNotify();
											}
										} catch (JSONException ex) {
											Log.e(TAG, ex.getMessage());
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										db.close();
										break;
									}
									super.handleMessage(msg);
								}
							}, 0);
					mHttpsAsyncRequest.execute(Common.ServerIp
							+ "/client_api/update_check.php");
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void run() {
			Message message = new Message();
			message.what = 0;
			handler.sendMessage(message);
		}
	};

	// ����null
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "Service onBind--->");
		return null;
	}

	// Service����ʱ����
	public void onCreate() {
		Log.i(TAG, "Service onCreate--->");

		timer = new Timer(true);
		timer.schedule(task, 1000, 1000 * 60);
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
	private void updateVerNotify() {
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