package com.desay.sport.friend;



import com.desay.sport.main.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class AskDialog {
	private AlertDialog dlg;
	private Context context;
	Handler HandlerMessage;
    private boolean checkon_off=true;
	public AskDialog(Context context,final Handler HandlerMessage){
		this.context=context;
		this.HandlerMessage=HandlerMessage;
	}
	
	public void OpenaskDialog(){		
		LayoutInflater factory=LayoutInflater.from(context);
        final View DialogView=factory.inflate(R.layout.asknet_dialog, null);   
        dlg=new AlertDialog.Builder(context)
        .setView(DialogView)
        .create(); 
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Button ok_button =(Button)DialogView.findViewById(R.id.adkd_button);
        CheckBox checkbox=(CheckBox)DialogView.findViewById(R.id.adkd_CBox);
        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean isChecked = ((CheckBox) v).isChecked();                                			
                if (isChecked)                         	
        			checkon_off=false;
                else
              	    checkon_off=true;             	  
            }
       });
        ok_button.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			SharedPreferences.Editor editor = context.getSharedPreferences("setnet_msg",0).edit();		
				editor.putBoolean("k_opendialog",checkon_off);
			    editor.commit(); 
    			Message msg = new Message();
	    		msg.what = 5;
	    		HandlerMessage.sendMessage(msg); 
	    		dlg.dismiss();
    		}
        });     
        dlg.show();      
	}

}
