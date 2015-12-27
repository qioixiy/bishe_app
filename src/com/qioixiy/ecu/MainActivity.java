package com.qioixiy.ecu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.Common;
import com.qioixiy.R;
import com.qioixiy.service.DBMisc;
import com.qioixiy.service.DBOpenHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.AsyncTask.Status;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Description:主页
 * @author http://blog.csdn.net/finddreams
 */
public class MainActivity extends Activity {
	MainActivity activity = this;
	public static final String TAG = "MainActivity";
	static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private MyGridView gridview;
	private Handler handler;
	private HttpsAsyncPostRequest httpsRequest = null;

	private String downloadFilename;
	private boolean UpdateCheckFlag = false;

	private String download_filename;
	private String download_size;
	private String download_time;
	private String download_version;
	private String download_md5;
	private String download_date;

	private MsgReceiver msgReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Intent intent = getIntent();
		// String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		setContentView(R.layout.activity_main);
		
		msgReceiver = new MsgReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.example.communication.RECEIVER");
		registerReceiver(msgReceiver, intentFilter);
		
		initView();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					String objStr = msg.getData().getString("data");
					try {
						JSONObject jsonObj = new JSONObject(objStr);
						String username = jsonObj.getString("title");
						JSONArray jsonObjs = jsonObj.getJSONArray("arr");
						String s = "";
						ArrayList mArrayList = new ArrayList();
						for (int i = 0; i < jsonObjs.length(); i++) {
							String str = jsonObjs.optJSONArray(i).optString(0);
							Log.e(TAG, str);
							mArrayList.add(str);
						}

						Intent intent = new Intent(MainActivity.this,
								DownloadFileListView.class);
						intent.putExtra("test", "test");
						intent.putStringArrayListExtra("file_list", mArrayList);
						startActivity(intent);
					} catch (JSONException ex) {
						Log.e(TAG, ex.getMessage());
					}
					break;
				}
				super.handleMessage(msg);
			}
		};

		Intent intent = new Intent();
		// 设置Class属性
		intent.setClass(MainActivity.this, ServiceUpdate.class);
		// 启动该Service
		startService(intent);
	}

	private void GotoFileListActivity() {
		if (httpsRequest == null || httpsRequest.getStatus() == Status.FINISHED) {
			httpsRequest = new HttpsAsyncPostRequest(handler, 0);

			httpsRequest.execute(Common.ServerIp + "/client_api/file_list.php");
		}
	}

	private void GotoLocalFileListViewActivity() {
		Intent intent = new Intent(MainActivity.this, LocalFileListView.class);
		startActivity(intent);
	}

	private void UpdateCheck(boolean flag) {
		UpdateCheckFlag = flag;
		HttpsAsyncPostRequest mHttpsAsyncRequest = new HttpsAsyncPostRequest(
				new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:
							boolean downloadFlag = false;

							DBMisc dbMisc = new DBMisc(
									activity.getApplicationContext());
							SQLiteDatabase db = dbMisc.getWritableDatabase();
							Cursor cursor = db.query("updateTable", null, null,
									null, null, null, null);
							String db_filename = "";
							if (cursor.moveToFirst()) {
								for (int i = 0; i < cursor.getCount(); i++) {
									cursor.move(i);
									db_filename = cursor.getString(1);
								}
							}

							String objStr = msg.getData().getString("data");
							String filename = null;
							String size = null;
							String time = null;
							String version = null;
							String md5 = null;
							Date date = null;
							try {
								JSONObject jsonObj = new JSONObject(objStr);
								filename = jsonObj.getString("filename");
								size = jsonObj.getString("size");
								time = jsonObj.getString("time");
								version = jsonObj.getString("version");
								md5 = jsonObj.getString("md5");
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								date = df.parse(time);
								boolean newer = !db_filename.equals(filename);
								// update
								if (newer) {
									downloadFlag = true;
									downloadFilename = filename;

									download_filename = filename;
									download_size = size;
									download_time = time;
									download_version = version;
									download_md5 = md5;
									download_date = date.toString();
								}
							} catch (JSONException ex) {
								Log.e(TAG, ex.getMessage());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							db.close();
							if (UpdateCheckFlag) {
								if (downloadFlag) {
									Intent intent = new Intent(
											MainActivity.this,
											DownloadFileActivity.class);
									String url = Common.ServerIp
											+ "/main/download.php"
											+ "?filename=" + downloadFilename;
									intent.putExtra("url", url);
									intent.putExtra("fileName",
											downloadFilename);
									startActivity(intent);
									break;
								}
							}
							// 动态加载布局生成View对象
							LayoutInflater inflater = getLayoutInflater();
							View layout = inflater.inflate(
									R.layout.dialog_alert,
									(ViewGroup) findViewById(R.id.dialog_alert));
							((TextView) layout
									.findViewById(R.id.dialog_alert_textview))
									.setText("已经是最新");

							AlertDialog.Builder ab = new AlertDialog.Builder(
									activity).setTitle("检测更新").setView(layout)
									.setNegativeButton("返回", null);
							if (downloadFlag) {
								((TextView) layout
										.findViewById(R.id.dialog_alert_textview))
										.setText("发现最新版本：" + version);
								ab.setPositiveButton("下载",
										new OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												Intent intent = new Intent(
														MainActivity.this,
														DownloadFileActivity.class);
												String url = Common.ServerIp
														+ "/main/download.php"
														+ "?filename="
														+ downloadFilename;
												intent.putExtra("url", url);
												intent.putExtra("fileName",
														downloadFilename);
												ArrayList<String> al = new ArrayList<String>();

												al.add(download_filename);
												al.add(download_size);
												al.add(download_time);
												al.add(download_version);
												al.add(download_md5);
												al.add(download_date);
												intent.putStringArrayListExtra(
														"extData", al);
												startActivity(intent);
											}
										});
							}
							ab.show();
							break;
						}
						super.handleMessage(msg);
					}
				}, 0);
		mHttpsAsyncRequest.execute(Common.ServerIp
				+ "/client_api/update_check.php");
	}

	private void initView() {
		gridview = (MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this));
		// 添加点击事件
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String[] strs = getResources().getStringArray(
						R.array.home_grid_texts);
				switch (arg2) {
				case 0:
					UpdateCheck(false);
					return;
				case 1:
					GotoFileListActivity();
					return;
				case 2:
					break;
				case 3:
					UpdateCheck(true);
					return;
				case 4:
					break;
				case 5:
					GotoLocalFileListViewActivity();
					return;
				case 6:
					break;
				case 7:
					break;
				default:
					break;
				}
				Toast.makeText(MainActivity.this, "没有实现", 1).show();
			}
		});
	}

	private class MsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String s = intent.getStringExtra("msg");
			if (s.equals("true")) {
				UpdateCheck(false);
			}
		}
	}
}
