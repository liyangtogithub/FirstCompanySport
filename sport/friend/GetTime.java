package com.desay.sport.friend;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetTime {
	private Calendar calc;
	private int year=0;
	private int month=0;
	private int day=0;
	
	public GetTime()
	{
		calc = Calendar.getInstance();
		year = calc.get(Calendar.YEAR);
	    month = calc.get(Calendar.MONTH);
	    day = calc.get(Calendar.DAY_OF_MONTH); 
	}
	
	public int getcurrentday()
	{
		return day;
	}
	
	public int getcurrentmonth()
	{
		return month;
	}
	
	public int getCurrentyear()
	{
		return year;
	}
	
	public void setcurrentday(int tday)
	{
		calc.set(year, month, tday);
		year  = calc.get(Calendar.YEAR);
		month = calc.get(Calendar.MONTH);
		day   = calc.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setcurrentmonth(int tmonth)
	{
		calc.set(year, tmonth, day);
		year  = calc.get(Calendar.YEAR);
		month = calc.get(Calendar.MONTH);
		day   = calc.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setCurrentyear(int tyear)
	{
		calc.clear();
		calc.set(tyear, month, day);
		year  = calc.get(Calendar.YEAR);
		month = calc.get(Calendar.MONTH);
		day   = calc.get(Calendar.DAY_OF_MONTH);
	}
	
	public void ClearTime()
	{	
		calc.clear();
	}
	
	public String get_endday()
	{	
		calc.set(year, month, day);
		String endday=getdateform(calc);	

		return endday;
	}
	public String get_startday()
	{	
		calc.set(year, month,1);
		String startday=getdateform(calc);	
		return startday;
		
	}
	public long get_endMillis()
	{	
		calc.set(year, month, day,23,59,59);
		long endmills=calc.getTimeInMillis();
		return endmills;
	}
	public long get_startMillis()
	{	
		calc.set(year, month, 1,0,0,0);
		long startmills=calc.getTimeInMillis();
		return startmills;
	}
	public String getdateform(Calendar calc)
	{
		Date date=calc.getTime(); 
		SimpleDateFormat foramtyear = new SimpleDateFormat("yyyy-MM-dd");
		String yearText = foramtyear.format(date);
		return yearText;
	}
	public String millisToDateform(long mills)
	{
		Date date=new java.util.Date(mills);
		SimpleDateFormat foramtyear = new SimpleDateFormat("yyyy-MM-dd");
		String yearText = foramtyear.format(date);
		return yearText;
	}
	
	public long getRecord_startMillis()
	{	
		Calendar calc = Calendar.getInstance();
		calc.set(calc.get(Calendar.YEAR), calc.get(Calendar.MONTH),calc.get(Calendar.DATE)- 7, 0, 0, 0);
		long startmills = calc.getTimeInMillis();
		return startmills;
	}
	public long getRecord_endMillis()
	{	
		Calendar calc = Calendar.getInstance();
		calc.set(calc.get(Calendar.YEAR), calc.get(Calendar.MONTH),calc.get(Calendar.DATE) ,23,59,59);
		long endmills = calc.getTimeInMillis();
		return endmills;
	}
	public Calendar getRecord_Calendar(int year,int month)
	{	
		Calendar calc = Calendar.getInstance();
		calc.set(year, month, 1, 0, 0, 0);
		return calc;
	}
	public long getRecord_startMillis(Calendar calc)
	{	
		long startmills = calc.getTimeInMillis();
		return startmills;
	}
	public long getRecord_endMillis(Calendar calc)
	{	
		calc.add(Calendar.MONTH,1);//月增加1天 
		calc.add(Calendar.DAY_OF_MONTH,-1);//日期倒数一日,
		calc.set(Calendar.HOUR,23);
		calc.set(Calendar.MINUTE,59);
		calc.set(Calendar.SECOND,59);
		long endmills = calc.getTimeInMillis();
		return endmills;
	}
	
}
