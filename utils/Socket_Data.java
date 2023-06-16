package com.desay.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.desay.sport.db.entity_loopheat;
import com.desay.sport.db.entity_mac;
import com.desay.sport.db.entity_sendrecord;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.entity_sportloop;
import com.desay.sport.friend.GetTime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Socket_Data {
	public static final String HEAD_LENGTH = "Content-Length: ";
	public static final String HEAD_VER = "x-extern-ver: 1.0\r\n";
	public static final String HEAD_IMEI = "x-extern-devid: ";
	public static final String HEAD_TRANSACTION_CODE = "x-extern-data-code: ";
	public static final String HEAD_RESPONSE_ERROR_CODE = "x-extern-error-code:";
	public static final String HEAD_RESPONSE_NEXT_CODE = "x-extern-next-code:";
	public static final String HEAD_PHONENUMBER = "x-up-calling-line-id: ";
	public static final String HEAD_TIME = "x-extern-data-time: ";
	public static final String BODY = "<body>";
	private static final String TAG = "Socket_Data";
	
	public static String Socket_Datahead(Context context,String transaction_code,String data)
	{
		StringBuffer sb = new StringBuffer();
		String time = getTime();
		String imei="123";
		try{
		imei = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		}catch(Exception e)
		{
			imei="123";
		}
		if(imei==null||imei.equals(""))
			imei="123";
		sb.append(HEAD_VER);
		sb.append(HEAD_IMEI + imei + "\r\n");
		sb.append(HEAD_TRANSACTION_CODE + transaction_code + "\r\n");//
		sb.append(HEAD_RESPONSE_ERROR_CODE + " 000\r\n");
		sb.append(HEAD_RESPONSE_NEXT_CODE +" 000\r\n");
		sb.append(HEAD_PHONENUMBER + "100" + "\r\n");
		sb.append(HEAD_TIME + time + "\r\n");
		sb.append("\r\n");
		sb.append(data);
	    return sb.toString();
	}

	public static String SendInfo(Context context, String datatype,
			String data, String transaction_code) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record>" + data + "</record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context, transaction_code,body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	
	public static String SendRegister(Context context, String username,
			String passwd,String showname,String email,String machtype, String machver) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record>" +
				      "<username>" + username+ "</username>" +
				      "<passwd>" + passwd+ "</passwd>" +
				      "<machtype>" + machtype+ "</machtype>" +
				      "<machver>" + machver+ "</machver>" +
				      "<useremail>" + email+ "</useremail>" +
				      "<showname>" + showname+ "</showname></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"100011",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);

		return sb.toString();
	}
	
	public static String SendLogin(Context context,String username,String passwd,String showname) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><passwd>" + passwd+ "</passwd>" +
				"<showname>" + showname+ "</showname></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"100012",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	
	public static String SendSearch(Context context,String username,String sid,int pages) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><searchid>" + sid
				+ "</searchid><targetPage>" + pages
				+ "</targetPage></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"20007",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendGetFList(Context context,String username,int pages) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><targetPage>" + pages
				+ "</targetPage></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"20008",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendFriendCom(Context context,String username,String fname,String code) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><friendName>" + fname
				+ "</friendName></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,code,body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}

	public static String SendMark(Context context,String username,String fname,String markname) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><friendName>" + fname
				+ "</friendName><alias>" + markname
				+ "</alias></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"20011",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendGetAddMsg(Context context,String username,String code) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,code,body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendRank(Context context,String code,String username,String beginDate,String endDate) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><startTime>" + beginDate
				+ "</startTime><endTime>" + endDate
				+ "</endTime></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,code,body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendRankpage(Context context,String code,String username,String beginDate,String endDate,int pages) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><startTime>" + beginDate
				+ "</startTime><endTime>" + endDate
				+ "</endTime><targetPage>" + pages
				+ "</targetPage></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,code,body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}

	public static String SendSportData(Context context,String name,ArrayList<entity_sendrecord> send_list,int start,int end ) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbdata = new StringBuffer();
		String body="";
		sbdata.append("<body>");
		sbdata.append("<record>");
		sbdata.append("<username>" + name+ "</username>");
		sbdata.append("<sportLogs>");
		for(int i=start;i<end;i++)
		 {   	 			
			 sbdata.append("<sportLog>");
			 sbdata.append("<startTime>" + send_list.get(i).getStarttime()+ "</startTime>");
			 sbdata.append("<endTime>" + send_list.get(i).getEndtime()+ "</endTime>");
			 sbdata.append("<sportTypeCode>" +send_list.get(i).getSporttype()+ "</sportTypeCode>");
			 sbdata.append("<distance>" +send_list.get(i).getDistance()+ "</distance>");
			 sbdata.append("<duration>" +send_list.get(i).getDurationtime()+ "</duration>");
			 sbdata.append("<calorie>" +send_list.get(i).getCalorie()+ "</calorie>");
			 sbdata.append("<mode>" +send_list.get(i).getMode()+ "</mode>");
			 sbdata.append("<avgspeed>" +send_list.get(i).getAvgspeed()+ "</avgspeed>");
			 sbdata.append("<pace>" +send_list.get(i).getPace()+ "</pace>");
			 sbdata.append("<locations>" +send_list.get(i).getmsg()+ "</locations>");
			 sbdata.append("</sportLog>");
		 }
		sbdata.append("</sportLogs>");
		sbdata.append("</record>");
		sbdata.append("</body>");
		body=sbdata.toString();
		body=Socket_Datahead(context,"30006",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendMedalData(Context context,String name,List<entity_sportachieve> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbdata = new StringBuffer();
		String body="";
		sbdata.append("<body>");
		sbdata.append("<record>");
		sbdata.append("<username>" + name+ "</username>");
		sbdata.append("<medals>");
		for(int i=0;i<list.size();i++)
		 {   	 			
			 sbdata.append("<medal>");
			 sbdata.append("<generateTime>"+list.get(i).getStarttime()+"</generateTime>");
			 sbdata.append("<medalTypeCode>"+list.get(i).getAchievetype()+"</medalTypeCode>");
			 sbdata.append("<medalCode>"+list.get(i).getAchievename()+"</medalCode>");
			 sbdata.append("<time>"+list.get(i).getAchieverecord()+"</time>");
			 sbdata.append("</medal>");
		 }
		sbdata.append("</medals>");
		sbdata.append("</record>");
		sbdata.append("</body>");
		body=sbdata.toString();
		body=Socket_Datahead(context,"30007",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendLoopData(Context context,String name,List<entity_sportloop> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbdata = new StringBuffer();
		String body="";
		sbdata.append("<body>");
		sbdata.append("<record>");
		sbdata.append("<username>" + name+ "</username>");
		for(int i=0;i<list.size();i++)
		 {   	 			
			 sbdata.append("<loops>");
			 sbdata.append("<startTime>"+list.get(i).getStarttime()+"</startTime>");
			 sbdata.append("<pace>"+list.get(i).getpace()+"</pace>");
			 sbdata.append("<distance>"+list.get(i).getDistance()+"</distance>");
			 sbdata.append("<speed>"+list.get(i).getSpeed()+"</speed>");
			 sbdata.append("</loops>");
		 }
		sbdata.append("</record>");
		sbdata.append("</body>");
		body=sbdata.toString();
		body=Socket_Datahead(context,"30010",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendHeatData(Context context,String name,List<entity_loopheat> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbdata = new StringBuffer();
		String body="";
		sbdata.append("<body>");
		sbdata.append("<record>");
		sbdata.append("<username>" + name+ "</username>");
		for(int i=0;i<list.size();i++)
		 {   	 			
			 sbdata.append("<value>");
			 sbdata.append("<startTime>"+list.get(i).getStarttime()+"</startTime>");
			 sbdata.append("<heart>"+list.get(i).getheat()+"</heart>");
			 sbdata.append("</value>");
		 }
		sbdata.append("</record>");
		sbdata.append("</body>");
		body=sbdata.toString();
		body=Socket_Datahead(context,"30011",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendIwanMacData(Context context,String name,entity_mac mac) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbdata = new StringBuffer();
		String body="";
		sbdata.append("<body>");
		sbdata.append("<record>");
		sbdata.append("<username>" + name + "</username>");
		sbdata.append("<mac>" + mac.getMac() + "</mac>");
		sbdata.append("</record>");
		sbdata.append("</body>");
		body=sbdata.toString();
		body=Socket_Datahead(context,"40008",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}
	public static String SendFindPassword(Context context,String username,String email) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><username>" + username
				+ "</username><useremail>" + email
				+ "</useremail></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"30014",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);
		return sb.toString();
	}

	public static String getTime() {
		Date date = new Date();
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = df1.format(date);
		return time;
	}
	public static String SendSizeForm(String a) {
        int n=0;
        n=a.length();
        switch(n)
		{
			case 2:
				a="000000"+a;   				
			    break;
			case 3:
				a="00000"+a;
				break;
            case 4:
            	a="0000"+a;
				break;
            case 5:
            	a="000"+a;
				break;
            case 6:
            	a="00"+a;
				break;
            case 7:
            	a="0"+a;
				break;
			default:
				break;
		} 
		return a;
	}
	public static String SendText(Context context) {
		StringBuffer sb = new StringBuffer();
		StringBuffer sbbody= new StringBuffer();
		String body="";
		sbbody.append("<body><record><softname>lift</softname><version>" + 2012080
					+ "</version></record></body>");
		body=sbbody.toString();
		body=Socket_Datahead(context,"30002",body);
		int a = body.length()+HEAD_LENGTH.length()+8;
		sb.append(HEAD_LENGTH +SendSizeForm(a+"")+"\r\n");
		sb.append(body);

		return sb.toString();
	}
}
