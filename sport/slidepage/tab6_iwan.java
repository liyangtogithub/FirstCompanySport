package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sportrecord;
import com.desay.sport.db.entity_table;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.GetTime;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.main.R;


import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class tab6_iwan extends Fragment implements View.OnClickListener
{
	private static final String TAG = "tab4_record";
	ImageView record_index1;
	ImageView record_index2;
	LinearLayout record_statistics_layout;
	LinearLayout statistics_table;// 统计界面图表
	ImageButton record_ib_button1;
	ImageButton record_ib_button2;
	ImageView statistics_left_arrows;// 时间左箭头
	ImageView statistics_right_arrows;
	TextView statistics_month;// 显示时间的标题
	TextView record_text1;
	TextView record_text2;
	TextView x_unit;
	TextView y_unit;
	TextView r4_tv_data1 = null;// 运动的持续时间,每天里是手环步数
	TextView r4_tv_data2 = null;// 卡路里
	TextView r4_tv_data3 = null;// 距离
	View Layout;
	// Context context;
	int allCount;// 数据库信息的总条数
	// 滑动分页加载方面
	private static int CurrentTab = SportData.TAB_STEP_ONE;
	/**
	 * 设置布局显示属性
	 */
	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT, 20);
	/**
	 * 设置布局显示目标最大化属性
	 */
	private LayoutParams FFlayoutParams = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.WRAP_CONTENT,
			LinearLayout.LayoutParams.WRAP_CONTENT);
	String username;
	sportDB spDB = null;
	int nowYear   =  0;
	int nowMonth  =  0;
	int nowDay    =  0;
	// 存储统计里，画图表的点
	static List<Integer> values1;
	Button statistics_button;
    private GetTime time = null;
    entity_table tableData = null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		IntentFilter intentFilter = new IntentFilter(); 
	    intentFilter.addAction(SportData.RECEIVER_LOAD);
	    intentFilter.addAction(SportData.RECEIVER_EXITSPORT);
	    intentFilter.addAction(SportData.RECEIVER_CLOSEDB);  	    
		getActivity().registerReceiver( recordBroadcastReceiver , intentFilter);
		super.onCreate(savedInstanceState);
	}

	private BroadcastReceiver recordBroadcastReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	    	if (BuildConfig.DEBUG)Log.d(TAG, "tab4_recordBroadcastReceiver");
	    	
	    	if(isAdded())
	    	{
		    	if(intent.getAction()==SportData.RECEIVER_CLOSEDB)
		    	{
		    		Log.d(TAG, "closedb");
		    		if (spDB!= null)
		    		{
		    			spDB.closeDB();
		    		}
		    	}
		    	else
	    		UIhandler.sendEmptyMessage(0);
	    	}
	    } 
	 };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		time = new GetTime();
		tableData = new entity_table();
		nowYear   =  time.getCurrentyear();
		nowMonth  =  time.getcurrentmonth();
		nowDay    =  time.getcurrentday();
		initView(inflater, container);
		return Layout;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		UIhandler.sendEmptyMessage(0);
		super.onStart();
	}

	private void initView(LayoutInflater inflater, ViewGroup container)
	{
		Layout = inflater.inflate(R.layout.tab6_iwan, container, false);
		record_statistics_layout = (LinearLayout) Layout
				.findViewById(R.id.record_statistics_layout);
		statistics_table = (LinearLayout) Layout
				.findViewById(R.id.statistics_table);
		((TextView) Layout.findViewById(R.id.tv_type1)).setText(getString(R.string.type_step_all));// 手环步数标题
		r4_tv_data1 = (TextView) Layout.findViewById(R.id.tv_data1);// 运动的持续时间
		r4_tv_data2 = (TextView) Layout.findViewById(R.id.tv_data2);// 卡路里
		r4_tv_data3 = (TextView) Layout.findViewById(R.id.tv_data3);// 距离
		record_index1 = (ImageView) Layout.findViewById(R.id.record_index1);
		record_index2 = (ImageView) Layout.findViewById(R.id.record_index2);
		record_ib_button1 = (ImageButton) Layout.findViewById(R.id.record_ib_button1);
		record_ib_button2 = (ImageButton) Layout.findViewById(R.id.record_ib_button2);
		record_text1 = (TextView) Layout.findViewById(R.id.record_text1);
		record_text2 = (TextView) Layout.findViewById(R.id.record_text2);
		x_unit=(TextView) Layout.findViewById(R.id.x_unit);
		y_unit=(TextView) Layout.findViewById(R.id.y_unit);
		record_ib_button1.setOnClickListener(this);
		record_ib_button2.setOnClickListener(this);
		record_text1.setOnClickListener(this);
		record_text2.setOnClickListener(this);
		
		// 点箭头调整时间
		statistics_left_arrows = (ImageView) Layout.findViewById(R.id.statistics_left_arrows);
		statistics_right_arrows = (ImageView) Layout.findViewById(R.id.statistics_right_arrows);
		statistics_month = (TextView) Layout.findViewById(R.id.statistics_month);
		statistics_right_arrows.setOnClickListener(new RightArrowsClick());
		statistics_left_arrows.setOnClickListener(new LeftArrowsClick());
		// 点击看大图表
		statistics_button = (Button) Layout.findViewById(R.id.statistics_button);
	}

	/**
	 * 统计界面，点击右边箭头的监听
	 */
	private class RightArrowsClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (CurrentTab == SportData.TAB_STEP_ONE )
			{
				if(time.getcurrentday()<nowDay || time.getcurrentmonth()<nowMonth 
						                       || time.getCurrentyear()< nowYear)
				{	
					time.setcurrentday(time.getcurrentday()+1);
					updateDay(time);	
				}
			}
			else 
			{
				if(time.getcurrentmonth()<nowMonth || time.getCurrentyear()< nowYear)
				{	
					time.setcurrentmonth(time.getcurrentmonth()+1);
					updateMonth(time);
				}
			}			
		}
	}

	/**
	 * 统计界面，点击左边箭头的监听
	 */
	private class LeftArrowsClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (CurrentTab == SportData.TAB_STEP_ONE )
			{				
				time.setcurrentday(time.getcurrentday()-1);			   
				updateDay(time);		
			}
			else 
			{	
				time.setcurrentmonth(time.getcurrentmonth()-1);
				updateMonth(time);
			}
		}
	}

	private void initData(int index)
	{
		switch (index)
		{
			case SportData.TAB_STEP_ONE:
				updateDay(time);
				break;
				
			case SportData.TAB_STEP_TWO:
				updateMonth(time);
				break;
		}
	}
	
	private void updateDay(GetTime gt)
	{
		updateStatisticsDay(gt.getCurrentyear(),gt.getcurrentmonth(),gt.getcurrentday());
	}
	
	private void updateStatisticsDay(int year,int month,int day)
	{
		statistics_month.setText(year + getString(R.string.type_year) + 
				 (month + 1) + getString(R.string.type_month) 
				 + day +getString(R.string.type_date));
		// 日总数统计	
		if(tableData!=null)tableData.clearData();
		tableData = spDB.getData_Everyday(username, year, month, day,SportData.MODE_IWAN);
		r4_tv_data1.setText(""+tableData.getData1());
		r4_tv_data2.setText(""+tableData.getData2());
		r4_tv_data3.setText(SportData.getKilometer(tableData.getData3()));
		getTabView(tableData.getTableValue());
	}
	
	private void updateMonth(GetTime gt)
	{
		updateStatisticsMonth(gt.getCurrentyear(),gt.getcurrentmonth(),gt.getcurrentday());
	}
	
	private void updateStatisticsMonth(int year,int month,int day)
	{
		statistics_month.setText(year + getString(R.string.type_year) + " "
				+ (month + 1) + getString(R.string.type_month));
		// 月总数统计
		if(tableData!=null)tableData.clearData();
		tableData = spDB.getData_EveryMonth(username,year, month,SportData.MODE_IWAN);
		r4_tv_data1.setText(""+tableData.getData1());
		r4_tv_data2.setText(""+tableData.getData2());
		r4_tv_data3.setText(SportData.getKilometer(tableData.getData3()));
		getTabView(tableData.getTableValue());
	}

	Handler UIhandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			username = SportData.getUserName(getActivity());// 获取当前用户名
			ChangeTabUI(CurrentTab);
		}
	};

	private void ChangeTabUI(int index)
	{
		if (spDB!= null)
		{
			spDB.closeDB();//切换记录内部3个界面，关一次数据库
		}		
		spDB = new sportDB(getActivity().getApplicationContext());
		switch (index)
		{
			case SportData.TAB_STEP_ONE:
				record_ib_button1.setImageResource(R.drawable.record_eveday_in);
				record_ib_button2.setImageResource(R.drawable.record_statistik_out);
				record_index1.setBackgroundResource(R.drawable.index_in);
				record_index2.setBackgroundResource(R.drawable.index_out);
				x_unit.setText(getString(R.string.unit_time));
				y_unit.setText(getString(R.string.type_step));
				record_text1.setTextColor(getResources().getColor(R.color.pink));
				record_text2.setTextColor(getResources().getColor(R.color.gray_dark));
				break;		
			case SportData.TAB_STEP_TWO:
				record_ib_button1.setImageResource(R.drawable.record_eveday_out);
				record_ib_button2.setImageResource(R.drawable.record_statistik_in);
				record_index2.setBackgroundResource(R.drawable.index_in);
				record_index1.setBackgroundResource(R.drawable.index_out);
				x_unit.setText(getString(R.string.statistics_day));
				y_unit.setText(getString(R.string.type_step));
				record_text2.setTextColor(getResources().getColor(R.color.pink));
				record_text1.setTextColor(getResources().getColor(R.color.gray_dark));
				break;		
		}
		CurrentTab = index;	
		initData(index);
	}


	private void getTabView(final List<Integer> values)
	{
		View view = new Cylindricality().execute(getActivity(), values, CurrentTab, time.getCurrentyear(), time.getcurrentmonth());
		statistics_table.removeAllViews();
		statistics_table.addView(view);
		statistics_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setAction("TableActivity");
				intent.putExtra("year", time.getCurrentyear());
				intent.putExtra("month", time.getcurrentmonth());
				intent.putIntegerArrayListExtra("value", (ArrayList<Integer>) values);
				intent.putExtra("CurrentTab", CurrentTab);
				startActivity(intent);
			}
		});
		
	}

	 public void onDestroyView() { 
		if (spDB!= null)
			{
				spDB.closeDB();
			}
	 super.onDestroyView();
  }
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
			case R.id.record_ib_button2:
			case R.id.record_text2:
				ChangeTabUI(SportData.TAB_STEP_TWO);
				break;
			case R.id.record_ib_button1:
			case R.id.record_text1:
				ChangeTabUI(SportData.TAB_STEP_ONE);
				break;
		}
	}

	@Override
	public void onDetach()
	{
		getActivity().unregisterReceiver( recordBroadcastReceiver );
		super.onDetach();
	}
	 @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		 Log.d(TAG, "onConfigurationChanged==="+newConfig.orientation);
		 switch(newConfig.orientation)
		 {
		 case Configuration.ORIENTATION_LANDSCAPE:
			 Intent intent = new Intent();
			 intent.setAction("TableActivity");
			 intent.putExtra("year", time.getCurrentyear());
			 intent.putExtra("month", time.getcurrentmonth());
			 intent.putIntegerArrayListExtra("value", (ArrayList<Integer>)tableData.getTableValue());
			 intent.putExtra("CurrentTab", CurrentTab);
			 startActivity(intent);
			 break;
		 }
		super.onConfigurationChanged(newConfig);
	}
}