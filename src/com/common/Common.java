package com.common;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
	public static final String ServerIp = "http://47.88.50.45";

	public static int GetNetWorkInfoType(Context context) {
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info == null) {
			// 跳转到无线网络设置界面
			// startActivity(new
			// Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			// 跳转到无限wifi网络设置界面
			// startActivity(new
			// Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
			return 0;
		}

		return info.getType();
	}

	public static boolean NetWorkIsWifi(Context context) {
		return GetNetWorkInfoType(context) == ConnectivityManager.TYPE_WIFI;
	}

	public static boolean NetWorkIsMobile(Context context) {
		return GetNetWorkInfoType(context) == ConnectivityManager.TYPE_MOBILE;
	}
}
