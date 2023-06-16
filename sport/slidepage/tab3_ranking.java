package com.desay.sport.slidepage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_sendrecord;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.AskDialog;
import com.desay.sport.friend.CheckFdActivity;
import com.desay.sport.friend.CustomListView;
import com.desay.sport.friend.CustomListView.OnRefreshListner;
import com.desay.sport.friend.FriendData;
import com.desay.sport.friend.GetMagActivity;
import com.desay.sport.friend.GetTime;
import com.desay.sport.friend.ListBottomLayout;
import com.desay.sport.friend.MyAdapter;
import com.desay.sport.friend.RankData;
import com.desay.sport.friend.SearchActivity;
import com.desay.sport.friend.SendServer;
import com.desay.sport.main.R;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.Socket_Data;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class tab3_ranking extends Fragment{

private static final String TAG = "tab3_ranking===";
private ImageView tab3_myhead,tab3msgi,tab3add;
private TextView tab3myname,tab3mydist,tab3myrank,tab3msgt,tab3ranktime;
//private Button button;
//private ListView listview;
private CustomListView listview;
private String rname="0";
private boolean fistrunser=false;
private ConnectionMAS CMAS;
private List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
private ArrayList<entity_sendrecord> send_list;
private static RankData RD;
private ListBottomLayout listlayout;
//private SimpleAdapter adapter;
private MyAdapter adapter;
private String fname="";
private int ss=0;
private Context context;
private View Layout;
private boolean showp=true;
//private static int msgnum=0;
private entity_userinfo info;
private sportDB db = null;
private GetTime gt;
private String webdata="";
private DisplayImageOptions options;
private String sdata="";
private int page=1;
private int dsize=0;

   public void onCreate (Bundle savedInstanceState) {
	   Log.e(TAG, "tab3_ranking_onCreate");
	   context = getActivity().getApplicationContext();
	   fistrunser=true;
	   IntentFilter intentFilter = new IntentFilter();
	   intentFilter.addAction(SportData.RECEIVER_RANKING);
	   intentFilter.addAction(SportData.RECEIVER_LOAD);
	   intentFilter.addAction(SportData.RECEIVER_UPLIST);
	   context.registerReceiver( rankBroadcastReceiver , intentFilter);
	   gt=new GetTime();
	   options = new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.headdefault)
    	.cacheInMemory()
  	    .cacheOnDisc()
  	    .build();
	   db = new sportDB(context);
	   super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	    Log.d(TAG, "tab3_ranking_onCreateView");
	    Layout = inflater.inflate(R.layout.tab3_ranking, container, false);
	    init();
	    uppersondate();
	    uprankdate(RD);	  
	return Layout;
    }
   public void onDestroyView() {
		  closenet();
		  if(db!=null)
		  db.closeDB();
	 super.onDestroyView();
    }
    public void onDetach()
   { 
	 context.unregisterReceiver(rankBroadcastReceiver); 
	 super.onDetach();
   };
   private void init()
   {   
	    tab3_myhead=(ImageView)Layout.findViewById(R.id.tab3_myhead);	    	    
	    tab3myname=(TextView)Layout.findViewById(R.id.tab3_myname); 
	    tab3mydist=(TextView)Layout.findViewById(R.id.tab3_mydist);
	    tab3myrank=(TextView)Layout.findViewById(R.id.tab3_myrank);
	    tab3ranktime=(TextView)Layout.findViewById(R.id.ranking_time);
	    tab3msgt=(TextView)Layout.findViewById(R.id.tab3_msgtext);
	    tab3msgi=(ImageView)Layout.findViewById(R.id.tab3_msgimg);
	    tab3add=(ImageView)Layout.findViewById(R.id.tab3_addfimg);
	    
//	    listview=(ListView)Layout.findViewById(R.id.tab3_list);
	    listview = (CustomListView)Layout.findViewById(R.id.tab3_list);
	    listlayout=new ListBottomLayout(context,Handlerrank);  
//	    listlayout.SetUpText(getString(R.string.updata_str));
	    listview.addFooterView(listlayout.getview(), null,true);	
	    setListener();
	    listview.setOnItemClickListener(new OnItemClickListener() 
		 { 			 
		    public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
		    {  
		    	if(!listlayout.isupdata())
		    	{
		    		fname=(String) ((TextView)arg1.findViewById(R.id.litem_name1)).getText();//获取点中商品的名称
		    	    Message msg = new Message();
		  	        msg.what = 9;
		  	        Handlerrank.sendMessage(msg);
		    	}
		    }   
        }); 
	    tab3msgi.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		SportData.msgnum=0;
 	    		tab3msgt.setText("");
 	 			tab3msgi.setVisibility(View.GONE);
 	    		Intent intent2=new Intent(context,GetMagActivity.class);
	            startActivityForResult(intent2,2);
 	    	}
 	    });
	    tab3add.setOnClickListener(new OnClickListener()
 	    {
 	    	public void onClick(View v)
 	    	{		
 	    		//跳到查找好友界面 
 	    		Message msg = new Message();
		  	    msg.what = 8;
	  	        Handlerrank.sendMessage(msg);
		    }   
        }); 		      
   }
   private BroadcastReceiver rankBroadcastReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) { 
	    	Log.d(TAG, ""+isAdded());
	    		String action = intent.getAction();
	    	int flag = intent.getIntExtra(SportData.LOAD, -1);
	    	    if (action.equals(SportData.RECEIVER_RANKING))
	    	     {
	    	    if(isAdded()&&fistrunser)
		        	{			 				
	    	    	 showp=true;
			         Message msg = new Message();
			         msg.what =16;
			         Handlerrank.sendMessage(msg);
				     fistrunser=false;
			        }
	    	    }else if (action.equals(SportData.RECEIVER_LOAD))
	    	    {
	    		 if(SportData.LOADSTATUS_LOGINOUT==flag||SportData.LOADSTATUS_LOGIN==flag)
	    		 { 
	    			 RD=null;
    			     if(data!=null)
    				  data.clear();
    			     page=1;
    			     SportData.msgnum=0;
	    		 }
	    		   if(isAdded())
	    	    	{
	    			    closenet();
	    	    	Message msg = new Message();
					msg.what = 13;
					Handlerrank.sendMessage(msg);
	    	    }
	    		
	    	 }else if (action.equals(SportData.RECEIVER_UPLIST))
	    	 {
	    		if(isAdded())
	    	    {
	    			 showp=true;
	    	    	 Message msg = new Message();
			         msg.what =16;
			         Handlerrank.sendMessage(msg);
				     fistrunser=false;
	    	    }
	    		
	        }
	    	
	    } 
	 };
 //从数据库更新个人信息
   public void uppersondate()
   {				
		String string="";
		info = db.GetUserInfo(context);
	    rname=SportData.getUserName(context);
	    if(info.getHeadPhoto()!=null && !info.getHeadPhoto().equals(""))
		{
			byte[] photo = SportData.hexStringToBytes(info.getHeadPhoto());
			Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0,photo.length);
			if(bmp==null)
				tab3_myhead.setImageResource(R.drawable.headdefault);
			else{
			   bmp = Bitmap.createScaledBitmap(bmp, 56, 56, true);
			   tab3_myhead.setImageBitmap(bmp);
			}
		}else{
	        tab3_myhead.setImageResource(R.drawable.headdefault);
		}
		if(rname.equals(SportData.DEFAULTUSERNAME))
		 {
			string=getString(R.string.noname_str);
			RD=null;
		 }
		else
			string=info.getShowname();
		tab3myname.setText(string);					
   }   
 //更新排名列表
 	public void uprankdate(RankData RD)
 	{
 		
 		if(RD!=null)
 		{
 		   data=RD.getListRankData();
 		   tab3mydist.setText(RD.getrankmymile());
 		   tab3myrank.setText(RD.getrankmynum());
 		   tab3ranktime.setText(RD.getrankmytime());
 		   if(data!=null&&data.size()>0)
 		   {
 			  if(data.size()==1&&data.get(0).get("f_name1").equals(rname))
 				 listlayout.showbuttom();
 			  else
 				 listlayout.hitbuttom(); 
// 			 adapter = new SimpleAdapter(context,data,R.layout.ranklist_item,   
// 		 		      new String[]{"f_headp","f_name1","f_name2","f_miles","f_num"},
// 		 		      new int[]{R.id.litem_head,R.id.litem_name1,R.id.litem_name2,R.id.litem_mils,R.id.litem_rank});
// 			 adapter.setViewBinder(new ListViewBinder()); 
 		   }
 		}else
 		{
 			//数据库获取距离
 			long distances=db.GetDistanceCount(rname,gt.get_startMillis(),gt.get_endMillis());
 			tab3mydist.setText(getString(R.string.ldis_str,SportData.getKilometer(distances)+""));
 			tab3myrank.setText(getString(R.string.rank_num_str,"N"));
 			listlayout.hitbuttom();
// 			data.clear();
 			
// 			adapter = new SimpleAdapter(context,data,R.layout.ranklist_item,   
// 		 		      new String[]{"f_headp","f_name1","f_name2","f_miles","f_num"},
// 		 		      new int[]{R.id.litem_head,R.id.litem_name1,R.id.litem_name2,R.id.litem_mils,R.id.litem_rank});  
// 			listview.setAdapter(adapter);
 		}
 		 if(SportData.msgnum>0)
	 	{
	 			tab3msgt.setText(SportData.msgnum+"");
			    tab3msgi.setVisibility(View.VISIBLE);
	 	}
 		 else
		{
 			tab3msgt.setText("");
 			tab3msgi.setVisibility(View.GONE);
		}
 		 adapter = new MyAdapter(context,options,data,R.layout.ranklist_item,   
	 		      new String[]{"f_headp","f_name1","f_name2","f_miles","f_num"},
	 		      new int[]{R.id.litem_head,R.id.litem_name1,R.id.litem_name2,R.id.litem_mils,R.id.litem_rank});
 		 listview.setAdapter(adapter); 
 	}
	
	Handler Handlerrank= new Handler(){	    	
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what)
    		{
    			case 1:
    				SharedPreferences userInfo = context.getSharedPreferences("setnet_msg",0);
    				boolean checkon_off= userInfo.getBoolean("k_opendialog",true);
    				Message msg1 = new Message();
    				if(checkon_off)				
    	    		    msg1.what =3;
    				else
    					msg1.what =10;
    				Handlerrank.sendMessage(msg1);	    				
    				break;	
    			case 2:	
    				CMAS=null;
    				listview.onRefreshComplete();
    				showToast(getString(R.string.nonet_str));
    				break;
    			case 3:
    				AskDialog AK=new AskDialog(context,Handlerrank);
    				AK.OpenaskDialog();
    				break;
    			case 4:
    				//请求排名
    				page=1;
//    				sdata=Socket_Data.SendRank(context,"20013",rname,gt.get_startday(),gt.get_endday());
    				showp=true;
    				sdata=Socket_Data.SendRankpage(context,"20013",rname,gt.get_startday(),gt.get_endday(),page);
    				CMAS=new ConnectionMAS(context,Handlerrank,sdata,6,7);
    				CMAS.start();
    				break;
    			case 5:
    				if(showp)
    				listlayout.openprogress();
    				handler.postDelayed(updateThread ,20000);
    				break;
    			case 6:
    				//更新排名
    				webdata=msg.getData().getString("body");
    				RankData getRD=new AnalyzeUtils(context).analyze_rank(webdata);
    				if(getRD!=null)
   				       RD=getRD;
   				    closenet();
   				    uppersondate();
    				uprankdate(RD);
    				showp=false;
//    				if(msgnum<0)
//    				{
    					sdata=Socket_Data.SendGetAddMsg(context,rname,"20012");
 				        CMAS=new ConnectionMAS(context,Handlerrank,sdata,15,7);
 				        CMAS.start();
 				        //启动服务同步数据到平台
 				        context.startService(new Intent(context,SendServer.class));
//    				}
 				      
    				break;
    			case 7:
    				//关闭进度条
    				closenet();
    				if(showp)
    				showToast(getString(R.string.sendrankfail_str));
    				listview.onRefreshComplete();
    				showp=false;
    				break;
    			case 8:
    				//添加好友按钮
    				Intent intent=new Intent(context,SearchActivity.class);
    		        startActivityForResult(intent,2);
    				break;
    			case 9:
    				//发送查看好友信息
    				if(CMAS==null)
    				{
    				showp=false;
    				sdata=Socket_Data.SendFriendCom(context,rname,fname,"20014");
    				CMAS=new ConnectionMAS(context,Handlerrank,sdata,14,7);
    				CMAS.start();
    				}
    				break;
    			case 10:
                    //从数据库中先上传没上传过的运动数据
    				send_list=db.GetNoSendRecord(rname,gt.get_startMillis(),gt.get_endMillis());
    				ss=send_list.size();
	        		 if(ss>0)
	        	     { 	    		         	    		      
	        			sdata=Socket_Data.SendSportData(context,rname,send_list,0,ss);
	            	 	CMAS=new ConnectionMAS(context,Handlerrank,sdata,11,7);
	        			CMAS.start();	
	        	     }
	        		 else
	        	     { 
	        			Message msg4 = new Message();
	 		  	        msg4.what = 4;
			  	        Handlerrank.sendMessage(msg4);
	        	     }
	    		     break;
    			case 11:
    				//更新数据库上传数据
    				db.UPSendRecordStatus(rname,send_list,0,ss);
    				Message msg11 = new Message();
 		  	        msg11.what = 4;
		  	        Handlerrank.sendMessage(msg11);
    			    break;
    			case 16:
    				if(rname.equals(SportData.DEFAULTUSERNAME))
    				{
    					showToast(getString(R.string.rankload_str));
    				}	    	
    				else{
    					Message msg12 = new Message();
    					msg12.what = 10;
    					Handlerrank.sendMessage(msg12);
    					
    				}
    				break;
    			case 13:
    				uppersondate();
    				uprankdate(RD);
    				break;
    			case 14:
    				webdata=msg.getData().getString("body");
			        FriendData fd=new AnalyzeUtils(context).analyze_friendmsg(webdata);
    				closenet();
    				if(fd!=null)
    				{
    					Intent intentf=new Intent(context,CheckFdActivity.class);
        				intentf.putExtra("FriendData",fd);
    			        startActivityForResult(intentf,2);
    				}
    				break;
    			case 15:
    				webdata=msg.getData().getString("body");
    				SportData.msgnum+= new AnalyzeUtils(context).saveSQL_msglist(webdata, db,rname);
    				closenet();
    				 if(SportData.msgnum>0)
    		 	 	{
    		 	 		tab3msgt.setText(SportData.msgnum+"");
    		 			tab3msgi.setVisibility(View.VISIBLE);
    		 	 	}else
    		 	 	{
    		 	 		tab3msgt.setText("");
    				    tab3msgi.setVisibility(View.GONE);
    		 	 	}
    				break;
    			case 12:
    				if(rname.equals(SportData.DEFAULTUSERNAME))
    				{
    					showToast(getString(R.string.rankload_str));
    				}	    	
    				else{
    				  dsize=data.size();
    				  if(dsize>=10*page)
    				     page++;
    				  showp=true;
    				  sdata=Socket_Data.SendRankpage(context,"20013",rname,gt.get_startday(),gt.get_endday(),page);
//    				  sdata=Socket_Data.SendRank(context,"20013",rname,gt.get_startday(),gt.get_endday());
    				  if(page==1)
    					  CMAS=new ConnectionMAS(context,Handlerrank,sdata,6,7);
    				  else 
    				      CMAS=new ConnectionMAS(context,Handlerrank,sdata,17,7);
    				  CMAS.start();
    				}
	    			break;
    			case 17:
    				webdata=msg.getData().getString("body");
    				RankData getRD2=new AnalyzeUtils(context).analyze_uprank(webdata,RD,data);
    				if(getRD2!=null)
   				       RD=getRD2;
   				    closenet();
    				showp=false;
            		if(data.size()>dsize)
    					 adapter.notifyDataSetChanged();
            		break;
    			  default:
    					break;
    			} 
    		}
    };
    private void showToast(String string)
	{
		showToast.normaltoast(context,string,showToast.ONE_SECOND);
	}
    Handler handler  = new Handler();  
    Runnable updateThread =  new Runnable(){  
       public void run() { 
    	   Message msg = new Message();
    	   msg.what = 7;
    	   Handlerrank.sendMessage(msg);
        }  
    };
    private void closenet()
    {
	  if(handler!=null&&updateThread!=null)
		   handler.removeCallbacks(updateThread);
	  listlayout.closeprogress();
	  if(CMAS!=null)
	  CMAS.closeSocket();
	  CMAS=null;
	  listview.onRefreshComplete();
    }
    private void setListener(){  
		  listview.setOnRefreshListner(new OnRefreshListner() {  
	          public void onRefresh() { 
	        	  if(rname.equals(SportData.DEFAULTUSERNAME))
  				{
	        		 listview.onRefreshComplete();
	        		 showToast(getString(R.string.rankload_str));
  				}
	        	 else if(listlayout.isupdata())
	 	        {  
	 	        	   listview.onRefreshComplete();
	 	        }
  				else{
  					Message msg12 = new Message();
  					msg12.what = 10;
  					Handlerrank.sendMessage(msg12);
  					showp=false;
  				}
	          }  
	     }); 
	  }
}
