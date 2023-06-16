package com.desay.sport.slidepage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.SendServer;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;
import com.desay.sport.multimedia.AddMusic;
import com.desay.sport.multimedia.Mp3Info;
import com.desay.sport.multimedia.MusicLib;
import com.desay.sport.multimedia.MusicList;
import com.desay.sport.multimedia.MusicService;

public class LocationOverlay extends Activity
{
	MapView mMapView = null;
	LinearLayout table = null;
	RelativeLayout locked_layout;
	LinearLayout unlocked_layout;
	Button tableButton = null;
	Button mapButton = null;
	ImageButton music;
	ImageButton lockbt;
	ImageButton unlockbt;
	ImageButton stop;
	ImageButton play;
	ImageView iv_return;
	View view = null;
	TextView tv_data;
	TextView tv_data2;
	TextView tv_data3;
	TextView y_unit;
	TextView x_unit;

	private boolean pause=false;
	private MapController mMapController = null;
	private LocationClient mLocClient;
	private LocationData locData = null;
	private MyLocationListenner myListener = new MyLocationListenner();
	private MyLocationOverlay myLocationOverlay;
	private static boolean isStart = true;
	boolean isFirstLoc = true;// 是否首次定位
	private double lastPointWei = 0;
	private double lastPointJing = 0;
	int singleDistance = 0;
	private static int allDistance;
	private static double cal;
	private boolean isDestroy=false;
	Graphic line=null;
	public MessageBroadcastReceiver receiver;
	private static long time = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_PAUSE = 2;

	public static final double WALK_ONE = 0.062;
	public static final double WALK_TWO = 0.136;
	public static final double WALK_THREE = 0.160;
	public static final double RUN_ONE = 0.1355;
	public static final double RUN_TWO = 0.1797;
	public static final double RUN_THREE = 0.1875;
	public static final double RIDE_ONE = 0.06;
	public static final double RIDE_TWO = 0.102;
	public static final double RIDE_THREE = 0.161;
	public static boolean StartSport = false;
	private static boolean ispause = false;
	private String username;
	private int state = 1;
	private static String begin_time = null;
	sportDB db;
	SpeakTool speakTool;
	private Context context;
	boolean lockScreen = true ;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.locationoverlay);
		context = LocationOverlay.this;
		locked_layout = (RelativeLayout) findViewById(R.id.lock_layout);
		unlocked_layout = (LinearLayout) findViewById(R.id.unlock_layout);
		lockbt = (ImageButton) findViewById(R.id.locked);
		unlockbt = (ImageButton) findViewById(R.id.unlock);
		stop = (ImageButton) findViewById(R.id.stop);
		play = (ImageButton) findViewById(R.id.play);
		music = (ImageButton) findViewById(R.id.music);
		mMapView = (MapView) findViewById(R.id.bmapView);
		tv_data = (TextView) findViewById(R.id.tv_data1);
		tv_data2 = (TextView) findViewById(R.id.tv_data2);
		tv_data3 = (TextView) findViewById(R.id.tv_data3);
		y_unit = (TextView) findViewById(R.id.y_unit);
		x_unit = (TextView) findViewById(R.id.x_unit);
		table = (LinearLayout) findViewById(R.id.table); // 向里面保存ListView
		tableButton = (Button) findViewById(R.id.tableButton);
		mapButton = (Button) findViewById(R.id.mapButton);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		initButton();
		begin_time = System.currentTimeMillis() + "";
		speakTool = new SpeakTool(this);

		db = new sportDB(this);
		cal = 0;
		allDistance = 0;
		mMapController = mMapView.getController();
		mMapView.getController().setZoom(18);
		mMapView.getController().enableClick(true);
		ispause = false;
		isStart = true;
		StartSport = true;
		optionInit();
		mLocClient.start();

		myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean GPS_status = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!GPS_status)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					LocationOverlay.this);
			builder.setMessage(R.string.opengps)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									// TODO Auto-generated method stub
									Intent intent = new Intent();
									intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									LocationOverlay.this.startActivity(intent);
								}
							}).setNegativeButton(R.string.no, null).show();


		}
		
		username = SportData.getUserName(this);

		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(SportService.COUNT_TIME);
		receiver=new MessageBroadcastReceiver();
		registerReceiver(receiver,intentFilter);
	}

	class MessageBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(action.equals(SportService.COUNT_TIME)){
				time=intent.getLongExtra("count", 0);
				if(!pause)
				tv_data.setText(getFormatTime(time));
			}
			if(action.equals(SportService.MAP_DOT)){
				
			}
	}}
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					sendBroadcast(new Intent(MusicService.MUSIC_GOON));
			}
		};
	};

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		pause=false;
		//view.invalidate();
		if(view!=null){
		table.removeAllViews();
		table.addView(view);
		}
		tv_data3.setText(SportData.getKilometer(allDistance));
		tv_data2.setText(SportData.getKilometer((int)(cal*1000)));
		mMapView.refresh();
		super.onStart();
	}


	private void initButton()
	{
		// TODO Auto-generated method stub
		iv_return.setClickable(false);
		lockbt.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				locked_layout.setVisibility(View.INVISIBLE);
				play.setClickable(true);
				unlockbt.setClickable(true);
				stop.setClickable(true);
				iv_return.setClickable(true);
				lockScreen = false ;
				
				iv_return.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						BackPressed();
					}
				});
				
				play.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						Intent countIntent=new Intent(LocationOverlay.this,SportService.class);
						countIntent.setAction(SportService.COUNT_TIME);
						if (state == STATE_RUNNING)
						{
							play.setImageResource(R.drawable.start);
							countIntent.putExtra("MSG", STATE_PAUSE);					
							mLocClient.unRegisterLocationListener(myListener);
							ispause=true;
							state=STATE_PAUSE;
						}
						else if (state == STATE_PAUSE)
						{
							play.setImageResource(R.drawable.pause);							
							countIntent.putExtra("MSG", STATE_RUNNING);
							ispause=false;							
							state=STATE_RUNNING;
						}startService(countIntent);
					}
				});
				stop.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						exitSport();
					}
				});
			}
		});

		unlockbt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				locked_layout.setVisibility(View.VISIBLE);
				play.setClickable(false);
				unlockbt.setClickable(false);
				stop.setClickable(false);
				iv_return.setClickable(false);
				lockScreen = true ;
			}
		});

		tableButton.setTextColor(Color.argb(0xff, 0xd0, 0x26, 0x69));
		tableButton.setBackgroundColor(Color.argb(99, 91, 16, 50));
		tableButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				table.setVisibility(v.VISIBLE);
				y_unit.setVisibility(v.VISIBLE);
				x_unit.setVisibility(v.VISIBLE);
				tableButton.setTextColor(Color.argb(0xff, 0xf8, 0xea, 0xef));
				mapButton.setTextColor(Color.argb(0xff, 0xd0, 0x26, 0x69));
				mapButton.setBackgroundColor(Color.argb(99, 91, 16, 50));
				tableButton.setBackgroundColor(Color.argb(0xff, 0xbb, 26, 0x5e));
			}
		});

		mapButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				table.setVisibility(v.INVISIBLE);
				y_unit.setVisibility(v.INVISIBLE);
				x_unit.setVisibility(v.INVISIBLE);
				mapButton.setTextColor(Color.argb(0xff, 0xf8, 0xea, 0xef));
				tableButton.setTextColor(Color.argb(0xff, 0xd0, 0x26, 0x69));
				tableButton.setBackgroundColor(Color.argb(99, 91, 16, 50));
				mapButton.setBackgroundColor(Color.argb(0xff, 0xbb, 26, 0x5e));
			}
		});
		music.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				List<Mp3Info> list = new ArrayList<Mp3Info>();
				list = db.GetMusic();
				if (list.size() == 0)
				{
					Intent intent = new Intent(LocationOverlay.this,
							AddMusic.class);
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(LocationOverlay.this,
							MusicList.class);
					startActivity(intent);
				}

			}
		});
	}

	private void exitSport()
	{
		// TODO Auto-generated method stub
		stopService(new Intent(this,SportService.class));
		if (allDistance > 0)
		{
			long endtime = System.currentTimeMillis();
		    ContentValues newValues = new ContentValues();	
			newValues.put(sportDB.ID_STARTTIME,begin_time);			
			newValues.put(sportDB.ID_ENDTIME, endtime + "");
		    newValues.put(sportDB.ID_SPORTTYPE,SportData.getSportType(context) + "");
			newValues.put(sportDB.ID_DISTANCE,allDistance + "");
			newValues.put(sportDB.ID_DURATIONTIME,time / 1000 + "");
			newValues.put(sportDB.ID_CALORIE,(int) cal + "");				
			newValues.put(sportDB.ID_MODE,"0");
			newValues.put(sportDB.ID_AVERAGE_SPEED,SportData.getKilometer((long) (allDistance * 3.6 * 1000 * 1000 / time)));
			newValues.put(sportDB.ID_FOOTNUM,"0");
			newValues.put(sportDB.ID_IFUPLOAD,SportData.UNUPLOAD);	
			db.InsertSportRecord(username, newValues);			
			Intent recordintent = new Intent();
			recordintent.putExtra("startTime", Long.parseLong(begin_time));
			recordintent.putExtra("endTime", endtime);
			recordintent.putExtra("sportType", SportData.getSportType(context));
			recordintent.putExtra("unit_time", (int) (time / 1000));
			recordintent.putExtra("unit_calorie", (int) cal + "");
			recordintent.putExtra("type_distance", allDistance);
			recordintent.putExtra("mode", 0);
			recordintent.setAction("RecordMap");
			
			LocationOverlay.this.startService(new Intent(LocationOverlay.this,SendServer.class));
			DecimalFormat df = new DecimalFormat("0.000");     
			speakTool.readInfo(mHandler,(int)(time / 1000 / 60),(int)((time / 1000) % 60), df.format((double)allDistance/1000)+"",SportData.getSportType(context));
			
			startActivity(recordintent);
		
			Intent broadIntent = new Intent(SportData.RECEIVER_EXITSPORT);
			broadIntent.putExtra("distance", allDistance);
			sendBroadcast(broadIntent);//运动停止广播 
			finish();
		}else{
		finish();
	}
	}

	private String getFormatTime(long time)
	{
		long second = (time / 1000) % 60;
		long minute = time / 1000 / 60 % 60;
		long hour = time / 1000 / 60 / 60;
		String strSecond = ("00" + second)
				.substring(("00" + second).length() - 2);
		String strMinute = ("00" + minute)
				.substring(("00" + minute).length() - 2);
		String strHour = ("00" + hour).substring(("00" + hour).length() - 2);
		return strHour + ":" + strMinute + ":" + strSecond;
	}

	public void optionInit()
	{
		mLocClient = new LocationClient(this);
		locData = new LocationData();
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(SportData.LISTENTERTIME * 1000);// 定位频率
		option.setPriority(LocationClientOption.GpsFirst);
		option.disableCache(true);
		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(myListener);
	}

	public class MyLocationListenner implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location)
		{
			if (location == null ||ispause) return;
			int locType = location.getLocType();
			System.out.println("loacatype"+locType);
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			if(locType==63){
				 showToast.normaltoast(context, getString(R.string.checkupdate_toast_network_error), showToast.TWO_SECOND);
			}
			if (locType != 61 && locType != 161) {
				view = new LineChart().execute(MyApplication.getInstance().getApplicationContext(),(float)time/60000,allDistance * 3.6 * 1000/ time);
				view.invalidate();
				
				if(!isFirstLoc){
					saveRoute(username,begin_time, 0+"",lastPointWei+"",lastPointJing+"",SportData.getKilometer((long) (allDistance * 3.6 * 1000 * 1000 / time)),SportData.UNUPLOAD);
				}
				if(!pause){
			    showToast.normaltoast(context, getString(R.string.location_error), showToast.TWO_SECOND);
				table.removeAllViews();
				table.addView(view);								
			}
				return;
				}
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			myLocationOverlay.setData(locData);
			//mMapView.refresh();
			if (!isFirstLoc)
			{
				if (isStart)
				{
					int d = (int) DistanceUtil.getDistance(new GeoPoint(
							(int) (lastPointWei * 1e6),
							(int) (lastPointJing * 1e6)), new GeoPoint(
							(int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6)));
					if (d < 100)
					{
						locData.latitude = lastPointWei;
						locData.longitude = lastPointJing;
					}
					ItemizedOverlay onver = new ItemizedOverlay<OverlayItem>(
							getResources().getDrawable(R.drawable.map_start),
							mMapView);
					GeoPoint p1 = new GeoPoint((int) (locData.latitude * 1E6),
							(int) (locData.longitude * 1E6));
					OverlayItem item1 = new OverlayItem(p1, "", "");
					item1.setMarker(getResources().getDrawable(
							R.drawable.map_start));
					onver.addItem(item1);
					mMapView.getOverlays().add(onver);
					mMapView.refresh();
					
					saveRoute(username,begin_time,0+"",locData.latitude+"",locData.longitude+"",0+"",SportData.UNUPLOAD);
				}
				GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
				mMapView.getOverlays().add(graphicsOverlay);
				//Graphic line = null;
				if (!isStart)
				{				
						if(location.getSatelliteNumber()==-1&&pause){
							//i=i+SportData.SPACETIME;
							SportData.COLLECTTIME=SportData.COLLECTTIME+SportData.LISTENTERTIME;
							view = new LineChart().execute(MyApplication.getInstance().getApplicationContext(),(float)time/60000,allDistance * 3.6 * 1000/ time);
							view.invalidate();
							System.out.println("huaxian");
							if(!pause){
							table.removeAllViews();
							table.addView(view);
						}
//							//String time=System.currentTimeMillis()+"";	
							saveRoute(username,begin_time,0+"",lastPointWei+"",lastPointJing+"",SportData.getKilometer((long) (allDistance * 3.6 * 1000 * 1000 / time)),SportData.UNUPLOAD);
							return;					
							}
					line = drawLine(lastPointWei, lastPointJing,
							locData.latitude, locData.longitude);
				}
				if(!pause)
				tv_data3.setText(SportData.getKilometer(allDistance));
				entity_userinfo userinfo = new entity_userinfo();
				userinfo = db.GetUserInfo(getApplicationContext());
				if (userinfo.getWeight() != null && singleDistance != 0)
				{
					int weight = Integer.valueOf(userinfo.getWeight());
					double speed = singleDistance * 3.6 / SportData.COLLECTTIME;
					switch (SportData.getSportType(context))
					{
						case 0:
							if (speed>2&&speed <= 4)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												WALK_ONE);
							}
							else if (speed > 4 && speed <= 8)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												WALK_TWO);
							}
							else if (speed > 8)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												WALK_THREE);
							}
							break;
						case 1:
							if (speed>2&&speed <= 8)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												RUN_ONE);
							}
							else if (speed > 8 && speed <= 12)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												RUN_TWO);
							}
							else if (speed > 12)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												RUN_THREE);
							}
							break;
						case 2:
							if (speed>2&&speed <= 16)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												RIDE_ONE);
							}
							else if (speed > 16 && speed <= 21)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												RIDE_TWO);
							}
							else if (speed > 21)
							{
								cal = cal
										+ getCal(weight, SportData.SPACETIME,
												RIDE_THREE);
							}
							break;
						default:
							break;
					}
				}
				if(!pause)
				tv_data2.setText(SportData.getKilometer((int) (cal * 1000)));

//				// String time=System.currentTimeMillis()+"";
				saveRoute(username,begin_time,singleDistance + "",locData.latitude + "",locData.longitude + "",SportData.getKilometer((long) (allDistance * 3.6 * 1000 * 1000 / time)),SportData.UNUPLOAD);
				view = new LineChart().execute(MyApplication.getInstance()
						.getApplicationContext(), (float)time/60000,allDistance * 3.6 * 1000/ time);
				view.invalidate();
				table.removeAllViews();
				table.addView(view);

				if (line != null || isStart)
				{
					graphicsOverlay.setData(line);
					lastPointWei = locData.latitude;
					lastPointJing = locData.longitude;
					mMapView.refresh();
					SportData.COLLECTTIME = 20;
				}
				isStart = false;
			}
			else if (isFirstLoc)
			{
				startService(new Intent(LocationOverlay.this,SportService.class));
				LineChart.x.clear();
				LineChart.values.clear();
				view = new LineChart().execute(MyApplication.getInstance()
						.getApplicationContext(), 0, 0);

				view.invalidate();
				table.removeAllViews();
				table.addView(view);
				allDistance = 0;
				GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
				mMapView.getOverlays().add(graphicsOverlay);
				lastPointWei = locData.latitude;
				lastPointJing = locData.longitude;
				isFirstLoc = false;
				 mMapView.refresh();
			}
			if(!pause)
			mMapController.animateTo(new GeoPoint(
					(int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6)));

		}
		private void saveRoute(String t_username,String t_starttime,String t_distance,String t_latitude,String t_longitude,String t_speed,String t_upload)
		{
			ContentValues newValues = new ContentValues();
			newValues.put(sportDB.ID_STARTTIME,t_starttime);
			newValues.put(sportDB.ID_DISTANCE, t_distance);
			newValues.put(sportDB.ID_LATITUDE,t_latitude);
			newValues.put(sportDB.ID_LONGITUDE,t_longitude);
			newValues.put(sportDB.ID_INSTANT_SPEED,t_speed);
			newValues.put(sportDB.ID_IFUPLOAD,t_upload);
			db.InsertSportRoute(t_username, newValues);
		}

		@Override
		public void onReceivePoi(BDLocation arg0)
		{
			// TODO Auto-generated method stub

		}
	}

	public double getCal(int weight, double time, double k)
	{
		double cal = weight * time * k;
		return cal;
	}

	public Graphic drawLine(double mLat1, double mLon1, double mLat2,
			double mLon2)
	{
		int lat = (int) (mLat1 * 1E6);
		int lon = (int) (mLon1 * 1E6);
		GeoPoint pt1 = new GeoPoint(lat, lon);
		lat = (int) (mLat2 * 1E6);
		lon = (int) (mLon2 * 1E6);
		GeoPoint pt2 = new GeoPoint(lat, lon);
		singleDistance = (int) DistanceUtil.getDistance(pt1, pt2);
		switch (SportData.getSportType(context))
		{
			case 0:
				if (singleDistance * 3.6 / SportData.COLLECTTIME > SportData.max_walk)
				{
					singleDistance = 0;
					SportData.COLLECTTIME = SportData.COLLECTTIME
							+ SportData.LISTENTERTIME;
					return null;
				}
			case 1:
				if (singleDistance * 3.6 / SportData.COLLECTTIME > SportData.max_run)
				{
					singleDistance = 0;
					SportData.COLLECTTIME = SportData.COLLECTTIME
							+ SportData.LISTENTERTIME;
					return null;
				}

			case 2:
				if (singleDistance * 3.6 / SportData.COLLECTTIME > SportData.max_drive)
				{
					singleDistance = 0;
					SportData.COLLECTTIME = SportData.COLLECTTIME
							+ SportData.LISTENTERTIME;
					return null;
				}

		}
		allDistance = (int) allDistance + singleDistance;
		Geometry lineGeometry = new Geometry();
		GeoPoint[] linePoints = new GeoPoint[2];
		linePoints[0] = pt1;
		linePoints[1] = pt2;
		lineGeometry.setPolyLine(linePoints);
		Symbol lineSymbol = new Symbol();
		Symbol.Color lineColor = null;
		if (allDistance * 3.6 * 1000/ time <= 2.0)
		{
			lineColor = lineSymbol.new Color();
			lineColor.red = 0xcc;
			lineColor.green = 0x73;
			lineColor.blue = 0x53;
			lineColor.alpha = 255;
		}
		else if (2.0 <allDistance * 3.6 * 1000/ time
				&&allDistance * 3.6 * 1000/ time<= 4.0)
		{
			lineColor = lineSymbol.new Color();
			lineColor.red = 0xcc;
			lineColor.green = 0x73;
			lineColor.blue = 0x53;
			lineColor.alpha = 255;
		}
		else if (4.0 <allDistance * 3.6 * 1000/ time
				&& allDistance * 3.6 * 1000/ time < 6.0)
		{
			lineColor = lineSymbol.new Color();
			lineColor.red = 0x74;
			lineColor.green = 0x8b;
			lineColor.blue = 0x82;
			lineColor.alpha = 255;
		}
		else if (6.0 <allDistance * 3.6 * 1000/ time
				&&allDistance * 3.6 * 1000/ time < 8.0)
		{
			lineColor = lineSymbol.new Color();
			lineColor.red = 0x87;
			lineColor.green = 0x74;
			lineColor.blue = 0x9b;
			lineColor.alpha = 255;
		}
		else
		{
			lineColor = lineSymbol.new Color();
			lineColor.red = 0xe1;
			lineColor.green = 0x3a;
			lineColor.blue = 0x53;
			lineColor.alpha = 255;
		}
		lineSymbol.setLineSymbol(lineColor, 8);
		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
		return lineGraphic;

	}

	@Override
	protected void onPause()
	{
		// isLocationClientStop = true;
		// mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		//isLocationClientStop = false;
		//mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		BackPressed();
	}

	@Override
	protected void onDestroy()
	{
		if (mLocClient != null) mLocClient.stop();
		time=0;
		begin_time="0";
//		allDistance=0;
		cal=0;
		StartSport=false;
		time = 0;
		unregisterReceiver(receiver);
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
	


	private void BackPressed()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
				LocationOverlay.this);
		builder.setMessage(R.string.exit)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								// TODO Auto-generated method stub
								exitSport();
							}
						}).setNegativeButton(R.string.no, null).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (lockScreen)
				{			
					return true;
				}
		}
		return super.onKeyDown(keyCode, event);
	}
}
