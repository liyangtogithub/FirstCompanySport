package com.desay.sport.main;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.desay.sport.data.CriticaNotification;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.net.Info;
import com.desay.sport.net.Login;
import com.desay.sport.slidepage.MyAlarmManagerReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Load extends Activity {

	protected static final int SPLASH_DISPLAY_LENGHT = 2000;
	private static final String TAG = "Load";
	private static final int MSG_START_MAINTAB = 1;
	Animation open_box;
    Context context;
    Handler handle;
	Runnable init_db;
	String illness = null;
	private sportDB db;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.startpage);
		context = Load.this;
		db = new sportDB(context);
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,MainTab.class);
				startActivity(i);
				finish();				
				overridePendingTransition(R.anim.load_in,R.anim.load_out); 

				SharedPreferences share = getSharedPreferences("medal_date",Activity.MODE_PRIVATE);
				int last_date = share.getInt("date", -1);
				int current_date =  Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
				if ( current_date != last_date )
				{					
					int criticalName = db.insertCriticalName(SportData.getUserName(context));
					if (criticalName != -1)
					{
						SharedPreferences.Editor edit = share.edit();
						edit.putInt("date",current_date);
						edit.commit();// 提交更新
						CriticaNotification.getNotification(criticalName,context);		
					}
				}
				
				// 每天九点报1次
				Calendar thisCalendar = Calendar.getInstance();// 当前
				int curentHour = thisCalendar.get(Calendar.HOUR_OF_DAY);

				Calendar nextCalendar = Calendar.getInstance();// 今天9点
				nextCalendar.set(Calendar.HOUR_OF_DAY, 9);
				nextCalendar.set(Calendar.MINUTE, 0);
				nextCalendar.set(Calendar.SECOND, 0);
				long todayTime = nextCalendar.getTimeInMillis();// 今天9点的毫秒数

				if (curentHour < 9)
				{
					regestAlarm(context, todayTime, 24 * 60 * 60 * 1000);
				}
				else
				{
					regestAlarm(context, todayTime + 24 * 60 * 60 * 1000,
							24 * 60 * 60 * 1000);
				}
				
				super.handleMessage(msg);
			}
		};
		
		init_db = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// psyTestDB.dropTable(context);
			db.CreateTable();
			Log.d(TAG, "init_ok");
		}
		};
	
		if(!db.tableIsExist())
		{
			new Thread(init_db).start();
		}
		else
			db.onUpgrade();
		
			Message msg = new Message();
			msg.what = MSG_START_MAINTAB;
			handle.sendMessageDelayed(msg, 1000);

	}
	/**
	 * 注册alarm startTime,开始时间，backTime 循环时间
	 */
	public void regestAlarm(Context context, long startTime, long backTime)
	{
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(
				context, MyAlarmManagerReceiver.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC, startTime, backTime, pi);
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			handle.removeMessages(MSG_START_MAINTAB);
		}
		return super.onKeyDown(keyCode, event);
	}
}
