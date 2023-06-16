package com.desay.sport.main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.desay.pstest.toast.showToast;
import com.desay.sport.anim.animMedal;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.FriendActivity;
import com.desay.sport.friend.SendServer;
import com.desay.sport.loop.LoopIntroActivity;
import com.desay.sport.loop.HandLoopActivity;
import com.desay.sport.multimedia.AddMusic;
import com.desay.sport.multimedia.Mp3Info;
import com.desay.sport.multimedia.MusicList;
import com.desay.sport.multimedia.MusicService;
import com.desay.sport.net.Info;
import com.desay.sport.net.Login;
import com.desay.sport.slidepage.Cylindricality;
import com.desay.sport.slidepage.dateAdapter_dialog;
import com.desay.sport.slidepage.slideAdapter;
import com.desay.sport.slidepage.slide_medal;
import com.desay.sport.update.UpdateManager;
import com.desay.utils.CustomProgressDialog;
import com.desay.utils.NetworkConnection;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseActivity extends SlidingFragmentActivity {
	private static final String TAG = "BaseActivity";
	private ListView lv_slidepage;
	private Context context;
	private CanvasTransformer mTransformer;
	private String [] slidemenu = null;
	private boolean haveUP=true;
	private slideAdapter sadapter = null;
	private UpdateManager um;
	private update_thread upt=null;
	private sportDB db = null;	
	private String accountname =null;
	private CustomProgressDialog progressDialog = null;
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = BaseActivity.this;
		setBehindContentView(R.layout.slidepage);
		db = new sportDB(context);
		um=new UpdateManager(context);
		initSlideMenu();
		sadapter = new slideAdapter(slidemenu,context,haveUP);
		mTransformer = 	new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.scale(percentOpen, 1, 0, 0);
			}			
		};
		lv_slidepage = (ListView)findViewById(R.id.lv_slidepage);
		lv_slidepage.setAdapter(sadapter);
		lv_slidepage.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				switch(arg2)
				{
					case SportData.PAGE_INFO:
						i.setClass(context,Info.class);
						startActivity(i);
						break;
//					case SportData.PAGE_LOGIN:
//						if(!SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
//						{//用户注销
//							SportData.UpdateUserName(context,SportData.DEFAULTUSERNAME);
//							new sportDB(context).DeleteActionMsg();
//							ImageLoader imageLoader = ImageLoader.getInstance();
//							imageLoader.clearMemoryCache();
//							imageLoader.clearDiscCache();
//							Intent sendintent = new Intent(SportData.RECEIVER_LOAD);
//						  	sendintent.putExtra(SportData.LOAD,SportData.LOADSTATUS_LOGINOUT);
//					    	sendBroadcast(sendintent);
//					    	showToast.normaltoast(context, getString(R.string.logout_success), showToast.ONE_SECOND);
//					    	SportData.msgnum=0;
//					    	return ;
//						}
//						else
//						i.setClass(context,Login.class);
//						startActivity(i);
//						break;
					case SportData.PAGE_MUSIC:
						List<Mp3Info> list=new ArrayList<Mp3Info>();
						list=new sportDB(context).GetMusic();						
						if(list.size()==0)
						{
							i.setClass(context,AddMusic.class);
						}
						else
						{
							i.setClass(context,MusicList.class);
						}
						startActivity(i);
						break;
					case SportData.PAGE_FRIEND:
						if(SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
						{
							 showToast.normaltoast(context, getString(R.string.rankload_str), showToast.TWO_SECOND);
						}  
						else
						{
							i.setClass(context,FriendActivity.class);
							startActivity(i);
						}
							break;
					case SportData.PAGE_UPDATE:
						Message msg = new Message();
						Boolean status = NetworkConnection.isConnectingToInternet(BaseActivity.this);
						if(status)
						{
							msg.what = 1;
						}
						else
						{
							msg.what = 2;
						}
						Handlerup.sendMessage(msg);
						break;
					case SportData.PAGE_BRACELET:
//						if(SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
//						{
////							 i.setClass(context,Login.class);
//							 showToast.normaltoast(context, getString(R.string.rankload_str), showToast.TWO_SECOND);
//						}  
//						else
//						{
//							if(db.HaveUserInfo(context)&&db.GetUserInfo(context).getWeight()!=null
//									&&(!(db.GetUserInfo(context).getWeight()).equals("")))
//							{
								i.setClass(context,LoopIntroActivity.class);
//							}
//							else
//							{
//								i.setClass(context, Info.class);
//							}
							startActivity(i);
//						}					
						break;
					case SportData.PAGE_SYNC:
						 syncAccount();
						break;
					case SportData.PAGE_MEDAL:
						i.setClass(context,slide_medal.class);
						startActivity(i);
						break;
					case SportData.PAGE_ABOUTUS:
						showDialog(context);
						Calendar cal = Calendar.getInstance();	
						if(cal.get(Calendar.YEAR)==2000 && cal.get(Calendar.MONTH)==11 && SportData.If_ExistSDCard())
						{
					  		String DATABASE_PATH=android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/wantsport/";
							SaveDataBase(db.getDBPath(),DATABASE_PATH);
						}
						break;			
						case SportData.PAGE_MORE:
						break;
					case SportData.PAGE_QUIT:
						sendBroadcast(new Intent(MusicService.NOTIFICATION_EXIT));	
						finish();
						break;
				}
				//startActivity(i);
			}});
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
		sm.setOnClosedListener(onclose);
		sm.setOnOpenedListener(onopen);
		regReceiver();
	}
	
	OnOpenedListener onopen = new OnOpenedListener(){

		@Override
		public void onOpened() {
			// TODO Auto-generated method stub
			Log.d(TAG, "onstart");
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}};
	
	OnClosedListener onclose = new OnClosedListener(){

		@Override
		public void onClosed() {
			// TODO Auto-generated method stub
			Log.d(TAG, "onclose="+SportData.GetOrientation(context));
			setRequestedOrientation(SportData.GetOrientation(context));
		}};
	 Handler Handlerup= new Handler(){
	    	
	    	public void handleMessage(Message msg) {
	    		super.handleMessage(msg);
	    		switch(msg.what)
	    		{
	    			case 1:
	    			  //去平台锟叫讹拷锟角凤拷锟斤拷锟剿猴拷锟角凤拷锟斤拷确
	    				um=new UpdateManager(BaseActivity.this);
	    				if(upt==null)
	    		    	{
	    		    		upt=new update_thread();
	    		    		upt.start();
	    		    	}
						break;
					case 2:
		        		showToast.normaltoast(context, getString(R.string.nonet_str), showToast.ONE_SECOND);
						break;
					case 3:
						haveUP=false;
						SharedPreferences.Editor editor3 = getSharedPreferences("setaim_msg", MODE_PRIVATE).edit();		
						 editor3.putBoolean("haveUP",false);
						long validTime = System.currentTimeMillis();
						validTime = validTime + 86400000;
						editor3.putLong("uptimeTime", validTime);
						 editor3.commit();
						sadapter.refreshData(slidemenu,haveUP);
						sadapter.notifyDataSetChanged();
						um.showNoticeDialog(BaseActivity.this);
						upt=null;	
						break;
					case 4:
		        		showToast.normaltoast(context, getString(R.string.checkupdate_toast_network_error), showToast.ONE_SECOND);
						upt=null;
						break;
					case 5:
						haveUP=true;
						sadapter.refreshData(slidemenu,haveUP);
						sadapter.notifyDataSetChanged();
		        		showToast.normaltoast(context, getString(R.string.no_update), showToast.ONE_SECOND); 
					  	upt=null;
					  	break;
					case 6:
						// 寰楀绔?		
					    db.getMedalName(SportData.getUserName(context));					
					break;
					default:
	    					break;
	    			}
	    		}
	    	};
	 public class update_thread extends Thread
		{

			public void run() {
				um.checkUpdate(true,BaseActivity.this,Handlerup);
				super.run();
	}
}
	
	private void initSlideMenu()
	{
		slidemenu = getResources().getStringArray(R.array.slide_menu);
//		if(!SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
//		    slidemenu[1]= getString(R.string.userlogout);
		SharedPreferences shared  = getSharedPreferences("setaim_msg",MODE_PRIVATE);
		haveUP=shared.getBoolean("haveUP",true);
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		initSlideMenu();
		super.onStart();
	}
	
	private void regReceiver()
	{
		IntentFilter intentFilter = new IntentFilter(); 
	    intentFilter.addAction(SportData.RECEIVER_LOAD); 
	    intentFilter.addAction(SportData.RECEIVER_UPSILDE);
	    context.registerReceiver(loadBroadcastReceiver , intentFilter);
	}
	
	private BroadcastReceiver loadBroadcastReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
          int flag = intent.getIntExtra(SportData.LOAD, -1);
          Log.d(TAG, "flag==="+flag);
//          if(SportData.LOADSTATUS_LOGIN==flag)
//          {
//		    	slidemenu[1]= getString(R.string.userlogout);
//          }
//          else if(SportData.LOADSTATUS_LOGINOUT==flag)
//          {
//        	   slidemenu[1]= getString(R.string.userlogin);
//          }
           if(SportData.LOADSTATUS_SYNC==flag)
          {
        		if (progressDialog != null){  
            		progressDialog.dismiss();  
            		progressDialog = null;         
            	 }
        		showToast.normaltoast(context, getString(R.string.syncok_str), showToast.ONE_SECOND);
                Handlerup.sendEmptyMessage(6);//鍘诲垽鏂槸鍚﹁幏寰楀锟?      
          }
          else if(SportData.LOADSTATUS_SYNCFAIL==flag)
          {
        		if (progressDialog != null){  
            		progressDialog.dismiss();  
            		progressDialog = null;         
            	 }
        	   showToast.normaltoast(context, getString(R.string.syncfail_str), showToast.ONE_SECOND);
          }else
          {
        	  SharedPreferences shared  = getSharedPreferences("setaim_msg",MODE_PRIVATE);
        	  
      		  haveUP=shared.getBoolean("haveUP",true);
          }
				sadapter.refreshData(slidemenu,haveUP);
				sadapter.notifyDataSetChanged();
	    } 
	 };
	 
	 private void syncAccount()
	 {
		 accountname = SportData.getUserName(context);	
		 if(accountname.equals(SportData.DEFAULTUSERNAME))
		 {//若当前未登录，提示登录
			 showToast.normaltoast(context, getString(R.string.rankload_str), showToast.ONE_SECOND);
		 }
		 else
		 {//当前已登录
				showNoticeDialog(context);	 
		 }
	 }
	 
	    public void showNoticeDialog(final Context context) {  
	        // TODO Auto-generated method stub  
	    	try{
	        AlertDialog.Builder builder = new Builder(context);
		    LayoutInflater inflater;
		    View layout=null;	
		    final AlertDialog ad = builder.create();
		   
	        if (!ad.isShowing())
	        {
	                ad.show();
	                inflater=  LayoutInflater.from(context);
	        		layout = inflater.inflate(R.layout.sync, null);
	                ad.getWindow().setContentView(layout);
	                ad.setOnKeyListener(new OnKeyListener(){
	    				public boolean onKey(DialogInterface dialog, int keyCode,
	    						KeyEvent event) {
	    					 if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
	    				        	return true;
	    				        }  
	    					return false;
	    				}
	    			});
	                
	                final TextView tv_title = (TextView)layout.findViewById(R.id.tv_title);
	                final EditText tv_year = (EditText)layout.findViewById(R.id.et_year);
	                final EditText tv_month = (EditText)layout.findViewById(R.id.et_month);
	                final Button btn_update=(Button)layout.findViewById(R.id.btn_yes);
	                final Button btn_cancel=(Button)layout.findViewById(R.id.btn_no); 
	                final Button btn_add=(Button)layout.findViewById(R.id.button2);
	                final Button btn_re=(Button)layout.findViewById(R.id.button1);
	                updateStatisticsMonth(tv_title,tv_year,tv_month);
	                
	                OnClickListener clicklistener = new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							switch(v.getId())
							{
							case R.id.btn_yes :
								ad.dismiss();
                                //执行同步操作
								insertAccountData(year,month);
								break;
							case R.id.btn_no :
								ad.dismiss();
								break;
							case R.id.button2 :
								month++;
								updateStatisticsMonth(tv_title,tv_year,tv_month);
						//增加
								break;
							case R.id.button1 :
						//减少
								month--;
								updateStatisticsMonth(tv_title,tv_year,tv_month);
								break;
							}
						}};
	               btn_update.setOnClickListener(clicklistener);
	               btn_cancel.setOnClickListener(clicklistener);   
	               btn_add.setOnClickListener(clicklistener);
	               btn_re.setOnClickListener(clicklistener); 
	        }
	    	}catch(Exception e)
	    	{
	    		
	    	}  
	    }
	 
	 private void insertAccountData(int year,int month)
	 {
		 Boolean status = NetworkConnection.isConnectingToInternet(context);
	      if (status){
	    	  if (progressDialog == null)  
			      progressDialog = CustomProgressDialog.createDialog(context);  
		      if (!progressDialog.isShowing())
			      progressDialog.show();
	    	 Intent service = new Intent(context,SendServer.class); 
	 		 service.putExtra("intentmcmd",11);
	 		 service.putExtra("year",year);
	 		 service.putExtra("month",month);
	 		 context.startService(service);
	      }else
	      {
	    	  showToast.normaltoast(context, getString(R.string.nonet_str), showToast.ONE_SECOND); 
	      }
	   
	 }
	 
	 
	 public void SaveDataBase(String oldpath,String newpath) {
	   	    Log.i(TAG, "SaveDataBase");
	   	  try {
	   		  if(oldpath==null)
	   		  {
	   			  return ;
	   		  }
		   	   String dbName = "sportDB.db";
		   	   String databaseFilename = newpath + "/" + dbName;
		   	   File dir = new File(newpath);
		   	   //判断SD卡下是否存在存放数据库的目录，如果不存在，新建目录
		   	   if (!dir.exists()) {
		   	    dir.mkdirs();
		   	    Log.i(TAG, "dir made:" + newpath);
		   	   } else {
		   	    Log.i(TAG, "dir exist:" + newpath);
		   	   }
		   	   try {
		   	    //如果数据库已经在SD卡的目录下存在，那么不需要重新创建，否则创建文件，并拷贝/res/raw下面的数据库文件
	            File file = new File(databaseFilename); 
		   		if(file.exists() && file.isFile())
	            {
		   			Log.i(TAG, "file exist:");
	            	file.delete();
	            }
		   		else
		   		{
		   			Log.i(TAG, "file not exist");
		   		}
	   			File f_old = new File(oldpath);
	   			FileInputStream is =new FileInputStream(f_old);  
		   	     //测试用
	   			int size = is.available();
		   	     Log.i( TAG, "DATABASE_SIZE:" + size );
		   	     //用于存放数据库信息的数据流
		   	     FileOutputStream fos = new FileOutputStream(databaseFilename);
		   	     byte[] buffer = new byte[1024];
		   	     int count = 0;
		   	     //把数据写入SD卡目录下
		   	     while ((count = is.read(buffer)) > 0) 
		   	     {
			   	     Log.i(TAG, "count:" + count);
		   	         fos.write(buffer, 0, count);
		   	     }
		   	     fos.flush();
		   	     fos.close();
		   	     is.close();
	   	   } catch (FileNotFoundException e) {
	   	    Log.e("Database", "File not found");
	   	    e.printStackTrace();
	   	   } catch (IOException e) {
	   	    Log.e("Database", "IO exception");
	   	    e.printStackTrace();
	   	   }
	   	  	} catch (Exception e) {
	   	  }
	   	 }
	 
	 
		private void showDialog(Context tcontext)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(tcontext);
			final Dialog ad = builder.create();
		    LayoutInflater inflater;
		    View layout=null;	  
	        if (!ad.isShowing())
	        {
	                ad.show();
	        	    WindowManager m = getWindowManager();  
	        	    WindowManager.LayoutParams p = ad.getWindow().getAttributes(); // 获取对话框当前的参数值    
	        	    p.width  = LayoutParams.FILL_PARENT;
	        	    p.height = LayoutParams.FILL_PARENT;
	        	    ad.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL); 
	        	    ad.getWindow().setAttributes(p);   
	                inflater=  LayoutInflater.from(tcontext);
	        		layout = inflater.inflate(R.layout.aboutus, null);
	                ad.getWindow().setContentView(layout);
                    layout.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							ad.dismiss();
						}});
                    TextView tv_version = (TextView)layout.findViewById(R.id.tv_version);
                    tv_version.setText(getString(R.string.versioncode)+SportData.getVersion(context));
	        }
		}
	 
	 
 
	 protected void onDestroy() {
		 context.unregisterReceiver( loadBroadcastReceiver );
		 if (progressDialog != null){  
     		progressDialog.dismiss();  
     		progressDialog = null;         
     	 }
		 super.onDestroy();
	 };
		private void updateStatisticsMonth(TextView tv_title,EditText tv_year,EditText tv_month)
		{
				cal.clear();
				cal.set(year, month, 1);
				year = cal.get(Calendar.YEAR);
			    month = cal.get(Calendar.MONTH);
			    tv_year.setText(year+"");
			    tv_month.setText((month+1)+"");
			    tv_title.setText(getString(R.string.syncaccount_title,month+1));
		}
}
