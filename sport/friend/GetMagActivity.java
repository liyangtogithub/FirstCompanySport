package com.desay.sport.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;

public class GetMagActivity extends Activity{

private static final String TAG = "AddFdActivity";
private List<Map<String,Object>> msgdata = new ArrayList<Map<String,Object>>();
private ImageView backimg;
private TextView tv_title;
private ListView msglist;
private String name="";
private String myname="";
private ConnectionMAS CMAS=null;
private MySimpleAdapter adapter;
private sportDB db;
private String sdata="";

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);	
	setContentView(R.layout.msg_main);
	init();
}

	protected void onResume() {
		// TODO Auto-generated method stub
	Log.d(TAG, "AddFdActivity_onResume");
		super.onResume();
	}
	public void onDestroy() {
		   closenet();
		   super.onDestroy();
	}
	 public void init()
	  {
		    backimg=(ImageView)findViewById(R.id.iv_return);
			msglist= (ListView)findViewById(R.id.msg_List);
			tv_title = (TextView) findViewById(R.id.tv_title);
			tv_title.setText(getString(R.string.message_str));
			backimg.setOnClickListener(new OnClickListener()
	 	    {
	 	    	public void onClick(View v)
	 	    	{		
	 	    		GetMagActivity.this.finish();
	 	    	}
	 	    });
//			msglist.setOnItemClickListener(new OnItemClickListener() 
//			 { 			 
//			    public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
//			    {  
//			      //跳转到详细信息界面
//			    	name=(String) ((TextView)arg1.findViewById(R.id.msgl_text1)).getText();//��ȡ������Ʒ�����
//			    	 Message msg = new Message();
//	 		    	 msg.what = 1;
//	 		    	 Handlermsg.sendMessage(msg);
//			    }   
//	        });
/*		    Intent intent = getIntent();
		    Bundle bundle = intent.getExtras();
		    msgdata= (List<Map<String,Object>>)bundle.getSerializable("msglist");*/
			myname=SportData.getUserName(GetMagActivity.this);
			db = new sportDB(GetMagActivity.this);
			msgdata=db.GetActionMsg(myname);
			adapter = new MySimpleAdapter(this,Handlermsg,msgdata,R.layout.search_item,
					new String[]{"m_headp","m_id","m_name","m_name3","m_day","m_msg","m_button"},
			  		new int[]{R.id.search_head,R.id.search_id,R.id.search_name,R.id.search_text1,R.id.search_text2,R.id.search_text3,R.id.search_actionb});
			msglist.setAdapter(adapter);	
//			MyAdapter adapter = new MyAdapter(this,null,msgdata,R.layout.msg_item,   
//		    		 new String[]{"m_headp","m_name","m_day","m_msg"},
//		    		 new int[]{R.id.msgl_head,R.id.msgl_text1,R.id.msgl_text2,R.id.msgl_text3});
//		    msglist.setAdapter(adapter);	
	    }
		Handler Handlermsg= new Handler(){	    	
	    	public void handleMessage(Message msg) {
	    		super.handleMessage(msg);
	    		switch(msg.what)
	    		{
                    case 1:
                    	//发送查看命令
                    	if(CMAS==null)
                    	{
                    		sdata=Socket_Data.SendFriendCom(GetMagActivity.this,myname,name,"20014");
	    				    CMAS=new ConnectionMAS(GetMagActivity.this,Handlermsg,sdata,3,4);
	    				    CMAS.start();
                    	}
	    				break;
                    case 2:
                    	CMAS=null;
	    			case 3:	
	    				//跳到好友个人信息界面
	    				FriendData fd=new AnalyzeUtils(GetMagActivity.this).analyze_friendmsg(msg.getData().getString("body"));
	    				closenet();
	    				if(fd!=null)
	    				{
	    					Intent intent=new Intent(GetMagActivity.this,CheckFdActivity.class);
	    				    intent.putExtra("FriendData",fd);
				            startActivityForResult(intent,2);
	    				}
	    				break;
	    			case 4:
	    				closenet();
	    				break;
	    			case 5:
	    				handler.postDelayed(updateThread ,20000);
	    				break;
	    			case 6:
	    				//发送查看命令
	    				if(CMAS==null)
                    	{
	    				name= msg.getData().getString("fname");
                    	sdata=Socket_Data.SendFriendCom(GetMagActivity.this,myname,name,"20014");
	    				CMAS=new ConnectionMAS(GetMagActivity.this,Handlermsg,sdata,3,4);
	    				CMAS.start();
                    	}
	    				break;
	    			case 8:
	    				//删除
	    				String mid= msg.getData().getString("fid");	
	    				db.DeleteActionMsg(mid);
	    				DeleatDataAdapter(msgdata,mid);
	    				break;
	    			  default:
	    					break;
	    			} 
	    		}
	    };
	    Handler handler  = new Handler();  
	     Runnable updateThread =  new Runnable(){  
	        public void run() { 
	     	   Message msg = new Message();
	     	   msg.what = 4;
	     	  Handlermsg.sendMessage(msg);
	         }  
	     };
	     private void closenet()
	     {
	 	   if(handler!=null&&updateThread!=null)
	 		   handler.removeCallbacks(updateThread);
	 	   if(CMAS!=null)
	 	     CMAS.closeSocket();
	  	   CMAS=null;
	     }
	     
	     public void DeleatDataAdapter(List<Map<String,Object>> data,String nameid)
		 {
		      for(int i=0;i<data.size();i++)    	 
			    { 		                
	    		    if(data.get(i).get("m_id").equals(nameid)) 
			        {
				      HashMap<String, Object> map = new HashMap<String, Object>();
			          map = (HashMap)data.get(i);			  
				      data.remove(map); 
				      adapter.notifyDataSetChanged();
			        }
			    } 
		 }
}
