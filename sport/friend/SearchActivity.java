package com.desay.sport.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class SearchActivity extends Activity{

private static final String TAG = "SercherActivity";

   private EditText sedit;
   private ImageView backimg;
   private Button sbutton;
   private TextView tv_title;
   private ListView list;
   private String searchid="";
   private ConnectionMAS CMAS=null;
   private String myname;
   private String fname;
   private String showname;
   private ListBottomLayout LBL;
   private List<Map<String,Object>> s_data = new ArrayList<Map<String,Object>>();
   private int pages=1;
   private boolean showp=true;
   private int dsize=0;
   private String webdata="";
   private MySimpleAdapter adapter;
   private String fstatus="";
   private String failstr="";
   protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);	
  	    setContentView(R.layout.search_main);
  	    init();
   }
   public void onDestroy() {
	   closenet();
	   super.onDestroy();
	}
    private void init()
    {
    	backimg=(ImageView)findViewById(R.id.iv_return);
    	sedit =(EditText)findViewById(R.id.search_edit);
    	sbutton=(Button)findViewById(R.id.search_b);
    	list=(ListView)findViewById(R.id.search_List);
    	tv_title = (TextView) findViewById(R.id.tv_title);
    	tv_title.setText(getString(R.string.search_str));
    	myname=SportData.getUserName(SearchActivity.this);
		LBL=new ListBottomLayout(SearchActivity.this,Handlersearch);  
 
    	backimg.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		SearchActivity.this.finish();	 	    		
 	    	}
 	    });
    	sbutton.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		 searchid= sedit.getText().toString().trim();
 	    		 pages=1;
 	    		 Message msg = new Message();
 	     	     msg.what = 1;
 	     	     Handlersearch.sendMessage(msg);
 	    	}
 	    });
    	 searchid="";
    	 pages=1;
 		 Message msg = new Message();
  	     msg.what = 1;
  	     Handlersearch.sendMessage(msg);
    }
    
    Handler Handlersearch= new Handler(){	    	
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what)
    		{
    			case 1:
    				String sdata=Socket_Data.SendSearch(SearchActivity.this,myname,searchid,pages);
    				if(CMAS==null)
    				{
    				  showp=false;
    				  failstr=getString(R.string.sendrankfail_str);
    				CMAS=new ConnectionMAS(SearchActivity.this,Handlersearch,sdata,3,4);
    				CMAS.start();
    				}
    				break;
    			case 2:
    				CMAS=null;
    				showToast(getString(R.string.nonet_str));
    				break;
    			case 3:	
    				 s_data.clear();
    				 list.removeFooterView(LBL.getview());
    				 webdata=msg.getData().getString("body");
    				 s_data=new AnalyzeUtils(SearchActivity.this).analyze_searchlist(webdata,s_data);
    				 if(s_data!=null&&s_data.size()>0)
    				 {
    				     if(s_data.size()>=30)
    				    	 list.addFooterView(LBL.getview(), null,true);
    					 upsearchdate(s_data);
    					 pages++;
    				 }
    				 closenet();
    				break;
    			case 4:
    				 closenet();
    				 showToast(failstr);
   				break;
    			case 5:	
    				if(showp)
	    				LBL.openprogress();
    				handler.postDelayed(updateThread ,20000);
  				break;
    			case 6:	
    				fname= msg.getData().getString("fname");
    				String lookdata=Socket_Data.SendFriendCom(SearchActivity.this,myname,fname,"20014");
    				if(CMAS==null)
    				{
    					showp=false;
    				CMAS=new ConnectionMAS(SearchActivity.this,Handlersearch,lookdata,7,10);
    				CMAS.start();
    				}
  				break;
    			case 7:
    				webdata=msg.getData().getString("body");
    				FriendData fd=new AnalyzeUtils(SearchActivity.this).analyze_friendmsg(webdata);
    				closenet();
    				if(fd!=null)
    				{Intent intent=new Intent(SearchActivity.this,CheckFdActivity.class);
    				intent.putExtra("FriendData",fd);
			        startActivityForResult(intent,5);
    				}
  				break;
    			case 8:	
    				fname= msg.getData().getString("fname");
    				fstatus= msg.getData().getString("fstatus");
    				showname= msg.getData().getString("showname");
    				if(myname.equals(SportData.DEFAULTUSERNAME))
    					showToast(getString(R.string.rankload_str));
    				else if(CMAS==null)
    				{
    				   if(fstatus.equals(getString(R.string.cancle_str)))
    				   {
    					  //进行删除
    					   if(!myname.equals(fname))
    				      {
    						showp=false;
    						failstr=getString(R.string.sendtofail_str);
    						String deletkdata=Socket_Data.SendFriendCom(SearchActivity.this,myname,fname,"20010");
	    					CMAS=new ConnectionMAS(SearchActivity.this,Handlersearch,deletkdata,13,4);
	    				    CMAS.start();
    				      }else
    				    	 showToast(getString(R.string.canntdelete_str));
    				   }else{
    					   if(!myname.equals(fname))
     				      {
     						showp=false;
     						failstr=getString(R.string.sendtofail_str);
	    				    String addkdata=Socket_Data.SendFriendCom(SearchActivity.this,myname,fname,"20009");
    				        CMAS=new ConnectionMAS(SearchActivity.this,Handlersearch,addkdata,9,4);
    				        CMAS.start();
     				      }else
     				    	 showToast(getString(R.string.canntadd_str));
    				   }
    				}
      				break;
    			case 9:	
    				//添加成功
    				closenet();
//    				webdata=msg.getData().getString("body");
//    				String as=new AnalyzeUtils(SearchActivity.this).analyze_returntoast(webdata);
    				upmarkAdapter(s_data,fname,getString(R.string.cancle_str));
    				showToast(getString(R.string.add_ok_str));
      				break;
    			case 10:
    				closenet();
    				break;
    			case 11:
    				 dsize=s_data.size();
    				 webdata=msg.getData().getString("body");
   				     new AnalyzeUtils(SearchActivity.this).analyze_searchlist(webdata,s_data);
   				     closenet();
   				     if(s_data!=null&&s_data.size()>dsize)
//   				        upsearchdate(s_data);
   				     {	
   				    	adapter.notifyDataSetChanged();
   				         pages++;
   				     }
   				     else 
   				    	list.removeFooterView(LBL.getview());
   				   
   				    break;
    			case 12:
    				showp=true;
    				String sdata2=Socket_Data.SendSearch(SearchActivity.this,myname,searchid,pages);
    				if(CMAS==null)
    				{
    				  failstr=getString(R.string.sendrankfail_str);
    				  CMAS=new ConnectionMAS(SearchActivity.this,Handlersearch,sdata2,11,4);
    				  CMAS.start();
    				}
    				break;
    			case 13:
    				//删除成功
    				closenet();
    				upmarkAdapter(s_data,fname,getString(R.string.attention_str));
    				showToast(getString(R.string.delete_ok_str));
    				break;
    			  default:
    					break;
    			} 
    		}
    };
  //更新好友列表
  	public void upsearchdate(List<Map<String,Object>> data)
  	{
  		adapter = new MySimpleAdapter(this,Handlersearch,data,R.layout.search_item,
  		new String[]{"s_headp","s_name","s_name3","s_city","s_miles","s_status"},
  		new int[]{R.id.search_head,R.id.search_name,R.id.search_text1,R.id.search_text2,R.id.search_text4,R.id.search_actionb});
  		list.setAdapter(adapter);	     
  	}
  	private void showToast(String string)
	{
  		showToast.normaltoast(SearchActivity.this,string,showToast.ONE_SECOND);
	}
  	 Handler handler  = new Handler();  
     Runnable updateThread =  new Runnable(){  
        public void run() { 
     	   Message msg = new Message();
     	   msg.what = 10;
     	  Handlersearch.sendMessage(msg);
         }  
     };
     private void closenet()
     {
 	   if(handler!=null&&updateThread!=null)
 		   handler.removeCallbacks(updateThread);
 	   if(CMAS!=null)
 	     CMAS.closeSocket();
  	   CMAS=null;
  	    if(showp)
  	    	 LBL.closeprogress();
     }
     public boolean onKeyDown(int keyCode, KeyEvent event) { 
	      if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	  SearchActivity.this.finish();
	          return true;
	        }  
	       return false;
	 }
     public void upmarkAdapter(List<Map<String,Object>> data,String nameid,String ststus)
	 {
		 for(int i=0;i<data.size();i++)    	 
		    { 		                
			 if(data.get(i).get("s_name").equals(nameid)) 
		        {
			      HashMap<String, Object> map = new HashMap<String, Object>();
		          map = (HashMap)data.get(i);			  	      
		          map.put("s_status",ststus);	          		          
			      adapter.notifyDataSetChanged();
		        }  
		    }
	 }

}
