package com.desay.sport.net;

import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ChooseCityActivity extends Activity {
	private static final String TAG = "ChooseCityActivity";
	private ExpandableListView provinceList;	
	private String[] groups;
    private String[][] childs;    
    private cityAdapter adapter;
    private Context context;
    private static boolean isFsetRun=true;
    private Handler handle;
    private  final int MSG_0 = 0;
    private  final int MSG_1 = 1;  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
        setContentView(R.layout.setcity);
        context = ChooseCityActivity.this;
        provinceList= (ExpandableListView)findViewById(R.id.provinceList);        
        if(isFsetRun) {
        	CityDB.importInitDatabase(context);
        	isFsetRun=false;
        }        
        handle = new Handler()
        {
        	public void handleMessage(Message msg)
        	{
        		super.handleMessage(msg); 
        		switch(msg.what)
        		{
        		case MSG_0:
        			adapter=new cityAdapter(context, provinceList, groups, childs);
        	        provinceList.setAdapter(adapter);	        
        	        provinceList.setOnChildClickListener(new OnChildClickListener() {
        				public boolean onChildClick(ExpandableListView parent, View v,
        						int groupPosition, int childPosition, long id) {
        					String cityName = (String)adapter.getChild(groupPosition, childPosition);
        					Intent intent = getIntent();
        					intent.putExtra("updatecity",cityName);
        					setResult(RESULT_OK, intent);
        					finish();					
        					return false;
        				}    	
        	        });
        				break;
        		case MSG_1:
    				CityDB.importInitDatabase(context);
    				isFsetRun=false;
    				finish();
        			break;
        		}
        	}
        };
        new Thread(new Runnable() {
        	public void run() {
        		try{
	                CityDB dbHelper = new CityDB(context, "db_passome.db");
	                groups = dbHelper.getAllProvinces();
	                List<String[][]> result = dbHelper.getAllCityAndCode(groups);
	                childs = result.get(0);
	                Message msg = new Message();
	                msg.what = MSG_0;
	                handle.sendMessage(msg);
        		}catch(Exception e)
        		{
        			e.printStackTrace();       			
        			Message msg = new Message();
        			msg.what = MSG_1;	    
        			handle.sendMessage(msg);  	
        		}               
        	}
        }).start();
    }	

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		   if ((keyCode == KeyEvent.KEYCODE_BACK)) 
		   { 
			    Intent intent = getIntent();
				intent.putExtra("updatecity","");
				setResult(RESULT_OK, intent);
				finish();
				return false; 				
	    	}	
	        return super.onKeyDown(keyCode, event);
	}
}
