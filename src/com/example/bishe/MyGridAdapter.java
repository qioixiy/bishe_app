package com.example.bishe;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description:gridviewadapter
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridAdapter extends BaseAdapter {
	public static final String TAG = "MyGridAdapter";
	private Context mContext;
	public String[] img_text;
	public int[] imgs;

	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		img_text = mContext.getResources().getStringArray(
				R.array.home_grid_texts);
		TypedArray ar = mContext.getResources().obtainTypedArray(
				R.array.home_grid_imgs);
		int len = ar.length();
		imgs = new int[len];
		for (int i = 0; i < len; i++) {
			imgs[i] = ar.getResourceId(i, 0);
		}
		ar.recycle();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}

}
