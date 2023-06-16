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

public class tab4_record extends Fragment implements View.OnClickListener
{

	private static final String TAG = "tab4_record";
	ImageView record_index0;
	ImageView record_index1;
	ImageView record_index2;

	LinearLayout record_list_layout;
	LinearLayout record_statistics_layout;
	LinearLayout statistics_table;// 统计界面图表

	ImageButton record_ib_button0;
	ImageButton record_ib_button1;
	ImageButton record_ib_button2;
	ImageView statistics_left_arrows;// 时间左箭头
	ImageView statistics_right_arrows;
	TextView statistics_month;// 显示时间的标题
	TextView record_text0;
	TextView record_text1;
	TextView record_text2;
	TextView x_unit;
	TextView y_unit;
	TextView tv_type1 = null;// 每天里是手环步数
	TextView r4_tv_data1 = null;// 运动的持续时间,每天里是手环步数
	TextView r4_tv_data2 = null;// 卡路里
	TextView r4_tv_data3 = null;// 距离
	private ListView record_list;
	private BestListAdapter adapter;
	private EveryAdapter every_adapter;
	private List<entity_sportrecord> data;
	View Layout;
	// Context context;
	int allCount;// 数据库信息的总条数
	private FrameLayout fl_record;
	// 滑动分页加载方面

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

	LinearLayout loadingLayout;
	private int lastItem = 0;
	private int curentPage = 1;
	private int AllPage = 0;
	// 最后一页的显示数量
	private int numLastPage = 1;
	private int lastId = 0;
	int showNum = 10;
	boolean first = true;
	String username;
	sportDB spDB = null;
	int nowYear  = 0 ;
	int nowMonth = 0;
	int nowDay   = 0;
	// 存储统计里，画图表的点
	static List<Integer> values1;
	Button statistics_button;
    private GetTime time = null;
    private entity_table tableData = null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SportData.SetRecordTab(getActivity(),SportData.TAB_STAGE_LOW);
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
		initScrollView();
		UIhandler.sendEmptyMessage(0);
		return Layout;
	}

	private void initView(LayoutInflater inflater, ViewGroup container)
	{

		Layout = inflater.inflate(R.layout.tab4_record, container, false);
		record_list_layout = (LinearLayout) Layout
				.findViewById(R.id.record_list_layout);
		record_statistics_layout = (LinearLayout) Layout
				.findViewById(R.id.record_statistics_layout);
		statistics_table = (LinearLayout) Layout
				.findViewById(R.id.statistics_table);

		tv_type1 = (TextView) Layout.findViewById(R.id.tv_type1);// 手环步数标题
		r4_tv_data1 = (TextView) Layout.findViewById(R.id.tv_data1);// 运动的持续时间
		r4_tv_data2 = (TextView) Layout.findViewById(R.id.tv_data2);// 卡路里
		r4_tv_data3 = (TextView) Layout.findViewById(R.id.tv_data3);// 距离
		record_list = (ListView) Layout.findViewById(R.id.record_list);
		fl_record = (FrameLayout) Layout.findViewById(R.id.fl_record);
		record_list.setDivider(null);
		adapter = new BestListAdapter(getActivity());
		every_adapter = new EveryAdapter(getActivity());
		data = new ArrayList<entity_sportrecord>();
		// record_list.setAdapter(every_adapter);
		record_list.setOnItemClickListener(new OnItemClickRecord());
		record_index0 = (ImageView) Layout.findViewById(R.id.record_index0);
		record_index1 = (ImageView) Layout.findViewById(R.id.record_index1);
		record_index2 = (ImageView) Layout.findViewById(R.id.record_index2);
		record_ib_button0 = (ImageButton) Layout.findViewById(R.id.record_ib_button0);
		record_ib_button1 = (ImageButton) Layout.findViewById(R.id.record_ib_button1);
		record_ib_button2 = (ImageButton) Layout.findViewById(R.id.record_ib_button2);
		record_text0 = (TextView) Layout.findViewById(R.id.record_text0);
		record_text1 = (TextView) Layout.findViewById(R.id.record_text1);
		record_text2 = (TextView) Layout.findViewById(R.id.record_text2);
		x_unit=(TextView) Layout.findViewById(R.id.x_unit);
		y_unit=(TextView) Layout.findViewById(R.id.y_unit);
		record_ib_button0.setOnClickListener(this);
		record_ib_button1.setOnClickListener(this);
		record_ib_button2.setOnClickListener(this);
		record_text0.setOnClickListener(this);
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
	 * listview滑动分页初始化
	 */
	private void initScrollView()
	{
		// 线性布局
		LinearLayout layout = new LinearLayout(getActivity());
		// 设置布局 水平方向
		layout.setOrientation(LinearLayout.HORIZONTAL);
		// 进度条
		ProgressBar progressBar = new ProgressBar(getActivity());
		// 进度条显示位置
		progressBar.setPadding(0, 0, 15, 0);
		// 把进度条加入到layout中
		layout.addView(progressBar, mLayoutParams);
		// 文本内容
		TextView textView = new TextView(getActivity());
		textView.setText(getString(R.string.downloading));
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		// 把文本加入到layout中
		layout.addView(textView, FFlayoutParams);
		// 设置layout的重力方向，即对齐方式是
		layout.setGravity(Gravity.CENTER);

		// 设置ListView的页脚layout
		loadingLayout = new LinearLayout(getActivity());
		loadingLayout.addView(layout, mLayoutParams);
		loadingLayout.setGravity(Gravity.CENTER);

		// 添加到脚页显示
		record_list.addFooterView(loadingLayout);
		// 给ListView添加适配器
		// record_list.setAdapter(every_adapter);
		// 给ListView注册滚动监听
		record_list.setOnScrollListener(new OnScroll());
	}

	/**
	 * 滑动监听
	 */
	private class OnScroll implements OnScrollListener
	{
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount)
		{
			lastItem = firstVisibleItem + visibleItemCount - 1;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{

			if (lastItem == every_adapter.getCount()
					&& scrollState == OnScrollListener.SCROLL_STATE_IDLE)

			{
				//sportDB spDB = new sportDB(getActivity().getApplicationContext());
				lastId = lastId + showNum;
				if (curentPage < AllPage)
				{
					curentPage++;
					data.clear();
					data = spDB.getRecord(username, lastId , showNum);
				}
				else
				{
					data = spDB.getRecord(username, lastId , numLastPage);
					// record_list.removeFooterView(loadingLayout);
					loadingLayout.setVisibility(View.GONE);
				}
				every_adapter.refreshData(data);
				every_adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 点击每次，最佳记录的监听
	 */
	private class OnItemClickRecord implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			if (allCount == 0)
			{
				return;
			}
			entity_sportrecord entity_sportrecordTemp = null;
			Intent intent = new Intent();
			if (adapter.getCount() > 0)
			{
				entity_sportrecordTemp = (entity_sportrecord) adapter.getItem(position);
			}
			if (every_adapter.getCount() > 0)
			{
				entity_sportrecordTemp = (entity_sportrecord) every_adapter.getItem(position);
			}
			if (entity_sportrecordTemp.getStarttime() != 0)
			{				
				intent.putExtra("startTime", entity_sportrecordTemp.getStarttime());
				intent.putExtra("endTime", entity_sportrecordTemp.getEndtime());
				intent.putExtra("sportType", entity_sportrecordTemp.getSporttype() );
				intent.putExtra("unit_time",entity_sportrecordTemp.getDurationtime());
				intent.putExtra("unit_calorie", entity_sportrecordTemp.getCalorie()+ "");
				intent.putExtra("type_distance",entity_sportrecordTemp.getDistance());
				intent.putExtra("mode",entity_sportrecordTemp.getMode());
				
				intent.setAction("RecordMap");
				startActivity(intent);
			}
		}
	}

	/**
	 * 统计界面，点击右边箭头的监听
	 */
	private class RightArrowsClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if(time.getcurrentmonth()<nowMonth || time.getCurrentyear()< nowYear)
			{	
				time.setcurrentmonth(time.getcurrentmonth()+1);
				updateMonth(time);
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
			time.setcurrentmonth(time.getcurrentmonth()-1);
			updateMonth(time);
		}
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
		tableData = spDB.getData_EveryMonth(username,year, month,SportData.MODE_GPS);
		r4_tv_data1.setText(SportData.getFormatTime(tableData.getData1()));
		r4_tv_data2.setText(""+tableData.getData2());
		r4_tv_data3.setText(SportData.getKilometer(tableData.getData3()));
		getTabView(tableData.getTableValue(),SportData.GetRecordTab(getActivity()));
	}

	Handler UIhandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			username = SportData.getUserName(getActivity());// 获取当前用户名
			ChangeTabUI(SportData.GetRecordTab(getActivity()));
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
			case SportData.TAB_STAGE_LOW:
				record_ib_button0.setImageResource(R.drawable.record_evetime_in);
				record_ib_button1.setImageResource(R.drawable.record_statistik_out);
				record_ib_button2.setImageResource(R.drawable.record_best_out);				
				record_index0.setBackgroundResource(R.drawable.index_in);
				record_index1.setBackgroundResource(R.drawable.index_out);
				record_index2.setBackgroundResource(R.drawable.index_out);
				record_text0.setTextColor(getResources().getColor(R.color.pink));
				record_text1.setTextColor(getResources().getColor(R.color.gray_dark));
				record_text2.setTextColor(getResources().getColor(R.color.gray_dark));
				break;
			case SportData.TAB_STAGE_MIDDLE:
				record_ib_button1.setImageResource(R.drawable.record_statistik_in);
				record_ib_button0.setImageResource(R.drawable.record_evetime_out);
				record_ib_button2.setImageResource(R.drawable.record_best_out);		
				record_index1.setBackgroundResource(R.drawable.index_in);
				record_index0.setBackgroundResource(R.drawable.index_out);
				record_index2.setBackgroundResource(R.drawable.index_out);
				x_unit.setText(getString(R.string.statistics_day));
				y_unit.setText(getString(R.string.type_distance));
				tv_type1.setText(getString(R.string.time));
				record_text1.setTextColor(getResources().getColor(R.color.pink));
				record_text0.setTextColor(getResources().getColor(R.color.gray_dark));
				record_text2.setTextColor(getResources().getColor(R.color.gray_dark));
				break;
			case SportData.TAB4_STAGE_HIGH:
				record_ib_button2.setImageResource(R.drawable.record_best_in);
				record_ib_button0.setImageResource(R.drawable.record_evetime_out);
				record_ib_button1.setImageResource(R.drawable.record_statistik_out);	
				record_index2.setBackgroundResource(R.drawable.index_in);
				record_index1.setBackgroundResource(R.drawable.index_out);
				record_index0.setBackgroundResource(R.drawable.index_out);
				record_text2.setTextColor(getResources().getColor(R.color.pink));
				record_text0.setTextColor(getResources().getColor(R.color.gray_dark));
				record_text1.setTextColor(getResources().getColor(R.color.gray_dark));
				break;		
		}
		setPageOrientation(SportData.SetRecordTab(getActivity(), index));
		initData(index);
		changeMargin(index);
	}
	
	private void setPageOrientation(int index)
	{
		int orient = -1;
		switch(index)
		{
		case SportData.TAB_STAGE_LOW:
		case SportData.TAB4_STAGE_HIGH:
			 orient =ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
			 break;
		case SportData.TAB_STAGE_MIDDLE:
			 orient =ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
			 break;
		}
		getActivity().setRequestedOrientation(SportData.SetOrientation(getActivity(), orient));
	}

	private void initData(int index)
	{

		initArgument();
		switch (index)
		{
			case SportData.TAB_STAGE_LOW:
				record_list_layout.setVisibility(View.VISIBLE);
				record_statistics_layout.setVisibility(View.INVISIBLE);
				record_list.setAdapter(every_adapter);
				allCount = spDB.getCount(username);
				if (allCount == 0)
				{
					data.add(new entity_sportrecord());
				}
				if (allCount > 0 && first)
				{
					first = false;
					if (allCount < showNum)
					{
						data = spDB.getRecord(username, 0, allCount);
					}
					else
					{
						data = spDB.getRecord(username, 0, showNum);
						loadingLayout.setVisibility(View.VISIBLE);
					}
					AllPage = allCount / showNum;
					numLastPage = allCount % showNum;
				}
				every_adapter.refreshData(data);
				every_adapter.notifyDataSetChanged();
				break;
			case SportData.TAB_STAGE_MIDDLE:
				record_list_layout.setVisibility(View.INVISIBLE);
				record_statistics_layout.setVisibility(View.VISIBLE);
				updateMonth(time);
				break;
			case SportData.TAB4_STAGE_HIGH:
				record_list_layout.setVisibility(View.VISIBLE);
				record_statistics_layout.setVisibility(View.INVISIBLE);
				allCount = spDB.getCount(username);
				record_list.setAdapter(adapter);
				for (int i = 0; i < 3; i++)
				{
					entity_sportrecord sportrecord_fast = spDB.GetFastest(username, i);
					data.add(sportrecord_fast);
				//	Log.i(tag,  "iii "+i+"sportrecord_fast  "+sportrecord_fast.getAvgspeed());
					entity_sportrecord sportrecord_far = spDB.GetFarthest(username, i);
				//	Log.i(tag,  "iii "+i+"sportrecord_far  "+sportrecord_far.getAvgspeed());
					data.add(sportrecord_far);
					entity_sportrecord sportrecord_long = spDB.GetLongest(username, i);
					data.add(sportrecord_long);
				}

				adapter.refreshData(data);
				adapter.notifyDataSetChanged();
				break;

		}

	}
	
	private void getTabView(final List<Integer> values,final int currentTab)
	{
		View view = new Cylindricality().execute(getActivity(), values, currentTab, time.getCurrentyear(), time.getcurrentmonth());
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
				intent.putExtra("CurrentTab", currentTab);
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
			case R.id.record_ib_button0:
			case R.id.record_text0:
				ChangeTabUI(SportData.TAB_STAGE_LOW);
				break;
			case R.id.record_ib_button1:
			case R.id.record_text1:
				ChangeTabUI(SportData.TAB_STAGE_MIDDLE);
				break;
			case R.id.record_ib_button2:
			case R.id.record_text2:
				ChangeTabUI(SportData.TAB4_STAGE_HIGH);
				break;
		}
	}

	private void changeMargin(int id)
	{
		RelativeLayout.LayoutParams fl = (RelativeLayout.LayoutParams) fl_record
				.getLayoutParams();
		if (SportData.TAB_STAGE_MIDDLE == id)
		{
			fl.leftMargin = 0;
			fl.rightMargin = 0;
		}
		else
		{
			fl.leftMargin = (int) getResources().getDimension(R.dimen.edge_20);
			fl.rightMargin = (int) getResources().getDimension(R.dimen.edge_20);
		}
	}

	private void initArgument()
	{
		first = true;
		lastId = 0;
		lastItem = 0;
		curentPage = 1;
		AllPage = 0;
		numLastPage = 1;
		showNum = 10;
		loadingLayout.setVisibility(View.GONE);
		record_list.removeAllViewsInLayout();
		data.clear();
		adapter.clearData();
		adapter.refreshData(data);
		adapter.notifyDataSetChanged();
		every_adapter.clearData();
		every_adapter.refreshData(data);
		every_adapter.notifyDataSetChanged();
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
		 Log.d(TAG, "onConfigurationChanged");
		 switch(newConfig.orientation)
		 {
		 case Configuration.ORIENTATION_LANDSCAPE:
			 Intent intent = new Intent();
			 intent.setAction("TableActivity");
			 intent.putExtra("year", time.getCurrentyear());
			 intent.putExtra("month", time.getcurrentmonth());
			 intent.putIntegerArrayListExtra("value", (ArrayList<Integer>)tableData.getTableValue());
			 intent.putExtra("CurrentTab", SportData.GetRecordTab(getActivity()));
			 startActivity(intent);
			 break;
		 }
		super.onConfigurationChanged(newConfig);
	}
}