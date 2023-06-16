package com.desay.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class OPenProgressBar {
	private boolean openDialogP=false;
	private int m_count;
	private Context op_context;
	private Handler op_handler;
	private CustomProgressDialog progressDialog = null;  //������Ի���
	public OPenProgressBar(Context op_context,Handler op_handler)
	{
		this.op_context=op_context;
		this.op_handler=op_handler;
	}
	
	public void startProgressDialog(){  
	  if (progressDialog == null){  
	      progressDialog = CustomProgressDialog.createDialog(op_context);  
	  }     	         
	    progressDialog.show();	
		m_count = 0;
		openDialogP=true;
		new Thread()
		{ 		
			public void run()
			{				
				try{
					while(openDialogP)
					{
						Thread.sleep(1000);
						m_count++;
						if(m_count>20)
						{	
							openDialogP=false;
							Message msg = new Message();
			    		    msg.what = 6;
			    		    op_handler.sendMessage(msg);								
						}
					}															
				}catch(Exception e)
				{
					e.printStackTrace();
					 openDialogP=false;
					if(progressDialog!=null)
					 progressDialog.dismiss(); //60���ر�	
					
				}
			}
		}.start();		
	}
	public void stopProgressDialog(){ 
		 openDialogP=false;
		  if (progressDialog != null){  
	        progressDialog.dismiss();  
	        progressDialog = null;         
	      }
		 
	 }		
}
