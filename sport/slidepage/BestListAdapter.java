package com.desay.sport.slidepage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sportrecord;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.MyListAdapter.ViewHolder;

import android.R.integer;
import android.content.Context;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BestListAdapter extends BaseAdapter
{

	String TAG = "BestListAdapter";
	private List<entity_sportrecord> items = new ArrayList<entity_sportrecord>();
	private Context context;
	LayoutInflater mInflater;

	Calendar cal = Calendar.getInstance();
	long startTime;

	public BestListAdapter(Context context)
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
		final BestViewHolder holder;
		if (convertView == null)
		{
			//Log.i("BestListAdapter.java", "convertView == null");
			convertView = mInflater.inflate(R.layout.best_item, null);
			holder = new BestViewHolder();
			// 栏目名称布局
			holder.partLayout = (LinearLayout) convertView
					.findViewById(R.id.best_classGroupLayout);
			holder.itemLayout = (RelativeLayout) convertView
					.findViewById(R.id.best_ll_item);
			holder.partName = (TextView) convertView
					.findViewById(R.id.best_class_part_name);
			TextPaint tp = holder.partName.getPaint();
			tp.setFakeBoldText(true);
			holder.className = (TextView) convertView
					.findViewById(R.id.best_time);
			holder.complete = (TextView) convertView
					.findViewById(R.id.best_complete);
			holder.duration = (TextView) convertView
					.findViewById(R.id.best_duration);
			// holder.tired = (TextView)
			// convertView.findViewById(R.id.best_tired);
			// holder.mIcon = (ImageView) convertView
			// .findViewById(R.id.best_completeIcon);
			holder.vedge = (View) convertView.findViewById(R.id.best_v_edge);
			convertView.setTag(holder);
		}
		else
		{
			holder = (BestViewHolder) convertView.getTag();
		}

		entity_sportrecord sportrecord = items.get(position);
		if (sportrecord != null)
		{
			//运动类型选择
			if (sportrecord.getSporttype() == 0)
			{
				holder.partName.setText(context
						.getString(R.string.type_walk));
			}
			else if (sportrecord.getSporttype() == 1)
			{
				holder.partName.setText(context
						.getString(R.string.type_run));
			}
			else {
				holder.partName.setText(context
						.getString(R.string.type_bicycle));
			}

			
			
			//UI划分
			if ((position % 3) == 0)//速度最快
			{
				holder.partLayout.setVisibility(View.VISIBLE);
				holder.partName.setVisibility(View.VISIBLE);
				if (position == 0)
				{
					holder.vedge.setVisibility(View.GONE);
				}
				else
				{
					holder.vedge.setVisibility(View.VISIBLE);
				}

				holder.className.setText(context.getString(R.string.type_fast));
				holder.complete.setText(new DecimalFormat("0.00").format((double)sportrecord.getAvgspeed())
						+ context.getString(R.string.type_speed));
				startTime = sportrecord.getStarttime();
				if (startTime == 0 )
				{
					holder.duration.setText( context.getString(R.string.type_nostart));
				}
				else {
					cal.setTimeInMillis(startTime);
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH)+1;
					int date = cal.get(Calendar.DAY_OF_MONTH);
					holder.duration.setText(year
							+ context.getString(R.string.type_year) + month
							+ context.getString(R.string.type_month) + date
							+ context.getString(R.string.type_date)
							+ context.getString(R.string.type_start));
				}
				holder.itemLayout
						.setBackgroundResource(R.drawable.list_rect_selector);

			}
			else if ((position % 3) == 1)//距离最远
			{
				holder.partLayout.setVisibility(View.GONE);
				holder.vedge.setVisibility(View.GONE);
				holder.className.setText(context.getString(R.string.type_far));
				//距离
				holder.complete.setText(SportData.getKilometer(sportrecord.getDistance() )					
						+ context.getString(R.string.type_distance));
				startTime = sportrecord.getStarttime();
				if (startTime == 0 )
				{
					holder.duration.setText( context.getString(R.string.type_nostart));
				}
				else {
					cal.setTimeInMillis(startTime);
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH)+1;
					int date = cal.get(Calendar.DAY_OF_MONTH);
					holder.duration.setText(
							year+context.getString(R.string.type_year)+
							month+context.getString(R.string.type_month)+
							date+context.getString(R.string.type_date)+
							 context.getString(R.string.type_start));
				}
				holder.itemLayout
						.setBackgroundResource(R.drawable.list_rect_selector);
			}
			else//时间最长
			{
				holder.partLayout.setVisibility(View.GONE);
				holder.vedge.setVisibility(View.GONE);
				holder.className.setText(context.getString(R.string.type_long));
				//时间
				holder.complete.setText( SportData.getFormatTime(sportrecord.getDurationtime()));
				
				startTime = sportrecord.getStarttime();
				
				if (startTime == 0 )
				{
					holder.duration.setText( context.getString(R.string.type_nostart));
				}
				else {
					cal.setTimeInMillis(startTime);
					int year = cal.get(Calendar.YEAR);
					int month = cal.get(Calendar.MONTH)+1;
					int date = cal.get(Calendar.DAY_OF_MONTH);
					holder.duration.setText(
							year+context.getString(R.string.type_year)+
							month+context.getString(R.string.type_month)+
							date+context.getString(R.string.type_date)+
							 context.getString(R.string.type_start));
				}			
				holder.itemLayout
						.setBackgroundResource(R.drawable.list_bottom_selector);//底色
			}

			//convertView.setBackgroundResource(R.drawable.list_bottom_selector);//圆角
		}
		return convertView;
	}

	private class BestViewHolder
	{
		LinearLayout partLayout;
		RelativeLayout itemLayout;
		TextView partName;
		TextView className;
		TextView duration;
		TextView complete;
		View vedge;
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
