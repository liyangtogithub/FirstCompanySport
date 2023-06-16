package com.desay.sport.friend;

import org.json.JSONException;

import com.desay.pstest.toast.showToast;
import com.desay.sport.main.R;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeName_Dialog {
	private AlertDialog dlg;
	private Context context;
	private String inputname="";
	private EditText edittext;
	private Handler HandlerMessage;
	private String markname="";
	public ChangeName_Dialog(Context context,Handler HandlerMessage,String markname){
		this.context=context;
		this.HandlerMessage=HandlerMessage;
		this.markname=markname;
	}
	
	public void EditDialog(){		
		LayoutInflater factory=LayoutInflater.from(context);
        final View DialogView=factory.inflate(R.layout.dialog_edit, null);    
        dlg=new AlertDialog.Builder(context)
        .setView(DialogView)
        .create(); 
        dlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        edittext =(EditText)DialogView.findViewById(R.id.d_changename);
        Button button1 =(Button)DialogView.findViewById(R.id.d_sureb);
        Button button2 =(Button)DialogView.findViewById(R.id.d_cancleb);
        edittext.setText(markname);

        button1.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			inputname = edittext.getText().toString().trim();
//    			if(inputname.equals(""))
//    				showToast.normaltoast(context, context.getString(R.string.inputisnull),showToast.ONE_SECOND);
//    			else{
    			    setinputname(inputname);
    		    	Message msg = new Message();
    		    	msg.what = 6;
  	    	        HandlerMessage.sendMessage(msg);
  	    	        dlg.dismiss();
  				    dlg.cancel();
//    			}
    		}
        });
        button2.setOnClickListener(new OnClickListener()
    	{
    		public void onClick(View v)
    		{
    			closedialog();
    		}
        });
        dlg.show();	      
	}
	public void setinputname(String inputname)
	{
		this.inputname=inputname;
	}
	public String getinputname(){
		return inputname;
	}
	public void closedialog()
	{
		if(dlg!=null)
		{
			dlg.dismiss();
			dlg.cancel();
		}
		
	}

}
