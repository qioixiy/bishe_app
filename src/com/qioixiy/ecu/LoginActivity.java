package com.qioixiy.ecu;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;

import com.common.Common;
import com.common.LoadingDialog;
import com.common.SessionManager;
import com.qioixiy.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	private HttpsAsyncPostRequest httpsRequest;
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
					String objStr = msg.getData().getString("data");
					// debugView.setText(debugView.getText() + objStr + "\n");
					try {
						JSONObject jsonObj = new JSONObject(objStr);
						String username = jsonObj.getString("username");
						String status = jsonObj.getString("status");
						String token = jsonObj.getString("token");
						String errString = jsonObj.getString("errString");
						if (status.equals("ok")) {
							SessionManager.GetInstance().setToken(token);
							Intent intent = new Intent(LoginActivity.this,
									MainActivity.class);
							String message = "test";
							intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
							startActivity(intent);
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									LoginActivity.this);
							builder.setTitle("提示");
							builder.setMessage(errString);
							// builder.setIcon(R.drawable.icon);
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dlg, int which) {
											dlg.dismiss();
										}
									});
							builder.create().show();
						}
					} catch (JSONException ex) {
						Log.e(TAG, ex.getMessage());
					}
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
		if (username.getText().toString().equals("")) {
			debugView.setText("请输入用户名");
			return;
		}
		if (password.getText().toString().equals("")) {
			debugView.setText("请输入密码");
			return;
		}
		if (httpsRequest == null || httpsRequest.getStatus() == Status.FINISHED) {
			httpsRequest = new HttpsAsyncPostRequest(handler, 0);

			httpsRequest.execute(Common.ServerIp + "/session/logincheck.php",
					"username", username.getText().toString(), "password",
					password.getText().toString());
		}

		dialog = new LoadingDialog(this);
		dialog.show();
	}
}