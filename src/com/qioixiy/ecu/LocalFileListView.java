package com.qioixiy.ecu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.common.Common;
import com.qioixiy.R;
import com.qioixiy.service.DBMisc;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LocalFileListView extends ListActivity {
	private Handler handler;
	private static final String TAG = "LocalFileListView";
	private List<Map<String, Object>> mData;
	private LocalFileListView activity = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = new ArrayList<Map<String, Object>>();
		String path = getExternalFilesDir("download").toString();
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++) {
			String file = files[i].toString();
			String fileName = getFileName(file);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", fileName);
			map.put("info", fileName);
			map.put("img", R.drawable.app_local);
			mData.add(map);
		}
		Log.d(TAG, "done");

		LocalFileListViewAdapter adapter = new LocalFileListViewAdapter(this);
		setListAdapter(adapter);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					String url = msg.getData().getString("url");
					String fileName = msg.getData().getString("fileName");
					Intent intent = new Intent(LocalFileListView.this,
							DownloadFileActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("fileName", fileName);
					startActivity(intent);
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.v(TAG, (String) mData.get(position).get("title"));
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView info;
		public Button viewBtn;
	}

	public class LocalFileListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public LocalFileListViewAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.localfile_viewlist,
						null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				holder.viewBtn = (Button) convertView
						.findViewById(R.id.btn_viewlist_local);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setBackgroundResource((Integer) mData.get(position).get(
					"img"));
			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));

			holder.viewBtn.setTag((String) mData.get(position).get("title"));
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					DBMisc dbMisc = new DBMisc(activity.getApplicationContext());
					SQLiteDatabase db = dbMisc.getWritableDatabase();
					Cursor cursor = db.query("updateTable", null, null, null,
							null, null, null);
					String db_filename = "";
					String db_size = "";
					String db_time = "";
					String db_version = "";
					String db_md5 = "";
					String db_date = "";
					if (cursor.moveToFirst()) {
						for (int i = 0; i < cursor.getCount(); i++) {
							cursor.move(i);
							db_filename = cursor.getString(1);
							db_size = cursor.getString(2);
							db_time = cursor.getString(3);
							db_version = cursor.getString(4);
							db_md5 = cursor.getString(5);
							db_date = cursor.getString(6);
						}
					}

					LayoutInflater inflater = getLayoutInflater();
					View layout = inflater.inflate(R.layout.dialog_alert,
							(ViewGroup) findViewById(R.id.dialog_alert));
					AlertDialog.Builder ab = new AlertDialog.Builder(activity)
							.setTitle("版本信息").setView(layout)
							.setNegativeButton("返回", null);

					((TextView) layout.findViewById(R.id.dialog_alert_textview))
							.setText("filename: " + db_filename + "\nsize: "
									+ db_size + " byte" + "\ntime: " + db_time
									+ "\nversion: " + db_version + "\nmd5: "
									+ db_md5 + "\ndate: " + db_date);
					ab.show();

				}
			});

			return convertView;
		}

		public void urlIntentDownloadFileActivity(String url, String fileName) {

			Message message = new Message();
			message.what = 0;
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			bundle.putString("fileName", fileName);
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}

	private String getFileName(String pathandname) {

		int start = pathandname.lastIndexOf("/");
		int end = pathandname.lastIndexOf(".");
		int len = pathandname.length();
		if (start != -1 && end != -1) {
			return pathandname.substring(start + 1, len);
		} else {
			return null;
		}

	}
}
