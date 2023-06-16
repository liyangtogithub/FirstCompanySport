package com.desay.sport.friend;

import com.desay.sport.main.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class DialogToast {
	private AlertDialog dlg;
	private Context context;
	Handler HandlerMessage;
	public DialogToast(Context context,final Handler HandlerMessage){
		this.context=context;
		this.HandlerMessage=HandlerMessage;
	}
	
	public void OpenDialogToast(String name){		
		LayoutInflater factory=LayoutInflater.from(context);
        final View DialogView=factory.inflate(R.layout.update, null);   
        dlg=new AlertDialog.Builder(context)
        .setView(DialogView)
        .create(); 
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView title=(TextView)DialogView.findViewById(R.id.tv_title);
        Button ok_button =(Button)DialogView.findViewById(R.id.btn_update);
        Button no_button =(Button)DialogView.findViewById(R.id.btn_cancel);
        TextView textname=(TextView)DialogView.findViewById(R.id.tv_toast);
        title.setText(context.getString(R.string.delete_friend_str));
        textname.setText(context.getString(R.string.ifd_friend_str,name));
        ok_button.setText(context.getString(R.string.delete_str));
        ok_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                
            	Message msg = new Message();
  	    	    msg.what = 7;
  	    	    HandlerMessage.sendMessage(msg);
            	dlg.dismiss();
    			dlg.cancel();
            }
       });
        no_button.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			dlg.dismiss();
    			dlg.cancel();
    		}
        });     
        dlg.show();      
	}

}

