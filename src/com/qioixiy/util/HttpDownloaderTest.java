package com.qioixiy.util;

import com.qioixiy.R;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HttpDownloaderTest extends Activity {
	private Button downloadTxtButton;
	private TextView testDebugView;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		downloadTxtButton = (Button) findViewById(R.id.test);
		downloadTxtButton.setOnClickListener(new DownloadTxtListener());
		testDebugView = (TextView) findViewById(R.id.testDebugView);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					testDebugView.setText(testDebugView.getText()
							+ msg.getData().getString("html") + "\n");
					break;
				}
				super.handleMessage(msg);
			}
		};
		/*
		 * if (savedInstanceState == null) {
		 * getSupportFragmentManager().beginTransaction() .add(R.id.container,
		 * new PlaceholderFragment()).commit(); }
		 */
	}

	class DownloadTxtListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			testDebugView.setText("...");
			String url = "http://www.cnblogs.com/zhuawang/p/3648551.html";
			url = "http://http://www.csdn.net/";
			// url = "https://www.baidu.com/";
			// url = "https://bishe-zxyuan.c9users.io/client_api/file_list.php";
			HttpDownloader httpDownloader = new HttpDownloader(handler);
			httpDownloader.execute(url);
		}
	}

	class DownloadMp3Listener implements OnClickListener {
		@Override
		public void onClick(View v) {
			HttpDownloader httpDownloader = new HttpDownloader(handler);
			int ret = httpDownloader.downFile(
					"http://www.cnblogs.com/zhuawang/p/3648551.html", "voa/",
					"a.html");
			System.out.println(ret);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment, container,
					false);
			return rootView;
		}
	}

}