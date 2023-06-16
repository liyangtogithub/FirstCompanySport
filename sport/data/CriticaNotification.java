package com.desay.sport.data;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;

public class CriticaNotification
{
	
	/**
	 * 得到批评的通知
	 */
	public static void getNotification(int criticalName,Context context)
	{
		//创建一个 NotificationManager的引用
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(ns);
		// 定义Notification的各种属性
		int icon = R.drawable.ic_launcher; //通知图标
		CharSequence tickerText = context.getString(R.string.app_name)+context.getString(R.string.medal_criticism_note); //状态栏显示的通知文本提示
		long when = System.currentTimeMillis(); //通知产生的时间，会在通知信息里显示
		//用上面的属性初始化 Nofification
		Notification notification = new Notification(icon,tickerText,when);
		notification.defaults |=Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
		RemoteViews contentView = new RemoteViews(context.getPackageName(),R.layout.view);
        contentView.setImageViewBitmap(R.id.note_image, SportData.drawableToBitmap(SportData.getIconDrawable(context, criticalName, SportData.ICON_MEDAL)));
		if (criticalName == 29)
		{
			contentView.setTextViewText(R.id.note_text,context.getString(R.string.medal_29_note));
		}
		else if (criticalName == 30)
		{
			contentView.setTextViewText(R.id.note_text,context.getString(R.string.medal_30_note));
		}
		else
		{
			contentView.setTextViewText(R.id.note_text,context.getString(R.string.medal_31_note));
		}
		
		notification.contentView = contentView;
		Intent notificationIntent = new Intent(context,MainTab.class);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		//notificationIntent.setAction("android.intent.action.MAIN") ;				
		PendingIntent contentIntent = PendingIntent.getActivity(
				context, 1, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		//把Notification传递给NotificationManager
		mNotificationManager.notify(0,notification);
	}
}
