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
 * @Desciption: 利用Http协议下载文件并存储到SDCard 1.创建一个URL对象
 *              2.通过URL对象,创建一个HttpURLConnection对象 3.得到InputStream
 *              4.从InputStream当中读取数据 存到SDCard 1.取得SDCard路径 2.利用读取大文件的IO读法，读取文件
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
	 * @Desciption: 只能读取文本文件，读取mp3文件会出现内存溢出现象
	 * @Author: LinYiSong
	 * @Date: 2011-3-25~2011-3-25
	 */
	class DownFileClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String urlStr = "http://172.17.54.91:8080/download/down.txt";
			try {
				/*
				 * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
				 * <uses-permission android:name="android.permission.INTERNET"
				 * />
				 */
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 取得inputStream，并进行读取
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
	 * @Desciption: 读取任意文件，并将文件保存到手机SDCard
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
				 * 通过URL取得HttpURLConnection 要网络连接成功，需在AndroidMainfest.xml中进行权限配置
				 * <uses-permission android:name="android.permission.INTERNET"
				 * />
				 */
				URL url = new URL(urlStr);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 取得inputStream，并将流中的信息写入SDCard

				/*
				 * 写前准备 1.在AndroidMainfest.xml中进行权限配置 <uses-permission
				 * android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				 * 取得写入SDCard的权限 2.取得SDCard的路径：
				 * Environment.getExternalStorageDirectory() 3.检查要保存的文件上是否已经存在
				 * 4.不存在，新建文件夹，新建文件 5.将input流中的信息写入SDCard 6.关闭流
				 */
				String SDCard = Environment.getExternalStorageDirectory() + "";
				String pathName = SDCard + "/" + path + "/" + fileName;// 文件存储路径

				File file = new File(pathName);
				InputStream input = conn.getInputStream();
				if (file.exists()) {
					System.out.println("exits");
					return;
				} else {
					String dir = SDCard + "/" + path;
					new File(dir).mkdir();// 新建文件夹
					file.createNewFile();// 新建文件
					output = new FileOutputStream(file);
					// 读取大文件
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