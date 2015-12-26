package com.qioixiy.ecu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.common.Common;
import com.qioixiy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * @Description:主页
 * @author http://blog.csdn.net/finddreams
 */
public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";
	static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private MyGridView gridview;
	private Handler handler;
	private HttpsAsyncPostRequest httpsRequest = null;

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

	private void CheckUpdate() {
		// client_api/update_check.php;
		HttpsAsyncPostRequest mHttpsAsyncRequest = new HttpsAsyncPostRequest(
				new Handler() {
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 0:

							break;
						}
						super.handleMessage(msg);
					}
				}, 0);
		String token = "";
		mHttpsAsyncRequest.execute("update_check", token);
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
					CheckUpdate();
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
