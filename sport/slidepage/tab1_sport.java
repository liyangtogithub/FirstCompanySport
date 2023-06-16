package com.desay.sport.slidepage;
import java.util.Calendar;
import com.desay.pstest.toast.showToast;
import com.desay.sport.anim.myAnimationDrawable;
import com.desay.sport.data.SportData;
import com.desay.sport.data.para_animsport;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.GetTime;
import com.desay.sport.friend.SendServer;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.main.R;
import com.desay.sport.net.Info;
import com.desay.utils.NetworkConnection;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class tab1_sport extends Fragment implements OnClickListener,AnimationListener{

private static final String TAG = "tab1_sport";
private static final int RELOADDATA = 1;
private ImageView iv_distance;
private ImageView iv_run;
private ImageView tv_plan;
private AnimationDrawable anim_distance;
private myAnimationDrawable anim_run;
private ImageView iv_start;
private LinearLayout ll_sporttype;
private TextView tv_sporttype;
View Layout;
private Context context;
Dialog ad = null;
private GetTime time= null;
private TextView tv_data1;
private TextView tv_data2;
private TextView tv_data3;
private sportDB db = null;
private Handler handle = null;
private TextView tv_alldistance_number;
private TextView tv_lastrun_number;
private long lastdistance = -1;
private long monthdistance = -1;

private ImageView iv_monthdistance_bg     = null;
private ImageView iv_monthdistance_fg     = null;
private ImageView iv_monthdistance_rotate = null;
private ImageView iv_lastrun_fg     = null;
private ImageView iv_lastrun_rotate = null;
private Animation rotate_lastrun = null;
private Animation rotate_monthdistance = null;
private DistanceAnim updateDistance;

private Animation alphato0_lastrun = null;
private Animation alphato1_lastrun = null;

private Animation alpha_monthdistance = null;
private Animation alphato0_monthdistance = null;
private Animation alphato1_monthdistance = null;

private static int count = -1;
@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		IntentFilter intentFilter = new IntentFilter(); 
	    intentFilter.addAction(SportData.RECEIVER_LOAD);
	    intentFilter.addAction(SportData.RECEIVER_CLOSEANIM);    
	    getActivity().getApplicationContext().registerReceiver( sportBroadcastReceiver , intentFilter);	
	    SportData.IFFIRSTLOGIN = true;
		super.onCreate(savedInstanceState);
	}
    public void onDestroyView() {
    	if(db!=null)
	  db.closeDB();
      super.onDestroyView();
    }

@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity().getApplicationContext();
		time = new GetTime();
		db = new sportDB(context);
		updateDistance = new DistanceAnim();
		
		if(BuildConfig.DEBUG)Log.d(TAG, "tab1_sport_onCreateView");
		Layout = inflater.inflate(R.layout.tab1_sport, container, false);
		iv_start = (ImageView)Layout.findViewById(R.id.iv_start);
		tv_sporttype = (TextView)Layout.findViewById(R.id.tv_sporttype);
		ll_sporttype = (LinearLayout)Layout.findViewById(R.id.ll_sporttype);
		tv_plan=(ImageView)Layout.findViewById(R.id.tv_plan);
		tv_alldistance_number = (TextView)Layout.findViewById(R.id.tv_alldistance_number);
		tv_lastrun_number = (TextView)Layout.findViewById(R.id.tv_lastrun_number);
		tv_data1 = (TextView)Layout.findViewById(R.id.tv_data1);
		tv_data2 = (TextView)Layout.findViewById(R.id.tv_data2);
		tv_data3 = (TextView)Layout.findViewById(R.id.tv_data3);

		ll_sporttype.setOnClickListener(this);
		iv_start.setOnClickListener(this);
		tv_plan.setOnClickListener(this);
		
	    handle = new Handler() 
	       {
              public void handleMessage(Message msg)
              {
                     switch (msg.what)
                     {
                     case R.id.tv_alldistance_number:
                    	 tv_alldistance_number.setText(SportData.getKilometer(msg.arg1)+""); 
                    	 if(msg.arg1==monthdistance)
                    	 {
                    		 iv_monthdistance_bg.startAnimation(alpha_monthdistance);
                    	 }
                    	 break;
                     case R.id.tv_lastrun_number:
                    	 tv_lastrun_number.setText(SportData.getKilometer(msg.arg1)+"");
                    	 if(msg.arg1==lastdistance)
                    	 {
             				if(monthdistance!=0)
             				{
             					 iv_monthdistance_rotate.startAnimation(rotate_monthdistance);
             			    }
                    	 }
                    	 break;
                     case RELOADDATA:
                         reGetDistance();
                		 clearAnim();
                	     reLoadData(monthdistance,lastdistance);
                    	break;
                     }
                     super.handleMessage(msg);
              }                   
	       };
        tv_sporttype.setText(getResources().getStringArray(R.array.sporttype)[SportData.getSportType(context)]);
        initAnim();
        
        
	return Layout;
	}

@Override
	public void onStart() {
		// TODO Auto-generated method stub
//	    Log.d(TAG, "lastdistance1="+lastdistance);
//	    Log.d(TAG, "monthdistance1="+monthdistance);
//        long pre_monthdistance = monthdistance;
        reGetDistance();
        Log.d(TAG, "lastdistance="+lastdistance);
        Log.d(TAG, "monthdistance="+monthdistance);
   
//        if(pre_monthdistance!=-1 && monthdistance>pre_monthdistance)// 
//        {//刚锻炼完退回到运动界面的情�?
//        	Log.d(TAG, "1111111111111111111111");
//        	clearAnim();
//        	reLoadData(0,0);
//        	if(lastdistance!=0 )
//			{
//        		iv_lastrun_rotate.startAnimation(rotate_lastrun);
//			}
//			else
//			{	
//				if(monthdistance!=0)
//				{
//					iv_monthdistance_rotate.startAnimation(rotate_monthdistance);
//			    }
//			}
//        }
//        else
//        {
//        	Log.d(TAG, "222222222222222222222222");
            if(SportData.IFFIRSTLOGIN == true)
            {
            	if(lastdistance!=0 )
    			{
            		iv_lastrun_rotate.startAnimation(rotate_lastrun);
    			}
    			else
    			{	
    				if(monthdistance!=0)
    				{
    					iv_monthdistance_rotate.startAnimation(rotate_monthdistance);
    			    }
    			}
            }
            else
            {
            	reLoadData(monthdistance,lastdistance);
            }	
//        }
		super.onStart();
	}

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	switch(v.getId())
	{
	case R.id.iv_start:
		Intent i = new Intent();
		if(db.HaveUserInfo(context)&&db.GetUserInfo(context).getWeight()!=null&&(!(db.GetUserInfo(context).getWeight()).equals("")))
		{
			Boolean status = NetworkConnection.isConnectingToInternet(context);
		      if (status){
		    	  i.setClass(context,LocationOverlay.class);
		    	  startActivity(i);
		      }else{
		    	  showToast.normaltoast(context, getString(R.string.nonet_str), showToast.ONE_SECOND); 
		      }
		}
		else
		{
			showToast.normaltoast(context, getString(R.string.inserinfo_toast), showToast.TWO_SECOND);	
			i.setClass(context,Info.class);
			startActivity(i);
		}
			
			context.sendBroadcast(new Intent(SportData.RECEIVER_EXITSPORT));
		break;
	case R.id.ll_sporttype:
		showDialog();
		break;	
	}
}

private void initAnim()
{
    iv_monthdistance_bg      = (ImageView)Layout.findViewById(R.id.iv_monthdistance_bg);
    iv_monthdistance_fg      = (ImageView)Layout.findViewById(R.id.iv_monthdistance_fg);
    iv_monthdistance_rotate  = (ImageView)Layout.findViewById(R.id.iv_monthdistance_rotate);
    iv_lastrun_fg            = (ImageView)Layout.findViewById(R.id.iv_lastrun_fg);
    iv_lastrun_rotate        = (ImageView)Layout.findViewById(R.id.iv_lastrun_rotate);
    
    rotate_lastrun=AnimationUtils.loadAnimation(context,R.anim.rotate);
    rotate_monthdistance=AnimationUtils.loadAnimation(context,R.anim.rotate); 
	alphato0_lastrun = AnimationUtils.loadAnimation(context,R.anim.alphato0); 
	alphato0_monthdistance = AnimationUtils.loadAnimation(context,R.anim.alphato0); 
    alphato1_lastrun = AnimationUtils.loadAnimation(context,R.anim.alphato1); 
	alphato1_monthdistance = AnimationUtils.loadAnimation(context,R.anim.alphato1); 
	alpha_monthdistance = AnimationUtils.loadAnimation(context,R.anim.alpha); 
	rotate_lastrun.setAnimationListener(this);
	rotate_monthdistance.setAnimationListener(this);
	alphato0_lastrun.setAnimationListener(this);
	alphato0_monthdistance.setAnimationListener(this);	
	alphato1_lastrun.setAnimationListener(this);
	alphato1_monthdistance.setAnimationListener(this);
	alpha_monthdistance.setAnimationListener(this);
}

private void clearAnim()
{//清除动画
		count = -1; 
	    iv_monthdistance_bg.clearAnimation();
	    iv_monthdistance_fg.clearAnimation();
	    iv_monthdistance_rotate.clearAnimation();
	    iv_lastrun_fg.clearAnimation();
	    iv_lastrun_rotate.clearAnimation();
}

private void reGetDistance()
{//重新读取距离
	Calendar cal = Calendar.getInstance();		
	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1 , 1, 0, 0, 0);//下月开始时�?
	long endTime = cal.getTimeInMillis();
	String currentUserName = SportData.getUserName(context);
    lastdistance = db.GetLastDistance(currentUserName);
    monthdistance = db.GetDistanceCount(currentUserName, time.get_startMillis(), endTime);
    tv_data1.setText(SportData.getFormatTime(db.GetDurationTimeCount(currentUserName, time.get_startMillis(), endTime)));
	tv_data2.setText(db.GetCalorieCount(currentUserName, time.get_startMillis(), endTime ) +"");
	tv_data3.setText(db.GetSportTimes(currentUserName, time.get_startMillis(), endTime)+"");	
}

private void reLoadData(long tmonthdistance ,long tlastdistance)
{//重新加载运动首页动画及显�?
	tv_alldistance_number.setText(SportData.getKilometer(tmonthdistance));
	tv_lastrun_number.setText(SportData.getKilometer(tlastdistance));
	if(tmonthdistance!=0)//check if not first show
	{
		iv_monthdistance_fg.setVisibility(View.VISIBLE);
		iv_monthdistance_rotate.setVisibility(View.GONE);
	}
	else
	{
		iv_monthdistance_fg.setVisibility(View.GONE);
		iv_monthdistance_rotate.setVisibility(View.VISIBLE);
	}
	if(tlastdistance!=0)//check if not first show
	{
		iv_lastrun_fg.setVisibility(View.VISIBLE);
		iv_lastrun_rotate.setVisibility(View.GONE);
	}
	else
	{
		iv_lastrun_fg.setVisibility(View.GONE);
		iv_lastrun_rotate.setVisibility(View.VISIBLE);
	}
}

@Override
public void onAnimationEnd(Animation animation) {
	// TODO Auto-generated method stub
	 if(animation.equals(rotate_lastrun))
	 {
		 iv_lastrun_rotate.startAnimation(alphato0_lastrun); 
		 iv_lastrun_fg.setVisibility(View.VISIBLE);
		 iv_lastrun_fg.startAnimation(alphato1_lastrun);


	 }
	 else if(animation.equals(alphato0_lastrun))
	 {
		 if(lastdistance != 0)
		 {
			 updateDistance.setData(lastdistance, R.id.tv_lastrun_number);
			 new Thread(updateDistance).start();
			 iv_lastrun_rotate.setVisibility(View.GONE);
		 }

	 }
	 else if(animation.equals(rotate_monthdistance))
	 {
		 iv_monthdistance_rotate.startAnimation(alphato0_monthdistance); 
		 iv_monthdistance_fg.setVisibility(View.VISIBLE);
		 iv_monthdistance_fg.startAnimation(alphato1_monthdistance);

	 }
	 else if(animation.equals(alphato0_monthdistance))
	 {
		 if(monthdistance != 0)
		 {
			 updateDistance.setData(monthdistance, R.id.tv_alldistance_number);
			 new Thread(updateDistance).start();
			 iv_monthdistance_rotate.setVisibility(View.GONE);
		 }

	 }
}

@Override
public void onAnimationRepeat(Animation animation) {
	// TODO Auto-generated method stub
	
}

@Override
public void onAnimationStart(Animation animation) {
	// TODO Auto-generated method stub
	
}

public void onDetach()
{ 
 context.unregisterReceiver(sportBroadcastReceiver); 
 super.onDetach();
};

private BroadcastReceiver sportBroadcastReceiver = new BroadcastReceiver() { 
    @Override 
    public void onReceive(Context context, Intent intent) { 
    	if (BuildConfig.DEBUG)Log.d(TAG, "sportBroadcastReceiver");
    	if(isAdded())
    	{
	    	if(SportData.IFFIRSTLOGIN && intent.getAction()==SportData.RECEIVER_CLOSEANIM)
	    	{//slide to other page
	    		Log.d(TAG, "closeanim");
	    		clearAnim();
	        	reLoadData(monthdistance,lastdistance);
	    		SportData.IFFIRSTLOGIN = false;
	    	}
	    	else
	    	{
	    		Message msg = new Message();
	    		msg.what = RELOADDATA;
	    		handle.sendMessage(msg);
	    	}
    	}
    } 
 };
 public void onPause() 
 { 
 	if(SportData.IFFIRSTLOGIN)
 	{//into new activity or current page is destroy!
 		Log.d(TAG, "closeanim");
 		clearAnim();
    	reLoadData(monthdistance,lastdistance);
 		SportData.IFFIRSTLOGIN = false;
 	} 
 	super.onPause();
 };

private void showDialog() { 
	Dialog_SportType dfrag =new Dialog_SportType(); 
    dfrag.setSportType(tv_sporttype);
    dfrag.show(getFragmentManager(), "dialog"); 
}

class DistanceAnim implements Runnable
{
//	private int count = -1;
	private int interval = -1;
	private long distance = -1;
	private int i = 1;
    private int type;
	
	public void setData(long t_distance,int animtype)
	{
		para_animsport para = SportData.getAnimParameter(t_distance);
		distance = t_distance;
		count = para.getCount();
		interval = para.getInterval();
		type = animtype;
		i = 1;
	}
	
    public void run()
    {   
		Log.d(TAG, "interval="+interval);
		Log.d(TAG, "distance="+distance);
    	while (!Thread.currentThread().isInterrupted() && i<=count)
        {
               Message msg = new Message();
               msg.what = type;
               if(i==count)
               {
            	   msg.arg1 = (int) distance;
               }	
               else 
               {
            	   msg.arg1 = (int) distance*i/count;
               }	
       	       handle.sendMessage(msg);
               try
               {
                      Thread.sleep(interval);
               }
               catch (InterruptedException e)
               {
                      Thread.currentThread().interrupt();
               }
   		       i++;
       }
    }
}
}
