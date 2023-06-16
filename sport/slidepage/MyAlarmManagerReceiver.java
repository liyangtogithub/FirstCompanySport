package com.desay.sport.slidepage;

import java.util.Calendar;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.desay.sport.data.CriticaNotification;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.SendServer;

public class MyAlarmManagerReceiver extends BroadcastReceiver
{
	Context context;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		this.context = context;
		context.startService(new Intent(context,SendServer.class));
		MyAlarmHandler.sendEmptyMessage(0);
	}

	Handler MyAlarmHandler = new Handler()
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
		}
	};
}
