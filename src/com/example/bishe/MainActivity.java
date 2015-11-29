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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";
	private Button httpsButton;

	private HttpsAsyncTask httpsTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		httpsButton = (Button) findViewById(R.id.create_https_button);
		httpsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				runHttpsConnection();
			}
		});
	}

	private void runHttpsConnection() {
		if (httpsTask == null || httpsTask.getStatus() == Status.FINISHED) {
			httpsTask = new HttpsAsyncTask(getApplicationContext());
			httpsTask.execute();
		}
	}
}