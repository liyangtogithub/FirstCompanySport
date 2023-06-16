package com.desay.sport.loop;



import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_mac;
import com.desay.sport.db.entity_sportrecord;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.GetTime;
import com.desay.sport.friend.SendServer;
import com.desay.sport.loop.HandLoopActivity.heap;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;
import com.desay.sport.net.Info;
import com.desay.sport.slidepage.LocationOverlay;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.CustomProgressDialog;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.speech.RecognizerResultsIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BLE extends Activity 
{

    public static final int REQUEST_SELECT_DEVICE = 1;
    public static final int REQUEST_ENABLE_BT = 2;    
    public static final int PROFILE_STATE_READY = 1;
    public static final int PROFILE_CONNECTED = 2;
    public static final int PROFILE_DISCONNECTED = 3;    
    private int mState = PROFILE_DISCONNECTED;
    private BluetoothAdapter mBluetoothAdapter;
    public static final int READ_SPORT_OVER=4;
    public static final int READ_SPORT_RAWOVER=5;
    public static final int READ_SPORT_NOTOVER=6;
    
    public static final int READ_HEART_OVER=7;
    public static final int READ_HEART_RAWOVER=8;
    public static final int READ_HEART_NOTOVER=9;
    
    private BluetoothDevice mDevice = null;
   // private BluetoothAdapter mBtAdapter = null;
    public static boolean  ReadInfo=false;
    private ImageView loadingbt;

    private String username = "";
	private sportDB db;
	
	public static boolean isActivity=false;
	public  boolean isParied = false;
	public TextView state;
	public TextView battery;
	public TextView tv_title ;
	public TextView syn_time;
	public ImageView loop_img_power ;
	public ProgressBar loop_progress ;
	public Button addDevice;
	public ProgressDialog progressDialog;
	private heap pre_heap ;//记录之前的块号
	private IntentFilter intentFilter=null;
	   private BroadcastReceiver receiver=null;

	   public static final String BATTERY_VALUE="desay.sport.batteryvalue";
	   public static final String LOAD_SPORT="desay.sport.downsport";
	   public static final String STOP_PR="desay.sport.stoppr";
	entity_userinfo userinfo = null;
	String readString = null;
	String dateString = null;
	int minute = 0;
	int samplingCircle = 0;
	int samplingCircleNum = 0;
	int surplus = 11;//还剩几条
	
//	boolean ifLoad = false;
	boolean firstCalander = true;
	Timer timer  = null;
	int lastFootNum = 0;
	int footNum = 0;
    GetTime gt=new GetTime();
	private ConnectionMAS CMAS=null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm");
	int numErrow = 0;
	int numOneDayCount = 0;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//ReadInfo=false;	
		setContentView(R.layout.new_loop);
		db = new sportDB(BLE.this);
		userinfo = db.GetUserInfo(this);
		state=(TextView) findViewById(R.id.loop_state);
		battery=(TextView) findViewById(R.id.loop_tv_power);
		syn_time=(TextView) findViewById(R.id.syn_time);
		ImageView iv_return = (ImageView)findViewById(R.id.iv_return);
		loop_img_power = (ImageView) findViewById(R.id.loop_img_power);
		iv_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.loop_control));
		loadingbt = (ImageView) findViewById(R.id.loop_loading);	
		loop_progress = (ProgressBar) findViewById(R.id.loop_progress);
		addDevice=(Button) findViewById(R.id.addDevice);
		addDevice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled())				
					mBluetoothAdapter.enable();
    			startActivity(new Intent(BLE.this,BraceletConnectForBLE.class));
    			finish();
			}
		});
		loadingbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(MainTab.mState==1){
            		loop_progress.setVisibility(View.VISIBLE);
    				loadingbt.setVisibility(View.INVISIBLE);
                		System.out.println("send broadcast..");
                		ReadSportData();
                		pre_heap = new heap();
            	}
                	}

            
        });
	
		username = SportData.getUserName(this);
		progressDialog=new ProgressDialog(BLE.this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.bluetooth_synmac));

		 String time=db.getIwanMac(username).getSynTime();
		 if(time==null||time.equals("")){
			 syn_time.setText(getString(R.string.syntime_none));
		 }else{
			 syn_time.setText(getString(R.string.syntime_is)+dateFormat.format(Long.parseLong(time)));
		 }
		
		 intentFilter=new IntentFilter();
		 intentFilter.addAction(GattUtils.CONNECT_STATE_BROAD);
		 intentFilter.addAction(GattUtils.DISCONNECT_STATE_BROAD);
		 intentFilter.addAction(GattUtils.CONFIRM_MSG);
		 intentFilter.addAction(STOP_PR);
		 intentFilter.addAction(LOAD_SPORT);
		 intentFilter.addAction(BATTERY_VALUE);
		 receiver=new ConnectStateBroad();
		 registerReceiver(receiver, intentFilter);
	}
	
  
	 class ConnectStateBroad extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action=intent.getAction();
				if(action.equals(STOP_PR)){
					if(progressDialog.isShowing())
						progressDialog.dismiss();
						showToast.normaltoast(BLE.this,getString(R.string.device_notexsit), Toast.LENGTH_SHORT);
				}else if(action.equals(GattUtils.CONFIRM_MSG)){
					if(progressDialog.isShowing())
						progressDialog.dismiss();
						state.setText(getString(R.string.bluetooth_already));
						showToast.normaltoast(BLE.this,getString(R.string.bluetooth_success), Toast.LENGTH_SHORT);
						sendBroadcast(new Intent(MainTab.WRITE_ACTIVITY));
				}else if(action.equals(GattUtils.DISCONNECT_STATE_BROAD)){	
					state.setText(getString(R.string.bluetooth_notconnect));
					battery.setText("--");
					loop_img_power.setImageResource(R.drawable.loop_power_0);
					if(progressDialog.isShowing())
						progressDialog.dismiss();
					showToast.normaltoast(BLE.this,getString(R.string.bluetooth_failure), Toast.LENGTH_SHORT);
					/*try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent connectintent=new Intent();
					connectintent.setAction(MainTab.ACTION_GATT_CONNECT);
					sendBroadcast(connectintent);*/
				}
				else if(action.equals(LOAD_SPORT)){
					System.out.println("load sport..");
					String sport=intent.getStringExtra("sport");
					downloadLoopSport(sport);
					System.out.println("read spot:"+sport);
				}else if(action.equals(BATTERY_VALUE)){
					SportData.bat=intent.getIntExtra("batt", 0);					
					showBatt(SportData.bat);
				}
			}
	    	
	    }
	 
	 
		private void showBatt(int bat)
		{
			battery.setText(getString(R.string.loop_manag_power) + bat+" %");
			if (bat <= 20)
			{
				loop_img_power.setImageResource(R.drawable.loop_power_0);
			}
			else if (bat <= 40)
			{
				loop_img_power.setImageResource(R.drawable.loop_power_20);
			}
			else if (bat <= 60)
			{
				loop_img_power.setImageResource(R.drawable.loop_power_40);
			}
			else if (bat <= 80)
			{
				loop_img_power.setImageResource(R.drawable.loop_power_60);
			}
			else if (bat < 100)
			{
				loop_img_power.setImageResource(R.drawable.loop_power_80);
			}
			else
			{
				loop_img_power.setImageResource(R.drawable.loop_power_100);
			}	
		}
	 
    @SuppressLint("NewApi")
	@Override
    public void onStart() 
    {
    	if(MainTab.mState!=1){
			if(db.getIwanMac(username).getMac()!=null)
			progressDialog.show();
    	}else{
    		state.setText(getString(R.string.bluetooth_already));
    		showBatt(SportData.bat);
    	}
    	 
    	 final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
	        mBluetoothAdapter = bluetoothManager.getAdapter();
	        new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while (!mBluetoothAdapter.isEnabled())
					{
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(MainTab.mState==1)
						blecontent_4();
					else{
					 Message msg = new Message();
			    	 msg.what = SportData.LOAD_SEND;
			    	 Handlerload.sendMessage(msg);
					}
				}
			}).start();
        super.onStart();
    }
    private void blecontent_4()
    {
    	System.out.println("bleconnect..");
    	  if(MainTab.mState!=1){
    		progressDialog.setMessage(getString(R.string.bluetooth_connecting));
			Intent intent=new Intent();
			intent.setAction(MainTab.ACTION_CONNECT);
			sendBroadcast(intent);
			}else{
				Intent intent=new Intent();
				intent.setAction(MainTab.READ_BATT);
				sendBroadcast(intent);
			}
    }

    @Override
    public void onDestroy() 
    {
    	db.closeDB();
    	if(progressDialog.isShowing())
    		progressDialog.dismiss();
    	unregisterReceiver(receiver);
    	//ReadInfo=false;
    	 closenet();
    	 db.closeDB();
        super.onDestroy();
    }
   
   
    @Override
    protected void onStop() 
    {
    	
    	super.onStop();
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	
    	super.onPause();
    }
    @Override
    public void onResume() 
    {
        super.onResume();       
    }
    
   
	/**
	 * 手环连接成功后，下载存储手环运动轨迹。
	 */
	private void downloadLoopSport(String info)
	{
		long startTime = 0;
		long endTime = 0;
		int duractionTime = 0;
		int distance = 0;
		int sporttype = 0;
		double avgSpeed = 0;
		int calorie = 0;
		int height = Integer.parseInt(userinfo.getHeight());
		db.openOrCreateDB();	
		readString = info;
		Calendar calendar = Calendar.getInstance();

		String sonArray[] = readString.split(":");
		
		if(sonArray.length==3 && sonArray[1].length()==24)
		{
		String temp[] = sonArray[1].split(",");
		dateString = temp[0];
		
		try
		{
			int number = Integer.valueOf(sonArray[0]);
			long date = Long.valueOf(dateString);
            if(pre_heap.number==-1)
			{
            	System.out.println("pre_heap.number==-1");
            	pre_heap.number = number;
            	pre_heap.date   = date;
			}
            else if(number<pre_heap.number)
            {
            	if(date>pre_heap.date)
            	{
            		System.out.println("date="+date+":pre_heap.date="+pre_heap.date);
            		DelSportData();
            		return;
            	}
            	else
            	{
            		numErrow = 0;//next day clear numErrow
	            		numOneDayCount = 0;
            		System.out.println("date<<<<pre_heap.date");
	            	pre_heap.number = number;
	            	pre_heap.date   = date;
            	}
            }
		}
		catch( NumberFormatException e)
		{
			System.out.println("NumberFormatException="+e.getMessage());
			DelSportData();
			return;
		}

	    calendar.set(Calendar.YEAR,Integer.parseInt(dateString.substring(0, 4)));
		calendar.set(Calendar.MONTH,Integer.parseInt(dateString.substring(4, 6)) - 1);
		calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateString.substring(6, 8)));
		calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(dateString.substring(8, 10)));
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		minute = Integer.parseInt(dateString.substring(10, 12));
		samplingCircle = Integer.parseInt( temp[2] );//间隔时间
		samplingCircleNum = Integer.parseInt(temp[1].substring(0,3));//间隔时间的倍数
		calendar.set( Calendar.MINUTE, minute + samplingCircle * samplingCircleNum );

			String loadArray[] = sonArray[2].split(",");
			for (int j = 0; j < loadArray.length; j++)
			{
				if ( j!=0 )			
					calendar.add(Calendar.MINUTE, samplingCircle);	
				startTime = calendar.getTimeInMillis();
				if (startTime <= 0 ) break;
				footNum = Integer.parseInt(loadArray[j].equals("")?"-1":loadArray[j]);
				if (footNum<0)continue;
				numOneDayCount++;
				//System.out.println("jieshou footNum == "+footNum);
				//System.out.println("numOneDayCount == "+numOneDayCount);
				if (footNum == 0)
					continue;		
				endTime = startTime + samplingCircle * 1000 * 60;			
				duractionTime = samplingCircle * 60;
				distance = (int) PublicUtils.getDistanceByStep(footNum, height);					
				avgSpeed = Double.parseDouble(SportData
								.getKilometer((long) (distance * 3.6 * 1000 / duractionTime)));
				sporttype = PublicUtils.getSportType(avgSpeed);
				calorie = PublicUtils.getCalorie(userinfo.getWeight(), distance);

				ContentValues values = new ContentValues();
				values.put("_username", username);
				values.put("_starttime", startTime );
				values.put("_endtime", endTime );
				values.put("_distance", distance + "");
				values.put("_durationtime", duractionTime + "");
				values.put("_sporttype", sporttype + "");
				values.put("_avgspeed", avgSpeed + "");
				values.put("_footNum", footNum );
				values.put("_calorie", calorie + "");
				values.put("_mode", "1");// 手环				
				values.put("_ifupload", SportData.UNUPLOAD);
				db.simpleInsertSportRecord(values);
			}
			int numOneDay = Integer.parseInt(temp[1].substring(4, 7));
			surplus = numOneDay - samplingCircleNum;
			System.out.println("surplus:" + surplus);
			if (numOneDayCount < numOneDay)
				numErrow++;
				
			if ( numErrow > 2 )
			{
				DelSportData();	
			}
			else 
			{
				if (surplus <= 10 && numOneDayCount == numOneDay )
				{
					if (sonArray[0].equals("000")&& surplus==0 )
					{
						updateLoopData();
					}
					else
					{
						numOneDayCount = 0;
						DelSportData();	
					}
				}
				else
				{
					ReadSportData();
				}
			}	
		}
		else
		{
			System.out.println("data_error = " + readString);
			if  ( ++numErrow > 2 )
			{
				DelSportData();			
			}
			else {
				ReadSportData();
			}
		}
		
	}
	
	private void ReadSportData()
	{
		Intent intent1 = new Intent();
		intent1.setAction(MainTab.READ_SPORT);
		sendBroadcast(intent1);
	}
	
	private void DelSportData()
	{
		numErrow = 0;
		Intent intent=new Intent();
		intent.setAction(MainTab.WRITE_SPORT);
		sendBroadcast(intent);
	}
		
	/**
	 * 手环数据上传完后，关闭进度条,存mac
	 */
	private void updateLoopData()
	{		
//		ifLoad = true;
		//if(mDialog.isShowing())
			//mDialog.cancel();
		ReadInfo=true;
		entity_mac mac=new entity_mac();
		mac.setSyntime(System.currentTimeMillis()+"");
		db.insertIwanMac(mac);
		syn_time.setText(getString(R.string.syntime_is)+dateFormat.format(System.currentTimeMillis()));
		loop_progress.setVisibility(View.INVISIBLE);
		loadingbt.setVisibility(View.VISIBLE);
		showToast.normaltoast(this, getString(R.string.loop_load_ok), showToast.THREE_SECOND);
		BLE.this.startService(new Intent(BLE.this,SendServer.class));
		//finish();//暂时关闭，正常要用
	}
	
    private boolean getiwanmac()
	{
    	entity_mac iwanmac = db.getIwanMac(username);
    	if(iwanmac!=null && iwanmac.getUpLoad()!=null&&iwanmac.getUpLoad().equals(SportData.UNUPLOAD))
    	{
    		String senddata=Socket_Data.SendIwanMacData(BLE.this,username,iwanmac);
     	 	CMAS=new ConnectionMAS(BLE.this,Handlerload,senddata,14,SportData.NO_NET);
 			CMAS.start();
			return false;
    	}
		return true;
	}
	 Handler Handlerload= new Handler(){	
	    	public void handleMessage(Message msg) {
	    		super.handleMessage(msg);
	    		switch(msg.what)
	    		{
	    		    case SportData.LOAD_SEND:
	    		    	if(getiwanmac())
	    		    	{ 
	    		    	  String senddata=Socket_Data.SendGetAddMsg(BLE.this,username,"40007");
	    				  CMAS=new ConnectionMAS(BLE.this,Handlerload,senddata,SportData.LOAD_OK,SportData.NO_NET);
	    				  CMAS.start();
	    		    	}
	    		    	break;
	    		    case SportData.NO_NET:
						//无网络或获取失败
	    		    	 closenet();
	    				 blecontent_4();
		    			break;
	    		    case SportData.LOAD_OK:
	    		    	closenet();
	       			    new AnalyzeUtils(BLE.this).GetIwanMac(msg.getData().getString("body"),username);
	    		    	blecontent_4();
	    		    	break;
	    		    case SportData.SEND_OK:
	    		    	closenet();
	        			db.refreshIwanMac(username);
	        			blecontent_4();
	    		    	break;
	    			case SportData.OPEN_THREAD:
					    handler.postDelayed(updateThread ,20000);
	    				break;
	    			  default:
	    					break;
	    			}
	    		}
	    	};
	Handler handler  = new Handler();  
  	Runnable updateThread =  new Runnable(){  
  	       public void run() { 
  	    	   Message msg = new Message();
  	    	   msg.what = SportData.NO_NET;
  	    	   Handlerload.sendMessage(msg);
  	  }  
   };
  private void closenet()
  {
  	if(CMAS!=null)
  	    CMAS.closeSocket();
  	if(handler!=null&&updateThread!=null)
  		handler.removeCallbacks(updateThread);
  		CMAS=null;
  }
  
	class heap
	{
		public int number ;
		public long date   ;
		public heap()
		{
			number = -1;
			date   = -1;
		}
	}
	
}
