package com.desay.sport.slidepage;

import java.util.Date;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.desay.sport.main.R;
import com.desay.sport.multimedia.MusicService;

public class SportService extends Service{
	Context context;
	private  long time = 0;
	private long startTime;
	public static final String COUNT_TIME="desay.sport.count";
	public static final String MAP_DOT="desay.sport.dot";
	private updateTime u;
	Intent timeintent;
	private int MSG;
	public static boolean StartSport=false;
	SpeakTool speakTool;

	class updateTime implements Runnable{

		@Override
		public void run() {
			time = new Date().getTime() - startTime;
			Message o=new Message();
			Bundle b=new Bundle();
			b.putLong("time", time);
			o.setData(b);
			handler.sendMessage(o);
			handler.postDelayed(u, 500);
		}	
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Bundle b=msg.getData();
			long time=b.getLong("time");
			timeintent=new Intent(COUNT_TIME);
			timeintent.putExtra("count", time);
			sendBroadcast(timeintent);
			super.handleMessage(msg);
		}
	};
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				sendBroadcast(new Intent(MusicService.MUSIC_GOON));
			}
		};
	};	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		context=this;
		
		
		Intent appIntent=new Intent(Intent.ACTION_MAIN);
		appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		appIntent.setClass(this, LocationOverlay.class);
		appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,appIntent, 0);
		Notification notification=new Notification(R.drawable.icon, "运动+", System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "运动+","正在运行" , contentIntent);
		notification.flags=Notification.FLAG_NO_CLEAR;
		startForeground(1, notification);
		startTime = new Date().getTime() - time;	
		u=new updateTime();
		handler.post(u);
		speakTool=new SpeakTool(this);
		speakTool.go(mHandler);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			MSG=intent.getIntExtra("MSG", 0);
			switch (MSG) {
			case LocationOverlay.STATE_PAUSE:
				handler.removeCallbacks(u);
				break;
			case LocationOverlay.STATE_RUNNING:
				startTime=new Date().getTime()-time;
				handler.post(u);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(u);
		stopForeground(true);
		super.onDestroy();
	}

}
