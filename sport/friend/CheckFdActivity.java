package com.desay.sport.friend;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.desay.sport.data.SportData;
import com.desay.sport.data.UpdataMedalUI;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.medalAdapter;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.CustomProgressDialog;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.desay.pstest.toast.showToast;

public class CheckFdActivity extends Activity{

private static final String TAG = "CheckFdActivity";
private FriendData fd=new FriendData();
private TextView textview=null;
private ImageView imagev=null;
private ImageView imagback=null;
private TextView mntext,fntext;
private Button sbutton=null;
private String status="0";
private ConnectionMAS CMAS=null;
private String myname="";
private String fname="";
String addkdata="";
private int fnum=0;
private int mnum=0;
private List<entity_sportachieve> sportachieve_list;
private CustomProgressDialog progressDialog = null; 
private LinearLayout rlayout;
private GridView mealview;
private String showname="";
private String failstr="";



@Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
 	   super.onCreate(savedInstanceState);
	   requestWindowFeature(Window.FEATURE_NO_TITLE);	
	   setContentView(R.layout.seedetail_main);
	   init();
	   myname=SportData.getUserName(CheckFdActivity.this);
    }
   public void onDestroy() {
	   closenet();
	   super.onDestroy();
	}
    private OnClickListener friendOnClick = new OnClickListener() {
		public void onClick(View v) {
			Message msg = new Message();
			switch (v.getId()) {
			case R.id.iv_return:			 
				CheckFdActivity.this.finish();
				break;
			case R.id.sdl_addb:
			if(myname.equals(SportData.DEFAULTUSERNAME))
			{
				showToast(getString(R.string.rankload_str));
			}else
			{
			    if(status.equals("0"))
			    {
			    	//发送取消关注命令
	 		    	msg.what = 6;
	 		    	Handlercheck.sendMessage(msg);
			    } else
			    {
			    	//发送关注命令
			    	msg.what = 1;
	 		    	Handlercheck.sendMessage(msg);
			    }
			    }
				break;
			case R.id.sdl_medal_text:
                //跳到奖章界面
				msg.what = 10;
 		    	Handlercheck.sendMessage(msg);
				break;
			case R.id.sdl_friend_morelayout:
				//跳到好友界面
				if(fnum>0)
				{
					msg.what = 8;
 		    	    Handlercheck.sendMessage(msg);
				}
				break;
			default:
				break;
			}
		}
	};
	Handler Handlercheck= new Handler(){	    	
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what)
    		{
    			case 1:
    				//添加好友
    				if(!myname.equals(fname))
    				{
    					failstr=getString(R.string.sendtofail_str);
    					addkdata=Socket_Data.SendFriendCom(CheckFdActivity.this,myname,fname,"20009");
    				    CMAS=new ConnectionMAS(CheckFdActivity.this,Handlercheck,addkdata,3,4);
    				    CMAS.start();
    				}else
    				 showToast(getString(R.string.canntadd_str));
    				break;	
    			case 2:	
    				showToast(getString(R.string.nonet_str));
    				break;
    			case 3:
    				closenet();
    				status="0";
    			    sbutton.setText(getString(R.string.cancle_str));
    			    showToast(getString(R.string.add_ok_str));
    				break;
    			case 4:	
    				closenet();
    				showToast(failstr);
    				break;
    			case 5:
    				if (progressDialog == null){  
					      progressDialog = CustomProgressDialog.createDialog(CheckFdActivity.this);  
					  } 
					if (!progressDialog.isShowing())
					    progressDialog.show();
    				handler.postDelayed(updateThread ,20000);
    				break;
    			case 6:	
    				//删除好友
    				if(!myname.equals(fname))
    				{
    					failstr=getString(R.string.sendtofail_str);
    					addkdata=Socket_Data.SendFriendCom(CheckFdActivity.this,myname,fname,"20010");
        				CMAS=new ConnectionMAS(CheckFdActivity.this,Handlercheck,addkdata,7,4);
        				CMAS.start();
    				}else
    				 showToast(getString(R.string.canntdelete_str));
    				
    				break;
    			case 7:
    				closenet();
    				status="1";
    			    sbutton.setText(getString(R.string.attention_str));
    			    showToast(getString(R.string.delete_ok_str));
    				Intent sendintent = new Intent(SportData.RECEIVER_UPLIST);
        			sendintent.putExtra("frinesname",fname);
        		    sendBroadcast(sendintent);  
    				break;
                case 8:
                	//发送查看好友列表命令
                	failstr=getString(R.string.sendrankfail_str);
    				addkdata=Socket_Data.SendGetFList(CheckFdActivity.this,fname,1);
    				CMAS=new ConnectionMAS(CheckFdActivity.this,Handlercheck,addkdata,9,4);
    				CMAS.start();
    				break;
    			case 9:	
    				List<Map<String,Object>> msglist= new ArrayList<Map<String,Object>>();
    				msglist=new AnalyzeUtils(CheckFdActivity.this).analyze_friendlist(msg.getData().getString("body"),msglist);
    				closenet();
    				if(msglist.size()>0)
    				{	
    					Intent intent=new Intent(CheckFdActivity.this,Friend_flActivity.class);
    				    Bundle bundle = new Bundle();
    				    bundle.putString("fname", fname);
    				    bundle.putInt("fnum",fnum);
    				    bundle.putSerializable("fflist",(Serializable)msglist);
               	    	intent.putExtras(bundle);
			            startActivityForResult(intent,7);
    				}
    				break;
    			case 10:
//    				Log.d("sportachieve_list","sportachieve_list4=="+sportachieve_list.size());
    				Intent intent=new Intent(CheckFdActivity.this,friend_medaling.class);
    				Bundle bundle = new Bundle();
    				bundle.putSerializable("meal",(Serializable)sportachieve_list);
    				intent.putExtras(bundle);
    		        startActivityForResult(intent,7);
    				break;
    			default:
    			    break;
    			} 
    		}
    };
	
	private void init()
    {   
    	Intent intent = getIntent();   
        fd= (FriendData)intent.getSerializableExtra("FriendData");
    	imagback=(ImageView)findViewById(R.id.iv_return);
    	imagback.setOnClickListener(friendOnClick);      
        textview=(TextView)findViewById(R.id.tv_title); 
        fname=fd.getmyname();
        showname=fd.getshowname();
        textview.setText(showname);
        textview=(TextView)findViewById(R.id.sdl_fname); 
        textview.setText(showname);
        imagev=(ImageView)findViewById(R.id.sdl_fimghd);
        if(fd.getheadimg().equals(""))
        	imagev.setImageResource(R.drawable.headdefault);
        else
        ImageLoader.getInstance().displayImage(fd.getheadimg(),imagev);
        sbutton=(Button)findViewById(R.id.sdl_addb);
        status=fd.getmystatus();
        if(status.equals("0"))
           sbutton.setText(getString(R.string.cancle_str));
        else
           sbutton.setText(getString(R.string.attention_str));
        sbutton.setOnClickListener(friendOnClick);
        textview=(TextView)findViewById(R.id.sdl_fcity); 
        textview.setText(fd.getmycity());
        textview=(TextView)findViewById(R.id.sdl_fdis); 
        textview.setText(fd.getmymile());
        textview=(TextView)findViewById(R.id.sdl_fnum); 
        textview.setText(fd.getmynum());
        textview=(TextView)findViewById(R.id.sdl_ftime); 
        textview.setText(fd.getmytime());
        textview=(TextView)findViewById(R.id.sdl_ldis_text); 
        textview.setText(fd.getmylongd());
        textview=(TextView)findViewById(R.id.sdl_type_text1); 
        textview.setText(fd.getsporttype1());
        textview=(TextView)findViewById(R.id.sdl_lsph_text); 
        textview.setText(fd.getmyspeed());
        textview=(TextView)findViewById(R.id.sdl_type_text2); 
        textview.setText(fd.getsporttype2());
        textview=(TextView)findViewById(R.id.sdl_ltime_text); 
        textview.setText(fd.getmyltime());
        textview=(TextView)findViewById(R.id.sdl_type_text3); 
        textview.setText(fd.getsporttype3());
        mnum=fd.getmedalnum();
        fnum=fd.getfriendnum();
        sportachieve_list=fd.getmeallist();
        medalui(fd.getmeallist());
        friendui();
   }
	private void medalui(List<entity_sportachieve> meal_list)
	{
		List<entity_sportachieve> mlist=meal_list;;
		mntext=(TextView)findViewById(R.id.sdl_medal_text); 
	    mntext.setText(getString(R.string.medal_str,mnum));
	    mealview= (GridView)findViewById(R.id.sdl_medal_image);
	    mealview.setOnItemClickListener(new OnItemClickListener() 
		 { 			 
		    public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
		    {  
		    	Message msg = new Message();
		    	msg.what = 10;
 		    	Handlercheck.sendMessage(msg);
		    }   
       });
	    int mn=mlist.size();
//	    Log.d("sportachieve_list",mn+"sportachieve_list1=="+sportachieve_list.size());
	    for(int i=mn;i<4;i++)
	    {
	    	entity_sportachieve sport=new entity_sportachieve();	    	   
	    	sport.setStarttime(-1);
	    	sport.setAchievetype(i); 
	    	sport.setAchievename(3*i);
	    	sport.setAchieverecord(-1);
	    	mlist.add(sport);
	    }
	    mealview.setAdapter(new medalAdapter(mlist,CheckFdActivity.this,R.layout.friend_item_medal));	  
	    setGridViewHeight(mealview);
	}
	private void friendui()
	 {
		   fntext=(TextView)findViewById(R.id.sdl_friend_text); 
		   fntext.setText(getString(R.string.friend_num_str,fnum));
		   DisplayImageOptions options = new DisplayImageOptions.Builder()
			 .showImageOnFail(R.drawable.headdefault)
		     .cacheInMemory()
		  	  .cacheOnDisc()
		  	  .build();
  		   if(fd.getfname1()!=null)
           {
//  			     rlayout=(LinearLayout)findViewById(R.id.sdl_friend_layout);
//  			     rlayout.setOnClickListener(friendOnClick);
  			     rlayout=(LinearLayout)findViewById(R.id.sdl_friend_morelayout);
			     rlayout.setOnClickListener(friendOnClick);
//			     rlayout.setVisibility(View.VISIBLE);
  			     textview=(TextView)findViewById(R.id.df_text1); 
         	     textview.setText(fd.getfname1());
         	     imagev=(ImageView)findViewById(R.id.df_imghead1);
         	     if(fd.getfhdimg1().equals(""))
         	        imagev.setImageResource(R.drawable.headdefault);
         	     else
         	        ImageLoader.getInstance().displayImage(fd.getfhdimg1(),imagev,options);
           }
  		   if(fd.getfname2()!=null)
          {
  			 
  			 textview=(TextView)findViewById(R.id.df_text2); 
       	     textview.setText(fd.getfname2());
       	     imagev=(ImageView)findViewById(R.id.df_imghead2);
       	     if(fd.getfhdimg2().equals(""))
       	        imagev.setImageResource(R.drawable.headdefault);
       	     else
       	        ImageLoader.getInstance().displayImage(fd.getfhdimg2(),imagev,options);
          }
  		 if(fd.getfname3()!=null)
         {
  			 textview=(TextView)findViewById(R.id.df_text3); 
       	     textview.setText(fd.getfname3());
       	     imagev=(ImageView)findViewById(R.id.df_imghead3);
       	     if(fd.getfhdimg3().equals(""))
       	        imagev.setImageResource(R.drawable.headdefault);
       	     else
       	        ImageLoader.getInstance().displayImage(fd.getfhdimg3(),imagev,options);
         }
  		if(fd.getfname4()!=null)
        {
  			 textview=(TextView)findViewById(R.id.df_text4); 
      	     textview.setText(fd.getfname4());
      	     imagev=(ImageView)findViewById(R.id.df_imghead4);
      	     if(fd.getfhdimg4().equals(""))
      	        imagev.setImageResource(R.drawable.headdefault);
      	     else
      	        ImageLoader.getInstance().displayImage(fd.getfhdimg4(),imagev,options);
         	}

	 }
	  Handler handler  = new Handler();  
	    Runnable updateThread =  new Runnable(){  
	       public void run() { 
	    	   Message msg = new Message();
	    	   msg.what = 4;
	    	   Handlercheck.sendMessage(msg);
	        }  
	    };
	    private void closenet()
	    {
	    	if(CMAS!=null)
	    	CMAS.closeSocket();
	      if(handler!=null&&updateThread!=null)
			   handler.removeCallbacks(updateThread);
		  CMAS=null;
		  if (progressDialog != null){  
		        progressDialog.dismiss();  
		        progressDialog = null;         
		   }
	    }
	    private void showToast(String string)
		{
	    	showToast.normaltoast(CheckFdActivity.this,string,showToast.ONE_SECOND);
		}
//		public void initmedal(int i)
//		{
//		       entity_sportachieve sportachieve=new entity_sportachieve();	    	   
//		       sportachieve.setStarttime(-1);
//	    	   sportachieve.setAchievetype(i); 
//	    	   sportachieve.setAchievename(i);
//			   sportachieve.setAchieverecord(-1);
//			   meal_list.add(sportachieve);
//		}
		public void setGridViewHeight(GridView listView) { 
			ListAdapter listAdapter = listView.getAdapter();   
	        int totalHeight = 0;  
	        View listItem = listAdapter.getView(0, null, listView);  
        	listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();   	
	        ViewGroup.LayoutParams params = listView.getLayoutParams();  
	        params.height = totalHeight;  
	        listView.setLayoutParams(params);  
		}
}
