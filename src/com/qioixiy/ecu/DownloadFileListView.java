/**
 * 
 */
package com.qioixiy.ecu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.common.httpUtils;
import com.qioixiy.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DownloadFileListView extends ListActivity {
	private Handler handler;
	private static final String TAG = "DownloadFileListView";
	private List<Map<String, Object>> mData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		ArrayList<?> mArrayList = intent.getStringArrayListExtra("file_list");
		mData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mArrayList.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			String title = (String) mArrayList.get(i);
			String info = (String) mArrayList.get(i);
			int img = R.drawable.icon;

			map.put("title", title);
			map.put("info", info);
			map.put("img", img);

			mData.add(map);
		}
		DownloadFileListViewAdapter adapter = new DownloadFileListViewAdapter(
				this);
		setListAdapter(adapter);
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0:
					String url = msg.getData().getString("url");
					String fileName = msg.getData().getString("fileName");
					Intent intent = new Intent(DownloadFileListView.this,
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

	public class DownloadFileListViewAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public DownloadFileListViewAdapter(Context context) {
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

				convertView = mInflater.inflate(R.layout.viewlist, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				holder.viewBtn = (Button) convertView
						.findViewById(R.id.view_btn);
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
					Button btn = (Button) v;
					String fileName = (String) btn.getTag();
					urlIntentDownloadFileActivity(httpUtils.hostname + "/main/download.php"
					+ "?filename=" + fileName,
					fileName);
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
}
