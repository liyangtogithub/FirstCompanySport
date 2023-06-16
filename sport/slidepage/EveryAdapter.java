package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sportrecord;
import com.desay.sport.main.R;
import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EveryAdapter extends BaseAdapter
{
	String tag = "EveryAdapter";
	private List<entity_sportrecord> items = new ArrayList<entity_sportrecord>();
	private Context context;
	LayoutInflater mInflater;
	Calendar cal = Calendar.getInstance();
	String clock = ":";
	int year = 0;
	int month = 0;
	int date = 0;
	int hour = 0;
	int minute = 0;

	public EveryAdapter(Context context)
	{
		this.context = context;
		mInflater = LayoutInflater.from(context);
		items = new ArrayList<entity_sportrecord>();
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final EveryViewHolder holder;
		if (convertView == null)
		{
			// Log.i("BestListAdapter.java", "convertView == null");
			convertView = mInflater.inflate(R.layout.every_item, null);
			
			holder = new EveryViewHolder();
			// 栏目名称布局
			holder.every_v_edge = (View) convertView
					.findViewById(R.id.every_v_edge);
			holder.every_sport_type = (TextView) convertView
					.findViewById(R.id.every_sport_type);
			holder.every_sport_time = (TextView) convertView
					.findViewById(R.id.every_sport_time);
			holder.r4_tv_data1 = (TextView) convertView
					.findViewById(R.id.tv_data1);
			holder.r4_tv_data2 = (TextView) convertView
					.findViewById(R.id.tv_data2);
			holder.r4_tv_data3 = (TextView) convertView
					.findViewById(R.id.tv_data3);		
			holder.rl_data1 = (RelativeLayout) convertView
					.findViewById(R.id.rl_data1);		
			holder.rl_data3 = (RelativeLayout) convertView
					.findViewById(R.id.rl_data3);		
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (EveryViewHolder) convertView.getTag();
		}

		entity_sportrecord sportrecord = items.get(position);
		if (sportrecord != null)
		{
			if (position == 0)
			{
				holder.every_v_edge.setVisibility(View.GONE);
			}
			else
			{
				holder.every_v_edge.setVisibility(View.VISIBLE);
			}
			// 运动类型选择
			if (sportrecord.getSporttype() == 0)
			{
				holder.every_sport_type.setText(context.getString(R.string.type_walk));
			}
			else if (sportrecord.getSporttype() == 1)
			{
				holder.every_sport_type.setText(context.getString(R.string.type_run));
			}
			else {
				holder.every_sport_type.setText(context.getString(R.string.type_bicycle));
			}

			long startTime = sportrecord.getStarttime();
			
			if (startTime == 0 )
			{
				holder.every_sport_time.setText(context.getString(R.string.type_nostart));
			}
			else {
				 cal.setTimeInMillis(startTime);
				 year = cal.get(Calendar.YEAR);
				 month = cal.get(Calendar.MONTH)+1;
				 date = cal.get(Calendar.DAY_OF_MONTH);
				 hour = cal.get(Calendar.HOUR_OF_DAY);
				 minute = cal.get(Calendar.MINUTE);
				if (minute<10)
				{
					clock = ":0";
				}else {
					clock = ":";
				}
				
				holder.every_sport_time.setText(
						year+context.getString(R.string.type_year)+
						month+context.getString(R.string.type_month)+
						date+context.getString(R.string.type_date)+" "+
						hour+clock+minute);
			}
			
			holder.r4_tv_data1.setText(SportData.getFormatTime(sportrecord.getDurationtime()));			
			holder.r4_tv_data2.setText(sportrecord.getCalorie()+"");
			holder.r4_tv_data3.setText(SportData.getKilometer(sportrecord.getDistance()));
			
			PublicUtils.updateTab(holder.rl_data1, R.drawable.shape_item_bottom_right , R.drawable.shape_item_bottom_left );
			PublicUtils.updateTab(holder.rl_data3, R.drawable.shape_item_bottom_left , R.drawable.shape_item_bottom_right );

		}
		return convertView;
	}

	private class EveryViewHolder
	{
		View every_v_edge; // 粉色隔离条
		TextView every_sport_type;// 运动类型
		TextView every_sport_time;// 运动开始时间
		TextView r4_tv_data1; //运动的持续时间
		TextView r4_tv_data2;//卡路里
		TextView r4_tv_data3;//距离
		RelativeLayout rl_data1;
		RelativeLayout rl_data3;
	}

	public void refreshData(List<entity_sportrecord> data)
	{
		items.addAll(data);
	}

	public void clearData()
	{
		items.clear();
	}
}
