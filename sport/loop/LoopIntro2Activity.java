package com.desay.sport.loop;

import java.util.ArrayList;
import java.util.List;

import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.MoveBg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoopIntro2Activity extends Activity
{
	String tag = "Loop2Activity";
	ImageView iv_return = null;
	TextView title = null;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hand_loop_3);
		ImageView iv_return = (ImageView)findViewById(R.id.iv_return);
		TextView tv_title   =  (TextView)findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.loop_use));
		iv_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});

	}


}
