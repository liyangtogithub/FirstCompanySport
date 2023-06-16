package com.desay.sport.main;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.desay.pstest.toast.showToast;
import com.desay.sport.slidepage.MoveBg;
import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.loop.BLE;
import com.desay.sport.loop.BluetoothLeService;
import com.desay.sport.loop.BluetoothService;
import com.desay.sport.loop.GattUtils;
import com.desay.sport.loop.HandLoopActivity;
import com.desay.sport.loop.BluetoothService.MyBinder;
import com.desay.sport.multimedia.MusicService;
import com.desay.sport.net.Info;
import com.desay.sport.net.Login;
import com.desay.sport.slidepage.MyPagerAdapter;
import com.desay.sport.slidepage.tab1_sport;
import com.desay.sport.slidepage.tab3_ranking;
import com.desay.sport.slidepage.tab4_record;
import com.desay.sport.slidepage.tab6_iwan;
import com.desay.sport.update.UpdateManager;
import com.desay.utils.NetworkConnection;
import com.desay.utils.WebData;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/*思路
 * 蓝牙2.1是先在设置里绑定手环，然后程序取到绑定手环的mac地址，通过mac找到远程设备，
 * 远程设备通过UUID得到SOCKET，SOcket获取输入输出流
 * 
 * 
 * */
@SuppressLint("NewApi")
public class MainTab extends BaseActivity implements OnClickListener{
	private static final String TAG = "MainTab";
	public static final int ANIMATION_DURATION = 300;
	private static final String macStart = "48:03:62";
	
	public static final int INIT_PAGE = 1;
	public static final int INIT_TAB  = 2;	
	public static final int OPEN_UPDATE  = 3;
	public static final int CHECK_UPDATE = 6;
	public static final int NO_UPDATE  = 5;
	LayoutInflater 		inflater;
	View			 	first;
	View 				second;
	View 				third;
	View 				four;

	TextView			textView1;  
	TextView			textView2;  
	TextView 			textView3;
	TextView 			textView4;	
	ImageView           iv_title;
	ImageView           iv_login;
	ViewPager 			viewPager;  
	int 				moveX;    
	int 				width;    
	int 				index;    
	List<View> 			viewList=new ArrayList<View>();
	private List<TextView> list;
	private Context context;
//	LocalActivityManager manager = null;
	
	TextView tab_front = null;
	
	private int tab1_width;
	private int tab2_width;
	private int tab3_width;
	
	private int tab_height;
	private int line_width;
	private RelativeLayout rl_tab = null;
	private int startX;//移动的起始位置
	private static int preindex;
	private boolean if_quit ;
	
	private Handler handle;
	private UpdateManager um;
	private long waitTime = 2000;  
	private long touchTime = 0; 
	private String mac;
	//blue4.0
		private BluetoothAdapter.LeScanCallback mLeScanCallback;
	    public static int mState ;
		public static final int REQUEST_SELECT_DEVICE = 1;
	    public static final int REQUEST_ENABLE_BT = 2;    
	    public static final int PROFILE_STATE_READY = 1;
	    public static final int PROFILE_CONNECTED = 2;
	    public static final int PROFILE_DISCONNECTED = 3;  
	    public static final int BLUE_ACTIVITY=4;
	    public static final int BLUE_NOT_ACTIVITY=5;
	    public static final int AUTH_FAIL=6;
	
	    public static int MONITOR_STATE;
	    
		private BroadcastReceiver receiver=null;
		private IntentFilter intentFilter=null;
	    public static final String ACTION_CONNECT="desay.sport.connect";
	    public static final String READ_HEART="desay.sport.readheart";
	    public static final String READ_SPORT="desay.sport.readsport";
	    public static final String WRITE_SPORT="desay.sport.writesport";
	    public static final String SETTING_PAR="desay.sport.settingsport";
	    public static final String JIHUO_SPORT="desay.sport.jihuo";
	    public static final String BATTERY_VALUE="desay.sport.battery";
	    public static final String READ_NAME="desay.sport.readname";
	    public static final String READ_BATT="desay.sport.readbatt";
	    public static final String WRITE_ACTIVITY="desay.sport.writeactivity";
	    public static final String DIS_CONNECT="desay.sport.disconnect";
	    
	    public static final String START_CONN="desay.sport.conn";
	    public static final String START_SCAN="desay.sport.scan";
	    public static final String BOX_READSPORT="desay.sport.boxreadsport";
	    public static final String BOX_DELETESPORT="desay.sport.boxdeletesport";
	    public static final String BOX_READBATT="desay.sport.boxreadbatt";
	    public static final String BOX_REMOVEBOND="desay.sport.removebond";
	    public static final String REMOVECONNECT="desay.sport.removeconnect";

		public boolean AddNew = false; //2.1只要点击添加新设备就设为true,连接流程中要添加确认
	    public boolean isunBind=false; //2.1点击添加新设备则解除之前配对，重新绑定服务启动连接
	    public static boolean isPaired=false;	//4.0有mac地址则不再进行确认
	    public static boolean SELECT_BLE=false;	//是否为支持ble设备
		public static boolean ifConnect = false;	//2.1是否连接上
		public static boolean isScanning=false;		//4.0是否启动了扫描
		private BluetoothDevice mDevice = null;	
	    private BluetoothAdapter mBtAdapter;
	    public BluetoothLeService mBluetoothLeService;
		public int SDK_Version;		
		private MyBinder binder;
		private String username = "";
		private String passWordString = null;
		private sportDB db = null;
		private boolean if_searched ;//判断是否有搜索到设备
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		super.onCreate(savedInstanceState);
		context = MainTab.this;
		db = new sportDB(context);
		mState= PROFILE_DISCONNECTED;
		if_quit = true;
		setContentView(R.layout.main_tab);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) 
		{
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
		SELECT_BLE = getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);		
		if (SELECT_BLE)
		{
						System.out.println("is ble..");
						final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
						mBtAdapter = bluetoothManager.getAdapter();
					        
					      
					        
							Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
							bindService(gattServiceIntent, mServiceConnection,BIND_AUTO_CREATE);
							
							
							final IntentFilter intentFilter = new IntentFilter();
							intentFilter.addAction(ACTION_CONNECT);
							intentFilter.addAction(READ_SPORT);
							intentFilter.addAction(WRITE_SPORT);
							intentFilter.addAction(READ_BATT);
							intentFilter.addAction(WRITE_ACTIVITY);
							intentFilter.addAction(START_SCAN);
							intentFilter.addAction(DIS_CONNECT);
							intentFilter.addAction(REMOVECONNECT);					
							intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
							intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
							intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
							intentFilter.addAction(BluetoothLeService.HISTORY_S_MSG);
							intentFilter.addAction(BluetoothLeService.BATTERY_MSG);
							registerReceiver(mGattUpdateReceiver, intentFilter);
							
							mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

						        @Override
						        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
						            runOnUiThread(new Runnable() {
						                @SuppressLint("NewApi")
										@Override
						                public void run() {
						                	System.out.println("mac:"+mac+",start scan.."+device.getAddress());
						                	//if(device.getAddress().equals("10:18:10:10:10:10"))
											if(mac!=null&&device.getAddress().equalsIgnoreCase(mac)){
												mBtAdapter.stopLeScan(mLeScanCallback);
												mBluetoothLeService.connect(mac);												
												System.out
														.println("stop...");
												isScanning = false;
												mState=3;
											}
											if (mac==null&&device.getName().equals("Desay iWan")) {
												System.out.println("has discover..");
												mBtAdapter.stopLeScan(mLeScanCallback);
												isScanning = false;
												mDevice = BluetoothAdapter.getDefaultAdapter()
														.getRemoteDevice(device.getAddress());
												mBluetoothLeService.connect(mDevice.getAddress());
												mState = 3;
											}				                   
						                }
						            });
						        }
						    };
							System.out.println("register broadcast");
					}else{
						System.out.println("blue 2.1");
						Intent intent1 = new Intent(this,
								BluetoothService.class);
						bindService(intent1, conn, Context.BIND_AUTO_CREATE);
						IntentFilter filter = new IntentFilter();
						filter.addAction(BluetoothDevice.ACTION_FOUND);
						filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
						filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
						this.registerReceiver(mReceiver, filter);
						
						IntentFilter filterRead = new IntentFilter();
						filterRead.addAction("com.desay.sleep");
						this.registerReceiver(readReceiver, filterRead);
						
						 intentFilter=new IntentFilter();
						 intentFilter.addAction(START_CONN);
						 intentFilter.addAction(START_SCAN);
						 intentFilter.addAction(BOX_DELETESPORT);
						 intentFilter.addAction(BOX_REMOVEBOND);
						 intentFilter.addAction(REMOVECONNECT);	
						 intentFilter.addAction(BOX_READSPORT);
						 intentFilter.addAction(BOX_READBATT);
						 receiver=new BoxBroadcast();
						 registerReceiver(receiver, intentFilter);
					}
		
	
		UpdateManager.init(this);
		handle = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what)
				{
				case INIT_PAGE:
					initTitle();
					initPager();
					initImageLoader(context);
					judgmentUP();//判断网络是否更新
					break;
				case INIT_TAB:
		    		initTabFront();
					break;
				case OPEN_UPDATE:
					SharedPreferences.Editor editor3 = getSharedPreferences("setaim_msg", MODE_PRIVATE).edit();		
					editor3.putBoolean("haveUP",false);
					long validTime3 = System.currentTimeMillis();
					validTime3 = validTime3 + 86400000;
					editor3.putLong("uptimeTime", validTime3);
					editor3.commit();
					um.showNoticeDialog(MainTab.this);
					Intent i_upsilde = new Intent(SportData.RECEIVER_UPSILDE);
					sendBroadcast(i_upsilde);
					break;
				case CHECK_UPDATE:
					um=new UpdateManager(MainTab.this);
    				update_thread upt=new update_thread();
    		    	upt.start();
		    		break;
				case NO_UPDATE:
					SharedPreferences.Editor editor = getSharedPreferences("setaim_msg", MODE_PRIVATE).edit();		
					long validTime = System.currentTimeMillis();
					validTime = validTime + 86400000;
					editor.putLong("uptimeTime", validTime);
					editor.putBoolean("haveUP",true);
					editor.commit();
				  	break;
				}
				super.handleMessage(msg);
			}
		};
		new Init(INIT_PAGE).start();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStart");		
		preindex = 0;
				
		super.onStart();
	}
	
	 public static String getModel(Context context) {
         String androidModel = null;
         androidModel = android.os.Build.MODEL;
         return androidModel;
 }
 //蓝牙2.1
		private ServiceConnection conn = new ServiceConnection()
		{

			@Override
			public void onServiceDisconnected(ComponentName name)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service)
			{
				// TODO Auto-generated method stub
				binder = (MyBinder) service;
				System.out.println("service.find..");
			}
		};
		
		class BoxBroadcast extends BroadcastReceiver{

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action=intent.getAction();
				if(action.equals(START_SCAN)){
					System.out.println("-----------BoxBroadcast_START_SCAN------------");
					if_searched = false;
					mBtAdapter.startDiscovery();
//					showToast.normaltoast(MainTab.this,getString(R.string.bluetooth_connecting),
//								Toast.LENGTH_SHORT);
				}else if(action.equals(BOX_READSPORT)){
					binder.readSportHistory();
				}else if(action.equals(BOX_DELETESPORT)){
					binder.deleteSportHistory();
				}else if(action.equals(BOX_READBATT)){
					binder.readBatt();
				}
				else if(action.equals(BOX_REMOVEBOND)){
					if(ifConnect){
					unbindService(conn);
					stopService(new Intent(MainTab.this, BluetoothService.class));
					isunBind=true;
					}
					//isPaired=false;
					ifConnect=false;
					removeDevice();
				}else if(action.equals(START_CONN)){
					connectLoop();
				}
				else if(REMOVECONNECT.equals(action))
				{
					System.out.println("REMOVECONNECT");
					if(ifConnect)
					{
						unbindService(conn);
//						stopService(new Intent(MainTab.this, BluetoothService.class));
//						isunBind=true;
						ifConnect=false;
					}		
				}
			}
			
		}
		
		
		private void removeDevice(){
			System.out.println("removedevice..");
			Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
			if (pairedDevices.size() > 0)
			{
				for (BluetoothDevice bondDevice : pairedDevices)
				{
					if (bondDevice.getName().equalsIgnoreCase("iWan Link") || bondDevice.getAddress().startsWith(macStart))
					{
					try {
						
						boolean a=removeBond(bondDevice.getClass(), bondDevice);
						System.out.println("remove :"+a);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
			}
		}
		static public boolean removeBond(Class btClass,BluetoothDevice btDevice) throws Exception{
			Method removeBondMethod=btClass.getMethod("removeBond");
			Boolean returnValue=(Boolean) removeBondMethod.invoke(btDevice);
			  return returnValue;
			  
		}
		private void connectLoop()
		{
			Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
			// 如果已配对，直接连接
			System.out.println("pairedDevices.size()="+pairedDevices.size());
			boolean isbonded = false;
			if (pairedDevices.size() > 0)
			{
				for (BluetoothDevice bondDevice : pairedDevices)
				{
					System.out.println("name="+bondDevice.getName()+";addr="+bondDevice.getAddress());
					if (bondDevice.getName().equals("iWan Link") || bondDevice.getAddress().startsWith(macStart))
					{
						isbonded = true;
						AddNew = false;
						Intent intent1 = new Intent(this,BluetoothService.class);
						intent1.putExtra("address", bondDevice.getAddress());
						startService(intent1);
						bindService(intent1, conn, Context.BIND_AUTO_CREATE);
						break;
					}
				}
			}
			if(!isbonded)
			{
				Intent connected=new Intent();
				connected.putExtra(SportData.IFPAIRED, false);
				connected.setAction(GattUtils.CONNECTE_FAILD);
				sendBroadcast(connected);
			}

		}

		private final BroadcastReceiver mReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				System.out.println("start scann device..");
				String action = intent.getAction();
				if (BluetoothDevice.ACTION_FOUND.equals(action))
				{
					try
					{
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						System.out.println("device name:"+device.getName()+device.getName().equals("iWan Link"));
					if(device.getName()!=null&&device.getName().equalsIgnoreCase("iWan Link")  || device.getAddress().startsWith(macStart))
						{
							System.out.println("has discovered..="+device.getAddress());
							AddNew=true;
	                		mBtAdapter.cancelDiscovery();
							Intent intent1 = new Intent(MainTab.this,BluetoothService.class);
							intent1.putExtra("address",device.getAddress());
							startService(intent1);
							bindService(intent1, conn, Context.BIND_AUTO_CREATE);
							isunBind=false;			
							if_searched = true;
					}}
					catch (Exception e)
					{
						//connectLoop();
					}
				}
				else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
				{
					if(!if_searched)
					{
						Intent connected=new Intent();
						connected.setAction(GattUtils.CONNECTE_FAILD);
						connected.putExtra(SportData.IFFOUNDED, false);
						sendBroadcast(connected);
					}
					System.out.println("-----------ACTION_DISCOVERY_FINISHED------------");
				}
				else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
				{
		            int currentState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
					System.out.println("先前状态："+intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE, -1));
					System.out.println("当前状态："+currentState);
					switch(currentState)
					{
					case BluetoothAdapter.STATE_OFF:
						System.out.println("蓝牙已关闭");
					break;
					case BluetoothAdapter.STATE_ON:
						System.out.println("蓝牙已打开");
					break;
					case BluetoothAdapter.STATE_TURNING_OFF:
						System.out.println("蓝牙正在关闭");
					break;
					case BluetoothAdapter.STATE_TURNING_ON:
						System.out.println("蓝牙正在打开");
					break;
					}
					
				}
			}
		};
		

		private final BroadcastReceiver readReceiver = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent)
			{
				
				String action = intent.getAction();
				
			 if ("com.desay.sleep".equals(action))
				{
					String s = intent.getStringExtra("type");
					/*if(s.equals("write"))
					{
	   					String writeInfo=intent.getStringExtra("message");
	   				}*/
					//返回的内容
					if (s.equals("read"))
					{
						String readInfo =  intent.getStringExtra("message");

						if (readInfo==null||readInfo.equals(""))
						{							
							return;
						}
						System.out.println("InfoCode:"+BluetoothService.infoCode+",readInfo:"+readInfo);
						//BluetoothService.infoCode = 500;
							switch(BluetoothService.infoCode){
//							case 0:
//								System.out.println("at:"+readInfo.equals("OK"));
//								if (readInfo.equals("OK"))// 串口已开始
//								{
//									if(AddNew)
//										binder.writeConfirm();									
//									else
//										binder.readName();
//								}
//								else
//								{
//									binder.start();
//								}
//								break;
							case 1:
								if(readInfo.indexOf("+NAME")==0){
									readInfo=readInfo.substring(7,readInfo.length()-1);
									if(readInfo.equals(username)){
											binder.setTime();
									}else{
										SportData.sendBroadCast(MainTab.this,MainTab.REMOVECONNECT);
										//isPaired=false;
										sendBroadcast(new Intent(GattUtils.NOT_PAIRED));
									}
								}
								break;
							case 3:
								if(readInfo.indexOf("+USERBOND")==0)
								{
									Intent intent1 =new Intent(BluetoothLeService.CHANGE_ACTIVITY);
									sendBroadcast(intent1);
									binder.readMac();
								}
								break;
							case 7:
								if(readInfo.indexOf("+MAC")==0)
								{
									readInfo=readInfo.substring(6,readInfo.length()-1);								
									db.saveIwanMac(context,readInfo,db);
									binder.writeName(username);
								}
							case 14:
								if (readInfo.indexOf("+POWER")==0)
								{   
									try
									{
										readInfo=readInfo.substring(8,readInfo.length()-1);
										System.out.println("batt:"+readInfo);
										Intent intent1=new Intent();
										intent1.setAction(HandLoopActivity.BATTERY_VALUE);
										intent1.putExtra("batt", readInfo);
										sendBroadcast(intent1);
									}catch(StringIndexOutOfBoundsException e)
									{
										System.out.println("error = "+e.getMessage());
										binder.readBatt();
									}
							
								}
								break;
							case 26:	
								if(readInfo.indexOf("+SP")==0 || readInfo.indexOf("HIS:")!=-1 )
								{
									readInfo=readInfo.substring(readInfo.indexOf(":")+2, readInfo.length()-1);
									Intent intent1=new Intent();
									intent1.setAction(HandLoopActivity.SPORT_INFO);
									intent1.putExtra("sportinfo", readInfo);
									sendBroadcast(intent1);
								}
								break;
							case 50:
								if(readInfo.indexOf("+NAME")==0)
								{
									binder.setTime();
								}
								break;
							case 56:
								if(readInfo.indexOf("+AUTH")==0)
								{
									sendBroadcast(new Intent(GattUtils.BOX_CONFIRM));
								}
								break;
							case 67:
								if(readInfo.indexOf("+MODEL")==0)
								{
									binder.writeAuth("1");
									
								}
								break;
							case 100:
								if(readInfo.indexOf("+TIME")==0)
								{
									binder.writeModel("2:00050020060");
								}
								break;
								default:
									break;
							}
					}
						
					
					
					//状态
					if (s.equals("state"))
					{
						int i = intent.getIntExtra("message", BluetoothService.STATE_NOTCONNECTED);
						if (i == BluetoothService.STATE_CONNECTING)
						{
//							showToast.normaltoast(MainTab.this,getString(R.string.bluetooth_connecting),
//									Toast.LENGTH_SHORT);
						}
						if (i ==BluetoothService.STATE_CONNECTED)
						{						
							System.out.println("maintab:sucusss");
							sendBroadcast(new Intent(GattUtils.CONNECTE_SUCCESS));
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//binder.start();
							if(AddNew)
								binder.writeConfirm();									
							else
								binder.readName();
						}
						if (i == BluetoothService.STATE_NOTCONNECTED)
						{
							Intent connected=new Intent();
							connected.setAction(GattUtils.CONNECTE_FAILD);
							sendBroadcast(connected);
							ifConnect = false;
						}
					}
				}
			}
		};
		
//蓝牙4.0

		Thread r=new Thread(new Runnable() {
			
			@SuppressLint("NewApi")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("do the handler..");
            	if(isScanning){
                	mBtAdapter.stopLeScan(mLeScanCallback);
    				isScanning = false;
    				mState = 2;
    				sendBroadcast(new Intent(BLE.STOP_PR));
            	}
			}
		});

			
			private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver()
			{
				@SuppressLint("NewApi")
				@Override
				public void onReceive(Context context, Intent intent)
				{
					final String action = intent.getAction();
					System.out.println("action = " + action);
					if (ACTION_CONNECT.equals(action))
					{
						String omac = db.getIwanMac(SportData.getUserName(context)).getMac();
						if(omac!=null && !omac.equals("")){
							isPaired=true;							
							System.out.println("readInfo:"+omac);
							mac=omac.substring(0, 2)+":"+omac.substring(2,4)+":"+omac.substring(4,6)+":"
									+omac.substring(6,8)+":"+omac.substring(8,10)+":"+omac.substring(10,12);
							if(isScanning)mBtAdapter.stopLeScan(mLeScanCallback);
							isScanning=false;
							//mBluetoothLeService.connect(newmac);
							mBtAdapter.startLeScan(mLeScanCallback);
							isScanning=true;
							 handle.postDelayed(r, 15000);
						}else{
							showToast.normaltoast(context, context.getString(R.string.loop_add_device), showToast.TWO_SECOND);
							//sendBroadcast(new Intent(BLE.STOP_PR));
						}
					}else if(BluetoothLeService.ACTION_GATT_CONNECTED.equals(action))
					{
						System.out.println("has connected..");
						mState=1;
						Intent connectIntent=new Intent();
						connectIntent.setAction(GattUtils.CONNECT_STATE_BROAD);
						sendBroadcast(connectIntent);
					}
					else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action))
					{
						mState=2;
						System.out.println("mState:"+mState);
						if(!isScanning){
							Intent connectIntent=new Intent();
							connectIntent.setAction(GattUtils.DISCONNECT_STATE_BROAD);
							sendBroadcast(connectIntent);
						}
					}
					else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action))
					{
						System.out.println("discovery.");
						if(!isPaired)
						mBluetoothLeService.enableAuth();
						sendBroadcast(new Intent(GattUtils.CONFIRM_MSG));
					}
					else if (BluetoothLeService.HISTORY_S_MSG.equals(action))
					{
						String sport=intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
						System.out.println(sport);
						Intent sportIntent=new Intent();
						sportIntent.setAction(BLE.LOAD_SPORT);
						sportIntent.putExtra("sport", sport);
						sendBroadcast(sportIntent);
					//	his.setText(sport);

					}else if(BluetoothLeService.BATTERY_MSG.equals(action)){
						int battery=intent.getIntExtra(BluetoothLeService.EXTRA_DATA, 0);
						Intent battIntent=new Intent();
						battIntent.setAction(BLE.BATTERY_VALUE);
						battIntent.putExtra("batt", battery);
						sendBroadcast(battIntent);
					}else if(READ_SPORT.equals(action)){
						mBluetoothLeService.readHis_S();
					}else if(WRITE_SPORT.equals(action)){
						mBluetoothLeService.writeSportHis();
					}else if(READ_BATT.equals(action)){
						mBluetoothLeService.readBatt();
					}else if(START_SCAN.equals(action)){
						if(mState==1||mState==3)
							mBluetoothLeService.disconnect();
						mState=2;
						isPaired=false;
						if(!isScanning){
							mac=null;						
							mBtAdapter.startLeScan(mLeScanCallback);
							isScanning=true;
							 handle.postDelayed(r, 15000);
						}					
					}else if(DIS_CONNECT.equals(action)){
						if(mState==1)
							mBluetoothLeService.disconnect();
						mState=2;
						isPaired=false;
					}
					else if(WRITE_ACTIVITY.equals(action)){

						mBluetoothLeService.writeActivity();
					}
					else if(REMOVECONNECT.equals(action))
					{
			        	System.out.println("disconnect..");
						if(mState==1)
							mBluetoothLeService.disconnect();
						mState=2;
						isPaired=false;
					}
				}
			};
			
			private final ServiceConnection mServiceConnection = new ServiceConnection()
			{
				@Override
				public void onServiceConnected(ComponentName componentName,
						IBinder service)
				{
					mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
					if (!mBluetoothLeService.initialize())
					{

						finish();
					}

				}

				@Override
				public void onServiceDisconnected(ComponentName componentName)
				{
					mBluetoothLeService = null;
				}
			}; 
	    
	private class Init extends  Thread
	{
        private int type = -1;
		public Init(int ttype)
		{
			type = ttype;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = type;
			handle.sendMessage(msg);
		}
	};
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	    long currentTime = System.currentTimeMillis();  
	    if((currentTime-touchTime)>=waitTime) {  
	        showToast.normaltoast(context, getString(R.string.exitapp), showToast.TWO_SECOND);
	        touchTime = currentTime; 
	    }
	    else 
	    {  
	        finish();  
	    } 
	}
	
    private void initTitle()
    {
		textView1 = (TextView) findViewById(R.id.tv_sport);
		textView2 = (TextView) findViewById(R.id.tv_record);
		textView3 = (TextView) findViewById(R.id.tv_ranking);
		textView4 = (TextView) findViewById(R.id.tv_medal);
		iv_title  = (ImageView) findViewById(R.id.iv_title);
		iv_login  = (ImageView) findViewById(R.id.iv_login);
		textView1.setOnClickListener(this);
		textView2.setOnClickListener(this);
		textView3.setOnClickListener(this);
		textView4.setOnClickListener(this);	
		iv_title.setOnClickListener(this);	
		iv_login.setOnClickListener(this);
		list=new ArrayList<TextView>();
		list.add(textView1);
		list.add(textView2);
		list.add(textView3);
		list.add(textView4);
    }

    private void initPager(){
		List<Fragment> frag = new ArrayList<Fragment>();
		frag.add(new tab1_sport());
		frag.add(new tab4_record());
		frag.add(new tab3_ranking());
		frag.add(new tab6_iwan());
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),frag));
		viewPager.setCurrentItem(SportData.TAB1_SPORT);
		viewPager.setOnPageChangeListener(new MyPageListener());
	}
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tv_sport:
			viewPager.setCurrentItem(SportData.TAB1_SPORT);
			break;
		case R.id.tv_record:
			viewPager.setCurrentItem(SportData.TAB2_RECORD);
			break;
		case R.id.tv_ranking:
			viewPager.setCurrentItem(SportData.TAB3_RANKING);  
			break;
		case R.id.tv_medal:
			viewPager.setCurrentItem(SportData.TAB4_MEDAL);  
			break;
		case R.id.iv_title:
			toggle();
			break;
		case R.id.iv_login:
			Intent i = new Intent();
			if(SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME))
			    i.setClass(context,Login.class);
				else if (db.HaveUserInfo(context)
						&& db.GetUserInfo(context).getWeight() != null
						&& (!(db.GetUserInfo(context).getWeight()).equals("")))
				{
					username = SportData.getUserName(this);
					passWordString = SportData.getUserPassword(MainTab.this);						
					if (mBtAdapter!=null&&!mBtAdapter.isEnabled()) {
						SportData.SetBTStatus(context,true);
						mBtAdapter.enable();
					}  
				if(SELECT_BLE){
					
				i.setClass(context,BLE.class);}
				else{
					
					i.setClass(context,HandLoopActivity.class);
					}
			}
			else
				{
				showToast.normaltoast(context, getString(R.string.inserinfo_toast), showToast.TWO_SECOND);	
				i.setClass(context,Info.class);
				}
			startActivity(i);
			break;
		}
	}

	class MyPageListener implements  OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {}
		@Override
		public void onPageSelected(int arg0) {
			changeTabUI(arg0);
			setPageOrientation(arg0);
		}
	}

	private void refreshTabData(int tab_index)
	{
		if(BuildConfig.DEBUG)Log.d(TAG, "refreshTabData===="+tab_index);
		if(preindex==SportData.TAB2_RECORD && tab_index!=SportData.TAB2_RECORD)
		{//when leave the page record,broadcast it 
			Intent i_closedb = new Intent(SportData.RECEIVER_CLOSEDB);
			sendBroadcast(i_closedb);
		}
		else if(preindex==SportData.TAB1_SPORT && tab_index!=SportData.TAB1_SPORT)
		{
			Intent i_closeanim = new Intent(SportData.RECEIVER_CLOSEANIM);
			sendBroadcast(i_closeanim);
		}
		Intent intent = new Intent();
		switch(tab_index)
		{
			case SportData.TAB1_SPORT:
				intent.setAction(SportData.RECEIVER_SPORT);
				break;
			case SportData.TAB2_RECORD:
				intent.setAction(SportData.RECEIVER_RECORD);
				break;
			case SportData.TAB3_RANKING:
				intent.setAction(SportData.RECEIVER_RANKING);
				break;
			case SportData.TAB4_MEDAL:
				intent.setAction(SportData.RECEIVER_MEDAL);
				break;
		}  
		preindex = tab_index;
        sendBroadcast(intent);
	}
	
	private void setPageOrientation(int index)
	{
		int orient = -1;
		switch(index)
		{
		case SportData.TAB1_SPORT:
		case SportData.TAB3_RANKING:
			 orient = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			 break;
		case SportData.TAB2_RECORD:	
			 orient= (SportData.GetRecordTab(context)==1) ? ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			break;
		case SportData.TAB4_MEDAL:
			orient = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
			break;
		}
		setRequestedOrientation(SportData.SetOrientation(context, orient));
	}
	
	
    private void changeTabUI(final int index)
    {
		if(tab_front==null)
		{
			if(rl_tab==null)
			{
				rl_tab = (RelativeLayout)findViewById(R.id.rl_tab);
			    line_width = ((View)rl_tab.findViewById(R.id.line)).getWidth();
			}
			tab_front = new TextView(this);
			PublicUtils.updateTab(tab_front, R.drawable.shape_left_selected10 , R.drawable.shape_left_selected );
			//tab_front.setBackgroundResource(R.drawable.shape_left_selected);
			tab_front.setTextColor(getResources().getColor(R.color.pink));
			tab_front.setText(getString(R.string.tab1_sport));
			tab_front.setGravity(Gravity.CENTER);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(tab1_width,tab_height);
			param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			rl_tab.addView(tab_front, param);  
		}
		tab_front.setText(getResources().getStringArray(R.array.tab)[index]);
		tab_front.setTextColor(getResources().getColor(R.color.pink));
		
		switch(index)
		{
		case SportData.TAB1_SPORT:
			PublicUtils.updateTab( tab_front, R.drawable.shape_left_selected10 , R.drawable.shape_left_selected );		
			break;
		case SportData.TAB2_RECORD:
		case SportData.TAB3_RANKING:
			tab_front.setBackgroundColor(Color.WHITE);
			break;
		case SportData.TAB4_MEDAL:
			PublicUtils.updateTab( tab_front, R.drawable.shape_right_selected10 , R.drawable.shape_right_selected );
			break;
		}
		int endX = getMovePos(index);
		TranslateAnimation anim = MoveBg.moveFrontBg(tab_front, startX, endX, 0, 0);
		startX = endX;
		anim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				refreshTabData(index);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub	
			}});
    }
  
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
    	Log.d(TAG, "if_quit=="+if_quit);
    	if(hasFocus && if_quit)
    	{
    		new Init(INIT_TAB).start();
    		if_quit = false;	
    	}
		super.onWindowFocusChanged(hasFocus);
	}
    
    private void initTabFront()//初始化TAB的前背景
    {
    	tab_height = textView1.getHeight();
    	tab1_width = textView1.getWidth();
    	tab2_width = textView2.getWidth();
    	tab3_width = textView3.getWidth();
		rl_tab = (RelativeLayout)findViewById(R.id.rl_tab);
		line_width = ((View)rl_tab.findViewById(R.id.line)).getWidth();
		if(tab_front==null)
		{
			tab_front = new TextView(this);
			PublicUtils.updateTab(tab_front, R.drawable.shape_left_selected10 , R.drawable.shape_left_selected );
			//tab_front.setBackgroundResource(R.drawable.shape_left_selected);
			tab_front.setTextColor(getResources().getColor(R.color.pink));
			tab_front.setText(getString(R.string.tab1_sport));
			tab_front.setGravity(Gravity.CENTER);
			RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(tab1_width,tab_height);
			param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			rl_tab.addView(tab_front, param);  
		}
    }
 

	private int getMovePos(int index)
    {
    	int dis = 0;
    	switch(index)
    	{
    	case 0:
    		break;
    	case 1:
    		dis = tab1_width+line_width;
    		break;
    	case 2:
    		dis = tab1_width+tab2_width+line_width*2;
    		break;
    	case 3:
    		dis = tab1_width+tab2_width+tab3_width+line_width*3;
    	    break;
    	}
        return dis;	
    }
    public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
			//	.enableLogging()// Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		if(!ImageLoader.getInstance().isInited())
		{
			ImageLoader.getInstance().init(config);
		}
	}
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    		sendBroadcast(new Intent(MusicService.NOTIFICATION_EXIT));
			if(SELECT_BLE){
				handle.removeCallbacks(r);
				if(mState==1)
				 unbindService(mServiceConnection);
			        unregisterReceiver(mGattUpdateReceiver);
			        if(mState==1){
			        	System.out.println("disconnect..");
			        	mBluetoothLeService.disconnect();
			        	mState=2;
			        }
			    //stopService(new Intent(this, BluetoothLeService.class));
		        BLE.ReadInfo=false;
			}
			else
			{
				unregisterReceiver(readReceiver);
				unregisterReceiver(receiver);
				unregisterReceiver(mReceiver);
				if(!isunBind){
				unbindService(conn);
				stopService(new Intent(this, BluetoothService.class));
				}
				ifConnect=false;
			}

    	super.onDestroy();
    }
    
    @Override
    public void finish() {
    	// TODO Auto-generated method stub

		if(mBtAdapter!=null && SportData.GetBTStatus(context)){
			BluetoothAdapter.getDefaultAdapter().disable();
			SportData.SetBTStatus(context, false);
		}
    	super.finish();
		
    }
	
	 public class update_thread extends Thread
		{
			public void run() {
				um.checkUpdate(true,MainTab.this,handle);
			    super.run();
			}
		}
	 public void judgmentUP()
	  {
		  SharedPreferences shared  = getSharedPreferences("setaim_msg",MODE_PRIVATE);
	    		long currentTime = System.currentTimeMillis();  
	    		long vaildTime = shared.getLong("uptimeTime", currentTime);	    		
	    		if(vaildTime <=currentTime)
	    		{	    			
					Boolean status = NetworkConnection.isConnectingToInternet(MainTab.this);
					if(status)
					{
						Message msg = new Message();
						msg.what = CHECK_UPDATE;
						handle.sendMessage(msg);
					}
	    		}
		  }
	 public boolean onKeyDown(int keyCode, KeyEvent event) {  

		if (keyCode == KeyEvent.KEYCODE_MENU) { 
			   toggle();
//		    super.openOptionsMenu();  // 调用这个，就可以弹出菜单 
		    return true;
		   }     
			return super.onKeyDown(keyCode, event); // 最后，一定要做完以后返回 true，或者在弹出菜单后返回true，其他键返回super，让其他键默认  
       } 
	 
	 
	 
}
