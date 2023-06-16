package com.desay.sport.slidepage;

import java.util.ArrayList;
import java.util.Calendar;

import com.desay.sport.data.SportData;
import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableActivity extends Activity
{
	private static final String TAG = "TableActivity";
	Calendar cal = Calendar.getInstance();
	int yearDefault = cal.get(Calendar.YEAR);
	int monthDefault = cal.get(Calendar.MONTH);
    private String xu ="";
    private String yu ="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.table_activity);
		TextView x_unit=(TextView) findViewById(R.id.x_unit);
		TextView y_unit=(TextView) findViewById(R.id.y_unit);
		LinearLayout table_activity = (LinearLayout) findViewById(R.id.table_activity);
		Intent intent = getIntent();
    	int year =	intent.getIntExtra("year", yearDefault);
	    int month =	intent.getIntExtra("month", monthDefault);	
        int CurrentTab = intent.getIntExtra("CurrentTab", SportData.TAB_STAGE_LOW);
        ArrayList<Integer> values = intent.getIntegerArrayListExtra("value");
		switch(CurrentTab)
		{
		case SportData.TAB_STAGE_MIDDLE:
			xu = getString(R.string.statistics_day);
			yu = getString(R.string.type_distance);
			 break;
		case SportData.TAB_STEP_ONE:
			xu = getString(R.string.unit_time);
			yu = getString(R.string.type_step);
			break;
		case SportData.TAB_STEP_TWO:
			xu = getString(R.string.statistics_day);
			yu = getString(R.string.type_step);
			break;
		}
		x_unit.setText(xu);
		y_unit.setText(yu);
		View view = new Cylindricality().execute(this,values,CurrentTab, year, month);
		table_activity.removeAllViews();
		table_activity.addView(view);
	}
	 @Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		 Log.d(TAG, "onConfigurationChanged");
		 switch(newConfig.orientation)
		 {
		 case Configuration.ORIENTATION_PORTRAIT:
			 Log.d(TAG, "ORIENTATION_PORTRAIT");
			 finish();
			 break;
		 }
		super.onConfigurationChanged(newConfig);
	}
	 
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode==KeyEvent.KEYCODE_BACK)return true;
			return super.onKeyDown(keyCode, event);
		}

}
