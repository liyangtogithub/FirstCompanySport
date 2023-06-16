package com.desay.sport.multimedia;

import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.LocationOverlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MusicList extends Activity {
	
	Context context;
	ImageView iv_return;
	ListView lv_list;
	ImageButton addBt;
	
	public  MusicListAdapter adapter;
	private  static  List<Mp3Info> alllist;

	public static int position;
	public static boolean isExit=false;
	private static boolean isbegin=true;
	private TextView tv_musicname;
	private ImageView iv_music;
	private ImageButton ib_premusic,ib_playmusic,ib_nextmusic;;
	private TextView playtime=null;
	private TextView durationTime=null;
	private SeekBar seekbar=null;
	private int currentPosition;
	private static final String MUSIC_CURRENT="com.alex.currentTime";
	private static final String MUSIC_NEXT="com.alex.next";
	public static final String MUSIC_EXIST="com.music.exist";
	private static final String MUSIC_FINISH="com.music.finish";
	public static final String MUSIC_PLAY="com.music.play";
	private static final int MUSIC_BEGIN=1;
	private static final int MUSIC_PAUSE=2;
	private static final int MUSIC_STOP=3;
	private static final int PROGRESS_CHANGE=4;

	private sportDB db;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_music);
		context = MusicList.this;
		db=new sportDB(this);
		isExit=true;
		position=MusicService.position;
		iv_return = (ImageView) findViewById(R.id.iv_return);
		lv_list = (ListView) findViewById(R.id.lv_list);
		alllist=db.GetMusic();
		lv_list.setAdapter(new MusicListAdapter(context, alllist));	
		((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.bg_music));
		iv_music=(ImageView) findViewById(R.id.iv_music);
		ib_premusic=(ImageButton)findViewById(R.id.ib_premusic);
		ib_playmusic=(ImageButton)findViewById(R.id.ib_playmusic);
		ib_nextmusic=(ImageButton)findViewById(R.id.ib_nextmusic);
		tv_musicname = (TextView) findViewById(R.id.tv_musicname);
		playtime=(TextView)findViewById(R.id.playtime);
		durationTime=(TextView)findViewById(R.id.duration);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		addBt=(ImageButton) findViewById(R.id.add);
		seekbar  = (SeekBar) findViewById(R.id.seekbar);
		
		iv_return.setOnClickListener(OnClick);
		ib_premusic.setOnClickListener(OnClick);
		ib_playmusic.setOnClickListener(OnClick);
		ib_nextmusic.setOnClickListener(OnClick);		
		addBt.setOnClickListener(OnClick);
		iv_return.setOnClickListener(OnClick);
		if(MusicService.isPlaying){
				ib_playmusic.setImageResource(R.drawable.fg_pausemusic);
			}else{
				ib_playmusic.setImageResource(R.drawable.fg_playmusic);
			}

		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			public void onStopTrackingTouch(SeekBar seekBar)
			{
				play();
			}

			public void onStartTrackingTouch(SeekBar seekBar)
			{
				play();
			}

			public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser)
			{
				if(fromUser)
				{
					Intent intent=new Intent(MusicList.this,MusicService.class);
					intent.putExtra("op",PROGRESS_CHANGE);
					intent.putExtra("progress",progress);
					startService(intent);
				}
			}
		});
	
		lv_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(MusicService.isReleased){
					position=arg2;
					loadClip();
					begin();
				}else{
				if(arg2==position){	
						play();
				}else{				
				stop();
				position=arg2;
				loadClip();
				begin();
				}}
			}});
		
		lv_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder=new AlertDialog.Builder(MusicList.this);						
				builder.setMessage(R.string.delete_title)
				.setPositiveButton(R.string.sure_str, new DialogInterface.OnClickListener() {							
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub		
						Mp3Info m=alllist.get(arg2);						
						db.DelMusic(m.getId());
						alllist.remove(arg2);
						MusicService.list=db.GetMusic();
						if(alllist.size()==0){
							tv_musicname.setText("");
							sendBroadcast(new Intent(MusicService.NOTIFICATION_EXIT));
						}
						MusicListAdapter adapter=new MusicListAdapter(MusicList.this,alllist);	
						if(arg2<position){
							position=position-1;
							MusicService.position=position;
							adapter.setItemIcon(position);
						}else if(arg2==position){
							stop();
							adapter.setItemIcon(position);
							begin();
						}
						adapter.notifyDataSetChanged();
						lv_list.setAdapter(adapter);	
					}
				}).setNegativeButton(R.string.cancle_str, null).show();
				return false;
			}
		});
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		context = MusicList.this;
		alllist=db.GetMusic();		
		
		adapter=new MusicListAdapter(MusicList.this,alllist);	
		lv_list.setAdapter(adapter);	
		boolean isThis = false;
		for(int i=0;i<alllist.size();i++){
			if(alllist.get(i).getId()==MusicService.MusicId){
				System.out.println("istrue..");
				isThis=true;
				adapter.setItemIcon(i);
				position=i;
				if(MusicService.isPlaying)
				ib_playmusic.setImageResource(R.drawable.fg_pausemusic);
				break;
			}
		}if(!isThis){
			System.out.println("isfalse..");
			stop();
			position=0;
			adapter.setItemIcon(-1);
			ib_playmusic.setImageResource(R.drawable.fg_playmusic);
		}
		IntentFilter filter=new IntentFilter();
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_NEXT);
		filter.addAction(MUSIC_EXIST);
		filter.addAction(MUSIC_FINISH);
		filter.addAction(MUSIC_PLAY);
		registerReceiver(musicReceiver,filter);
		loadClip();
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(musicReceiver);
		super.onDestroy();
	};
	
	OnClickListener OnClick = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.iv_return:
				isExit=false;
				finish();
				break;
			case R.id.add:
				Intent intent1 =new Intent();
				intent1.setClass(context, MusicLib.class);
				startActivity(intent1);
				break;
			case R.id.ib_premusic:
				latestOne();
			break;
			
			case R.id.ib_playmusic:
			if(alllist.size()!=0){
				if(MusicService.isReleased){
					position=0;
					loadClip();
					begin();
				}else{
					play();
				}
			}
			break;
			
			case R.id.ib_nextmusic:
				nextOne();
			break;	
			}
		}};
	
		@Override
		public void onBackPressed() {	
			isExit=false;
			finish();
		};
	
	public void loadClip()
	{	
	if(alllist.size()!=0){
		Mp3Info m=alllist.get(position);
		seekbar.setMax(m.getTime());
		tv_musicname.setText(m.getMusicName());
		playtime.setText(toTime(0));
		durationTime.setText(toTime(m.getTime()));
		Bitmap bitmap=BitmapTool.getbitBmBykey(context, m.getAlbumkey());
		if(bitmap==null){
			iv_music.setImageBitmap(null);
		}else{
		iv_music.setImageBitmap(bitmap);
		}
		adapter=new MusicListAdapter(context, alllist);
		adapter.setItemIcon(position);
		adapter.notifyDataSetChanged();
		lv_list.setAdapter(adapter);
		lv_list.setSelection(position);
	}
	}

	private void begin()
	{	
		isbegin=true;
		MusicService.isPlaying=true;
		ib_playmusic.setImageResource(R.drawable.fg_pausemusic);
		Intent intent=new Intent();
		intent.setClass(this, MusicService.class);
		intent.putExtra("op",MUSIC_BEGIN);
		intent.putExtra("position", position);
		startService(intent);
		adapter=new MusicListAdapter(context, alllist);
		adapter.setItemIcon(position);
		adapter.notifyDataSetChanged();
		lv_list.setAdapter(adapter);
		lv_list.setSelection(position);
		isbegin=false;
	}


	public void play()
	{
		if(MusicService.isPlaying){
			ib_playmusic.setImageResource(R.drawable.fg_playmusic);			
		}else{
			ib_playmusic.setImageResource(R.drawable.fg_pausemusic);
		}
		
		Intent intent=new Intent();
		intent.setClass(this, MusicService.class);
		intent.putExtra("position", position);
		intent.putExtra("op",MUSIC_PAUSE);
		startService(intent);
	}


	private void stop()
	{
		Intent intent=new Intent();
		intent.setClass(this, MusicService.class);
		intent.putExtra("op",MUSIC_STOP);
		startService(intent);
	}


	public void latestOne()
	{
		stop();
		if(position==0)
		{
			position=alllist.size()-1;
		}
		else if(position>0)
		{
			position--;
		}
		loadClip();
		begin();
	}

	/**
	 * 下一首
	 */
	public void nextOne()
	{
		stop();
		if(position==alllist.size()-1)
		{
			position=0;
		}
		else
		{
			position++;
		}
		loadClip();
		begin();
	}

	protected BroadcastReceiver musicReceiver=new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context,Intent intent)
		{
			String action=intent.getAction();
			if(action.equals(MUSIC_CURRENT))
			{
				currentPosition=intent.getExtras().getInt("currentTime");
				playtime.setText(toTime(currentPosition));
				seekbar.setProgress(currentPosition);
			}
			
			if(action.equals(MUSIC_NEXT))
			{
				nextOne();
			}
			if(action.equals(MUSIC_EXIST))
			{
				loadClip();
			}
			if(action.equals(MUSIC_PLAY)){
				play();
			}
			if(action.equals(MUSIC_FINISH)){
				finish();
				isExit=false;
			}
		}
	};

		
	public String toTime(int time)
	{

		time/=1000;
		int minute=time/60;
		int second=time%60;
		minute%=60;
		return String.format("%02d:%02d",minute,second);
	}
	
	
}
