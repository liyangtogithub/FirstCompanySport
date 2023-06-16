package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.main.R;
import com.desay.sport.plan.planFunc;
import com.desay.sport.plan.planInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;


public class tab2_plan extends Fragment implements View.OnClickListener{
private static final String TAG = "tab2_plan";
ImageView iv_index;
ImageButton ib_button0;
ImageButton ib_button1;
ImageButton ib_button2;
private ListView list;
private MyListAdapter adapter;
private List<ClassItem> data;
private planFunc planfunc;
private Context context;
private int j=0;
Thread thread;
View Layout;


@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreate");
		context = getActivity().getApplicationContext();
		IntentFilter intentFilter = new IntentFilter(SportData.RECEIVER_PLAN); 
		context.registerReceiver( myBroadcastReceiver , intentFilter);
		super.onCreate(savedInstanceState);
	}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(TAG, "tab2_plan_onCreateView");
		Layout = inflater.inflate(R.layout.tab2_plan, container, false);
		planfunc = new planFunc();
		list=(ListView)Layout.findViewById(R.id.list);
		list.setDivider(null);
		adapter=new MyListAdapter(context);			
		data=new ArrayList<ClassItem>();
		list.setAdapter(adapter);
		iv_index=(ImageView)Layout.findViewById(R.id.index);
		ib_button0 = (ImageButton)Layout.findViewById(R.id.ib_button0);
		ib_button1 = (ImageButton)Layout.findViewById(R.id.ib_button1);
		ib_button2 = (ImageButton)Layout.findViewById(R.id.ib_button2);
		ib_button0.setOnClickListener(this);
		ib_button1.setOnClickListener(this);
		ib_button2.setOnClickListener(this);
		return Layout;		
}

	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	    	if (BuildConfig.DEBUG)Log.d(TAG, "tab2_plan_myBroadcastReceiver");
	    	Log.d(TAG, ""+isAdded());
	    	if(isAdded())
	    	{
	    		ChangeTabUI(SportData.TAB_STAGE_LOW);
	    	}
	    } 
	 };
	 
	public void onDetach()
	{
		super.onDetach();
		context.unregisterReceiver(myBroadcastReceiver); 
	};
	 
     private void ChangeTabUI(int index)
     {
    	 switch(index)
    	 {
    	 case SportData.TAB_STAGE_LOW:
			 ib_button0.setBackgroundResource(R.drawable.one);
			 ib_button1.setBackgroundResource(R.drawable.nottwo);	
			 ib_button2.setBackgroundResource(R.drawable.notthree);		 
			 iv_index.setBackgroundResource(R.drawable.index1);
    		 break;
    	 case SportData.TAB_STAGE_MIDDLE:
			 ib_button0.setBackgroundResource(R.drawable.notone);
			 ib_button1.setBackgroundResource(R.drawable.two);	
			 ib_button2.setBackgroundResource(R.drawable.notthree);
			 iv_index.setBackgroundResource(R.drawable.index2);
    		 break;
    	 case SportData.TAB4_STAGE_HIGH:
			 ib_button0.setBackgroundResource(R.drawable.notone);
			 ib_button1.setBackgroundResource(R.drawable.nottwo);	
			 ib_button2.setBackgroundResource(R.drawable.three);
			 iv_index.setBackgroundResource(R.drawable.index3);
    		 break; 
    	 }
		 initData(index);
     }

	private void initData(int index)
	{
		data.clear();
		List<List<planInfo>> planweek =	planfunc.readPlan(context, planFunc.PLANNAME_KEEPHEALTH, index);
		planInfo info=new planInfo();
		
		for(int i=0;i<planweek.size();i++)
		{
			List<planInfo> list_times = planweek.get(i);
			for(int j=0;j<list_times.size();j++)
			{
				info=list_times.get(j);
				String time=getString(R.string.duration)+info.getDurationtime()+getString(R.string.minutes);
				String tired=getString(R.string.tried)+info.getTired();
				ClassItem item=new ClassItem(getResources().getStringArray(R.array.times)[j],getString(R.string.uncomplete),time,tired,2, getResources().getStringArray(R.array.weeks)[i]);
				if(j==0)
				{
					item.ifTop=true;	
				}
				else if(j==list_times.size()-1){
					item.ifBottom=true;
				}
				data.add(item);
				Log.d(TAG, "time"+time);
				Log.d(TAG, "tired"+tired);
			}		
		}
		adapter.refreshData(data);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.ib_button0:
			 ChangeTabUI(SportData.TAB_STAGE_LOW);
			 break;
		case R.id.ib_button1:
			 ChangeTabUI(SportData.TAB_STAGE_MIDDLE);
			 break;
		case R.id.ib_button2:
			 ChangeTabUI(SportData.TAB4_STAGE_HIGH);
			 break;
		}
	}

}
