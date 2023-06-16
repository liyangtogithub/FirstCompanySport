package com.desay.utils;


import com.desay.sport.main.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CustomProgressDialog extends Dialog{
	 private Context context = null; 
	  public static CustomProgressDialog customProgressDialog = null;  
	   
	  public CustomProgressDialog(Context context){  
	      super(context);  
	      this.context = context;  
	  }  

	    

	  public CustomProgressDialog(Context context, int theme) {  		 
	      super(context, theme);  
	      this.context = context; 
	  }  
	    
	  public static CustomProgressDialog createDialog(Context context){  
		  customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);  
	      customProgressDialog.setContentView(R.layout.progressbar_ui);  
	      customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;  
	      customProgressDialog.setCancelable(true);//�Ƿ���԰���ͬ����ȡ��
	      customProgressDialog.setCanceledOnTouchOutside(false);
	       return customProgressDialog;	
	      	  
	  } 
	  
	  public static CustomProgressDialog createDialog_Sync(Context context,int string_id){  
		  customProgressDialog = new CustomProgressDialog(context,R.style.CustomProgressDialog);  
		  final View layout = LayoutInflater.from(context).inflate(R.layout.progressbar_ui, null);
		  customProgressDialog.setContentView(layout); 
	      TextView id_tv_loadingmsg = (TextView)layout.findViewById(R.id.id_tv_loadingmsg);
	      id_tv_loadingmsg.setText(context.getString(string_id));
	      customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;  
	      customProgressDialog.setCancelable(true);//�Ƿ���԰���ͬ����ȡ��
	      customProgressDialog.setCanceledOnTouchOutside(false);
	       return customProgressDialog;	
	      	  
	  }
	 
	  public void onWindowFocusChanged(boolean hasFocus){  	        
	      if (customProgressDialog == null){  
	          return;  
	      }  	        
	  }  	 

	  public CustomProgressDialog setTitile(String strTitle){  

	      return customProgressDialog;  
	  }

}
