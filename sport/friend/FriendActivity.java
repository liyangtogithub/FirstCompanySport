package com.desay.sport.friend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.CustomProgressDialog;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class FriendActivity extends Activity {
	
	private TextView smsg_text,sno_text,myntest,mycity,mymiles;
	private ListView f_listview;
	private ImageView addf_img,myhd,fbackimg,smsgimg;
	
	private static final int ITEM1=Menu.FIRST;   
	private static final int ITEM2=Menu.FIRST+1;   
	private static final int ITEM3=Menu.FIRST+2;
//	private SimpleAdapter adapter;
	private MyAdapter adapter;
	private List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
//	public static int msgnum = 0;
	private ListBottomLayout LBL;
	private String myname="0";
	private String fname="";//用户名
	private String markname="";
	private String showname="";
	ChangeName_Dialog CN;
	private boolean showp=true;
	private boolean islist=false;
	private String sdata="";
	private ConnectionMAS CMAS=null;
	private TextView tv_title;
	private CustomProgressDialog progressDialog = null;
	private entity_userinfo info;
	private sportDB db = null;
	private int pages=1;
	private int dsize=0;
	private String webdata="";
	private DisplayImageOptions options;
	private String failstr="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("FriendActivity","onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.friend_main);
		init();
		getmydata();
		 options = new DisplayImageOptions.Builder()
		 .showImageOnFail(R.drawable.headdefault)
	     .cacheInMemory()
	  	  .cacheOnDisc()
	  	  .build();
		 IntentFilter intentFilter = new IntentFilter();
		 intentFilter.addAction(SportData.RECEIVER_UPLIST);
		 registerReceiver(serverBroadcastReceiver ,intentFilter);
		Message msg = new Message();
  	    msg.what = 1;
  	    Handlerfriend.sendMessage(msg);
//  	  f_listview.setOnScrollListener(new ListOnScroll(Handlerfriend,LBL.isupdata()));   
	}
	public void onDestroy() {
		   closenet();
		   db.closeDB();
		   unregisterReceiver(serverBroadcastReceiver); 
		   super.onDestroy();
	}
	  /*************长按弹出选则菜单*************/  
    public void onCreateContextMenu(ContextMenu menu, View arg1,ContextMenuInfo menuInfo) {   
    	 menu.add(0,ITEM1,0,R.string.look_str);   
    	 menu.add(0,ITEM2,0,R.string.delete_str);   
    	 menu.add(0,ITEM3,0,R.string.remark_str); 		
    }
     
    public boolean onContextItemSelected(MenuItem item) { 
    	 
    	 Message msg = new Message();
         switch(item.getItemId())   
         {   
          case ITEM1:
//        	 fname1=fname;
        	 msg.what = 8;
     	     Handlerfriend.sendMessage(msg);
          break;   
          case ITEM2: 
//        	  fname1=fname;
              msg.what = 15;
    	     Handlerfriend.sendMessage(msg);
          break; 
          case ITEM3:
//        	  fname1=fname;
	    	  msg.what = 4;
	    	  Handlerfriend.sendMessage(msg);
          break;   
         }   
         return super.onContextItemSelected(item);   
    } 
	private void init()
	{
		myntest=(TextView)findViewById(R.id.f_mynametext);
		mycity=(TextView)findViewById(R.id.f_mycitytext);
		mymiles=(TextView)findViewById(R.id.f_mymilestext);
		myhd=(ImageView)findViewById(R.id.f_myhead);
		smsg_text=(TextView)findViewById(R.id.f_msgtext);
		smsgimg=(ImageView)findViewById(R.id.f_msgimg);
	    sno_text=(TextView)findViewById(R.id.frined_noList);
		f_listview = (ListView)findViewById(R.id.friend_List);
		addf_img=(ImageView)findViewById(R.id.f_addfimg);
		fbackimg=(ImageView)findViewById(R.id.iv_return);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(getString(R.string.myfriend_str));
		registerForContextMenu(f_listview);    //将list加到菜单
		LBL=new ListBottomLayout(FriendActivity.this,Handlerfriend);  
		f_listview.addFooterView(LBL.getview(), null,true);
		smsgimg.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		//请求消息体	
 	    		 Message msg = new Message();
 		    	 msg.what = 16;
 		    	 Handlerfriend.sendMessage(msg);
 	    	}
 	    });
		addf_img.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		//跳到查找好友界面 
 	    		Intent intent=new Intent(FriendActivity.this,SearchActivity.class);
		        startActivityForResult(intent,6);
 	    	}
 	    });
		fbackimg.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		FriendActivity.this.finish();
 	    	}
 	    });

		f_listview.setOnItemClickListener(new OnItemClickListener() 
		 { 			 
		    public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
		    {  
		    	if(!LBL.isupdata())
		    	{
 		    		fname=(String)((TextView)arg1.findViewById(R.id.list_name)).getText(); 
 		    		showname=(String)((TextView)arg1.findViewById(R.id.show_name)).getText(); 
 	 		    	markname=(String)((TextView)arg1.findViewById(R.id.list_text1)).getText(); 
 					f_listview.showContextMenu();  
		    	}
		    }   
        }); 
	}
	private void getmydata()
	{
		//从数据库获取个人信息
		String string="";
		db = new sportDB(FriendActivity.this);
		info = db.GetUserInfo(FriendActivity.this);
		myname=SportData.getUserName(FriendActivity.this);
	    if(info.getHeadPhoto()!=null && !info.getHeadPhoto().equals(""))
		{
			byte[] photo = SportData.hexStringToBytes(info.getHeadPhoto());
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0,photo.length);
			bmp = Bitmap.createScaledBitmap(bmp, 56, 56, true);
			myhd.setImageBitmap(bmp);
		}else{
			myhd.setImageResource(R.drawable.headdefault);
		}
		if(myname.equals(SportData.DEFAULTUSERNAME))
		 {
			string=getString(R.string.noname_str);
		 }
		else
		{
			string=info.getShowname();
			if(SportData.msgnum>0)
				smsg_text.setText(SportData.msgnum+"");
		}
		myntest.setText(string);
		if(info.getCity()!=null && !info.getCity().equals(""))
			mycity.setText(info.getCity());
		GetTime gt=new GetTime();
		long distances=db.GetDistanceCount(myname,gt.get_startMillis(),gt.get_endMillis());
		mymiles.setText(getString(R.string.ldis_str,SportData.getKilometer(distances)));
	}
	//更新好友列表
	public void upfrienddate(List<Map<String,Object>> data)
	{
		if(data==null||data.size()<1)
	    {
			f_listview.setVisibility(View.GONE);
			sno_text.setVisibility(View.VISIBLE);
			islist=false;
		 }else{
			f_listview.setVisibility(View.VISIBLE);
			sno_text.setVisibility(View.GONE);	
//			adapter = new MySimpleAdapter(this,null,data,R.layout.friendlist_item,   
//			new String[]{"f_headp","f_name1","f_name2","f_city","f_miles"},
//			new int[]{R.id.list_head,R.id.list_name,R.id.list_text1,R.id.list_text2,R.id.list_text4});
//			adapter.setViewBinder(new ListViewBinder());
			adapter = new MyAdapter(this,options,data,R.layout.friendlist_item,   
			new String[]{"f_headp","f_name1","f_name3","f_name2","f_city","f_miles"},
			new int[]{R.id.list_head,R.id.list_name,R.id.show_name,R.id.list_text1,R.id.list_text2,R.id.list_text4});
			f_listview.setAdapter(adapter);
			islist=true;
		}		     
	}
	 public void DeleatDataAdapter(List<Map<String,Object>> data,String nameid)
	 {
	      for(int i=0;i<data.size();i++)    	 
		    { 		                
			   if(data.get(i).get("f_name1").equals(nameid)) 
		        {
			      HashMap<String, Object> map = new HashMap<String, Object>();
		          map = (HashMap)data.get(i);			  
			      data.remove(map); 
			      adapter.notifyDataSetChanged();
		        }
		    } 
	 }
	 public void upmarkAdapter(List<Map<String,Object>> data,String nameid,String showname)
	 {
		 for(int i=0;i<data.size();i++)    	 
		    { 		                
			 if(data.get(i).get("f_name1").equals(nameid)) 
		        {
			      HashMap<String, Object> map = new HashMap<String, Object>();
		          map = (HashMap)data.get(i);			  	      
		          if(markname.equals(""))
		        	  map.put("f_name2",showname);
		          else
		              map.put("f_name2",markname);		          		          
			      adapter.notifyDataSetChanged();
		        }  
		    }
	 }
	
		Handler Handlerfriend= new Handler(){	    	
	    	public void handleMessage(Message msg) {
	    		super.handleMessage(msg);
	    		switch(msg.what)
	    		{
	    			case 1:
	    				pages=1;
	    				if(myname.equals(SportData.DEFAULTUSERNAME))
	    				{
	    					showToast(getString(R.string.rankload_str));
	    				}	    	
	    				else{
	    				    //发送获取好友命令
	    					showp=true;
	    					failstr=getString(R.string.sendrankfail_str);
		    				sdata=Socket_Data.SendGetFList(FriendActivity.this,myname,pages);
		    				CMAS=new ConnectionMAS(FriendActivity.this,Handlerfriend,sdata,14,3);
		    				CMAS.start();
	    				}
	    				break;	
	    			case 2:	
	    				CMAS=null;
	    				showToast(getString(R.string.nonet_str));
	    				break;
	    			case 3:	
	    				closenet();
	    				showToast(failstr);
	    				break;
	    			case 4:
	    				if(markname.equals(showname))
	    					CN=new ChangeName_Dialog(FriendActivity.this,Handlerfriend,"");
	    				else
	    				    CN=new ChangeName_Dialog(FriendActivity.this,Handlerfriend,markname);
	    	    		CN.EditDialog();
	    				break;
	    			case 5:
	    				if(islist&&showp)
	    				LBL.openprogress();
	    				else{
	    					if (progressDialog == null){  
	    					      progressDialog = CustomProgressDialog.createDialog(FriendActivity.this);  
	    					  } 
	    					if (!progressDialog.isShowing())
	    					    progressDialog.show();
	    				}
	    				handler.postDelayed(updateThread ,20000);
	    				break;
	    			case 6:	
	    				//发送备注命令
	    				if(CMAS==null)
	    				{
	    					markname=CN.getinputname();
	    					sdata=Socket_Data.SendMark(FriendActivity.this,myname,fname,markname);
		    				showp=false;
		    				failstr=getString(R.string.sendtofail_str);		    				
	    					CMAS=new ConnectionMAS(FriendActivity.this,Handlerfriend,sdata,11,3);
	    				    CMAS.start();
	    				}
	    				break;
	    			case 7:
	    				//发送删除命令
	    				if(CMAS==null)
	    				{
	    					sdata=Socket_Data.SendFriendCom(FriendActivity.this,myname,fname,"20010");
		    				showp=false;
		    				failstr=getString(R.string.sendtofail_str);
	    					CMAS=new ConnectionMAS(FriendActivity.this,Handlerfriend,sdata,10,3);
	    				    CMAS.start();
	    				}
	    				break;
                    case 8:
                    	//发送查看命令
	    				if(CMAS==null)
	    				{
	    					sdata=Socket_Data.SendFriendCom(FriendActivity.this,myname,fname,"20014");
		    				showp=false;
	    					CMAS=new ConnectionMAS(FriendActivity.this,Handlerfriend,sdata,9,13);
	    				    CMAS.start();
	    				}
	    				break;
	    			case 9:	
	    				//跳到好友个人信息界面
	    				webdata=msg.getData().getString("body");
	    				FriendData fd=new AnalyzeUtils(FriendActivity.this).analyze_friendmsg(webdata);
	    				closenet();
	    				if(fd!=null)
	    				{
	    					Intent intent=new Intent(FriendActivity.this,CheckFdActivity.class);
	    				    intent.putExtra("FriendData",fd);
				            startActivityForResult(intent,6);
	    				}
	    				break;
	    			case 10:	
	    				//删除好友成功	
	    				closenet();
	    				DeleatDataAdapter(data,fname);
	    				break;
	    			case 11:	
	    				//备注好友成功，更新界面
	    				closenet();
	    				upmarkAdapter(data,fname,showname);
	    				break;
	    			case 12:
	    				//发送获取好友命令
	    				showp=true;
	    				dsize=data.size();
	    				if(dsize>=30*pages)
	    				   pages++;
	    				
	    				if(CMAS==null)
	    				{
	    					sdata=Socket_Data.SendGetFList(FriendActivity.this,myname,pages);
	    				    CMAS=new ConnectionMAS(FriendActivity.this,Handlerfriend,sdata,19,13);
	    				    CMAS.start();
	    				}
	    				break;
	    			case 13:
	    				showp=false;
	    				closenet();
	    				break;
	    			case 14:
	    				//更新列表
	    				data.clear();
	    				webdata=msg.getData().getString("body");
	    				new AnalyzeUtils(FriendActivity.this).analyze_friendlist(webdata,data);
	    				closenet();
	    				upfrienddate(data);
//	    				if(msgnum<1)
//	    				{
	    					sdata=Socket_Data.SendGetAddMsg(FriendActivity.this,myname,"20012");
	    				    CMAS=new ConnectionMAS(FriendActivity.this,Handlerfriend,sdata,18,13);
	    				    CMAS.start();
//	    				}
	    				break;
	    			case 15:
	    				DialogToast dt=new DialogToast(FriendActivity.this,Handlerfriend);
	    	    		dt.OpenDialogToast(markname);
	    				break;
	    			case 16:
	    				//查看关注消息
	    				List<Map<String,Object>> msgdata=db.GetActionMsg(myname);
	    				if(!myname.equals(SportData.DEFAULTUSERNAME)&&msgdata.size()>0)
	    				{
	    					SportData.msgnum=0;
    					    smsg_text.setText("");
	    				    Intent intent2=new Intent(FriendActivity.this,GetMagActivity.class);
				            startActivityForResult(intent2,6); 
	    				}	    	
	    				else{	    				  
	    						showToast(getString(R.string.no_addmsg_str));
	    				}
	    				break;
	    			case 17:
	    				//跳到关注消息界面
	    				if(SportData.msgnum<1)
	    				 { 
	    					 webdata=msg.getData().getString("body");
	    					 SportData.msgnum+= new AnalyzeUtils(FriendActivity.this).saveSQL_msglist(webdata, db,myname);
	    				 }
	    				 closenet();	
	    				Intent intent2=new Intent(FriendActivity.this,GetMagActivity.class);
//	    				 Bundle bundle = new Bundle();
//	    				 bundle.putSerializable("msglist",(Serializable)msglist);
//	               		 intent2.putExtras(bundle);
				        startActivityForResult(intent2,6);
	    				break;
	    			case 18:
	    				showp=false;
	    				webdata=msg.getData().getString("body");
	    				SportData.msgnum+= new AnalyzeUtils(FriendActivity.this).saveSQL_msglist(webdata,db,myname);
	    				if(SportData.msgnum>0)
	    				   smsg_text.setText(SportData.msgnum+"");
	    				closenet();
	    				break;
	    			case 19:
	    				webdata=msg.getData().getString("body");
	    				new AnalyzeUtils(FriendActivity.this).analyze_myfriendlist(webdata,data);
	    				if(data.size()>dsize)
	    					 adapter.notifyDataSetChanged();
//	    				upfrienddate(data);
	    				closenet();	
	    				
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
	    	   msg.what = 3;
	    	   Handlerfriend.sendMessage(msg);
	        }  
	    };
	    private void closenet()
	    {
	    	if(CMAS!=null)
	    	CMAS.closeSocket();
	      if(handler!=null&&updateThread!=null)
			   handler.removeCallbacks(updateThread);
		  if(islist)
		     LBL.closeprogress();
		  CMAS=null;
		  if (progressDialog != null){  
		        progressDialog.dismiss();  
		        progressDialog = null;         
		   }
	    }
	    private void showToast(String string)
		{
	    	showToast.normaltoast(FriendActivity.this,string,showToast.ONE_SECOND);
		}
	    public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			closenet();

		 }  
		       return super.onKeyDown(keyCode, event);
		}
	    private BroadcastReceiver serverBroadcastReceiver = new BroadcastReceiver() { 
		    @Override 
		    public void onReceive(Context context, Intent intent) { 
		    	String action = intent.getAction();
		    	if (action.equals(SportData.RECEIVER_UPLIST))
		    	 {
		    	    	 Message msg = new Message();
				         msg.what =10;
				         Handlerfriend.sendMessage(msg);
		        }
		    	
		    } 
		 };
}
