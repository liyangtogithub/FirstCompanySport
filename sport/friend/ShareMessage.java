package com.desay.sport.friend;

import java.io.File;

import com.desay.sport.main.R;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareMessage {
//	shareMessage(context, "fenxiang","www", "this is share app1","/storage/sdcard0/DCIM/Camera/IMG_20130512_130005.jpg");
	/**   
	  * 分享功能   
	  * @param context 上下文   
	  * @param activityTitle Activity的名字   
	  * @param msgTitle 消息标题   
	  * @param msgText 消息内容   
	  * @param imgPath 图片路径，不分享图片则传null   
	  */     

	public void shareMsg(Context context,String name,String dis,String time,String imgPath) { 
		String activityTitle=context.getString(R.string.share_title_str);
		String msgTitle=context.getString(R.string.share_str);
		String msgText=context.getString(R.string.share_msg_str,name,dis,time); 			  
			  Intent intent = new Intent(Intent.ACTION_SEND);    
			  if (imgPath == null || imgPath.equals("")) {    
			   intent.setType("text/plain"); // ���ı�     
			  } else {    
			   File f = new File(imgPath);    
			   if (f != null && f.exists() && f.isFile()) {    
			     intent.setType("image/png");    
			     Uri u = Uri.fromFile(f);    
			     intent.putExtra(Intent.EXTRA_STREAM, u);    
			     }    
			  }    
			  intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);    
			  intent.putExtra(Intent.EXTRA_TEXT, msgText);    
			  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
			  context.startActivity(Intent.createChooser(intent, activityTitle));    
	} 
}
