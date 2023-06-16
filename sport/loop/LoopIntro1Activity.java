package com.desay.sport.loop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.desay.sport.main.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoopIntro1Activity extends Activity {
	ImageView iv_return = null;
	TextView title = null;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewPager advPager = null;
	private AtomicInteger what = new AtomicInteger(0);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hand_loop_1);
		initViewPager();
	}


	@SuppressWarnings("deprecation")
	private void initViewPager() {		
		title = (TextView) findViewById(R.id.tv_title);
		title.setText(getString(R.string.loop_introduce));
		
		iv_return = (ImageView) findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();	
		int screenHeight = dm.heightPixels;	
		int distance = 0;
		if (screenHeight<=480)
			distance = 60;
		else if( screenHeight<=900)
			distance = 100;
		else
			distance = 200;
		advPager = (ViewPager) findViewById(R.id.adv_pager);
		ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);

		List<LinearLayout> advPics = new ArrayList<LinearLayout>();
		//1
		LinearLayout one_Layout = new LinearLayout(this);  
		one_Layout.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		one_Layout.setOrientation(LinearLayout.VERTICAL);  
		 //ͼƬ
		ImageView img1 = new ImageView(this);
		img1.setBackgroundResource(R.drawable.intro_one);
		LinearLayout.LayoutParams one_LayoutParams = new LinearLayout.LayoutParams(  
				dip2px(200), dip2px(200));  		
		one_Layout.addView(img1, one_LayoutParams); 
        //
    	TextView one_TextView = new TextView(this);
        one_TextView.setText(R.string.introduce1);  
        one_TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.fontsize_38));
        LinearLayout.LayoutParams oneT_LayoutParams = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        oneT_LayoutParams.setMargins(dip2px(25), dip2px(distance), dip2px(25),dip2px(7));
        one_Layout.addView(one_TextView, oneT_LayoutParams); 
        one_Layout.setGravity(Gravity.CENTER_HORIZONTAL);
        advPics.add(one_Layout);
       //2
        LinearLayout two_Layout = new LinearLayout(this);  
        two_Layout.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		
        two_Layout.setOrientation(LinearLayout.VERTICAL); 
		   
		 //ͼƬ
		ImageView img2 = new ImageView(this);
		img2.setBackgroundResource(R.drawable.intro_two);
		LinearLayout.LayoutParams two_LayoutParams = new LinearLayout.LayoutParams(  
				dip2px(200), dip2px(200));  
		two_Layout.setGravity(Gravity.CENTER_HORIZONTAL);
		two_Layout.addView(img2, two_LayoutParams); 
		//
		TextView two_TextView = new TextView(this);
        two_TextView.setText(R.string.introduce2);  
		two_TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.fontsize_38));
        LinearLayout.LayoutParams twoT_LayoutParams = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        twoT_LayoutParams.setMargins(dip2px(25), dip2px(distance), dip2px(25),dip2px(7));
        two_Layout.addView(two_TextView, twoT_LayoutParams); 
        advPics.add(two_Layout);
        
        //2
        LinearLayout three_Layout = new LinearLayout(this);  
        three_Layout.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		
        three_Layout.setOrientation(LinearLayout.VERTICAL); 
		   
		 //ͼƬ
		ImageView img3 = new ImageView(this);
		img3.setBackgroundResource(R.drawable.intro_three);
		LinearLayout.LayoutParams three_LayoutParams = new LinearLayout.LayoutParams(  
				dip2px(200), dip2px(200));  
		three_Layout.setGravity(Gravity.CENTER_HORIZONTAL);
		three_Layout.addView(img3, three_LayoutParams); 
		//
		TextView three_TextView = new TextView(this);
		three_TextView.setText(R.string.introduce3);  
		three_TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.fontsize_38));
        LinearLayout.LayoutParams threeT_LayoutParams = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        threeT_LayoutParams.setMargins(dip2px(25), dip2px(distance), dip2px(25),dip2px(7));
        three_Layout.addView(three_TextView, threeT_LayoutParams); 
        advPics.add(three_Layout);
        
        //2
        LinearLayout four_Layout = new LinearLayout(this);  
        four_Layout.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		
        four_Layout.setOrientation(LinearLayout.VERTICAL); 
		   
		 //ͼƬ
		ImageView img4 = new ImageView(this);
		img4.setBackgroundResource(R.drawable.intro_four);
		LinearLayout.LayoutParams four_LayoutParams = new LinearLayout.LayoutParams(  
				dip2px(200), dip2px(200));  
		four_Layout.setGravity(Gravity.CENTER_HORIZONTAL);
		four_Layout.addView(img4,four_LayoutParams); 
		//
		TextView four_TextView = new TextView(this);
        four_TextView.setText(R.string.introduce4); 
		four_TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.fontsize_38));
        LinearLayout.LayoutParams fourT_LayoutParams = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        fourT_LayoutParams.setMargins(dip2px(25), dip2px(distance), dip2px(25),dip2px(7));
        four_Layout.addView(four_TextView, fourT_LayoutParams); 
        advPics.add(four_Layout);
        
        //2
        LinearLayout five_Layout = new LinearLayout(this);  
        five_Layout.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));		
        five_Layout.setOrientation(LinearLayout.VERTICAL); 
		   
		 //ͼƬ
		ImageView img5 = new ImageView(this);
		img5.setBackgroundResource(R.drawable.intro_five);
		LinearLayout.LayoutParams five_LayoutParams = new LinearLayout.LayoutParams(  
				dip2px(200), dip2px(200));  
		five_Layout.setGravity(Gravity.CENTER_HORIZONTAL);
		five_Layout.addView(img5, five_LayoutParams); 
		//
		TextView five_TextView = new TextView(this);
        five_TextView.setText(R.string.introduce5);
		five_TextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.fontsize_38));
        LinearLayout.LayoutParams fiveT_LayoutParams = new LinearLayout.LayoutParams(  
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        fiveT_LayoutParams.setMargins(dip2px(25), dip2px(distance), dip2px(25),dip2px(7));
        five_Layout.addView(five_TextView, fiveT_LayoutParams); 
        advPics.add(five_Layout);
		
		
		imageViews = new ImageView[advPics.size()];

		for (int i = 0; i < advPics.size(); i++) {
			imageView = new ImageView(this);
			LinearLayout.LayoutParams dot = new LinearLayout.LayoutParams(  
	                dip2px(10),dip2px(10));  
	        dot.setMargins(dip2px(7),dip2px((int)distance/3),dip2px(7),dip2px(10));
	        imageView.setLayoutParams(dot);
			//imageView.setPadding(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
			imageViews[i] = imageView;
			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_focus);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.banner_dian_blur);
			}
			group.addView(imageViews[i]);
		}

		advPager.setAdapter(new AdvAdapter(advPics));
		advPager.setOnPageChangeListener(new GuidePageChangeListener());
	}
	
	public int dip2px(float dpValue) { 
		final float scale = this.getResources().getDisplayMetrics().density;  
		return (int) (dpValue * scale + 0.5f);  }  

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			what.getAndSet(arg0);
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.banner_dian_focus);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.banner_dian_blur);
				}
			}

		}

	}

	private final class AdvAdapter extends PagerAdapter {
		private List<LinearLayout> views = null;

		public AdvAdapter(List<LinearLayout> views) {
			this.views = views;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(views.get(arg1), 0);
			return views.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

	}

}
