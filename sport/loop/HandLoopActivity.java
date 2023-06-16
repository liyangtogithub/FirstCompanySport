package com.desay.sport.loop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.desay.pstest.toast.showToast;
import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_mac;
import com.desay.sport.db.entity_sportrecord;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.SendServer;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.Socket_Data;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HandLoopActivity extends Activity 
{
	String tag = "receive   ";
	Handler handle;
	private String username = "";
	private sportDB db;
	public  boolean isParied = false;
	private BluetoothAdapter _bluetooth;
	entity_userinfo userinfo = null;
	String readString = null;
	String sportHistoryString = null;
	String passWordString = null;
	//int height = 0;
	boolean ifConnect = false;
	boolean ifActivate = true;
	int numActivate=0;//写入激活信息的次数
	String dateString = null;
	int minute = 0;
	int samplingCircle = 0;
	int samplingCircleNum = 0;
	int surplus = 11;//还剩几条
	ImageView iv_return = null;
	boolean firstCalander = true;
	int lastFootNum = 0;
	int footNum = 0;
	public static final String SPORT_INFO="desay.sport.sportinfo";
	public static final String BATTERY_VALUE="desay.sport.batt";
	private IntentFilter intentFilter=null;
	private BroadcastReceiver receiver=null;
    private ImageView loadingbt;
	public TextView state;
	public TextView battery;
	public TextView syn_time;
	public TextView tv_title ;
	public Button addDevice;
	public ProgressBar loop_progress;
	public ProgressDialog progressDialog;
	public ImageView loop_img_power ;
	private ConnectionMAS CMAS=null;
	private heap pre_heap ;//记录之前的块号
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm"); 
	int numErrow = 0;
	int numOneDayCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_loop);
		username = SportData.getUserName(this);
		db = new sportDB(HandLoopActivity.this);
		userinfo = db.GetUserInfo(this);
		_bluetooth = BluetoothAdapter.getDefaultAdapter();
		initView();
		initData();	
	}

	private void initView()
	{
		state=(TextView) findViewById(R.id.loop_state);
		battery=(TextView) findViewById(R.id.loop_tv_power);
		syn_time=(TextView) findViewById(R.id.syn_time);
		ImageView iv_return = (ImageView)findViewById(R.id.iv_return);
		loop_img_power = (ImageView) findViewById(R.id.loop_img_power);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		loadingbt = (ImageView) findViewById(R.id.loop_loading);
		loop_progress = (ProgressBar) findViewById(R.id.loop_progress);
		addDevice=(Button) findViewById(R.id.addDevice);
		addDevice.setEnabled(false);
		addDevice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setAction(MainTab.BOX_REMOVEBOND);
				sendBroadcast(intent);
				if (!_bluetooth.isEnabled())				
					_bluetooth.enable();
				startActivity(new Intent(HandLoopActivity.this,HandLoopConnect.class));
				finish();
			}
		});
		iv_return.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		loadingbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(MainTab.ifConnect){
					loop_progress.setVisibility(View.VISIBLE);
					loadingbt.setVisibility(View.INVISIBLE);
					ReadSportData();
					pre_heap = new heap();
				}
			}
		});
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.loop_control));

		String time = db.getIwanMac(username).getSynTime();
		if (time == null || time.equals(""))
			syn_time.setText(getString(R.string.syntime_none));
		else
			syn_time.setText(getString(R.string.syntime_is)+ dateFormat
					.format(Long.parseLong(time)));
		progressDialog=new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.bluetooth_connecting));
	}
	
	private void initData()
	{		 
		intentFilter = new IntentFilter();
		intentFilter.addAction(SPORT_INFO);
		intentFilter.addAction(BATTERY_VALUE);
		intentFilter.addAction(MainTab.BOX_REMOVEBOND);
		intentFilter.addAction(GattUtils.NOT_PAIRED);
		intentFilter.addAction(GattUtils.CONNECTE_FAILD);
		intentFilter.addAction(GattUtils.BOX_CONFIRM);
		receiver = new ReadInfoBroadcast();
		registerReceiver(receiver, intentFilter);
	}

	/**
	 * 扫描本机蓝牙设备，打开手机蓝牙
	 */
	@Override
	protected void onStart()
	{
		Message msg = new Message();
   	    msg.what = SportData.LOAD_SEND;
   	    Handlerload.sendMessage(msg);
   	    blecontent_2();
   	    if(!MainTab.ifConnect){
		   	 if(db.getIwanMac(username).getMac()!=null)
					progressDialog.show();				
   	    }else{
   	    	state.setText(getString(R.string.bluetooth_already));
   	    	showBatt(SportData.bat);
   	    }
   	    
		 String time=db.getIwanMac(username).getSynTime();
		 if(time==null||time.equals("")){
			 syn_time.setText(getString(R.string.syntime_none));
		 }else{
		 syn_time.setText(getString(R.string.syntime_is)+dateFormat.format(Long.parseLong(time)));
		 }
		super.onStart();
		
	}
	 private void blecontent_2()
	 {		 
		 new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					while (!_bluetooth.isEnabled())
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
					Handlerload.sendEmptyMessage(20);
					if (MainTab.ifConnect == false)
					{
						bc_startConnect();
						// connectLoop();
					}
					else
					{
						//state.setText(getString(R.string.bluetooth_already));
						readBatt();
					}

				}
			}).start();
	 }
	 
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}

	class ReadInfoBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(action.equals(SPORT_INFO)){
				String sportinfo=intent.getStringExtra("sportinfo");
				System.out.println("sportinfo:"+sportinfo);
				if (sportinfo.equals("FINISH CONFIRM") )							
					ReadSportData();														
				else 
					downloadLoopSport(sportinfo);
			}else if(action.equals(GattUtils.NOT_PAIRED)){
				if(progressDialog.isShowing())
					progressDialog.dismiss();
				showToast.normaltoast(HandLoopActivity.this,getString(R.string.loop_jianquan_ng), Toast.LENGTH_SHORT);					
			}else if(action.equals(GattUtils.CONNECTE_FAILD)){
				if(progressDialog.isShowing())
					progressDialog.dismiss();
				state.setText(getString(R.string.bluetooth_notconnect));
				battery.setText("--");
				loop_img_power.setImageResource(R.drawable.loop_power_0);
				if(intent.getBooleanExtra(SportData.IFPAIRED, true))
					showToast.normaltoast(HandLoopActivity.this,getString(R.string.bluetooth_failure), Toast.LENGTH_SHORT);
				else
					showToast.normaltoast(HandLoopActivity.this,getString(R.string.paired_failure), Toast.LENGTH_SHORT);
			}else if(action.equals(GattUtils.BOX_CONFIRM)){
				MainTab.ifConnect=true;
				if(progressDialog.isShowing())
					progressDialog.dismiss();
				state.setText(getString(R.string.bluetooth_already));
				showToast.normaltoast(HandLoopActivity.this,getString(R.string.bluetooth_success), Toast.LENGTH_SHORT);
				readBatt();
			}else if(action.equals(BATTERY_VALUE)){
				String batt=intent.getStringExtra("batt");					
				try{
					SportData.bat=Integer.parseInt(batt.substring(0,batt.length()-1));
					showBatt(SportData.bat);
				}catch (NumberFormatException e) {
    				// TODO Auto-generated catch block
					System.out.println("error = "+e.getMessage());
    				readBatt();
    			}
			}
		}
		
	}	
	
	private void showBatt(int bat)
	{
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
		else if (bat< 100)
		{
			loop_img_power.setImageResource(R.drawable.loop_power_80);
		}
		else
		{
			loop_img_power.setImageResource(R.drawable.loop_power_100);
		}
		battery.setText(getString(R.string.loop_manag_power) + bat+"%");	
	}
	
	
	private void bc_startConnect()
	{
		Intent scanIntent = new Intent();
		scanIntent.setAction(MainTab.START_CONN);
		sendBroadcast(scanIntent);
	}
	
    private void readBatt()
    {
		Intent scanIntent=new Intent();
		scanIntent.setAction(MainTab.BOX_READBATT);
		sendBroadcast(scanIntent);	
    }
    
	/**
	 * 手环连接成功后，下载存储手环运动轨迹。
	 * 存入运动轨迹临时表，暂不做计算
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
	            		//next day clear 
	            		numErrow = 0;
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
			samplingCircle = Integer.parseInt(temp[2]);//间隔时间
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
			//System.out.println("numOneDay:" + numOneDay);
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
		intent1.setAction(MainTab.BOX_READSPORT);
		sendBroadcast(intent1);
	}
	
	private void DelSportData()
	{
		numErrow = 0;
		Intent intent1 = new Intent();
		intent1.setAction(MainTab.BOX_DELETESPORT);
		sendBroadcast(intent1);
	}
	
	
	/**
	 * 手环数据上传完后，将数据计算后存入记录表
	 * 关闭进度条,存mac
	 */
	private void updateLoopData()
	{
		entity_mac mac=new entity_mac();
		mac.setSyntime(System.currentTimeMillis()+"");
		db.insertIwanMac(mac);
		syn_time.setText(getString(R.string.syntime_is)+dateFormat.format(System.currentTimeMillis()));
		loop_progress.setVisibility(View.INVISIBLE);
		loadingbt.setVisibility(View.VISIBLE);
		showToast.normaltoast(this, getString(R.string.loop_load_ok),showToast.THREE_SECOND);
		startService(new Intent(HandLoopActivity.this,SendServer.class));
	}

	
	
	private boolean getiwanmac()
	{
    	entity_mac iwanmac = db.getIwanMac(username);
    	if(iwanmac.getUpLoad()!=null && iwanmac.getUpLoad().equals(SportData.UNUPLOAD))
    	{
    		String senddata=Socket_Data.SendIwanMacData(HandLoopActivity.this,username,iwanmac);
     	 	CMAS=new ConnectionMAS(HandLoopActivity.this,Handlerload,senddata,14,SportData.NO_NET);
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
	    		    	  String senddata=Socket_Data.SendGetAddMsg(HandLoopActivity.this,username,"40007");
	    				  CMAS=new ConnectionMAS(HandLoopActivity.this,Handlerload,senddata,SportData.LOAD_OK,SportData.NO_NET);
	    				  CMAS.start();
	    		    	}
	    		    	break;
	    		    case SportData.NO_NET:
						//无网络或获取失败
	    				 //blecontent_2();
		    			break;
	    		    case SportData.LOAD_OK:
	    		    	closenet();
	       			    new AnalyzeUtils(HandLoopActivity.this).GetIwanMac(msg.getData().getString("body"),username);
	    		    	//blecontent_2();
	    		    	break;
	    		    case SportData.SEND_OK:
	    		    	closenet();
	        			db.refreshIwanMac(username);
	        			//blecontent_2();
	    		    	break;
	    			case SportData.OPEN_THREAD:
					    handler.postDelayed(updateThread ,20000);
	    				break;
	    			case 20:
	    				addDevice.setEnabled(true);
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
	@Override
	public void onDestroy()
	{
		BluetoothService.resetThread_TimeOut();
		unregisterReceiver(receiver);
		closenet();
		db.closeDB();
		super.onDestroy();
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
