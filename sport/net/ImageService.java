package com.desay.sport.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

import com.desay.sport.data.SportData;

public class ImageService {
	
	String drawable="";
	public String loadDrawable(final String imageUrl,final ImageCallback imageCallback) {
//			if (!drawable.equals("")) {
//				return drawable;
//			}
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((String) message.obj);
			}
		};
		new Thread() {
			@Override
			public void run() {
				
				try {
					drawable = loadImageFromUrl(imageUrl);
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = handler.obtainMessage(0,"");
					handler.sendMessage(message);
				}
				
			}
		}.start();
		return drawable;
	}

	public static String loadImageFromUrl(String url)throws Exception  {
		URL m;
		InputStream i = null;
		String headstring="";
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
			 ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		      byte[]  buffer = new byte[1204*1024];  
		      int len = 0;  
		      while ((len = i.read(buffer)) != -1)  
		      {  
		         outStream.write(buffer,0,len);  
		       }  
		        i.close();        
		        headstring=SportData.bytesToHexString(outStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();	
		}	
//		Drawable d = Drawable.createFromStream(i, "src");
		return headstring;
	}

	public interface ImageCallback {
		public void imageLoaded(String imageString);
	}


}
