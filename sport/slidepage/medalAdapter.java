package com.desay.sport.slidepage;

import java.util.List;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.main.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class medalAdapter extends BaseAdapter{
    protected static final String TAG = "medalAdapter";
	private LayoutInflater mInflater;
	private List<entity_sportachieve> list ;
	private Context context;
	private int layoutID; 
	public medalAdapter(List<entity_sportachieve> tlist,Context tcontext,int layoutID){
		list = tlist;
		context = tcontext;
		this.layoutID = layoutID; 
		mInflater = LayoutInflater.from(context);
//		Log.d(TAG, "size====:"+list.size());
	}
	
	public void refreshData(List<entity_sportachieve> data)
	{
		list = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		Log.d(TAG,"position====="+position); 
//		Log.d(TAG,"getCount()====="+getCount()); 
//		Log.d(TAG, "getview:"+"position="+position+";"+"getAchievename="+list.get(position).getAchievename()+";"+"getAchieverecord="+list.get(position).getAchieverecord());
		final ViewHolder holder;
		if (convertView == null) 
		{
			convertView = mInflater.inflate(layoutID, null);
			holder = new ViewHolder();
			holder.MedalIcon   = (ImageView) convertView.findViewById(R.id.iv_medal_icon);
			holder.MedalName   = (TextView) convertView.findViewById(R.id.tv_medal_toast);		
			holder.MedalNumber = (TextView) convertView.findViewById(R.id.tv_medal_number);		
			convertView.setTag(holder);
		}
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}	
	    if(list.get(position).getStarttime() != -1)
	    {
	    	holder.MedalIcon.setImageDrawable(SportData.getIconDrawable(context,list.get(position).getAchievename(),SportData.ICON_MEDAL));
	    }
	    else
	    {
	    	holder.MedalIcon.setImageDrawable(SportData.getIconDrawable(context,list.get(position).getAchievetype(),SportData.ICON_NOMEDAL));
	    }
	        if(list.get(position).getAchieverecord()<2)
	        {
	        	holder.MedalNumber.setVisibility(View.INVISIBLE);
	        } 
	        else
	        {
		        holder.MedalNumber.setVisibility(View.VISIBLE);
		        holder.MedalNumber.setText("X "+ list.get(position).getAchieverecord());
	        }
        	holder.MedalName.setText(context.getResources().getStringArray(R.array.medal_word)[list.get(position).getAchievename()]);  
	        return convertView;
	}
	
	public class ViewHolder {
		ImageView MedalIcon;
		TextView  MedalName;
		TextView  MedalNumber;
	}

}
