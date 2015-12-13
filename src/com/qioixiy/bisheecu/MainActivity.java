package com.qioixiy.bisheecu;

import com.qioixiy.R;

import android.app.Activity;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Intent intent = getIntent();
		// String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		setContentView(R.layout.activity_main);
		initView();
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
			}
		});
	}
}
