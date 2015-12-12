package com.example.bishe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		startMainAvtivity();
	}

	private void startMainAvtivity() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();// ������Activity
			}
		}, 1000);// ����ִ��ʱ��
	}
}