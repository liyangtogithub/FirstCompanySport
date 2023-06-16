package com.desay.sport.slidepage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sportroute;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.CutScreen;
import com.desay.sport.friend.GetTime;
import com.desay.sport.friend.ShareMessage;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;
import com.desay.sport.main.R.id;
import com.desay.sport.share.OnekeyShare;
import com.desay.sport.share.ShareContentCustomizeDemo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordMap extends Activity
{
	String tag = "RecordMap";
	Button record_tableButton;
	Button record_mapButton;
	TextView y_unit = null;
	TextView x_unit = null;
	MapView record_mMapView = null;
	LinearLayout record_table = null;
	RelativeLayout record_map= null;
	View view;
	MKMapViewListener mMapListener = null;
	private Context context;
	// private Bitmap bmp_map = null;
	private ImageView iv_map_cutter = null;
	long startTime;
	long endTime;
	sportDB spDB = new sportDB(this);
	String username;
	View shareView = null;
	View rootView = null;
	 private Handler handle;

	 String dis;
	 String time;
	 int sportType = 0;
	 //int mode = 0;
	 String iconPathString;
	 String shareContent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_map);
		context = RecordMap.this;
		initView();
		initButton();

		username = SportData.getUserName(this);// 获取当前用户名
		updateUI.sendEmptyMessage(1);

		
		handle = new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				rootView = shareView.getRootView();
				iconPathString = new CutScreen(RecordMap.this).getBitmappath(rootView);
				showShare(false, null);
			}
		};

		ShareSDK.initSDK(this);
	}

	private void initView()
	{
		y_unit = (TextView) findViewById(R.id.y_unit);
		x_unit = (TextView) findViewById(R.id.x_unit);
		record_table = (LinearLayout) findViewById(R.id.record_table);
		record_mMapView = (MapView) findViewById(R.id.record_bmapView);
		iv_map_cutter = (ImageView) findViewById(R.id.iv_map_cutter);
		TextView r4_tv_data1 = (TextView) findViewById(R.id.tv_data1);// 运动的持续时间
		TextView r4_tv_data2 = (TextView) findViewById(R.id.tv_data2);// 卡路里
		TextView r4_tv_data3 = (TextView) findViewById(R.id.tv_data3);// 距离
		ImageView iv_return = (ImageView) findViewById(R.id.iv_return);
		ImageView iv_share = (ImageView) findViewById(R.id.share);

		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.type_record));
		Intent intent = getIntent();

		//mode = intent.getIntExtra("mode", 0);
		startTime = intent.getLongExtra("startTime", 0);
		endTime = intent.getLongExtra("endTime", 0);
		sportType = intent.getIntExtra("sportType", 0);
		dis = SportData.getKilometer(intent.getIntExtra("type_distance", 0));
		time = SportData.getFormatTime(intent.getIntExtra("unit_time", 0));
		r4_tv_data1.setText(time);
		r4_tv_data2.setText(intent.getStringExtra("unit_calorie"));
		r4_tv_data3.setText(dis);
		iv_return.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		iv_share.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				shareView = v;
				// 分享内容
				String sportTypeString = null;
				if (sportType == 0)
				{
					sportTypeString = getString(R.string.type_walk);
				}
				else if (sportType == 1)
				{
					sportTypeString = getString(R.string.type_run);
				}
				else
				{
					sportTypeString = getString(R.string.type_bicycle);
				}
				shareContent = username + getString(R.string.share_content1)
						+ time + "," + sportTypeString + dis
						+ getString(R.string.share_content)+getTime();
				if (View.INVISIBLE == record_table.getVisibility())
				{
					capturemapclick(v);
				}
				else
				{
					rootView = shareView.getRootView();
					iconPathString = new CutScreen(RecordMap.this).getBitmappath(rootView);
					showShare(false, null);
				}

			}
		});
	}
	
	public String getTime() {
		Date date = new Date();
		DateFormat df1 = new SimpleDateFormat("yyyy"+getString(R.string.type_year)+"MM"+getString(R.string.type_month)
				+"dd"+getString(R.string.type_date)+"hh"+getString(R.string.type_oclock)+"mm"+getString(R.string.type_minute)
				+"ss"+getString(R.string.type_second));
		String time = df1.format(date);
		return time;

	}

	private void initButton()
	{
		record_tableButton = (Button) findViewById(R.id.record_tableButton);
		record_mapButton = (Button) findViewById(R.id.record_mapButton);
		record_tableButton.setBackgroundColor(Color.argb(99, 91, 16, 50));
		record_tableButton.setTextColor(Color.argb(0xff, 0xd0, 0x26, 0x69));// 灰色
		
		record_mapButton.setBackgroundColor(Color.argb(0xff, 0xbb, 26, 0x5e));// 默认地图浅颜色
		record_tableButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				record_table.setVisibility(v.VISIBLE);
				y_unit.setVisibility(v.VISIBLE);
				x_unit.setVisibility(v.VISIBLE);

				record_tableButton.setTextColor(Color.argb(0xff, 0xf8, 0xea,0xef));
				record_tableButton.setBackgroundColor(Color.argb(0xff, 0xbb,26, 0x5e));
				record_mapButton.setTextColor(Color.argb(0xff, 0xd0, 0x26, 0x69));
				record_mapButton.setBackgroundColor(Color.argb(99, 91, 16, 50));
			}
		});

		
		record_mapButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				record_table.setVisibility(v.INVISIBLE);
				y_unit.setVisibility(v.INVISIBLE);
				x_unit.setVisibility(v.INVISIBLE);
				
				record_mapButton.setTextColor(Color.argb(0xff, 0xf8, 0xea, 0xef));
				record_mapButton.setBackgroundColor(Color.argb(0xff, 0xbb, 26,0x5e));
				record_tableButton.setTextColor(Color.argb(0xff, 0xd0, 0x26,0x69));
				record_tableButton.setBackgroundColor(Color.argb(99, 91, 16, 50));				
			}
		});
	}

	Handler updateUI = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			drawMap();
			drawSpeedTable();
			getListener();
			setListener();

		}
	};

	/**
	 * 设置监听
	 */
	private void setListener()
	{

		record_mMapView.regMapViewListener(
				MyApplication.getInstance().mBMapManager, mMapListener);
	}

	/**
	 * 画速度图表
	 */
	private void drawSpeedTable()
	{
		List<Double> x1 = new ArrayList<Double>();
		List<Double> y = null;

		y = spDB.getGpsTablePoint(username, startTime);
		// 若只有一个点，图表显示不出此点，所以多加个点，画出线
		if (y.size() == 1)
		{
			y.add(y.get(0));
		}

		for (int i = 0; i < y.size(); i++)
		{
			x1.add(i * (SportData.SPACETIME));
		}	
		Log.i(tag, "drawSpeedTable x1.size()" + x1.size());
		view = new LineChart().execute(MyApplication.getInstance()
				.getApplicationContext(), x1, y );
		view.invalidate();
		record_table.removeAllViews();
		record_table.addView(view);
	}
	

	private void drawMap()
	{
		List<entity_sportroute> sportrouteList = null;	
		sportrouteList = spDB.getMapPoint(username,startTime);

		//sportrouteList.clear();
		if (sportrouteList != null)
		{
			List<GeoPoint> points = new ArrayList<GeoPoint>();
			List<Float> speeds = new ArrayList<Float>();
			double lat = 0;
			double longt = 0;
			for (int i = 0; i < sportrouteList.size(); i++)
			{
				entity_sportroute sportroute = sportrouteList.get(i);
				lat = sportroute.getLatitude();
				longt = sportroute.getLongitude();
				GeoPoint p = new GeoPoint((int) (lat * 1E6), (int) (longt * 1E6));
				if (sportroute.getDistance() != 0 || i == 0)
				{
					points.add(p);
					speeds.add(sportroute.getSpeed());
				}
			}

			if (speeds.size() == 0)
			{
				return;
			}
			BuildMap buildmap = new BuildMap();
			record_mMapView = buildmap.creatmap(getApplicationContext(),
					record_mMapView, points, speeds);
		}
		else {
			record_map.setVisibility(View.INVISIBLE);			
		}
		
	}

	/*
	 * 按钮点击事件 获取截图，异步方法
	 */
	public void capturemapclick(View v)
	{		
		 record_mMapView.getCurrentMap();
	}

	/*
	 * 获取监听
	 */
	private void getListener()
	{
		mMapListener = new MKMapViewListener()
		{

			@Override
			public void onMapMoveFinish()
			{
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo)
			{
			}

			@Override
			public void onGetCurrentMap(Bitmap bitmap)
			{
				iv_map_cutter.setImageBitmap(bitmap);
				iv_map_cutter.setVisibility(View.VISIBLE);
				// 获取照片路径
				handle.sendEmptyMessage(1);
			}

			@Override
			public void onMapAnimationFinish()
			{

			}
		};

	}

	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	private void showShare(boolean silent, String platform)
	{
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher,getString(R.string.app_name));	
		oks.setAddress("12345678901");
		oks.setTitle(shareContent);
		oks.setTitleUrl("http://care.desay.com");
		oks.setText(shareContent);
		if ( ExistSDCard() && getSDFreeSize()> 1 )
		{
			oks.setImagePath(iconPathString);//这句决定图片否
//			oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");		
		}	
		oks.setUrl("http://care.desay.com");
		oks.setFilePath(iconPathString);
		oks.setComment(getString(R.string.share));
		oks.setSite(getString(R.string.app_name));
		oks.setSiteUrl("http://care.desay.com");
		oks.setVenueName("Southeast in China");
		oks.setVenueDescription("This is a beautiful place!");
		oks.setLatitude(23.016087f);
		oks.setLongitude(114.332708f);
	
		oks.setSilent(silent);
		if (platform != null) oks.setPlatform(platform);
		// 去除注释，可令编辑页面显示为Dialog模式
//		oks.setDialogMode();
		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
//		oks.setCallback(new OneKeyShareCallback());
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
		// 在九宫格设置自定义的图标
		Bitmap logo = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo_other);
		String label = getResources().getString(R.string.share_other);		
		OnClickListener listener = new OnClickListener()
		{
			public void onClick(View v)
			{
				new ShareMessage().shareMsg(RecordMap.this, username, dis,
						time, iconPathString);
				oks.finish();
			}
		};
		oks.setCustomerLogo(logo, label, listener);
		oks.show(RecordMap.this);
	}
	
	private boolean ExistSDCard()
	{
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
			return false;
	}

	public long getSDFreeSize()
	{
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/** 将action转换为String */
	public static String actionToString(int action)
	{
		switch (action)
		{
			case Platform.ACTION_AUTHORIZING:
				return "ACTION_AUTHORIZING";
			case Platform.ACTION_GETTING_FRIEND_LIST:
				return "ACTION_GETTING_FRIEND_LIST";
			case Platform.ACTION_FOLLOWING_USER:
				return "ACTION_FOLLOWING_USER";
			case Platform.ACTION_SENDING_DIRECT_MESSAGE:
				return "ACTION_SENDING_DIRECT_MESSAGE";
			case Platform.ACTION_TIMELINE:
				return "ACTION_TIMELINE";
			case Platform.ACTION_USER_INFOR:
				return "ACTION_USER_INFOR";
			case Platform.ACTION_SHARE:
				return "ACTION_SHARE";
			default:
			{
				return "UNKNOWN";
			}
		}
	}

	@Override
	protected void onPause()
	{
		record_mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		iv_map_cutter.setVisibility(View.GONE);
		record_mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		record_mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		record_mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onDestroy()
	{
		record_mMapView.destroy();
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
}
