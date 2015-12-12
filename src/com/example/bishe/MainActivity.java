package com.example.bishe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @Description:ึ๗าณ
 * @author http://blog.csdn.net/finddreams
 */
public class MainActivity extends Activity {
	static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	private MyGridView gridview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		gridview = (MyGridView) findViewById(R.id.gridview);
		gridview.setAdapter(new MyGridAdapter(this));

	}
}
