package com.qioixiy.ecu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.common.Common;
import com.common.httpUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

class HttpsAsyncRequest extends AsyncTask<String, Void, String> {

	public static final String TAG = "HttpsAsyncRequest";
	private Handler handler;
	private int messageWhat;
	private StringBuffer sBuffer = new StringBuffer();

	public HttpsAsyncRequest(Handler _handler, int _messageWhat) {
		this.handler = _handler;
		this.messageWhat = _messageWhat;
	}

	private static class HttpUtils {
		public static HttpClient getHttpsClient() {
			HttpClient mHttpsClient = new DefaultHttpClient();
			SSLSocketFactory mSSLSocketFactory = SSLSocketFactory
					.getSocketFactory();
			if (mSSLSocketFactory != null) {
				Scheme mScheme = new Scheme("https", mSSLSocketFactory, 443);
				mHttpsClient.getConnectionManager().getSchemeRegistry()
						.register(mScheme);
			}
			return mHttpsClient;
		}
	}

	private String HandleSubCommandLogin(String username, String password) {
		final String HTTPS_URL = Common.ServerIp + "/session/logincheck.php";

		HttpPost request = new HttpPost(HTTPS_URL);
		HttpClient httpClient = HttpUtils.getHttpsClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				60000);

		try {
			List<NameValuePair> mNameValuePair = new ArrayList<NameValuePair>();

			mNameValuePair.add(new BasicNameValuePair("username", username));
			mNameValuePair.add(new BasicNameValuePair("password", password));
			mNameValuePair.add(new BasicNameValuePair("device", "android"));

			HttpEntity httpEntity = new UrlEncodedFormEntity(mNameValuePair,
					"utf-8");

			request.setEntity(httpEntity);

			HttpResponse httpResponse = httpClient.execute(request);
			if (httpResponse != null) {
				StatusLine statusLine = httpResponse.getStatusLine();
				if (statusLine != null
						&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(
								httpResponse.getEntity().getContent(), "UTF-8"));
						String line = null;
						while ((line = reader.readLine()) != null) {
							sBuffer.append(line);
						}
						Log.d(TAG, sBuffer.toString());
					} catch (Exception e) {
						Log.e("https", e.getMessage());
					} finally {
						if (reader != null) {
							reader.close();
							reader = null;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			Log.e(TAG, "finally");
		}

		return null;
	}

	String HandleSubCommandGetFileList(String token) {
		final String HTTPS_URL = Common.ServerIp + "/client_api/file_list.php";

		HttpPost request = new HttpPost(HTTPS_URL);
		HttpClient httpClient = HttpUtils.getHttpsClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				60000);

		try {
			List<NameValuePair> mNameValuePair = new ArrayList<NameValuePair>();

			mNameValuePair.add(new BasicNameValuePair("token", token));
			mNameValuePair.add(new BasicNameValuePair("device", "android"));

			HttpEntity httpEntity = new UrlEncodedFormEntity(mNameValuePair,
					"utf-8");

			request.setEntity(httpEntity);

			HttpResponse httpResponse = httpClient.execute(request);
			if (httpResponse != null) {
				StatusLine statusLine = httpResponse.getStatusLine();
				if (statusLine != null
						&& statusLine.getStatusCode() == HttpStatus.SC_OK) {
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(
								httpResponse.getEntity().getContent(), "UTF-8"));
						String line = null;
						while ((line = reader.readLine()) != null) {
							sBuffer.append(line);
						}
						Log.d(TAG, sBuffer.toString());
					} catch (Exception e) {
						Log.e("https", e.getMessage());
					} finally {
						if (reader != null) {
							reader.close();
							reader = null;
						}
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} finally {
			Log.e(TAG, "finally");
		}

		return null;
	}

	@Override
	protected String doInBackground(String... params) {
		String subCommand = params[0];

		if (subCommand.equals("logincheck")) {
			if (params.length == 3) {
				return HandleSubCommandLogin(params[1], params[2]);
			}
		} else if (subCommand.equals("file_list")) {
			if (params.length == 2) {
				HandleSubCommandGetFileList("token");
			}
		} else {
			try {
				String s = httpUtils
						.doHttpsPost(
								Common.ServerIp + "/session/logincheck.php",
								"device=android&username=test&password=password");
				Log.d(TAG, s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e(TAG, e.toString());
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (!TextUtils.isEmpty(sBuffer.toString())) {
			Message message = new Message();
			message.what = messageWhat;
			Bundle bundle = new Bundle();
			bundle.putString("data", sBuffer.toString());
			message.setData(bundle);
			handler.sendMessage(message);
		}
	}
}
