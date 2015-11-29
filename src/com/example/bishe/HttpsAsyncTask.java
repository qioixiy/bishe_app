package com.example.bishe;

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

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

class HttpsAsyncTask extends AsyncTask<Void, Void, Void> {

	public static final String TAG = "HttpsConnTask";

	private StringBuffer sBuffer = new StringBuffer();
	Context context = null;

	public HttpsAsyncTask(Context applicationContext) {
		context = applicationContext;
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

	@Override
	protected Void doInBackground(Void... params) {
		// final String HTTPS_EXAMPLE_URL = "https://www.baidu.com/";
		final String HTTPS_EXAMPLE_URL = "https://bishe-zxyuan.c9users.io:8080/mysql/conn.php";

		HttpPost request = new HttpPost(HTTPS_EXAMPLE_URL);
		HttpClient httpClient = HttpUtils.getHttpsClient();
		try {
			List<NameValuePair> mNameValuePair = new ArrayList<NameValuePair>();

			mNameValuePair.add(new BasicNameValuePair("UserName", "UserName"));
			mNameValuePair.add(new BasicNameValuePair("Password", "Password"));

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
			Log.e("https", e.getMessage());
		} finally {
			Log.e("finally", "finally");
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (!TextUtils.isEmpty(sBuffer.toString())) {
			if (context != null) {
				Toast.makeText(context, sBuffer.toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
