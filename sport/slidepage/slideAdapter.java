package com.desay.sport.slidepage;

import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.MyListAdapter.ViewHolder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class slideAdapter extends BaseAdapter{
    protected static final String TAG = "slideAdapter";
	private LayoutInflater mInflater;
	private String[] list ;
	private Context context;
	private boolean havenew=true;
	public slideAdapter(String[] tlist,Context tcontext,boolean hnew){
		list = tlist;
		context = tcontext;
		mInflater = LayoutInflater.from(context);
		havenew=hnew;
	}
	
	public void refreshData(String[] data,boolean hnew)
	{
		list = data;
		havenew=hnew;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		Log.d(TAG, "getview");
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_slidepage, null);
			holder = new ViewHolder();
			holder.itemName = (TextView) convertView.findViewById(R.id.tv_content);
			holder.img = (ImageView)convertView.findViewById(R.id.tv_image);
			holder.itemNew = (TextView)convertView.findViewById(R.id.tv_upnew);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}	
		    holder.itemName.setText(list[position]);
		    holder.img.setImageDrawable(SportData.getIconDrawable(context,position,SportData.ICON_SLIDEPAGE));
//		    Log.d("position=="+position,"havenew=="+havenew);
		    if(position==SportData.PAGE_UPDATE&&!havenew)
		    	holder.itemNew.setVisibility(View.VISIBLE);
		    else
		    	holder.itemNew.setVisibility(View.GONE);
		return convertView;
	}
	
	public class ViewHolder {
		TextView itemName;
		ImageView img;
		TextView itemNew;
	}

}
