package com.desay.sport.data;

import android.view.View;

public class PublicUtils
{	
	/**
	 *  判断手机sdk版本
	 */
	public static int getSDKVersionNumber()
	{
		int sdkVersion;
		try
		{
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);

		}
		catch (NumberFormatException e)
		{

			sdkVersion = 0;
		}
		return sdkVersion;
	}
	

    /**
	 *  根据手机sdk版本，配置View的背景
	 */
    public static void updateTab( View view ,int selectSdk10 , int selectSdkOther )
	{
    	int  sdkVersion = PublicUtils.getSDKVersionNumber();
		//Log.i(TAG, "sdkVersion   "+sdkVersion);
    	
    	if (sdkVersion < 12)
		{
    		view.setBackgroundResource(selectSdk10);
		}
		else {
			view.setBackgroundResource(selectSdkOther);
		}
	}

    /**
	 *  如果输入参数小于10，就返回前面加0的字符串
	 *  例如输入9，返回09。输入10，返回10
	 */
    public static String getDoubleDigitString( int digit )
	{
		String doubleDigit = ""+digit;
		if (digit < 10)
		{
			doubleDigit = "0"+digit;
		}		
		return doubleDigit;
	}
    
    /**
	 * 计算走了多少米
	 */
    public static float getDistanceByStep(long step, int height)
	{
		float dis = 0;
		dis = (float) (// kilometers
				height * 1 / 3.0 // centimeters
				/ 100.0)
				* step; // centimeters/kilometer
		return dis;
	}

	/**
	 * 通过速度：公里/小时，来判断是哪种运动类型
	 */
    public static int getSportType(double avgSpeed)
	{
		int sportType = 0;
		if (avgSpeed < SportData.max_walk)
		{
			sportType = 0;
		}
		else if (avgSpeed < SportData.max_run && avgSpeed >= SportData.max_walk)
		{
			sportType = 1;
		}
		else
		{
			sportType = 2;
		}
		return sportType;
	}

	/**
	 * 计算卡路里
	 */
    public static int getCalorie(String weight,int dis)
	{
		int cal = 0;
		if (weight != null&&!weight.equals(""))
		{
			int weight1 = Integer.valueOf(weight);
			 cal=(int) (((2.21*weight1 * 0.708)
			         // Distance:
			         * dis // centimeters
			         / 1000.0)); // centimeters/kilometer
		}
		return cal;
	}
	
}
