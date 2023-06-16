package com.desay.sport.data;

import java.util.Calendar;

import android.util.Log;
public class SwitchDayNum
{
	/**
	 * 返回某月有多少天
	 */
	public static int getDayNum(int year, int month)
	{
		int numDays = 0;
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				numDays = 31;
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				numDays = 30;
				break;
			case 2:
				if (((year % 4 == 0) && !(year % 100 == 0))
						|| (year % 400 == 0)) numDays = 29;
				else
					numDays = 28;
				break;
		}
		return numDays;
	}

	/**
	 * 考虑了跨年情况：判断两个时间相差几天
	 */
	public static int getIntervalDays(Calendar startCalendar , Calendar currentCalendar)
	{
		startCalendar.set(Calendar.HOUR_OF_DAY, 23);//起点
		currentCalendar.set(Calendar.HOUR_OF_DAY, 9);//终点
	//	Log.i("days" , " days "+startCalendar.getTime());
	//	Log.i("days" , " days "+currentCalendar.getTime());
		int days = 0;
		while (startCalendar.before(currentCalendar))
		{
			days++;
			startCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}	
	//	Log.i("days" , " days "+days);
		return days;
	}

}
