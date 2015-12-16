/*
 * Copyright (c) 2014, �ൺ˾ͨ�Ƽ����޹�˾ All rights reserved.
 * File Name��LoadingDialog.java
 * Version��V1.0
 * Author��zhaokaiqiang
 * Date��2014-10-24
 */

package com.common;

import java.util.Timer;
import java.util.TimerTask;

import com.qioixiy.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 
 * @ClassName: com.example.animationloading.LoadingDialog
 * @Description: ��������Dialog
 * @author zhaokaiqiang
 * @date 2014-10-27 ����4:42:52
 * 
 */
public class LoadingDialog extends Dialog {

	protected static final String TAG = "LoadingDialog";
	// �������
	private static final int DURATION = 800;
	// ǰ��ͼƬ
	private ImageView img_front;
	// ��ʱ�����������ϵĲ��Ŷ���
	private Timer animationTimer;
	// ��ת����
	private RotateAnimation animationL2R;

	private Animation animation;

	private Context context;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			img_front.setAnimation(animationL2R);
			animationL2R.start();
			// img_front.setAnimation(animation);
			// animation.start();
		};

	};

	public LoadingDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);

		img_front = (ImageView) findViewById(R.id.img_front);
		animationTimer = new Timer();

		// �����ҵ���ת������������ת�ǶȺ���ת����
		animationL2R = new RotateAnimation(0f, -90f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		// ���ö���������ʱ��
		animationL2R.setDuration(DURATION);
		// �������н���֮�󣬱������֮���״̬
		animationL2R.setFillAfter(true);
		// �����ظ��Ĵ���
		animationL2R.setRepeatCount(1);
		// �����ظ�ģʽΪ���˶�
		animationL2R.setRepeatMode(Animation.REVERSE);

		// animation = AnimationUtils.loadAnimation(context,
		// R.anim.anim_load_dialog);

		// ִ�м�����񣬿�ʼ�����0��ÿ��DURATION * 2ִ��һ��
		animationTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 0, DURATION * 2);

	}

	@Override
	protected void onStop() {
		super.onStop();
		animationTimer.cancel();
	}
}