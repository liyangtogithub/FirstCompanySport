package com.desay.sport.friend;

import java.util.ArrayList;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class Friend_flActivity extends Activity {
	
	private ListView ffl_list;	
	private ImageView backimg;
	private TextView tv_title;
	private List<Map<String,Object>> ffldata = new ArrayList<Map<String,Object>>();
	private String ff_name="";//用户名
	private String sdata="";
	private ConnectionMAS CMAS;
	private ListBottomLayout LBL;
	private String fname="";
	private int fnum=0;
	private int pages=2;
//	private SimpleAdapter adapter;
	private MyAdapter adapter;
	private boolean showp=true;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Friend_flActivity","onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.friend_flist);
		init();
		options = new DisplayImageOptions.Builder()
	    .cacheInMemory()
	  	.cacheOnDisc()
	  	.build();
	}
	public void onDestroy() {
		   closenet();
		   super.onDestroy();
	}
	private void init()
	{
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.friend_str));
		ffl_list = (ListView)findViewById(R.id.ffdl_List);		
		backimg=(ImageView)findViewById(R.id.iv_return);
		backimg.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		Friend_flActivity.this.finish();
 	    	}
 	    });

		ffl_list.setOnItemClickListener(new OnItemClickListener() 
		{ 			 
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
			{  
				ff_name=(String) ((TextView)arg1.findViewById(R.id.list_name)).getText();//获取点的名称
			    Message msg = new Message();
			  	msg.what = 1;
			  	Handlerfffl.sendMessage(msg);
			}   
	    }); 
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		fname=bundle.getString("fname");
		fnum=bundle.getInt("fnum");
		LBL=new ListBottomLayout(Friend_flActivity.this,Handlerfffl); 
		if(fnum>30)
		{
			ffl_list.addFooterView(LBL.getview(), null,true);	
		}
		ffldata= (List<Map<String,Object>>)bundle.getSerializable("fflist");
		upfrienddate(ffldata);
	}
		Handler Handlerfffl= new Handler(){	    	
	    	public void handleMessage(Message msg) {
	    		super.handleMessage(msg);
	    		switch(msg.what)
	    		{
	    			case 1:
	    				//发送查看命令
	    				showp=false;
                    	sdata=Socket_Data.SendFriendCom(Friend_flActivity.this,SportData.getUserName(Friend_flActivity.this),ff_name,"20014");
	    				CMAS=new ConnectionMAS(Friend_flActivity.this,Handlerfffl,sdata,3,4);
	    				CMAS.start();
	    				break;	
	    			case 2:	
	    				showToast(getString(R.string.nonet_str));
	    				break;
	    			case 3:	
	    				//跳到好友个人信息界面
	    				FriendData fd=new AnalyzeUtils(Friend_flActivity.this).analyze_friendmsg(msg.getData().getString("body"));
	    				closenet();
	    				if(fd!=null)
	    				{
	    					Intent intent=new Intent(Friend_flActivity.this,CheckFdActivity.class);
	    				    intent.putExtra("FriendData",fd);
				            startActivityForResult(intent,6);
	    				}
	    				break;
	    			case 4:	
	    				closenet();
	    			//	showToast(getString(R.string.sendfail_str));
	    				break;
	    			case 5:
	    				if(showp)
	    				LBL.openprogress();
	    				handler.postDelayed(updateThread ,20000);
	    				break;
	    			case 6:
	    				ffldata=new AnalyzeUtils(Friend_flActivity.this).analyze_friendlist(msg.getData().getString("body"),ffldata);
	    				if(pages*30<fnum)	
	    					pages++;
	    				else
	    					ffl_list.removeFooterView(LBL.getview());
	    				adapter.notifyDataSetChanged();
//	    				upfrienddate(ffldata);
	    				closenet();	
	    				break;
	    			case 12:
	    				//发送获取好友命令
	    				showp=true;
	    				sdata=Socket_Data.SendGetFList(Friend_flActivity.this,fname,pages);
	    				CMAS=new ConnectionMAS(Friend_flActivity.this,Handlerfffl,sdata,6,4);
	    				CMAS.start();
	    				break;
	    			  default:
	    					break;
	    			} 
	    		}
	    };
	    public void upfrienddate(List<Map<String,Object>> data)
		{	
//	    	adapter = new SimpleAdapter(this,ffldata,R.layout.friendlist_item,   
//					 new String[]{"f_headp","f_name1","f_city","f_miles"},
//					 new int[]{R.id.list_head,R.id.list_text1,R.id.list_text2,R.id.list_text4});
//			adapter.setViewBinder(new ListViewBinder()); 
	    	adapter = new MyAdapter(this,options,ffldata,R.layout.friendlist_item,   
					 new String[]{"f_headp","f_name1","f_name3","f_city","f_miles"},
					 new int[]{R.id.list_head,R.id.list_name,R.id.list_text1,R.id.list_text2,R.id.list_text4});
			ffl_list.setAdapter(adapter);
				
		}
	    Handler handler  = new Handler();  
	    Runnable updateThread =  new Runnable(){  
	       public void run() { 
	    	   Message msg = new Message();
	    	   msg.what = 4;
	    	   Handlerfffl.sendMessage(msg);
	        }  
	    };
	    private void closenet()
	    {
	    	if(CMAS!=null)
	    	CMAS.closeSocket();
	      if(handler!=null&&updateThread!=null)
			   handler.removeCallbacks(updateThread);
		  CMAS=null;
		  if(showp)
		  LBL.closeprogress();
		  showp=false;
	    }
	    private void showToast(String string)
		{
	    	showToast.normaltoast(Friend_flActivity.this,string,showToast.ONE_SECOND);
		}
}