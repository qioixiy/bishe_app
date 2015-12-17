package com.qioixiy.ecu;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qioixiy.R;
import com.qioixiy.test.ListViewTest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
	private HttpsAsyncRequest httpsRequest = null;

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
								ListViewTest.class);
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

	private void initView() {
		gridview = (MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this));
		// 添加点击事件
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int index = arg2 + 1;
				Toast.makeText(getApplicationContext(), "你按下了选项：" + index,
						Toast.LENGTH_LONG).show();

				if (httpsRequest == null
						|| httpsRequest.getStatus() == Status.FINISHED) {
					httpsRequest = new HttpsAsyncRequest(handler, 0);
					String token = "";
					httpsRequest.execute("file_list", token);
				}
			}
		});
	}
}
