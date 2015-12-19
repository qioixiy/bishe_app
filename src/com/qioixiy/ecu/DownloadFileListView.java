/**
 * 
 */
package com.qioixiy.ecu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qioixiy.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author allin
 * 
 */
public class DownloadFileListView extends ListActivity {

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
		// mData = getData();
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "v0.1");
		map.put("info", "v0.1.dat");
		map.put("img", R.drawable.icon);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "v0.2");
		map.put("info", "v0.2.dat");
		map.put("img", R.drawable.icon);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "v0.3");
		map.put("info", "v0.3.dat");
		map.put("img", R.drawable.icon);
		list.add(map);

		return list;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Log.v("MyListView4-click", (String) mData.get(position).get("title"));
	}

	public void showInfo() {
		new AlertDialog.Builder(this).setTitle("请确认是否下载").setMessage("......")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView info;
		public Button viewBtn;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
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

			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					showInfo();
				}
			});

			return convertView;
		}
	}
}
