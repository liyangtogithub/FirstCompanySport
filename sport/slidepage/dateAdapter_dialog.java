package com.desay.sport.slidepage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.MyListAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class dateAdapter_dialog extends BaseAdapter
{
	private static final String TAG = "dateAdapter";
	private String[] list;
	private Context context;
	private LayoutInflater mInflater;
	private int position_split_plan;//计划运动在数组中对应的位置
	private int position_split_free;//自由运动在数组中对应的位置
	private int icontype;//对话框类型
//	public static final int aaa = 5;
	
	
	public dateAdapter_dialog(Context tcontext,String[] tlist,int icon_type) {
		context = tcontext;
		mInflater = LayoutInflater.from(context);
		list = tlist;
		icontype = icon_type;
	}
	
	public void setSplitLine(int tsplit_plan,int tsplit_free)
	{
		position_split_plan = tsplit_plan;
		position_split_free = tsplit_free;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "-------"+position+"="+list[position]);
		ViewHolder holder;
		if (convertView == null)
		{		
			convertView = mInflater.inflate(R.layout.item_dialog, null);
			holder = new ViewHolder();
//			holder.title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.icon =  (ImageView) convertView.findViewById(R.id.iv_icon);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		    holder.content.setText(list[position]); 
		    holder.icon.setImageDrawable(SportData.getIconDrawable(context,position,icontype));
		//屏蔽标题显示
//        if(position==position_split_plan || position ==position_split_free)
//        {
//        	holder.title.setVisibility(View.VISIBLE);
//        	holder.content.setVisibility(View.GONE);
//        	holder.title.setText(list[position]);    	
//        }
//        else
//        {
//        	holder.title.setVisibility(View.GONE);
//        	holder.content.setVisibility(View.VISIBLE);
//        	holder.content.setText(list[position]); 
//        }
		return convertView;
	}

	public class ViewHolder {
//		TextView title;
		TextView content;
		ImageView icon;
	}
	
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
