package com.desay.sport.friend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_loopheat;
import com.desay.sport.db.entity_mac;
import com.desay.sport.db.entity_sendrecord;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.entity_sportloop;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;



public class SendServer extends Service {

	private  MyHandler mHandler = new MyHandler();
	private String loadname="";
	private int ss=0;
	private int N=0;
	private int end=0;
	private sportDB db = null;
	private ConnectionMAS CMAS=null;
	private String senddata="";
	private ArrayList<entity_sendrecord> send_list;
	private GetTime gt;
	private int intentmcmd=1;
	private long starttime_end;
	private long starttime_begin;
	private boolean openprogress=false;
	private Calendar cal = Calendar.getInstance();
	private int year = cal.get(Calendar.YEAR);
	private int month = cal.get(Calendar.MONTH);
	private boolean sendsync=false;
	public IBinder onBind(Intent intent) {
		return null;
	}
	public void onCreate() { 
		super.onCreate(); 
		Log.e("SendServer", "onCreate()");
	}  
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);			
		Log.e("SendServer", "===onStart====");
		db = new sportDB(SendServer.this);
		gt=new GetTime();
		loadname=SportData.getUserName(SendServer.this);
		openprogress=false;
		sendsync=false;
	    Message msg = new Message();
	    try{
	    	intentmcmd=intent.getIntExtra("intentmcmd",1);
	    	year=intent.getIntExtra("year",year);
	    	month=intent.getIntExtra("month",month);
	    }catch(Exception e)
	    {
	    	intentmcmd=1;
	    }
	   	msg.what = intentmcmd;
	   	mHandler.sendMessage(msg);
	}

	public void onDestroy() {	
		Log.e("SendServer", "============onDestroy=======================");		
		closenet();
		db.closeDB();
		super.onDestroy();
	}

    private class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what)
    		{   			
    		case 1:
    			//
    			if(!loadname.equals(SportData.DEFAULTUSERNAME))
        		{
    			   send_list=db.GetNoSendRecord(loadname,0,0);
    			   ss=send_list.size();
    			  if(getpersonmsg()&&getmedalmsg())
	        	  {
    				  Message msg1 = new Message();
    		  	      msg1.what = 6;
    	  	          mHandler.sendMessage(msg1);
	        	   }
        		}
	        	break;
    		case 3:
    			closenet();
    			entity_userinfo info = db.GetUserInfo(SendServer.this);
    		    info.setUpLoad(SportData.UPLOAD);
    		    db.SaveUserInfo(SendServer.this,info);
    		    if(getmedalmsg())
    		    { 
    		    	Message msg1 = new Message();
 		  	        msg1.what = 6;
 	  	            mHandler.sendMessage(msg1);
    		    }
    			break;
    		case 4:
    			closenet();
    			if(openprogress)
    			{
    				Intent sendintentF = new Intent(SportData.RECEIVER_LOAD);
   			        sendintentF.putExtra(SportData.LOAD,SportData.LOADSTATUS_SYNCFAIL);
   		            sendBroadcast(sendintentF);
   		            openprogress=false;
    			}
    			break;
    		case 5:
    			handler.postDelayed(updateThread ,50000);
    			break;
    		case 6: 
    			//发送数据库未上传运动数据
//    			Log.e("SendServer", "=====ss=6=========="+ss);
    			if(ss>0)
 			    {
    			  N=0;
 				  if(ss>30) 
 					 end=30;
 				  else
 					 end=ss;
    			  senddata=Socket_Data.SendSportData(SendServer.this,loadname,send_list,N,end);
         	 	  CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,7,4);
     			  CMAS.start();
 				}else if(getiwanmac()&&getloopheat())
    			{
 					if(sendsync)
 	    			{
 	    				Message msg6 = new Message();
 	 		  	        msg6.what = 13;
 	 	  	            mHandler.sendMessage(msg6);
 	    			}else
 	    				SendServer.this.stopSelf();
 			    }
	        	break;
    		case 7:
    			//更新数据库运动数据状态
    			closenet();
    			db.UPSendRecordStatus(loadname,send_list,N,end);
    			N=end;
    			if(ss-end>0)
    			{ 
    				if(ss-end>30) 
     					end=30+N;
     				 else
     					end=ss;
    				senddata=Socket_Data.SendSportData(SendServer.this,loadname,send_list,N,end);
             	 	CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,7,4);
         			CMAS.start();
    			}else if(getiwanmac()&&getloopheat())
    			{
 					if(sendsync)
 	    			{
 	    				Message msg7 = new Message();
 	 		  	        msg7.what = 13;
 	 	  	            mHandler.sendMessage(msg7);
 	    			}else
 	    				SendServer.this.stopSelf();
 			    }
    			break;
    		case 8:
    			closenet();
    			db.RefreshUploadStatus_SportAchieve(loadname,SportData.TYPE_MEDAL);
    			Message msg1 = new Message();
		  	    msg1.what = 6;
	  	        mHandler.sendMessage(msg1);
    			break;
    		case 9:
    			 closenet();
    			 new AnalyzeUtils(SendServer.this).GetRecord(msg.getData().getString("body"),loadname,starttime_begin,starttime_end);
    			 senddata=Socket_Data.SendGetAddMsg(SendServer.this,loadname,"40007");
 				 CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,16,4);
 				 CMAS.start();
    			break;
    		case 10:
    			//获取运动数据
    			Calendar cc=gt.getRecord_Calendar(year,month);
    			starttime_begin=gt.getRecord_startMillis(cc);
    			starttime_end =gt.getRecord_endMillis(cc);
    			senddata=Socket_Data.SendRank(SendServer.this,"20006",loadname,gt.millisToDateform(starttime_begin),gt.millisToDateform(starttime_end));
				CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,9,4);
				CMAS.start();
    			break;
    		case 11:
    			sendsync=true;
    			openprogress=true;
    			Message msg11 = new Message();
		  	    msg11.what = 1;
	  	        mHandler.sendMessage(msg11);
			    break;
    		case 12:
    			new AnalyzeUtils(SendServer.this).GetMedals(msg.getData().getString("body"),loadname);
    			Message msg10 = new Message();
		  	    msg10.what = 10;
	  	        mHandler.sendMessage(msg10);
    			break;
    		case 13:
    			//获取奖章数据
    			sendsync=false;
    			senddata=Socket_Data.SendGetAddMsg(SendServer.this,loadname,"30009");
			    CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,12,4);
			    CMAS.start();
    			break;
    		case 14:
    			//上传手环运动成功
    			closenet();
    			db.refreshIwanMac(loadname);
    			if(getloopheat())
    			{
    				if(sendsync)
        			{
        				Message msg14 = new Message();
     		  	        msg14.what = 13;
     	  	            mHandler.sendMessage(msg14);
        			}else
        				SendServer.this.stopSelf();
    			}
    			break;
    		case 15:
    			//上传手环心率成功
    			closenet();
    			db.refreshSendHeat(loadname);
    			if(sendsync)
    			{
    				Message msg7 = new Message();
 		  	        msg7.what = 13;
 	  	            mHandler.sendMessage(msg7);
    			}else
    				SendServer.this.stopSelf();
    			break;
    		case 16:
    			//获取手环运动数据
    			 closenet();
    			 new AnalyzeUtils(SendServer.this).GetIwanMac(msg.getData().getString("body"),loadname);
    			 senddata=Socket_Data.SendRank(SendServer.this,"30013",loadname,gt.millisToDateform(starttime_begin),gt.millisToDateform(starttime_end));
 				 CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,17,4);
 				 CMAS.start();
    			break;
    		case 17:
    			//获取心率运动数据
    			 closenet();
    			 new AnalyzeUtils(SendServer.this).GetHeatRecord(msg.getData().getString("body"),loadname,starttime_begin,starttime_end);
    			 Intent sendintent = new Intent(SportData.RECEIVER_LOAD);
   			     sendintent.putExtra(SportData.LOAD,SportData.LOADSTATUS_SYNC);
   		         sendBroadcast(sendintent);   		       
   		         SendServer.this.stopSelf();
   		         break;
			default:
				break;
    		}
		}
    }
    //查看个人信息上传状态
    private boolean getpersonmsg()
	{
    	entity_userinfo info = db.GetUserInfo(SendServer.this);
    	if(info!=null&&info.getUpLoad()!=null)
    	{
    		if(info.getUpLoad().equals(SportData.UNUPLOAD))
    		{
    		   int sex=1;
    			if(info.getSex().equals(getString(R.string.woman_str)))
    				sex=0;
    		   StringBuffer sb = new StringBuffer();
    		   sb.append("<username>" + loadname+ "</username>");
    		   sb.append("<sex>" + sex+ "</sex>");
    		   sb.append("<birthday>" + info.getBirthday()+ "</birthday>");
    		   sb.append("<height>" + info.getHeight()+ "</height>");
    		   sb.append("<weight>" + info.getWeight()+ "</weight>");
    		   sb.append("<city>" + info.getCity()+ "</city>");
    		   if(info.getHeadPhoto()==null)
    		      sb.append("<portrait>" + ""+ "</portrait>");
    		   else
    		      sb.append("<portrait>" + info.getHeadPhoto()+ "</portrait>");
    		   senddata=Socket_Data.SendInfo(SendServer.this,"10010", sb.toString(), "30008");
    		   CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,3,4);
			   CMAS.start();
			   return false;
    		}
    	}
		return true;
	}
    //查看奖章上传状态
    private boolean getmedalmsg()
	{
    	List<entity_sportachieve> achiece = db.GetSportAchieve(loadname,"1",SportData.UNUPLOAD);
    	if(achiece!=null&&achiece.size()>0)
    	{
    		senddata=Socket_Data.SendMedalData(SendServer.this,loadname,achiece);
     	 	CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,8,4);
 			CMAS.start();
			return false;
    	}
		return true;
	}
//    //查看手环运动上传状态
//    private boolean getloopsport()
//	{
//    	List<entity_sportloop> sportloop = db.getHandLoopRecord(loadname,0,0);
//    	if(sportloop!=null&&sportloop.size()>0)
//    	{
//    		senddata=Socket_Data.SendLoopData(SendServer.this,loadname,sportloop);
//     	 	CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,14,4);
// 			CMAS.start();
//			return false;
//    	}
//		return true;
//	}
    //查看心率上传状态
    private boolean getloopheat()
	{
    	List<entity_loopheat> heatloop = db. getLoopHeart(loadname);
    	if(heatloop!=null&&heatloop.size()>0)
    	{
    		senddata=Socket_Data.SendHeatData(SendServer.this,loadname,heatloop);
     	 	CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,15,4);
 			CMAS.start();
			return false;
    	}
		return true;
	}
    //查看MAC上传状态
    private boolean getiwanmac()
	{
    	entity_mac iwanmac = db.getIwanMac(loadname);
    	if(iwanmac.getUpLoad()!=null && iwanmac.getUpLoad().equals(SportData.UNUPLOAD))
    	{
    		senddata=Socket_Data.SendIwanMacData(SendServer.this,loadname,iwanmac);
     	 	CMAS=new ConnectionMAS(SendServer.this,mHandler,senddata,14,4);
 			CMAS.start();
			return false;
    	}
		return true;
	}
    Handler handler  = new Handler();  
	Runnable updateThread =  new Runnable(){  
	       public void run() { 
	    	   Message msg = new Message();
	    	   msg.what = 4;
	    	   mHandler.sendMessage(msg);
	  }  
   };
   private void closenet()
   {
	if(CMAS!=null)
	    CMAS.closeSocket();
	if(handler!=null&&updateThread!=null)
		handler.removeCallbacks(updateThread);
		CMAS=null;
   }
}
