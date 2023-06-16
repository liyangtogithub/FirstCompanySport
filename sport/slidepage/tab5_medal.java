package com.desay.sport.slidepage;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.data.SportData;
import com.desay.sport.data.UpdataMedalUI;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;

public class tab5_medal extends Fragment
{
	String tag = "tab5_medal";
	View view = null;// 总布局
	private UpdataMedalUI UMUI;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		IntentFilter intentFilter = new IntentFilter(); 
	    intentFilter.addAction(SportData.RECEIVER_LOAD);
		getActivity().registerReceiver( medalBroadcastReceiver , intentFilter);
		super.onCreate(savedInstanceState);
	}

	private BroadcastReceiver medalBroadcastReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	    	if (BuildConfig.DEBUG)Log.d(tag, "tab5_medalBroadcastReceiver");
	    	
	    	if(isAdded())
	    	{
	    		medalhandler.sendEmptyMessage(0);
	    	}
	    } 
	 };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		initViewTab5(inflater, container);
		medalhandler.sendEmptyMessage(0);
		return view;
	}

	private void initViewTab5(LayoutInflater inflater, ViewGroup container)
	{
		view = inflater.inflate(R.layout.tab5_medal, container, false);
		UMUI=new UpdataMedalUI(getActivity().getApplicationContext(),view);
		UMUI.init();
	}

	Handler medalhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			sportDB spDB = new sportDB(getActivity().getApplicationContext());
			// 所有奖章全查
			List<entity_sportachieve> medal_list = spDB.GetSportAchieve(
					SportData.getUserName(getActivity()), "1", null);

			//Log.i(tag, "medal_list.size()" + medal_list.size());
			UMUI.UpUi(medal_list);
		}

	};
	
	@Override
	public void onDetach()
	{
		getActivity().unregisterReceiver( medalBroadcastReceiver );
		super.onDetach();
	}
}
