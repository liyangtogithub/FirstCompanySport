package com.desay.sport.loop;

import com.desay.sport.data.PublicUtils;
import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoopIntroActivity extends Activity
{
	ImageView iv_return = null;
	TextView title = null;
	LinearLayout loop_lay1 = null;
	LinearLayout loop_lay3 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hand_loop_control);
		initView();	 
	}
	
 
	private void initView()
	{
		iv_return = (ImageView) findViewById(R.id.iv_return);
		title = (TextView) findViewById(R.id.tv_title);
		loop_lay1 = (LinearLayout) findViewById(R.id.loop_lay1);
		loop_lay3 = (LinearLayout) findViewById(R.id.loop_lay3);	
		title.setText(getString(R.string.loop_help));
		iv_return.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		loop_lay1.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(LoopIntroActivity.this,LoopIntro1Activity.class));
			}
		});
	
		loop_lay3.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(LoopIntroActivity.this,LoopIntro2Activity.class));
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	




















	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

}
