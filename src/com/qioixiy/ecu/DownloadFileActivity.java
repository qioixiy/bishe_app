package com.qioixiy.ecu;

import java.io.File;
import java.util.ArrayList;

import com.qioixiy.R;
import com.qioixiy.FileDownloader.DownloadProgressListener;
import com.qioixiy.FileDownloader.FileDownloader;
import com.qioixiy.service.DBMisc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadFileActivity extends Activity {
	protected static final String TAG = "DownloadFileActivity";
	DownloadFileActivity activity = this;
	private String downloadPath = null;
	private String fileName = null;
	private EditText downloadpathText;
	private TextView resultView;
	private ProgressBar progressBar;

	private String download_filename;
	private String download_size;
	private String download_time;
	private String download_version;
	private String download_date;

	/**
	 * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息 消息队列中的消息由当前线程内部进行处理
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressBar.setProgress(msg.getData().getInt("size"));
				float num = (float) progressBar.getProgress()
						/ (float) progressBar.getMax();
				int result = (int) (num * 100);
				resultView.setText(result + "%");

				if (progressBar.getProgress() == progressBar.getMax()) {
					Toast.makeText(DownloadFileActivity.this,
							fileName + "下载成功", 1).show();

					if (null != download_filename) {
						DBMisc dbMisc = new DBMisc(
								activity.getApplicationContext());
						SQLiteDatabase db = dbMisc.getWritableDatabase();
						final ContentValues cv = new ContentValues();
						cv.put("filename", download_filename);
						cv.put("size", download_size);
						cv.put("time", download_time);
						cv.put("version", download_version);
						cv.put("date", download_date);
						int res = db.update("updateTable", cv, "filename='"
								+ download_filename + "'", null);
						if (res == 0) {
							long res1 = db.insert("updateTable", null, cv);
							if (res1 == -1) {
								Toast.makeText(activity, "更新下载记录失败",
										Toast.LENGTH_SHORT).show();
							}
						}
					}
					DownloadFileActivity.this.finish();
				}
				break;
			case -1:
				Toast.makeText(DownloadFileActivity.this, R.string.error, 1)
						.show();
				break;
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		String fileName = intent.getStringExtra("fileName");

		ArrayList<String> as = intent.getStringArrayListExtra("extData");
		if (null != as) {
			download_filename = as.get(0);
			download_size = as.get(1);
			download_time = as.get(2);
			download_version = as.get(3);
			download_date = as.get(4);
		}

		progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
		resultView = (TextView) this.findViewById(R.id.resultView);

		downloadPath = getExternalFilesDir("download").getAbsolutePath();
		this.fileName = fileName;
		DownloadStart(url, fileName);
	}

	void DownloadStart(String url, String fileName) {

		Log.d(TAG, Environment.getExternalStorageState()
				+ Environment.MEDIA_MOUNTED);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			download(url, downloadPath, fileName);
		} else {
			Toast.makeText(DownloadFileActivity.this, R.string.sdcarderror, 1)
					.show();
		}
	}

	/**
	 * 主线程(UI线程) 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
	 * 
	 * @param path
	 * @param savedir
	 */
	private void download(final String path, final String savedir,
			final String saveFileName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileDownloader loader = new FileDownloader(
						DownloadFileActivity.this, path, savedir, 3,
						saveFileName);
				progressBar.setMax(loader.getFileSize());// 设置进度条的最大刻度为文件的长度

				try {
					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {// 实时获知文件已经下载的数据长度
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);// 发送消息
						}
					});
				} catch (Exception e) {
					handler.obtainMessage(-1).sendToTarget();
				}
			}
		}).start();
	}
}