package com.desay.sport.multimedia;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desay.pstest.toast.showToast;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.LocationOverlay;

public class MusicLib extends Activity{
	Button complishBt;
	//Button cancelBt;
	CheckBox allSelectBt;
	ImageView iv_return ;
	boolean allcheck=false;
	ListView lib_list;
	private AlertDialog.Builder  builder = null;
	private AlertDialog ad = null;
	private  MusicLibListAdapter adapter;
	private  List<Mp3Info> mp3List = null;
	private sportDB db;
	private ScanSDcardReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.musiclib);
		complishBt=(Button) findViewById(R.id.complish);
		allSelectBt=(CheckBox) findViewById(R.id.allselect);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.musiclib));
		complishBt.setOnClickListener(OnClick);
		iv_return.setOnClickListener(OnClick);
		allSelectBt.setOnClickListener(OnClick);
		lib_list=(ListView) findViewById(R.id.lib_list);	
		mp3List=new ArrayList<Mp3Info>();
		db=new sportDB(this);
		setListData();
		
		IntentFilter intentfilter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_STARTED);
		intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
		intentfilter.addDataScheme("file");
		receiver = new ScanSDcardReceiver();
		registerReceiver(receiver, intentfilter);
	}

	OnClickListener OnClick = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.complish:
				Iterator i = adapter.getchoseMap().values().iterator();		
				List<Mp3Info> list=new ArrayList<Mp3Info>();
				list=db.GetMusic();
				if(list.size()==0){
					sendBroadcast(new Intent("com.music.finish"));
					MusicList.isExit=false;
				}
				if(!i.hasNext()){
					if(list.size()==0){
					Intent backIntent=new Intent(MusicLib.this,AddMusic.class);
					startActivity(backIntent);
					finish();}
					else{
							finish();
					}
					return;
				}
				while (i.hasNext()) {
					Mp3Info o = (Mp3Info) i.next();
					//mp3List.add(o);
					HashMap<String , String> values=new HashMap<String, String>();
					values.put("_singer", o.getSinger());
					values.put("_time", o.getTime()+"");
					values.put("_musicName", o.getMusicName());
					values.put("_musicPath", o.getMusicPath());
					values.put("_albumKey", o.getAlbumkey());
					values.put("_musicId", o.getId()+"");
					db.InsertMusic(values);
					MusicService.list=db.GetMusic();
				}
				if(!MusicList.isExit){
				Intent intent=new Intent();
				intent.setClass(MusicLib.this, MusicList.class);
				startActivity(intent);
				}
				finish();
				break;
			case R.id.allselect:
				adapter = new MusicLibListAdapter(MusicLib.this, mp3List,lib_list);
				if(allcheck){	

					adapter.CancelAll();					
					allcheck=false;
				}else{
			
					adapter.SelecteAll();
					allcheck=true;
				}
				lib_list.setAdapter(adapter);
				break;
			case R.id.iv_return:
				back();
				break;
		}};
};
	private void back() {
				List<Mp3Info> list=new ArrayList<Mp3Info>();
				list=db.GetMusic();
				if(list.size()==0){
					Intent backIntent=new Intent(MusicLib.this,AddMusic.class);
					startActivity(backIntent);
					sendBroadcast(new Intent("com.music.finish"));
					finish();
				}else{
					if(!MusicList.isExit){
					Intent backIntent=new Intent(MusicLib.this,MusicList.class);
					startActivity(backIntent);}
					finish();
				}
	}
		@Override
		public void onBackPressed() {
			back();
		};
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			unregisterReceiver(receiver);
			super.onDestroy();
		}
		private void setListData(){
			Cursor c =null;
		try {
			c=this.getContentResolver()
			.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.ALBUM_KEY},
					null, null, null);
		    if (c==null || c.getCount()==0){
		    	builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.nomusic).setPositiveButton(R.string.sure_str, null);
				ad = builder.create();
				ad.show();
		    }
		    c.moveToFirst();		    
		    for(int i=0;i<c.getCount();i++){
		    	Mp3Info m=new Mp3Info();
		    	m.setId(c.getInt(3));
		    	m.setMusicName(c.getString(4));
		    	m.setSinger(c.getString(2));
		    	m.setTime(c.getInt(1));
		    	m.setMusicPath(c.getString(5));
		    	m.setAlbumkey(c.getString(6));
		    	mp3List.add(m);
		    	c.moveToNext();	
		    }
		    adapter = new MusicLibListAdapter(this, mp3List,lib_list);
		    adapter.notifyDataSetChanged();
		    lib_list.setAdapter(adapter);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			   c.close();
		}
			
		}
		
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			menu.add(0, 1, 0, R.string.scan_music);
			return true;
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			scan();
			return super.onOptionsItemSelected(item);
		}
		public void scan() {
			
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					Uri.parse("file://"
							+ Environment.getExternalStorageDirectory().getAbsolutePath())));
		}
		class ScanSDcardReceiver extends BroadcastReceiver {
			private AlertDialog.Builder builder = null;
			private AlertDialog ad = null;

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
					builder = new AlertDialog.Builder(context);
					builder.setMessage(R.string.scanning);
					ad = builder.create();
					ad.show();
				} else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
					mp3List.clear();
					setListData();
					ad.cancel();
                    showToast.normaltoast(context, getString(R.string.scanned), showToast.ONE_SECOND);
				}
			}
		}	

}
