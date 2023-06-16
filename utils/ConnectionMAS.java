package com.desay.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import org.apache.http.params.HttpConnectionParams;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectionMAS extends Thread{
	
	private Context context;
	private Handler handler;
	private Socket socket=null;
	private boolean openread;
	private String message;
	private BufferedReader in;
	private int n1=0,m2=0;
	protected static final String TAG = "ConnectionMAS";
	public String imei="123";
	public String transaction_code   =new String();
	public String response_code=new String();
	public String phonenumber  =new String();	
	public String body  =new String();
	public ConnectionMAS(Context context,Handler handler,String message,int n1,int m2)
	{
		this.context=context;
		this.handler=handler;
		this.message=message;
		this.n1=n1;
		this.m2=m2;
	}
	public void run() {
	      Boolean status = NetworkConnection.isConnectingToInternet(context);
	      if (!status){
	    	   Message msg = new Message();
	 		   msg.what = 2;	    
	 		   handler.sendMessage(msg); 
	    	  return;
	       }
	       Message msg = new Message();
		   msg.what = 5;	    
		   handler.sendMessage(msg); 
		   try {
//				String ip_p = "59.33.252.109";;
//				socket=new Socket(ip_p,2978);
				String ip_p = "121.15.245.100";
				java.net.InetAddress x;
				x = java.net.InetAddress.getByName("port.desay.com");
				ip_p = x.getHostAddress();
//				Log.d(TAG, "ip===" + ip_p);
				socket=new Socket(ip_p,6800);
				OutputStream os = socket.getOutputStream();
				os.write(message.getBytes("GBK"));
				os.flush();
				Log.d(TAG,"message==sendok" );
				openread=true;
				Thread mThread = new Thread(mRunnable);
				mThread.start();
			} catch (Exception e) {
				Log.d(TAG, "ioerror");
				closeSocket();
				e.printStackTrace();
			}
  }
	private Runnable mRunnable=new Runnable(){
		public void run()
		{
			while(openread)
			{
				try{
					String tempbf;
					 in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"gb2312"));
	    			 StringBuffer html = new StringBuffer();
	    			 while ((tempbf = in.readLine()) != null) {
	    				html.append(tempbf + "\n");	    				
	    			 }
			         if(html.length() > 0)
			          {	
	    			   body="";
					   analyzing(html.toString().trim());
					   Message msg=new Message();
					   Bundle bundle = new Bundle();
	    			   bundle.putString("body",body);
					   if(response_code.equals("001"))                       
	                        msg.what=n1;	                        	                        
		                else
					        msg.what =m2;
					   msg.setData(bundle);
					   handler.sendMessage(msg);			           
			           html.delete(0, html.length());
			           closeSocket();
			        }		   
				}catch(Exception E){
					E.printStackTrace();
					closeSocket();
				}
			}
		}
	};
	public void closeSocket()
	{		  
          try {
        	  openread=false;
        	  if(socket!=null)
			      socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}         
	}
	public void analyzing(String data) {
		
		String[] strarray = data.split("\n");
		for (int i = 0; i < strarray.length; i++) {
			if (strarray[i].startsWith(Socket_Data.HEAD_IMEI)) {
				imei = strarray[i].substring(
						strarray[i].indexOf(Socket_Data.HEAD_IMEI)
								+ Socket_Data.HEAD_IMEI.length()).trim();
			} else if (strarray[i].startsWith(Socket_Data.HEAD_PHONENUMBER)) {
				phonenumber = strarray[i].substring(
						strarray[i].indexOf(Socket_Data.HEAD_PHONENUMBER)
								+ Socket_Data.HEAD_PHONENUMBER.length()).trim();
			} else if (strarray[i]
					.startsWith(Socket_Data.HEAD_TRANSACTION_CODE)) {
				transaction_code = strarray[i].substring(
						strarray[i].indexOf(Socket_Data.HEAD_TRANSACTION_CODE)
								+ Socket_Data.HEAD_TRANSACTION_CODE.length())
						.trim();
			} else if (strarray[i].startsWith(Socket_Data.HEAD_RESPONSE_ERROR_CODE)) {
				response_code = strarray[i].substring(
						strarray[i].indexOf(Socket_Data.HEAD_RESPONSE_ERROR_CODE)
								+ Socket_Data.HEAD_RESPONSE_ERROR_CODE.length())
						.trim();
			} else if (strarray[i].startsWith(Socket_Data.BODY)) {
				body = data.substring(data.indexOf(Socket_Data.BODY)).trim();
				break;
			}
		}
		Log.d(TAG, "Response_Data.imei===" + imei);
		Log.d(TAG, "Response_Data.phonenumber===" + phonenumber);
		Log.d(TAG, "Response_Data.transaction_code==="
				+ transaction_code);
		Log.d(TAG, "Response_Data.response_code==="
				+ response_code);
//		Log.d(TAG, body.indexOf("</body>")+"Response_Data.body===" + body);
	}

}
