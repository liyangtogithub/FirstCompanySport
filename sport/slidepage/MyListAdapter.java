package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.desay.sport.main.R;



import android.content.Context;
import android.graphics.Color;
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

public class MyListAdapter extends BaseAdapter {

	private List<ClassItem> items;
	private Context context;
	private LayoutInflater mInflater;
	
	public MyListAdapter(Context context) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
	 	items = new ArrayList<ClassItem>();
	}
	
	public ClassItem getMessageByIndex(int index) {
		return items.get(index);
	}

	public void addItem(ClassItem item) {
		items.add(item);
		//this.notifyDataSetChanged();
	}

	public void removeAll() {
		items.clear();
		//this.notifyDataSetChanged();
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.plan_item, null);
			holder = new ViewHolder();
			// 栏目名称布局
			holder.partLayout = (LinearLayout)convertView.findViewById(R.id.classGroupLayout);
			holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.ll_item); 
			holder.partName = (TextView) convertView.findViewById(R.id.class_part_name);
			TextPaint tp=holder.partName.getPaint();
			tp.setFakeBoldText(true);
			holder.className = (TextView) convertView.findViewById(R.id.time);
			holder.complete=(TextView) convertView.findViewById(R.id.complete);
			holder.duration=(TextView) convertView.findViewById(R.id.duration);
			holder.tired=(TextView) convertView.findViewById(R.id.tired);
			holder.mIcon = (ImageView)convertView.findViewById(R.id.completeIcon);
			holder.vedge = (View)convertView.findViewById(R.id.v_edge);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ClassItem classItem = items.get(position);
		if(classItem != null){
			if(classItem.ifTop){
				holder.partLayout.setVisibility(View.VISIBLE);
				holder.partName.setVisibility(View.VISIBLE);
				if(position==0)
				{
					holder.vedge.setVisibility(View.GONE);
				}
				else
				{
					holder.vedge.setVisibility(View.VISIBLE);
				}
				
				holder.partName.setText(classItem.partName);
				holder.className.setText(classItem.className);
				holder.complete.setText(classItem.complate);
				holder.duration.setText(classItem.duration);
				holder.tired.setText(classItem.tired);
				holder.itemLayout.setBackgroundResource(R.drawable.list_rect_selector);
				
			}else if(classItem.ifBottom){
				holder.partLayout.setVisibility(View.GONE);
				holder.vedge.setVisibility(View.GONE);
				holder.partName.setText(classItem.partName);
				holder.className.setText(classItem.className);
				holder.complete.setText(classItem.complate);
				holder.duration.setText(classItem.duration);
				holder.tired.setText(classItem.tired);
				holder.itemLayout.setBackgroundResource(R.drawable.list_bottom_selector);
			}
			else{
				holder.partLayout.setVisibility(View.GONE);
				holder.vedge.setVisibility(View.GONE);
				holder.partName.setText(classItem.partName);
				holder.className.setText(classItem.className);
				holder.complete.setText(classItem.complate);
				holder.duration.setText(classItem.duration);
				holder.tired.setText(classItem.tired);
				holder.itemLayout.setBackgroundResource(R.drawable.list_rect_selector);
			}
			if(classItem.complate.equals("已完成")){
				holder.mIcon.setBackgroundResource(R.drawable.completed);
			}else{
			holder.mIcon.setBackgroundResource(R.drawable.uncomplete);
			}
			convertView.setBackgroundResource(R.drawable.list_bottom_selector);
		}
		return convertView;
	}

	public class ViewHolder {
		LinearLayout partLayout;
		RelativeLayout itemLayout;
		TextView partId;
		TextView partName;
		TextView className;
		TextView duration;
		TextView complete;
		TextView tired;
		ImageView mIcon;
		View vedge;		
	}
	
	public void refreshData(List<ClassItem> data)
	{
		items = data;
	}
	
//	private void setBackgroundDrawable(View view, int resID) {
//		view.setBackgroundDrawable(context.getResources().getDrawable(resID));
//	}
}
