package com.desay.sport.slidepage;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.desay.sport.data.CriticaNotification;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;

public class BootCompletedReceiver extends BroadcastReceiver
{
	Context context;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.context = context;
		BootHandler.sendEmptyMessage(0);
	}

	Handler BootHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			sportDB db = new sportDB(context);
			SharedPreferences share = context.getSharedPreferences("medal_date",Activity.MODE_PRIVATE);
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

			if (curentHour <= 9)
			{
				regestAlarm(context, todayTime, 24 * 60 * 60 * 1000);
			}
			else
			{
				regestAlarm(context, todayTime + 24 * 60 * 60 * 1000,
						24 * 60 * 60 * 1000);
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
