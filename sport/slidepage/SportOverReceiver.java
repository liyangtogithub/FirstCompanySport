package com.desay.sport.slidepage;

import java.util.Calendar;

import com.desay.sport.anim.animMedal;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;

import android.R.integer;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 运动停止时接收的广播
 */
public class SportOverReceiver extends BroadcastReceiver
{
	String tag = "SportOverReceiver";
	Context context;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.context = context;
		if ( intent.getIntExtra("distance", 0)!=0 )
		{			
			medalHandler.sendEmptyMessage(0);				
		}
	}

	Handler medalHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			sportDB spDB = new sportDB(context);
			// 返回奖章的编号
			int medalName = spDB.getMedalName(SportData.getUserName(context));

			// 每天九点报1次
			Calendar thisCalendar = Calendar.getInstance();// 当前
			int curentHour = thisCalendar.get(Calendar.HOUR_OF_DAY);
			
			Calendar nextCalendar = Calendar.getInstance();// 今天9点
			nextCalendar.set(Calendar.HOUR_OF_DAY, 9);
			nextCalendar.set(Calendar.MINUTE, 0);
			nextCalendar.set(Calendar.SECOND, 0);
			long todayTime = nextCalendar.getTimeInMillis();//今天9点的毫秒数
			
		//	Log.i(tag, "curentHour   " + curentHour);
		//	Log.i(tag, "nextCalendar.   " + nextCalendar.get(Calendar.HOUR_OF_DAY));
			if (curentHour < 9 )
			{
				regestAlarm(context, todayTime, 24 * 60 * 60 * 1000);
			}
			else
			{
				regestAlarm(context, todayTime+24 * 60 * 60 * 1000, 24 * 60 * 60 * 1000);
			}
			if(medalName!=-1)//if not -1,play medal animation
			{

				Intent i = new Intent(context,animMedal.class);
				i.putExtra("medalname", medalName);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}

		}
	};

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
}
