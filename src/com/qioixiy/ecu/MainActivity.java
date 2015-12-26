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
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Intent intent = getIntent();
		// String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		setContentView(R.layout.activity_main);
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

	private void UpdateCheck() {
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
							String db_size = null;
							String db_time = null;
							String db_version = null;
							String db_date = null;
							if (cursor.moveToFirst()) {
								for (int i = 0; i < cursor.getCount(); i++) {
									cursor.move(i);
									int db_id = cursor.getInt(0);
									db_filename = cursor.getString(1);
									db_size = cursor.getString(2);
									db_time = cursor.getString(3);
									db_version = cursor.getString(4);
									db_date = cursor.getString(5);
								}
							}

							String objStr = msg.getData().getString("data");
							String filename = null;
							String size = null;
							String time = null;
							String version = null;
							Date date = null;
							try {
								JSONObject jsonObj = new JSONObject(objStr);
								filename = jsonObj.getString("filename");
								size = jsonObj.getString("size");
								time = jsonObj.getString("time");
								version = jsonObj.getString("version");
								SimpleDateFormat df = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								date = df.parse(time);
								boolean newer = !db_filename.equals(filename);
								newer = true;
								// update
								if (newer) {
									downloadFlag = true;
									downloadFilename = filename;
									final ContentValues cv = new ContentValues();
									cv.put("filename", filename);
									cv.put("size", size);
									cv.put("time", time);
									cv.put("version", version);
									cv.put("date", date.toString());
									int res = db
											.update("updateTable", cv,
													"filename='" + filename
															+ "'", null);
									if (res == 0) {
										long res1 = db.insert("updateTable",
												null, cv);
										if (res1 == -1) {
											Toast.makeText(activity,
													"更新下载记录失败",
													Toast.LENGTH_SHORT).show();
										}
									}
								}
							} catch (JSONException ex) {
								Log.e(TAG, ex.getMessage());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// 动态加载布局生成View对象
							LayoutInflater inflater = getLayoutInflater();
							View layout = inflater
									.inflate(
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
														+ "?filename=" + downloadFilename;
												intent.putExtra("url", url);
												intent.putExtra("fileName",
														downloadFilename);
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
					UpdateCheck();
					return;
				case 1:
					GotoFileListActivity();
					return;
				case 2:
					break;
				case 3:
					break;
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
}
