package com.desay.sport.friend;


import com.desay.sport.main.R;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ListBottomLayout {
	private OnClickListener mClickListener;	
	private LinearLayout tab3updata;
	private ProgressBar tab3progressBar ;
	private Button tab3button;
	private TextView tab3noList,uptext;
	private View view;
	private Handler handler;
	private boolean isshow=false;
	public ListBottomLayout(Context context,Handler handler) {
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view=inflater.inflate(R.layout.listbuttom,null);
		setView(view);
		oninitInflate();
		this.handler=handler;

	}
	public void oninitInflate() {
		tab3updata = (LinearLayout)view.findViewById(R.id.tab3_updata);
		tab3progressBar = (ProgressBar)view.findViewById(R.id.tab3_progressBar);
		tab3button = (Button)view.findViewById(R.id.tab3_button);
		tab3noList=(TextView)view.findViewById(R.id.tab3_no_List);
		uptext=(TextView)view.findViewById(R.id.tab3_uptext);
		tab3updata.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		if(!tab3progressBar.isShown())
 	    		{
 	    			Message msg = new Message();
 	    		    msg.what = 12;
 	    		    handler.sendMessage(msg);	
 	    		}
 	    	}
 	    });
		tab3button.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		Message msg = new Message();
 	    		msg.what = 8;
 	    		handler.sendMessage(msg);	    		
 	    	}
 	    });
	}
	
	public void setView(View view)
	{
		this.view=view;
	}
	public View getview()
	{
		return view;	
	}
	public void showbuttom()
	{
		tab3button.setVisibility(View.VISIBLE);
		tab3noList.setVisibility(View.VISIBLE);
	}
	public void hitbuttom()
	{
		tab3button.setVisibility(View.GONE);
		tab3noList.setVisibility(View.GONE);
	}
	public void openprogress()
	{
		tab3progressBar.setVisibility(View.VISIBLE);
		isshow=true;
	}
	public void closeprogress()
	{
		tab3progressBar.setVisibility(View.GONE);
		isshow=false;
	}
	public boolean isupdata()
	{
		return isshow;
	}
	public void SetUpText(String ss)
	{
		uptext.setText(ss);
	}
}
