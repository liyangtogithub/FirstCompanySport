package com.desay.sport.slidepage;


import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class MyPagerAdapter extends FragmentPagerAdapter {
	private static final String TAG = "MyPagerAdapter";
	private List<Fragment> mFragments;
	public MyPagerAdapter(FragmentManager fm,List<Fragment> list)
	{
		super(fm);
		mFragments = list;	
	}
	
	@Override
	public Fragment getItem(int arg0) {
		Log.d(TAG, "getItem==="+arg0);
		return mFragments.get(arg0);
	}
	
	@Override
	public int getCount() {
		return mFragments.size();
	}

}
