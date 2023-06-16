package com.desay.sport.loop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.UUID;

import com.desay.sport.data.PublicUtils;
import com.desay.sport.data.SportData;
import com.desay.sport.main.MainTab;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class BluetoothService extends Service{
    	public  int mState;
    	public static int infoCode;
    	public static boolean mbConectOk=false;
    	public static BluetoothSocket mbsSocket=null;
    	public static InputStream misIn=null;
    	public static OutputStream mosOut=null;
    	public static String address = null;
    	public StringBuffer msbReadBuf =new StringBuffer();;  
		public static final int STATE_CONNECTING = 2; 
	    public static final int STATE_CONNECTED = 3;  
	    public static final int STATE_NOTCONNECTED=4;
	    public static Context mContext;
	    private static int timetick;
	    private static boolean If_Receive_OK;
	    private static int repeattimes;
	    private MyBinder myBinder=new MyBinder();
	    StringBuffer sb = new StringBuffer();
	    private OutDataThread outdatathread = null;
	    @Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			return myBinder;
		}

	    //设置连接状态
	    private synchronized void setState(int state) {
	    	System.out.println("setState..="+state);
	        mState = state;
	        Intent intent=new Intent("com.desay.sleep");
	        intent.putExtra("address", address);
	        intent.putExtra("type", "state");
	        intent.putExtra("message", state);
	        mContext.sendBroadcast(intent);
	    }
	    
	    @SuppressLint("NewApi") protected boolean createConnect(){
   	      if (address == null)
          {
   	    	System.out.println("address == null");
   	    	return false;
          }
	    	 if(mbConectOk){
	 	    	try {
	 	    		  misIn.close();
	 	    	      mosOut.close();
	 	    	      mbsSocket.close();

	 			} catch (IOException localIOException2) {
	 				// TODO: handle exception
//	 				while(true){
	 					misIn=null;
	 					mosOut=null;
	 					mbsSocket=null;
	 					mbConectOk=false;
//	 				}
	 			}}

//	 				while(true){			
	 					try {
	 						   int i = Build.VERSION.SDK_INT;
	 						   System.out.println("connect to:"+address);
	 						   if(address==null)return false;
	 				           BluetoothDevice localBluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
	 				           UUID localUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	 				           if (i >= 10){
	 				        	 mbsSocket = localBluetoothDevice.createInsecureRfcommSocketToServiceRecord(localUUID);
	 				           }else{
	 				        	 mbsSocket = localBluetoothDevice.createRfcommSocketToServiceRecord(localUUID);
	 				           }	 				      	 				         
	 				           System.out.println("connecting..");
	 				           setState(STATE_CONNECTING);
	 				           mbsSocket.connect();
	 				           setState(STATE_CONNECTED);
	 				           System.out.println("connected..");
	 				           mosOut = mbsSocket.getOutputStream();
	 				           misIn = mbsSocket.getInputStream();	
	 				           mbConectOk=true;
	 				           return true;
	 				          }catch (IOException localIOException1) {
	 				        	 System.out.println("exception=="+localIOException1+"repeattimes = "+repeattimes);
	 				        	 System.out.println("mbsSocket ======="+mbsSocket); 
	 				        	 if(mbsSocket == null || repeattimes++ == 10)
	 				        	 {
		 				        	 setState(STATE_NOTCONNECTED); 
		 				        	 repeattimes = 0;
			 						 address=null;
			 						 mbConectOk=false;
//			 						 break;
	 				        	 }
	 				        	 else
	 				        	 {
		 				        	 try {
		 				        		    System.out.println("bc_startConnect"); 
											Thread.sleep(1000);		
											bc_startConnect();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
	 				        	 }
	 					}
//	 				}
	 				return false;
	    }
	    
	    @Override
	    public void onStart(Intent intent, int startId) {
	    // TODO Auto-generated method stub
		    if(intent!=null)
		    {
			    mContext=this;	
			    System.out.println("start address:"+intent.getStringExtra("address")); 
			    address = intent.getStringExtra("address");
//			    new OutDataThread().execute(new String[0]);
			    if (outdatathread != null && outdatathread.isAlive() )
				{
			    	try
					{						
			    		outdatathread.interrupt();
			    		outdatathread = null;
					}
					catch (Exception e)
					{
						 System.out.println("stop  Thead "); 
					}
				}
			    outdatathread = new OutDataThread();
			    outdatathread.start();
		    }
		    else
		    {
		    	System.out.println("intent is null");
		    }
	   }

		private void bc_startConnect()
		{
			Intent scanIntent = new Intent();
			scanIntent.setAction(MainTab.START_CONN);
			sendBroadcast(scanIntent);
		}
	    
	class OutDataThread extends Thread
	{
		// private final int CONNECT_FAIL = -1;
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			System.out.println("data background..");
			String tempbf = "";
			BufferedReader in = null;
			if (createConnect())
			{
				in = new BufferedReader(new InputStreamReader(misIn));
				try
				{
					while ((tempbf = in.readLine()) != null)
					{
						System.out.println("read line=" + tempbf);
						if (!tempbf.equals("") && !tempbf.equals("O")
								&& !tempbf.equals("OK")
								&& (tempbf.indexOf("+SOS") == -1))
						{
							resetThread_TimeOut();
							Intent intent = new Intent("com.desay.sleep");
							intent.putExtra("type", "read");
							intent.putExtra("message", tempbf);
							System.out.println("read :" + tempbf);
							sendBroadcast(intent);
						}
					}
				}
				catch (IOException e)
				{
					System.out.println("read exception:" + e);
				}
			}
			// return CONNECT_FAIL;
			super.run();
		}

	}
		
	    @Override
	    public boolean onUnbind(Intent intent) {
	    // TODO Auto-generated method stub
	    	 System.out.println("---------onUnbind---------");
	    	 if (mbConectOk)mbConectOk = false;
	    	    try
	    	    {    	       
	    	       if(mbsSocket!=null)
	    	       {
	    	    	   mbsSocket.close();
	    	    	   mbsSocket = null;
	    	       }
	    	       if(misIn!=null)
	    	       {
	    	    	   misIn.close();
	    	    	   misIn = null;
	    	       }
	    	       if(mosOut!=null)
	    	       {
	    	    	   mosOut.close();
	    	    	   mosOut = null;
	    	       }
	    	       
	    	       return true;
	    	    }
	    	    catch (IOException localIOException)
	    	    {
	    	       misIn = null;
	    	       mosOut = null;
	    	       mbsSocket = null;
	    	    }
	    return super.onUnbind(intent);
	    }

		@Override
		public void onDestroy()
		{
			super.onDestroy();
			if (outdatathread != null)
			{
				outdatathread = null;
			}
		}
		
		public class MyBinder extends Binder
		{
			public void write(String test)
			{
				System.out.println("write=" + test + ";mbConectOk=" + mbConectOk);
				if (mbConectOk)
				{
					try
					{
						if ("T+USERBOND".equals(test)|| test.startsWith("T+HIS_DATA")) // 若为确认指令，则超时处理延长为15秒。
						SportData.MAX_TIMETICKS = 20;
						else
							SportData.MAX_TIMETICKS = 5;
						startThread_TimeOut(test);
						// mosOut.write((test+"\r\n").getBytes());
						mosOut.write((test).getBytes());
						Log.d("heimi", "write:" + test);
					}
					catch (IOException e)
					{
					}
				}
			}
			
		    public void creatData()
			{
				String name = "T+HIS_DATA";
				write(name);
				infoCode = 100;
			}
			
			public void setTime()
			{
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int date = calendar.get(Calendar.DAY_OF_MONTH);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				String name = "T+DATETIME="+year+
						      PublicUtils.getDoubleDigitString(month+1)+
						      PublicUtils.getDoubleDigitString(date)+
						      PublicUtils.getDoubleDigitString(hour)+
							  PublicUtils.getDoubleDigitString(minute);
				write(name);
				infoCode = 100;
			}
			
//			public void start()
//			{
//				String name = "T";
//				write(name);
//				infoCode = 0;
//			}
			public void readName()
			{
				String name = "T+NAME";
				write(name);
				infoCode = 1;
			}

			public void readPwd()
			{
				String pwd = "T+PWD";
				write(pwd);
				infoCode = 2;
			}

		/*	public void readSex()
			{
				String sex = "T+SEX";
				write(sex);
				infoCode = 3;
			}

			public void readAge()
			{
				String age = "AT_YO";
				write(age);
				infoCode = 4;
			}

			public void readHeight()
			{
				String height = "T+HEIGHT";
				write(height);
				infoCode = 5;
			}

			public void readWeight()
			{
				String weight = "T+WEIGHT";
				write(weight);
				infoCode = 6;
			}
	*/
			public void readMac()
			{
				String mac = "T+MAC";
				write(mac);
				infoCode = 7;
			}

			public void readModel()
			{
				String model = "T+MODEL";
				write(model);
				infoCode = 8;
			}

			public void readSN()
			{
				String sn = "T+SN";
				write(sn);
				infoCode = 9;
			}

			public void readHW()
			{
				String hw = "T+HW";
				write(hw);
				infoCode = 10;
			}

			public void readFW()
			{
				String fw = "T+FW";
				write(fw);
				infoCode = 11;
			}

			public void readSW()
			{
				String sw = "T+SW";
				write(sw);
				infoCode = 12;
			}

			public void readMFN()
			{
				String mfn = "T+MFN";
				write(mfn);
				infoCode = 13;
			}

			public void readBatt()
			{
				String batt = "T+POWER";
				write(batt);
				infoCode = 14;
			}

			public void readTime()
			{
				String date = "T+DATETIME";
				write(date);
				infoCode = 15;
			}

			public void readSos()
			{
				String sos = "T+SOS";
				write(sos);
				infoCode = 16;
			}

			public void readT_model()
			{
				String t_model = "T+T_MODEL";
				write(t_model);
				infoCode = 17;
			}

			public void readS_model()
			{
				String s_model = "T+S_MODEL";
				write(s_model);
				infoCode = 18;
			}

			public void readH_model()
			{
				String h_model = "T+H_MODEL";
				write(h_model);
				infoCode = 19;
			}

			public void readAC()
			{
				String rl = "T+HR_RL";
				write(rl);
				infoCode = 20;
			}

			public void readDC()
			{
				String il = "T+HR_IL";
				write(il);
				infoCode = 21;
			}

			public void readHeartRate()
			{
				String rate = "T+HEARTRATE";
				write(rate);
				infoCode = 22;
			}

			public void readHRO2()
			{
				String o2 = "T+HRO2";
				write(o2);
				infoCode = 23;
			}

			public void readTemp()
			{
				String temp = "T+TEMPERATURE";
				write(temp);
				infoCode = 24;
			}

			public void readG_sensor()
			{
				String sensor = "T+GSENSOR";
				write(sensor);
				infoCode = 25;
			}

			public void readSportHistory()
			{
				String sport = "T+HIS_DATA=\"00\"";
				write(sport);
				infoCode = 26;
			}
			public void deleteSportHistory()
			{
				String sport = "T+HIS_DATA=\"01\"";
				write(sport);
				infoCode = 26;
			}
			public void readtempHistory()
			{
				String temp = "T+TPR_HIS";
				write(temp);
				infoCode = 27;
			}

			public void readRateHistory()
			{
				String rate = "T+HIS_DATA=\"10\"";
				write(rate);
				infoCode = 28;
			}
			public void deleteRateHistory()
			{
				String rate = "T+HIS_DATA=\"11\"";
				write(rate);
				infoCode = 28;
			}

			// 写at指令
			public void writeName(String name)
			{
				String m_name = "T+NAME=\"" + name + "\"";
				write(m_name);
				infoCode = 50;
			}
			
			public void writeModel(String model){
				String m_model="T+MODEL=\""+model+"\"";
				write(m_model);
				infoCode=67;
			}

			public void writePwd(String pwd)
			{
				String m_pwd = "T+PWD=\"" + pwd + "\"";
				write(m_pwd);
				infoCode = 51;
			}

			public void writeSex(int sex)
			{
				String m_sex = "T+SEX=\"" + sex + "\"";
				write(m_sex);
				infoCode = 52;
			}

			public void writeAge(int age)
			{
				String m_age = "T+YO=\"" + age + "\"";
				write(m_age);
				infoCode = 53;
			}

			public void writeHeight(int height)
			{
				String m_height = "T+HEIGHT=\"" + height + "\"";
				write(m_height);
				infoCode = 54;
			}

			public void writeWeight(int weight)
			{
				String m_weight = "T+WEIGHT=\"" + weight + "\"";
				write(m_weight);
				infoCode = 55;
			}

			// 写鉴权
			public void writeAuth(String auth)
			{
				String m_auth = "T+AUTH=\"" + auth + "\"";
				write(m_auth);
				infoCode = 56;
			}
			// 查是否手环正运行或休眠
			public void checkLoopState()
			{
				String m_auth = "T+AUTH";
				write(m_auth);
				infoCode = 66;
			}

			public void writeDate(String date)
			{
				String m_date = "T+DATETIME=\"" + date + "\"";
				write(m_date);
				infoCode = 57;
			}

			public void writeMotor(int motor)
			{
				String m_motor = "T+MOTOR=\"" + motor + "\"";
				write(m_motor);
				infoCode = 58;
			}
			public void writeConfirm(){
				String m_confirm="T+USERBOND";
				write(m_confirm);
				infoCode=3;
			}

			public void writeLed(int led)
			{
				String m_led = "T+LED=\"" + led + "\"";
				write(m_led);
				infoCode = 59;
			}

			public void writeT_Model(String T_model)
			{
				String m_T_Model = "T+T_MODEL=\"" + T_model + "\"";
				write(m_T_Model);
				infoCode = 60;
			}

			public void writeS_Model(String S_model)
			{
				String m_S_Model = "T+S_MODEL=\"" + S_model + "\"";
				write(m_S_Model);
				infoCode = 61;
			}

			public void writeH_Model(String H_model)
			{
				String m_H_Model = "T+H_MODEL=\"" + H_model + "\"";
				write(m_H_Model);
				infoCode = 62;
			}

			public void writeT_His(int num)
			{
				String T_His = "T+TPR_HIS=\"" + num + "\"";
				write(T_His);
				infoCode = 63;
			}

			public void writeS_His(int num)
			{
				String S_His = "T+SPT_HIS=\"" + num + "\"";
				write(S_His);
				infoCode = 64;
			}

			public void writeH_His(int num)
			{
				String H_His = "T+HR_HIS=\"" + num + "\"";
				write(H_His);
				infoCode = 65;
			}
		}
	    
	    class IfReceiveOK implements Runnable
	    {
	        String order = null;
	    	public IfReceiveOK(String order)
	    	{
	    		this.order = order;
	    	}
	    	
	    	@Override
	    	public void run() {
	    		// TODO Auto-generated method stub
	    		while(true)
	    		{	
	    			try {
	    				System.out.println("IfReceiveOK:timetick="+timetick+";If_Receive_OK="+If_Receive_OK);
	    				timetick++;
	    				Thread.sleep(500);
	    				if(timetick> SportData.MAX_TIMETICKS && If_Receive_OK !=true )
	    				{
	    					if(repeattimes < SportData.MAX_REPEATTIMES)
	    					{
	    						System.out.println("repeattimes < MAX_REPEATTIMES");
		    					myBinder.write(order);
		    					repeattimes ++;
	    					}
	    					else
	    					{
	    						System.out.println("repeattimes = MAX_REPEATTIMES");
	    						resetThread_TimeOut();
	    						setState(STATE_NOTCONNECTED);
	    					}
	                        break;
	    				}
	    				if(If_Receive_OK)break;
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    		}
	    	}

	    };
	   
	    private void startThread_TimeOut(String order)
	    {
			If_Receive_OK = false;
			timetick = 0;
			new Thread(new IfReceiveOK(order)).start();
	    }
	    
	    public static void resetThread_TimeOut()
	    {
	    	If_Receive_OK = true;//保证整个发送接收过程结束
	    	repeattimes = 0;
	    	timetick = 0;
	    }
	}		
