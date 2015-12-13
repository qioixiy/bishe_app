package com.qioixiy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpDownloader extends AsyncTask<String, Void, String> {
	private static final String TAG = "HttpDownloader";
	private URL url = null;
	private Handler handler;

	public HttpDownloader(Handler _handler)
	{
		handler = _handler;
	}
	/**
	 * ����URL�����ļ���ǰ��������ļ����е��������ı��������ķ���ֵ�����ļ����е����� 1.����һ��URL����
	 * 2.ͨ��URL���󣬴���һ��HttpURLConnection���� 3.�õ�InputStram 4.��InputStream���ж�ȡ����
	 * 
	 * @param urlStr
	 * @return
	 */
	public String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// ����һ��URL����
			url = new URL(urlStr);
			// ����һ��Http����
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// ʹ��IO����ȡ����
			buffer = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			Log.e(TAG, "download:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				Log.e(TAG, "download buffer.close():" + e.getMessage());
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * �ú����������� -1�����������ļ����� 0�����������ļ��ɹ� 1�������ļ��Ѿ�����
	 */
	public int downFile(String urlStr, String path, String fileName) {
		InputStream inputStream = null;
		try {
			FileUtils fileUtils = new FileUtils();

			if (fileUtils.isFileExist(path + fileName)) {
				return 1;
			} else {
				inputStream = getInputStreamFromUrl(urlStr);
				File resultFile = fileUtils.write2SDFromInput(path, fileName,
						inputStream);
				if (resultFile == null) {
					return -1;
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "downFile:" + e.getMessage());
			e.printStackTrace();
			return -1;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				Log.e(TAG, "downFile close:" + e.getMessage());
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * ����URL�õ�������
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public InputStream getInputStreamFromUrl(String urlStr)
			throws MalformedURLException, IOException {
		url = new URL(urlStr);
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = urlConn.getInputStream();
		return inputStream;
	}

	@Override
	protected String doInBackground(String... arg0) {
		downFile("http://www.cnblogs.com/zhuawang/p/3648551.html", "voa/",
				"a.html");
		return download(arg0[0]);
	}
	@Override
	protected void onPostExecute(String result) {
		Log.e(TAG, result);
		Message message = new Message();
		message.what = 0;
		Bundle bundle=new Bundle(); 
		bundle.putString("html", result.toString());
		message.setData(bundle);
		handler.sendMessage(message);
	}
}