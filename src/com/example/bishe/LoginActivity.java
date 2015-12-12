package com.example.bishe;

import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LoginActivity extends Activity {
	public static final String TAG = "MainActivity";
	private Button httpsButton;
	private HttpsAsyncTask httpsTask;
	private Handler handler;
	private TextView debugView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		debugView = (TextView)findViewById(R.id.debugView);
		httpsButton = (Button) findViewById(R.id.login);
		httpsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				runHttpsConnection();
			}
		});
		
		handler = new Handler() {
	          public void handleMessage(Message msg) {
	               switch (msg.what) {
	                    case 0:
	                    	debugView.setText(debugView.getText()
	                    			+ msg.getData().getString("html")
	                    			+ "\n");
	                    	break;
	               }
	               super.handleMessage(msg);
	          }
	     };
	}

	private void runHttpsConnection() {
		if (httpsTask == null || httpsTask.getStatus() == Status.FINISHED) {
			httpsTask = new HttpsAsyncTask(handler);
			EditText username = (EditText)findViewById(R.id.accountEdittext);
			EditText password = (EditText)findViewById(R.id.pwdEdittext);
			
			String params0 = username.getText().toString();
			String params1 = password.getText().toString();
			Log.d(TAG, params0);
			Log.d(TAG, params1);
			httpsTask.execute(params0, params1);
		}
	}
}