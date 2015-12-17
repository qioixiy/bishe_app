package com.qioixiy.ecu;

import android.os.Bundle;

import com.common.LoadingDialog;
import com.qioixiy.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	public static final String TAG = "LoginActivity";
	private HttpsAsyncRequest httpsRequest;
	private Handler handler;
	private TextView debugView;
	private LoadingDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		debugView = (TextView) findViewById(R.id.debugView);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					debugView.setText(debugView.getText()
							+ msg.getData().getString("html") + "\n");
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					String message = "test";
					intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
					startActivity(intent);
					dialog.hide();
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	public void login(View view) {
		GetTokenWithHttps();
	}

	private void GetTokenWithHttps() {
		EditText username = (EditText) findViewById(R.id.accountEdittext);
		EditText password = (EditText) findViewById(R.id.pwdEdittext);
		String params0 = "logincheck";
		String params1 = username.getText().toString();
		String params2 = password.getText().toString();
		Log.d(TAG, params1);
		Log.d(TAG, params2);
		if (params0.equals("")) {
			debugView.setText("请输入用户名");
			return;
		}
		if (params1.equals("")) {
			debugView.setText("请输入密码");
			return;
		}
		if (httpsRequest == null || httpsRequest.getStatus() == Status.FINISHED) {
			httpsRequest = new HttpsAsyncRequest(handler);
			httpsRequest.execute(params0, params1, params2);
		}

		dialog = new LoadingDialog(this);
		dialog.show();
	}
}