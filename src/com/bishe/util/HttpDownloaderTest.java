package com.bishe.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @Project: Android_MyDownload
 * @Desciption: ����HttpЭ�������ļ����洢��SDCard 1.����һ��URL����
 *              2.ͨ��URL����,����һ��HttpURLConnection���� 3.�õ�InputStream
 *              4.��InputStream���ж�ȡ���� �浽SDCard 1.ȡ��SDCard·�� 2.���ö�ȡ���ļ���IO��������ȡ�ļ�
 * 
 * @Author: LinYiSong
 * @Date: 2011-3-25~2011-3-25
 */
public class HttpDownloaderTest extends Activity {

	private Button downFileBtn;
	private Button downMP3Btn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.bishe.R.layout.activity_test);

		downFileBtn = (Button) this.findViewById(com.bishe.R.id.test);
		downFileBtn.setOnClickListener(new DownFileClickListener());
	}

	/**
	 * 
	 * @Project: Android_MyDownload
	 * @Desciption: ֻ�ܶ�ȡ�ı��ļ�����ȡmp3�ļ�������ڴ��������
	 * @Author: LinYiSong
	 * @Date: 2011-3-25~2011-3-25
	 */
	class DownFileClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String urlStr = "http://172.17.54.91:8080/download/down.txt";
			try {
				/*
				 * ͨ��URLȡ��HttpURLConnection Ҫ�������ӳɹ�������AndroidMainfest.xml�н���Ȩ������
				 * <uses-permission android:name="android.permission.INTERNET"
				 * />
				 */
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// ȡ��inputStream�������ж�ȡ
				InputStream input = conn.getInputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						input));
				String line = null;
				StringBuffer sb = new StringBuffer();
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				System.out.println(sb.toString());

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @Project: Android_MyDownload
	 * @Desciption: ��ȡ�����ļ��������ļ����浽�ֻ�SDCard
	 * @Author: LinYiSong
	 * @Date: 2011-3-25~2011-3-25
	 */
	class DownMP3ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String urlStr = "http://172.17.54.91:8080/download/1.mp3";
			String path = "file";
			String fileName = "2.mp3";
			OutputStream output = null;
			try {
				/*
				 * ͨ��URLȡ��HttpURLConnection Ҫ�������ӳɹ�������AndroidMainfest.xml�н���Ȩ������
				 * <uses-permission android:name="android.permission.INTERNET"
				 * />
				 */
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// ȡ��inputStream���������е���Ϣд��SDCard

				/*
				 * дǰ׼�� 1.��AndroidMainfest.xml�н���Ȩ������ <uses-permission
				 * android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				 * ȡ��д��SDCard��Ȩ�� 2.ȡ��SDCard��·����
				 * Environment.getExternalStorageDirectory() 3.���Ҫ������ļ����Ƿ��Ѿ�����
				 * 4.�����ڣ��½��ļ��У��½��ļ� 5.��input���е���Ϣд��SDCard 6.�ر���
				 */
				String SDCard = Environment.getExternalStorageDirectory() + "";
				String pathName = SDCard + "/" + path + "/" + fileName;// �ļ��洢·��

				File file = new File(pathName);
				InputStream input = conn.getInputStream();
				if (file.exists()) {
					System.out.println("exits");
					return;
				} else {
					String dir = SDCard + "/" + path;
					new File(dir).mkdir();// �½��ļ���
					file.createNewFile();// �½��ļ�
					output = new FileOutputStream(file);
					// ��ȡ���ļ�
					byte[] buffer = new byte[4 * 1024];
					while (input.read(buffer) != -1) {
						output.write(buffer);
					}
					output.flush();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					output.close();
					System.out.println("success");
				} catch (IOException e) {
					System.out.println("fail");
					e.printStackTrace();
				}
			}
		}

	}
}