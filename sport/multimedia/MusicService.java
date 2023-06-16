package com.desay.sport.multimedia;


import java.util.ArrayList;
import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

public class MusicService extends Service{

	private static final String MUSIC_CURRENT = "com.alex.currentTime";
	private static final String MUSIC_LIST = "com.alex.next";
	private static final int MUSIC_BEGIN = 1;
	private static final int MUSIC_PAUSE = 2;
	private static final int MUSIC_STOP = 3;
	private static final int PROGRESS_CHANGE = 4;
	private static final String NOTIFICATION_PRE="com.notificaiton.pre";
	private static final String NOTIFICATION_NEXT="com.notification.next";
	private static final String NOTIFICATION_PLAY="com.notification.play";
	private static final String NOTIFICATION_STOP="com.notification.stop";
	public static final String NOTIFICATION_EXIT="com.notification.exit";
	public static final String MUSIC_GOON="com.music.goon";
	private static  NotificationManager notificationManager;
	public static List<Mp3Info> list=new ArrayList<Mp3Info>();
	private MediaPlayer mp = null;
	public static int MusicId;
	int progress;
	public static boolean isReleased = true;
	public static  boolean isPlaying=false;
	public static boolean  isBegin=false;
	private Handler handler=null;
	private sportDB db;
	private  int currentPosition;
	public static int position=0;
	private static final int CURRENTTIME = 1;
	private MusicList musicList;
	@Override
	public void onCreate() {
		super.onCreate();
		db=new sportDB(this);
		musicList=new MusicList();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE");
		filter.addAction(NOTIFICATION_NEXT);
		filter.addAction(NOTIFICATION_PRE);
		filter.addAction(NOTIFICATION_PLAY);
		filter.addAction(NOTIFICATION_STOP);
		filter.addAction(NOTIFICATION_EXIT);
		filter.addAction(MUSIC_GOON);
		filter.addAction(SportData.RECEIVER_EXITSPORT);
		registerReceiver(InComingSMSReceiver, filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mp!=null){
			mp.stop();
			mp = null;
		}
		if (handler!=null){
			handler.removeMessages(1);
			handler=null;
		}
		unregisterReceiver(InComingSMSReceiver);
	}
	
	private void sendNotification(){
		 Mp3Info m=list.get(position);
		 notificationManager=(NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		 Notification notification=new Notification(R.drawable.ic_launcher,m.getMusicName(),System.currentTimeMillis());
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(),
					R.layout.notification);
		Bitmap bitmap = BitmapTool.getbitBmBykey(this,
				m.getAlbumkey());
		if (bitmap != null) {
			remoteViews.setImageViewBitmap(R.id.image, bitmap);
		} else {
			remoteViews.setImageViewResource(R.id.image,
					R.drawable.ic_launcher);
		}
		
					remoteViews.setOnClickPendingIntent(R.id.btnPrevious_player,
							PendingIntent.getBroadcast(this, 0,
									new Intent(NOTIFICATION_PRE),
									PendingIntent.FLAG_ONE_SHOT));

					remoteViews.setOnClickPendingIntent(R.id.btnPlay_player,
							getinten(this, remoteViews));

					remoteViews.setOnClickPendingIntent(R.id.btnNext_player,
							PendingIntent.getBroadcast(this, 0,
									new Intent(NOTIFICATION_NEXT),
									PendingIntent.FLAG_ONE_SHOT));
					
		remoteViews.setTextViewText(R.id.no_musicname,m.getMusicName());
		remoteViews.setTextViewText(R.id.no_singer, m.getSinger());
		
		
		notification.contentView = remoteViews;
		notification.contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MusicList.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		notificationManager.notify(0,notification);		
	}
	private static PendingIntent getinten(Context context,
			RemoteViews remoteViews) {
		PendingIntent intent = null;
		intent = PendingIntent.getBroadcast(context, 0, new Intent(
				NOTIFICATION_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
		if(isBegin){
			if (MusicService.isPlaying) {
				remoteViews.setImageViewResource(R.id.btnPlay_player,
						R.drawable.fg_pausemusic);
			} else {
				
				remoteViews.setImageViewResource(R.id.btnPlay_player,
						R.drawable.fg_playmusic);
			}
			isBegin=false;
		}
			else{
			if (!isPlaying) {
				remoteViews.setImageViewResource(R.id.btnPlay_player,
						R.drawable.fg_pausemusic);
			} else {
				
				remoteViews.setImageViewResource(R.id.btnPlay_player,
						R.drawable.fg_playmusic);
			}
		}
		return intent;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			list=db.GetMusic();
			position=intent.getIntExtra("position", 0);
			int flag=intent.getIntExtra("op", MUSIC_PAUSE);
			switch(flag){
			case MUSIC_BEGIN:
				begin(position);
				sendNotification();
				break;
			case MUSIC_PAUSE:
				pause();
				break;
			case MUSIC_STOP:
				stop();
				break;
			case PROGRESS_CHANGE:				
				currentPosition=intent.getIntExtra("progress", -1);
				mp.seekTo(currentPosition);
				break;
				default:
					break;
			}
			//sendNotification();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return super.onStartCommand(intent, flags, startId);
	}	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void begin(int position){		
			Mp3Info m=list.get(position);
			MusicId=m.getId();
			String url=m.getMusicPath();
			Uri myUri=Uri.parse(url);
			mp=MediaPlayer.create(this, myUri);
			init();
			setUp();
			mp.setLooping(true);
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(NOTIFICATION_NEXT);
					sendBroadcast(intent);
				}
			});
		isBegin=true;
		isPlaying=true;
		isReleased=false;
	}
	
	private void pause(){
		if (isPlaying){
			mp.pause();
		}else {
			mp.start();
		}
		sendNotification();
		isPlaying=isPlaying?false:true;
	}
	
	private void stop(){
		if(mp!=null){
			if(isPlaying){
				if(!isReleased){
					mp.stop();
					mp.release();
					isReleased=true;
				}
				isPlaying=false;
			}
		}
	}
	
	private void setUp(){
		mp.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(CURRENTTIME);
			}
		});
	}
	private void init(){
		
			handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					
					switch(msg.what){
					case CURRENTTIME:
						try {
							Intent intent = new Intent(MUSIC_CURRENT);
							currentPosition=mp.getCurrentPosition();
							intent.putExtra("currentTime", currentPosition);
							sendBroadcast(intent);
							handler.sendEmptyMessageDelayed(CURRENTTIME, 600);
							break;
						} catch (Exception e) {
							// TODO: handle exception
						}
						}
					super.handleMessage(msg);						
					}
			};
	}
	

	protected BroadcastReceiver InComingSMSReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
				TelephonyManager telephonymanager = 
					(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				switch (telephonymanager.getCallState()) {
					case TelephonyManager.CALL_STATE_RINGING:
						if(mp!=null){
							if(isPlaying){
								if(!isReleased){
						mp.pause();
								}}}
						break;
					case TelephonyManager.CALL_STATE_OFFHOOK:
						if(mp!=null){
							if(isPlaying){
								if(!isReleased){
						mp.pause();
								}}}
						break;
					case TelephonyManager.CALL_STATE_IDLE:
							if(mp!=null){
								if(isPlaying){
									if(!isReleased){	
						mp.start();}}}
						break;
					 default:
						 break;
				}
			}else if(intent.getAction().equals(NOTIFICATION_NEXT)){
				stop();
				if(position==list.size()-1)
				{
					position=0;
				}
				else
				{
					position++;
				}
				begin(position);
				MusicList.position=position;
				sendNotification();
				if(MusicList.isExit){
					sendBroadcast(new Intent(MusicList.MUSIC_EXIST));
				}
			}else if(intent.getAction().equals(NOTIFICATION_PRE)){
				stop();
				if(position==0)
				{
					position=list.size()-1;
				}
				else if(position>0)
				{
					position--;
				}
				begin(position);
				MusicList.position=position;
				sendNotification();
				if(MusicList.isExit){
					sendBroadcast(new Intent(MusicList.MUSIC_EXIST));
				}
					
			}else if(intent.getAction().equals(NOTIFICATION_STOP)){
				stop();
				if(notificationManager!=null)
				notificationManager.cancel(0);
			}else if(intent.getAction().equals(NOTIFICATION_PLAY)){
				if(MusicList.isExit){
					sendBroadcast(new Intent(MusicList.MUSIC_PLAY));
					sendNotification();
				}else {
					pause();
				}				
				
			}else if(intent.getAction().equals(NOTIFICATION_EXIT)){
				stop();
				if(notificationManager!=null)
				notificationManager.cancel(0);
			}else if(intent.getAction().equals(SportData.RECEIVER_EXITSPORT)){
				if(mp!=null){
					if(isPlaying){
						if(!isReleased){
					mp.pause();
						}}
				}
			}else if(intent.getAction().equals(MUSIC_GOON)){
				if(mp!=null){
					if(isPlaying){
						if(!isReleased){
					mp.start();
						}}
				}
			}
			
		}
	};
	
	

}
