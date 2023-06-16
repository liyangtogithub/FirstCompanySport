package com.desay.sport.loop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.loop.HandLoopConnect.ReadInfoBroadcast;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddLoopActivity extends Activity implements OnClickListener
{
	ImageView iv_return = null;
	TextView title = null;
	private ViewPager viewPager;
	TextView tab_front = null;
	private List<View> views;
	private View view1, view3;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private AtomicInteger what = new AtomicInteger(0);
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_loop2_1);
		initView();	
		InitViewPager();
		
	}
	
	private void initView()
	{
		iv_return = (ImageView) findViewById(R.id.iv_return);
		title = (TextView) findViewById(R.id.tv_title);	
		title.setText(getString(R.string.loop_manage_text2));
		iv_return.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});		
			Button loop_connect = (Button) findViewById(R.id.loop_connect);
			loop_connect.setBackgroundResource(R.drawable.selector_btn_dark);
			loop_connect.setOnClickListener(this);			
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		super.onStart();
	}	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub		
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private void InitViewPager()
	{
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
		viewPager = (ViewPager) findViewById(R.id.adv_pager);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();

		view1 = inflater.inflate(R.layout.new_loop2_2, null);
		views.add(view1);

		view3 = inflater.inflate(R.layout.new_loop2_2, null);
		ImageView loop_img2 = (ImageView) view3.findViewById(R.id.loop_img);
		loop_img2.setImageResource(R.drawable.blue_already);
		views.add(view3);	

		imageViews = new ImageView[views.size()];
		for (int i = 0; i < views.size(); i++)
		{
			imageView = new ImageView(this);
			LinearLayout.LayoutParams dot = new LinearLayout.LayoutParams(dip2px(10), dip2px(10));
			dot.setMargins(dip2px(7), dip2px(10), dip2px(7), dip2px(10));
			imageView.setLayoutParams(dot);
			// imageView.setPadding(dip2px(10), dip2px(10), dip2px(10),
			// dip2px(10));
			imageViews[i] = imageView;
			if (i == 0)
			{
				imageViews[i].setBackgroundResource(R.drawable.banner_dian_focus);
			}
			else
			{
				imageViews[i].setBackgroundResource(R.drawable.banner_dian_blur);
			}
			group.addView(imageViews[i]);
		}

		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.loop_connect:									
					//4.0开始连接蓝牙的代码	
				sendBroadcast(new Intent(MainTab.WRITE_ACTIVITY));			
				finish();
				break;
				
		}
		
	}

	public int dip2px(float dpValue)
	{
		final float scale = this.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	class MyViewPagerAdapter extends PagerAdapter
	{
		private List<View> listViews;

		MyViewPagerAdapter(List<View> listViews)
		{
			this.listViews = listViews;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(listViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			container.addView(listViews.get(position), 0);
			return listViews.get(position);
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return listViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	class MyOnPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int arg0)
		{

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageSelected(int arg0)
		{
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++)
			{
				imageViews[arg0]
						.setBackgroundResource(R.drawable.banner_dian_focus);
				if (arg0 != i)
				{
					imageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}
		}
	}

}
		
	
	


