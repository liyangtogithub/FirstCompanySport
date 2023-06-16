package com.desay.sport.net;

import java.util.Calendar;
import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.SendServer;
import com.desay.sport.main.R;
import com.desay.sport.net.ImageService.ImageCallback;
import com.desay.utils.AnalyzeUtils;
import com.desay.utils.ConnectionMAS;
import com.desay.utils.CustomProgressDialog;
import com.desay.utils.NetworkConnection;
import com.desay.utils.Socket_Data;
import com.desay.utils.WebData;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity  implements OnClickListener{
	protected static final String TAG = "Login";
	private TextView tv_title;
	private ImageView iv_return;
	private EditText et_username;
	private EditText et_password;
	private EditText et_repassword;
	private EditText et_email;
	private TextView et_forgetpass;
	private RelativeLayout rl_thirdlogin;
	private Button btn_reg;
	private Button btn_login;
	private Button btn_send;
	
	private ImageView iv_qq = null;
	private ImageView iv_sina = null;
	private ImageView iv_tencent = null;
	
	Context context;
	Handler handle;
	private int status ;
	private CustomProgressDialog progressDialog = null;
	private ConnectionMAS CMAS;
	private String username ="";
	private String password ="";
	private String repassword ="";
	private String emailt ="";
	private sportDB db;
	private Platform pf = null;
	private String showname="";
	private String failstr="";
//	private Tencent mTencent = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.login);
		status = SportData.WINDOW_LOGIN;
		context = Login.this;
		tv_title = (TextView) findViewById(R.id.tv_title);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		et_repassword  = (EditText) findViewById(R.id.et_repassword);
		et_email  = (EditText) findViewById(R.id.et_email);
		et_forgetpass = (TextView) findViewById(R.id.et_forgetpass);
		btn_reg = (Button) findViewById(R.id.btn_reg);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_send= (Button) findViewById(R.id.btn_send);
		rl_thirdlogin = (RelativeLayout) findViewById(R.id.rl_thirdlogin);
		iv_qq = (ImageView) findViewById(R.id.iv_qq);
		iv_sina = (ImageView) findViewById(R.id.iv_sina);
		iv_tencent = (ImageView) findViewById(R.id.iv_tencent);
		tv_title.setText(getString(R.string.login));
		
		btn_reg.setOnClickListener(this);
		btn_login.setOnClickListener(this);
		iv_return.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		et_forgetpass.setOnClickListener(this);
		iv_qq.setOnClickListener(this);
		iv_sina.setOnClickListener(this);
		iv_tencent.setOnClickListener(this);
		
		db = new sportDB(context);
		regReceiver();
		ShareSDK.initSDK(context);
		
//		mTencent = Tencent.createInstance("100568578", this.getApplicationContext());

	}
	
//	private class BaseUiListener implements IUiListener {
//
//		@Override
//		public void onComplete(JSONObject response) {
//			com.desay.sport.share.Util.showResultDialog(context, response.toString(), "登录成功");
//			doComplete(response);
//		}
//
//		protected void doComplete(JSONObject values) {
//
//		}
//
//		@Override
//		public void onError(UiError e) {
//			Util.toastMessage(Login.this, "onError: " + e.errorDetail);
//			Util.dismissDialog();
//		}
//
//		@Override
//		public void onCancel() {
//			Util.toastMessage(Login.this, "onCancel: ");
//			Util.dismissDialog();
//		}
//	}
	
	public void onDestroy() {
		   context.unregisterReceiver( loadBroadcastReceiver );
		   closenet();
		   db.closeDB();
		   ShareSDK.stopSDK(context);
		   super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.iv_return:
			if(status != SportData.WINDOW_LOGIN)
			{
				changeWindowsUI(SportData.WINDOW_LOGIN);
			}
			else
			{
				finish();
			}
			break;
		case R.id.btn_reg:
			if(status == SportData.WINDOW_LOGIN)
			{		 
				changeWindowsUI(SportData.WINDOW_REG);
			}
			else
			{
				if(checkInputValid())
				{
                    //增加注册逻辑
					showname=username;
					Message msg = new Message();
		  	   	    msg.what = SportData.REG_SEND;
		  	   	    Handlerload.sendMessage(msg);
				}
			}

			break;
		case R.id.btn_login:
			if(checkInputValid())
			{
				showname=username;
				Message msg = new Message();
	  	   	    msg.what =SportData.LOAD_SEND;
	  	   	    Handlerload.sendMessage(msg);
			}
			break;
		case R.id.et_forgetpass:
			if(status == SportData.WINDOW_LOGIN)
			{		 
				changeWindowsUI(SportData.WINDOW_EMAIL);
			}
			break;
		case R.id.btn_send:
			if(checkInputValid())
			{
				Message msg = new Message();
	  	   	    msg.what =SportData.EMAIL_SEND;
	  	   	    Handlerload.sendMessage(msg);
			}
			break;
		case R.id.iv_qq:
		case R.id.iv_sina:
		case R.id.iv_tencent:
			if(NetworkConnection.isConnectingToInternet(context))
			{
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                v.startAnimation(shake);
                Message msg = new Message();
                msg.what = SportData.ANIMATION_END;
                msg.arg1 = v.getId();
                Handlerload.sendMessageDelayed(msg, 300); 		
			}
			else
			    showToast.normaltoast(context, context.getString(R.string.nonet_str), showToast.ONE_SECOND); 
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(status!= SportData.WINDOW_LOGIN)
			{
				changeWindowsUI(SportData.WINDOW_LOGIN);
			}
			else
			{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void getPlatform(int pf_id)
	{
		switch(pf_id)
		{
			case R.id.iv_qq:
//				if (!mTencent.isSessionValid()) {
//					IUiListener listener = new BaseUiListener() {
//						@Override
//						protected void doComplete(JSONObject values) {
//
//						}
//					};
//					mTencent.login(this, "all", listener);
//				} else {
//					mTencent.logout(this);
//				}
				pf = new QZone(context);
				break;
			case R.id.iv_sina:
				pf = ShareSDK.getPlatform(context, SinaWeibo.NAME);
				break;
			case R.id.iv_tencent:
				pf = ShareSDK.getPlatform(context, TencentWeibo.NAME);
				break;
		}
		pf.setPlatformActionListener(pl);
		pf.authorize();
//		finish();
	}
	private PlatformActionListener pl = new PlatformActionListener(){

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
			Log.e(TAG, "====onCancel====");
		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
            Log.e(TAG, "onComplete====plat.getDb().getUserId()"+arg0.getDb().getUserId());               
            Log.e(TAG, "onComplete====plat.getDb().getUserName()"+arg0.getDb().getUserName());
        	username = arg0.getDb().getUserId();
    		password = "123";
    		showname=arg0.getDb().getUserName();
    		Message msg = new Message();
  	   	    msg.what =SportData.THRID_LOAD;
  	   	    Handlerload.sendMessage(msg);
		}

	 
		
		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			Log.e(TAG, "====onError:msg="+arg2.getMessage()); 	
			showToast.normaltoast(context, getString(R.string.error_thirdlogin), showToast.TWO_SECOND);
		}};
		
	private BroadcastReceiver loadBroadcastReceiver = new BroadcastReceiver() { 
	    @Override 
	    public void onReceive(Context context, Intent intent) 
	    { 
          int flag = intent.getIntExtra(SportData.LOAD, -1);
          if(SportData.LOADSTATUS_SYNC==flag)
          {
        		dialogDismiss(R.string.syncok_str);	
          }
          else if(SportData.LOADSTATUS_SYNCFAIL==flag)
          {
       		    dialogDismiss(R.string.syncfail_str);
          } 
	    } 
	 };
	 private void dialogDismiss(int string_id)
	 {
 		if (progressDialog != null){  
    		progressDialog.dismiss();  
    		progressDialog = null;         
    	 }
		showToast.normaltoast(context, getString(string_id), showToast.ONE_SECOND);
		finish();
	 }
		private void regReceiver()
		{
			IntentFilter intentFilter = new IntentFilter(); 
		    intentFilter.addAction(SportData.RECEIVER_LOAD); 
		    context.registerReceiver(loadBroadcastReceiver , intentFilter);
		}
		
	private void changeWindowsUI(int st)
	{
		switch(st)
		{
		case SportData.WINDOW_REG://切换到注册界面
			tv_title.setText(getString(R.string.register));
			rl_thirdlogin.setVisibility(View.GONE);
			et_username.setText("");
			et_password.setText("");
			et_repassword.setText("");
			et_forgetpass.setText("");
			et_email.setText("");
			et_password.setVisibility(View.VISIBLE);
			et_repassword.setVisibility(View.VISIBLE);
			et_email.setVisibility(View.VISIBLE);
			btn_login.setVisibility(View.GONE);
			btn_reg.setVisibility(View.VISIBLE);
			btn_send.setVisibility(View.GONE);
			status = SportData.WINDOW_REG;
			break;
		case SportData.WINDOW_LOGIN://切换到登录界面
			tv_title.setText(getString(R.string.login));
			rl_thirdlogin.setVisibility(View.VISIBLE);
			et_username.setText("");
			et_password.setText("");
			et_repassword.setText("");
			et_forgetpass.setText(getString(R.string.forgetpass));
			et_password.setVisibility(View.VISIBLE);
			et_repassword.setVisibility(View.GONE);
			et_email.setVisibility(View.GONE);
			btn_login.setVisibility(View.VISIBLE);
			btn_reg.setVisibility(View.VISIBLE);
			btn_send.setVisibility(View.GONE);
			status = SportData.WINDOW_LOGIN;
			break;
		case SportData.WINDOW_EMAIL://切换到找回密码界面
			et_username.setText("");
			et_email.setText("");
			tv_title.setText(getString(R.string.emailpass));
			rl_thirdlogin.setVisibility(View.GONE);
			et_forgetpass.setText(getString(R.string.email_msg));
			et_password.setVisibility(View.GONE);
			et_repassword.setVisibility(View.GONE);	
			et_email.setVisibility(View.VISIBLE);
			btn_login.setVisibility(View.GONE);
			btn_reg.setVisibility(View.GONE);
			btn_send.setVisibility(View.VISIBLE);
			status = SportData.WINDOW_EMAIL;
			break;
		
		}
	}
	
	
	
	private boolean checkInputValid()
	{
		username = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();
		repassword = et_repassword.getText().toString().trim();
		emailt = et_email.getText().toString().trim(); 
		boolean retValue = false;
		
		Message msg = new Message();
		msg.what = -1;
	    if(username.equals(""))
	    {
	    	msg.what = SportData.TOAST_USERNAME_NULL;
	    	et_username.requestFocus();
	    }
	    else if((et_password.isShown()&&password.equals(""))||(et_repassword.isShown() && repassword.equals("")))
	    {
	    	msg.what = SportData.TOAST_PASSWORD_NULL;	
	    	if(password.equals(""))
	    		et_password.requestFocus();
	    	else
	    		et_repassword.requestFocus();
	    }
	    else if(et_repassword.isShown() && !password.equals(repassword))
	    {
	    	msg.what = SportData.TOAST_PASSWORD_INVALID;	
    		et_password.requestFocus();
	    }
	    else if(et_email.isShown()&&!SportData.isEmail(emailt))
	    {
	    	msg.what = SportData.TOAST_EMAIL_NULL;	
	    	et_email.requestFocus();
	    }
	    else
	    {
	    	retValue = true;
	    }
	    Log.d(TAG, "msg.what="+msg.what);
	   if(msg.what!=-1)Handlerload.sendMessage(msg); 
		return retValue;
	}
	private void saveregmsg()
	 {		 
		 SportData.UpdateUserName(Login.this,username);	
		 SportData.UpdateUserPassword(Login.this,password);
		 db.UpdateDefaultRecord(context,username,1);
		 db.SaveUserPassword(context,password,showname);
	 	 Intent sendintent = new Intent(SportData.RECEIVER_LOAD);
	  	 sendintent.putExtra(SportData.LOAD,SportData.LOADSTATUS_LOGIN);
     	 sendBroadcast(sendintent);
//    	 Login.this.startService(new Intent(Login.this,SendServer.class));
     	 finish();
	 }
	private void saveloadmsg(final entity_userinfo info)
	 {
		SportData.UpdateUserName(Login.this,username);
		SportData.UpdateUserPassword(Login.this,password);
        boolean firstlogin = !SportData.getUserName(context).equals(SportData.DEFAULTUSERNAME) && !db.HaveUserInfo(context); 
		db.UpdateDefaultRecord(context,username,0);
		db.SaveUserPassword(context,password,showname);
		 try{
			 if(info!=null)
	         {
				 info.setUpLoad(SportData.UPLOAD);
		        	 db.SaveUserInfo(context,info);
		       	 	 Intent sendintent = new Intent(SportData.RECEIVER_LOAD);
		    	  	 sendintent.putExtra(SportData.LOAD,SportData.LOADSTATUS_LOGIN);
		        	 sendBroadcast(sendintent);
				 if(info.getheadphotourl()!=null)
		         {
				   String cachedImage = new ImageService().loadDrawable(info.getheadphotourl().trim(),
					    new ImageCallback() {
						public void imageLoaded(String imageUrl) {
						    if(imageUrl!=null&&!imageUrl.equals(""))
						 	{
							info.setHeadPhoto(imageUrl);
							info.setUpLoad(SportData.UPLOAD);
							Log.d(TAG, "db.SaveUserInfo=====");
						    db.SaveUserInfo(context,info);
					   	 	Intent sendintent = new Intent(SportData.RECEIVER_LOAD);
				    	  	sendintent.putExtra(SportData.LOAD,SportData.LOADSTATUS_LOGIN);
				        	sendBroadcast(sendintent);
						 	}
						}
			       });
		         }
	         }		 
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }	
		if(firstlogin)
		{
			insertAccountData(context,R.string.please_sync);
		}
		else
		{
			Login.this.startService(new Intent(Login.this,SendServer.class));
			finish();
		}
	 }
	
	 public void insertAccountData(Context context,int string_id)
	 {
		 Boolean status = NetworkConnection.isConnectingToInternet(context);
	      if (status){
	    	  if (progressDialog == null)  
	    		  progressDialog = CustomProgressDialog.createDialog_Sync(context,string_id);  
		      if (!progressDialog.isShowing())
		    	  progressDialog.show();

		  	 Calendar cal = Calendar.getInstance();
			 int year = cal.get(Calendar.YEAR);
			 int month = cal.get(Calendar.MONTH);
		     Intent service = new Intent(context,SendServer.class); 
	 		 service.putExtra("intentmcmd",11);
	 		 service.putExtra("year",year);
	 		 service.putExtra("month",month);
	 		 context.startService(service);
	      }else
	      {
	    	  showToast.normaltoast(context, context.getString(R.string.nonet_str), showToast.ONE_SECOND); 
	      }
	   
	 }
    Handler Handlerload= new Handler(){
    	
    	public void handleMessage(Message msg) {
    		super.handleMessage(msg);
    		switch(msg.what)
    		{
    			case SportData.LOAD_SEND:
    			  //去平台判断是否有账号是否正确
    				failstr=getString(R.string.loadfail_str);
     				String sdata=Socket_Data.SendLogin(context,username,password,username);
    				CMAS=new ConnectionMAS(context,Handlerload,sdata,SportData.LOAD_OK,SportData.LOAD_FAIL);
	    			CMAS.start();
    				break;	
    			case SportData.NO_NET:
					showToast.normaltoast(context, getString(R.string.nonet_str), showToast.ONE_SECOND);
	    			break;
    			case SportData.LOAD_OK:
    				//登陆成功
    				closenet();
    				entity_userinfo UVO=new AnalyzeUtils(context).analyze_load(msg.getData().getString("body"));    		   
    				saveloadmsg(UVO);
	    			break;
    			case SportData.LOAD_FAIL:
    				//登陆失败
    				closenet();
    			    String tstring=new AnalyzeUtils(context).analyze_returntoast(msg.getData().getString("body"));
    				if(tstring==null||tstring.equals(""))
    					tstring=failstr;
					showToast.normaltoast(context,tstring, showToast.ONE_SECOND);
    				break;
    			case SportData.OPEN_THREAD:
				    if (progressDialog == null){  
					      progressDialog = CustomProgressDialog.createDialog(context);  
					  } 
				    if (!progressDialog.isShowing())
					    progressDialog.show();
				    handler.postDelayed(updateThread ,20000);
    				break;
    			case SportData.SEND_FAIL:
    				closenet();
					showToast.normaltoast(context,failstr, showToast.ONE_SECOND);
    				break;
    			case SportData.REG_SEND:
    				failstr=getString(R.string.regfail_str);
    				String rdata=Socket_Data.SendRegister(context,username,password,username,emailt,WebData.machtype,WebData.machver);
					CMAS=new ConnectionMAS(context,Handlerload,rdata,SportData.REG_OK,SportData.LOAD_FAIL);
    				CMAS.start();
    				break;
    			case SportData.REG_OK:
    				closenet();
    				saveregmsg();
    				break;
    			case SportData.TOAST_USERNAME_NULL:
					showToast.normaltoast(context, getString(R.string.usernameisnull), showToast.ONE_SECOND);
					break;
				case SportData.TOAST_PASSWORD_NULL:
					showToast.normaltoast(context, getString(R.string.passwordisnull), showToast.ONE_SECOND);
					break;
				case SportData.TOAST_PASSWORD_INVALID:
					showToast.normaltoast(context, getString(R.string.passwordinvalid), showToast.ONE_SECOND);
					break;
				case SportData.TOAST_EMAIL_NULL:
					showToast.normaltoast(context, getString(R.string.emailinvalid), showToast.ONE_SECOND);
					break;
				case SportData.EMAIL_SEND:
					failstr=getString(R.string.sendtofail_str);
					String edata=Socket_Data.SendFindPassword(context,username,emailt);
					CMAS=new ConnectionMAS(context,Handlerload,edata,SportData.EMAIL_OK,SportData.LOAD_FAIL);
    				CMAS.start();
					break;
				case SportData.EMAIL_OK:
					closenet();
    			    String estring=new AnalyzeUtils(context).analyze_returntoast(msg.getData().getString("body"));
    			    if(estring==null||estring.equals(""))
    					tstring=getString(R.string.sendok_str);
    			    showToast.normaltoast(context,estring, showToast.TWO_SECOND);
//    			    finish();
    			    break;
				case SportData.ANIMATION_END:
					getPlatform(msg.arg1);
					break;
				case SportData.THRID_LOAD:
					failstr=getString(R.string.loadfail_str);
					String tdata=Socket_Data.SendLogin(context,username,password,showname);
    				CMAS=new ConnectionMAS(context,Handlerload,tdata,SportData.LOAD_OK,SportData.THRID_LOAD_FAIL);
	    			CMAS.start();
					break;
				case SportData.THRID_LOAD_FAIL :
    				closenet();
    			    String tfstring=new AnalyzeUtils(context).analyze_returntoast(msg.getData().getString("body"));
    				if(tfstring.equals(getString(R.string.login_other)))
    				{
    					String rfdata=Socket_Data.SendRegister(context,username,password,showname,emailt,WebData.machtype,WebData.machver);
    					CMAS=new ConnectionMAS(context,Handlerload,rfdata,SportData.REG_OK,SportData.LOAD_FAIL);
        				CMAS.start();
    				}else
    					showToast.normaltoast(context, getString(R.string.loadfail_str), showToast.ONE_SECOND);
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
    	    	   msg.what = SportData.SEND_FAIL;
    	    	   Handlerload.sendMessage(msg);
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
}
