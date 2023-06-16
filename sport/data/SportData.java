package com.desay.sport.data;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class SportData {
	public static final String RECEIVER_SPORT      = "com.desay.wantsport.sport";
	public static final String RECEIVER_RECORD     = "com.desay.wantsport.record";
	public static final String RECEIVER_RANKING    = "com.desay.wantsport.ranking";	
	public static final String RECEIVER_MEDAL      = "com.desay.wantsport.medal";
	public static final String RECEIVER_PLAN       = "com.desay.wantsport.plan";	
	public static final String RECEIVER_LOAD       = "com.desay.wantsport.load";
	public static final String RECEIVER_EXITSPORT  = "com.desay.wantsport.exitsport";
	public static final String RECEIVER_CLOSEDB    = "com.desay.wantsport.closedb";
	public static final String RECEIVER_CLOSEANIM  = "com.desay.wantsport.closeanim";
	public static final String RECEIVER_UPSILDE    = "com.desay.wantsport.upsilde";
	public static final String RECEIVER_UPLIST    = "com.desay.wantsport.uplist";
	
	public static final int TAB1_SPORT   = 0;
	public static final int TAB2_RECORD  = 1;
	public static final int TAB3_RANKING = 2;	
	public static final int TAB4_MEDAL  = 3;
	
	public static final int MODE_GPS  = 0;
	public static final int MODE_IWAN = 1;	
	
	public static final int TAB_STAGE_LOW    = 0;	
	public static final int TAB_STAGE_MIDDLE = 1;	
	public static final int TAB4_STAGE_HIGH  = 2;
	public static final int TAB_STEP_ONE  = 3;
	public static final int TAB_STEP_TWO  = 4;
	
	public static final int POSITION_SPLIT_PLAN   = 0;
	public static final int POSITION_SPLIT_FREE   = 2;	
		
	public static final int TOAST_SEX_NULL         = 3;	
	public static final int TOAST_BIRTHDAY_NULL    = 4;	
	public static final int TOAST_HEIGHT_NULL      = 5;
	public static final int TOAST_WEIGHT_NULL      = 6;	
	public static final int TOAST_CITY_NULL        = 7;
	public static final int LOAD_JUMP              = 0;
	public static final int LOAD_SEND              = 1;
	public static final int NO_NET                 = 2;
	public static final int LOAD_OK                = 3;
	public static final int LOAD_FAIL              = 4;
	public static final int OPEN_THREAD            = 5;
	public static final int SEND_FAIL              = 6;
	public static final int REG_SEND               = 7;
	public static final int REG_OK                 = 8;
	public static final int TOAST_USERNAME_NULL    = 9;	
	public static final int TOAST_PASSWORD_NULL    = 10;	
	public static final int TOAST_PASSWORD_INVALID = 11;
	public static final int TOAST_EMAIL_NULL       = 12;
	public static final int EMAIL_SEND             = 13;
	public static final int EMAIL_OK               = 14;
	public static final int ANIMATION_END          = 15;
	public static final int THRID_LOAD             = 16;
	public static final int THRID_LOAD_FAIL        = 17;
	public static final int SEND_OK                = 4;
	
	public static int LISTENTERTIME=20;
	public static double SPACETIME=1.0/3;
	public static double HEART_SPACETIME=1.0;
	public static int COLLECTTIME=20;
	public static double max_walk=8;
	public static double max_run=16;
	public static double max_drive=35;
	public static final int WINDOW_REG = 0;	//当前是登录界面
	public static final int WINDOW_LOGIN  = 1;//当前是注册界面
	public static final int WINDOW_EMAIL  = 2;//当前是找回密码界面
	
	public static final int PAGE_INFO     = 0;
//	public static final int PAGE_LOGIN    = 1;
	public static final int PAGE_MUSIC    = 1;	
	public static final int PAGE_FRIEND   = 2;
	public static final int PAGE_UPDATE   = 3;
	public static final int PAGE_BRACELET = 4;
	public static final int PAGE_SYNC     = 5;	
	public static final int PAGE_ABOUTUS  = 7;
	public static final int PAGE_MEDAL    = 6;
	public static final int PAGE_QUIT     = 8;	
	public static final int PAGE_MORE     = 9;	
	
	public static String    DEFAULTUSERNAME      = "defaultname";
	public static String    PREFERENCE_FILENAME  = "SP";	
	public static final String ID_ORIENTATION = "orientation";
	public static final String ID_RECORDTAB = "recordtab";	
	public static final String ID_BTSTATUS = "btstatus";	
	public static boolean   IFFIRSTLOGIN  = false;//detect if first login 
	
	public static final int ICON_SPORTTYPE      = 0;	
	public static final int ICON_SEX            = 1;		
	public static final int ICON_PHOTOSELECT    = 2;	
	public static final int ICON_SLIDEPAGE      = 3;
	public static final int ICON_MEDAL          = 4;
	public static final int ICON_NOMEDAL        = 5;

	
	public static final int REQUESTCODE_NONE = 0;
	public static final int REQUESTCODE_CITY       = 1;	
	public static final int REQUESTCODE_HEADPHOTO  = 2;
	public static final int REQUESTCODE_PHOTOGRAPH = 3;
	public static final int REQUESTCODE_PHOTOZOOM  = 4;
	public static final int REQUESTCODE_PHOTOSAVE  = 5;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	public static final String LOAD = "load"; 
	public static final String UPLOAD = "upload"; 
	public static final String UNUPLOAD = "unupload";
	
	public static final int TYPE_ACHIEVE =  0 ; 
	public static final int TYPE_MEDAL   =  1 ;
	
	public static final String ACTIVATE_FILE = "activate_file";
	public static final String IFACTIVATE = "ifactivate";
	public static final String IFPAIRED = "ifpaired";
	public static final String IFFOUNDED = "iffounded";
	
	public static final int LOADSTATUS_LOGIN      =  0 ; 
	public static final int LOADSTATUS_LOGINOUT   =  1 ;
	public static final int LOADSTATUS_SYNC       =  2 ;
	public static final int LOADSTATUS_SYNCFAIL   =  3 ;
	public static int msgnum=0;
	
	public static int MAX_TIMETICKS         =  5 ; 
	public static final int MAX_REPEATTIMES       =  3 ;
	
	public static int bat = 0;
	
	public static String bytesToHexString(byte[] src){   
		    StringBuilder stringBuilder = new StringBuilder("");   
		    if (src == null || src.length <= 0) 
		    {   
		        return null;   
		    }   
		   for (int i = 0; i < src.length; i++) {   
		       int v = src[i] & 0xFF;   
		        String hv = Integer.toHexString(v);   
		       if (hv.length() < 2) 
		       	{   
		            stringBuilder.append(0);   
		        }   
		        stringBuilder.append(hv);   
		    }   
		    return stringBuilder.toString();   
       } 
	
	public static byte[] hexStringToBytes(String hexString) {   
		    if (hexString == null || hexString.equals("")) {   
		        return null;   
		    }   
		    hexString = hexString.toUpperCase();   
		    int length = hexString.length() / 2;   
		    char[] hexChars = hexString.toCharArray();   
		    byte[] d = new byte[length];   
		    for (int i = 0; i < length; i++) {   
		        int pos = i * 2;   
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
		    }   
		   return d;   
		}
	private static byte charToByte(char c) {   
		    return (byte) "0123456789ABCDEF".indexOf(c);   
	   } 
	
	public static String GetSlidePageTitle(Context context,int pageID)
	{
		return context.getResources().getStringArray(R.array.slide_menu)[pageID];
	}

	public static String getUserName(Context context)//获取当前用户名
	{
		return GetStringPreference(context,sportDB.ID_USERNAME);
	}
	
	public static void UpdateUserName(Context context,String username)//更新当前用户名
	{
		SaveStringPreference(context,sportDB.ID_USERNAME,username);
	}
	
	public static String getUserPassword(Context context)//获取当前用户密码
	{
		return GetStringPreference(context,sportDB.ID_PASSWORD);
	}
	
	public static void UpdateUserPassword(Context context,String password)//更新当前用户密码
	{
		SaveStringPreference(context,sportDB.ID_PASSWORD,password);
	}
	public static int getSportType(Context context)//获取当前运动类型
	{
		return GetIntPreference(context,sportDB.ID_SPORTTYPE);
	}
	
	public static void UpdateSportType(Context context,int sporttype)//更新当前运动类型
	{
		SaveIntPreference(context,sportDB.ID_SPORTTYPE,sporttype);
	}
	public static int GetOrientation(Context context)//获取当前屏幕状态
	{
		return GetIntPreference(context,ID_ORIENTATION);
	}
	
	public static int SetOrientation(Context context,int orientation)//设置当前屏幕状态
	{
		SaveIntPreference(context,ID_ORIENTATION,orientation);
		return orientation;
	}
	
	public static int GetRecordTab(Context context)//获取当前屏幕状态
	{
		return GetIntPreference(context,ID_RECORDTAB);
	}
	
	public static int SetRecordTab(Context context,int recordtab)//设置当前屏幕状态
	{
		SaveIntPreference(context,ID_RECORDTAB,recordtab);
		return recordtab;
	}
	
	public static boolean GetBTStatus(Context context)//获取进入应用前蓝牙状态
	{
		return GetBoolPreference(context,ID_BTSTATUS);
	}
	
	public static boolean SetBTStatus(Context context,boolean status)//设置进入应用前蓝牙状态
	{
		SaveBoolPreference(context,ID_BTSTATUS,status);
		return status;
	}
	
	 public static void SaveBoolPreference(Context context,String type,boolean status)
	 {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILENAME, context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean(type, status);
			editor.commit(); 
	 }
	 
	 public static void SaveStringPreference(Context context,String type,String data)
	 {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILENAME, context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(type, data);
			editor.commit(); 
	 }
	 
	 public static void SaveIntPreference(Context context,String type,int data)
	 {
			SharedPreferences sp = context.getSharedPreferences(PREFERENCE_FILENAME, context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt(type, data);
			editor.commit(); 
	 }
	 public static boolean GetBoolPreference(Context context,String type)
	 {
			SharedPreferences settings = context.getSharedPreferences(PREFERENCE_FILENAME, context.MODE_PRIVATE);
			boolean result = settings.getBoolean(type, false);
			return result;
	 }
	
	 public static String GetStringPreference(Context context,String type)
	 {
			SharedPreferences settings = context.getSharedPreferences(PREFERENCE_FILENAME, context.MODE_PRIVATE);
			String result = settings.getString(type, DEFAULTUSERNAME);
			return result;
	 }
	 
	 public static int GetIntPreference(Context context,String type)
	 {
			SharedPreferences settings = context.getSharedPreferences(PREFERENCE_FILENAME, context.MODE_PRIVATE);
			int result = settings.getInt(type,0);
			return result;
	 }
	 public static boolean If_ExistSDCard() {  
			  if (android.os.Environment.getExternalStorageState().equals(  
			    android.os.Environment.MEDIA_MOUNTED)) {  
			   return true;  
			  } else  
			   return false;  
			 }
	
	public static Drawable getIconDrawable(Context context,int position,int type)
	{
		int resID = 0;
		switch(type)
		{
		case ICON_SPORTTYPE:
            resID = R.array.icons_sporttype;
			break;
		case ICON_SEX:
            resID = R.array.icons_sex;
			break;
		case ICON_PHOTOSELECT:
            resID = R.array.icons_photoselect;
			break;
		case SportData.ICON_SLIDEPAGE:
            resID = R.array.icons_slidepage;
			break;
		case ICON_MEDAL:
            resID = R.array.icons_medal;
			break;
		case SportData.ICON_NOMEDAL:
            resID = R.array.icons_nomedal;
			break;	
		}
		TypedArray icons = context.getResources().obtainTypedArray(resID);
		Drawable drawable = icons.getDrawable(position);
		icons.recycle();
		return drawable;
	}
	
public static Bitmap drawableToBitmap(Drawable drawable) {
        
        Bitmap bitmap = Bitmap
                        .createBitmap(
                                        drawable.getIntrinsicWidth(),
                                        drawable.getIntrinsicHeight(),
                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
}
	
	public static String getFormatTime(long second)
	{
		  StringBuffer result = new StringBuffer();
		long h = second / 3600;
		long m = second % 3600 / 60;
		long s = second  % 60;
		if(h>0)
		{
			if(h<10)
				result.append("0"+h+"h");
			else
				result.append(h+"h");
		}
		
		if(m<10)
			result.append("0"+m+"\'");
		else
			result.append(m+"\'");
		
		if(s<10)
			result.append("0"+s+"\"");
		else
			result.append(s+"\"");
		
		return result.toString();
	}
	public static String sporttpye(Context context,String aa)
	 {
		 String string=context.getString(R.string.type_walk);
		 if(aa.equals("0"))
		 {
			 string=context.getString(R.string.type_walk);
		 }else if(aa.equals("1"))
		 {
			 string=context.getString(R.string.type_run);
		 }else if(aa.equals("2"))
		 {
			 string=context.getString(R.string.type_bicycle);
		 }
		return string;
	 }
	
	public static para_animsport getAnimParameter(long distance)
	 {//默认数字播放 1 秒
			para_animsport para = new para_animsport();
        	if(distance > 0  && distance < 100)
        	{
        		distance/=1;
        	}
        	else if(distance >=100 && distance<1000)
        	{
        		distance/=10;
        	}
        	else if(distance >=1000 && distance<10000)
        	{
        		distance/=100;
        	}
        	else if(distance >=10000 && distance<100000)
        	{
        		distance/=1000;
        	}
        	else if(distance >=100000 && distance<1000000)
        	{
        		distance/=10000;
        	}	
    		para.setCount((int)distance);
    		para.setInterval(500/(int)distance);
		    return para;
	 }
	
	public static String getKilometer(long meter)
	 {
		  String result = null;
		  if(meter%1000==0)
		  {
			  result =""+ meter/1000;
		  }
		  else
		  {
	           if(meter<10000)
	           {
	        	   
	        	   if(meter%100==0)
	                   result = String.format("%4.1f",(double)meter/1000 );  	   
	        	   else if(meter%10==0)
                       result = String.format("%4.2f",(double)meter/1000 ); 
	        	   else
	        		   result = String.format("%4.3f",(double)meter/1000 );   
	           }  
	           else if(meter > 10000 && meter < 100000)
	           {
	        	  if(meter%100==0)
	                   result = String.format("%4.1f",(double)meter/1000 );    
	        	   else
	        		   result = String.format("%4.2f",(double)meter/1000 );  
	           }
	           else if(meter > 100000 && meter < 1000000)
	           {
	        	   result = String.format("%4.1f",(double)meter/1000 );   
	           }
	           else
	           {
	        	   result =""+ meter/1000;
	           }
		  }
		    return result;
	 }
	
	public static String getVersion(Context context)//获取版本号
	{
		try {
			PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return context.getString(R.string.version_unknown);
		}
	}
	//判断邮箱格式
	public static boolean isEmail(String strEmail) {
	     String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
	     Pattern p = Pattern.compile(strPattern);
	     Matcher m = p.matcher(strEmail);
	     return m.matches();
	}
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		 //创建一个和原始图片一样大小位图  
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	public static void sendBroadCast(Context context,String action)
	{
		Intent scanIntent = new Intent();
		scanIntent.setAction(action);
		context.sendBroadcast(scanIntent);
	}

}
