package com.desay.sport.friend;

import java.util.List;
import java.util.Map;

import com.desay.sport.main.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {  
	private LayoutInflater mInflater;  
	private List<Map<String, Object>> list;  
	private int layoutID;  
	private String flag[];  
	private int ItemIDs[];
	private DisplayImageOptions options;
	   
	   public MyAdapter(Context context,DisplayImageOptions options,List<Map<String, Object>> list,  
	           int layoutID, String flag[], int ItemIDs[]) {  
	       this.mInflater = LayoutInflater.from(context);  
	       this.list = list;  
	       this.layoutID = layoutID;  
	       this.flag = flag;  
	       this.ItemIDs = ItemIDs;
	       this.options=options; 
	   }  
	   public int getCount() {  
	       // TODO Auto-generated method stub   
	       return list.size();  
	   }  
	   public Object getItem(int arg0) {  
	       // TODO Auto-generated method stub   
	       return list.get(arg0);  
	   }  
	   public long getItemId(int arg0) {  
	       // TODO Auto-generated method stub   
	       return arg0;  
	   }  
	   public View getView(int position, View convertView, ViewGroup parent) {  
	       convertView = mInflater.inflate(layoutID, null);  
	       for (int i = 0; i < flag.length; i++) {//备注1   
	           if (convertView.findViewById(ItemIDs[i]) instanceof TextView) {  
	               TextView tv = (TextView) convertView.findViewById(ItemIDs[i]);  
	               tv.setText((String) list.get(position).get(flag[i]));  
	           }else if (convertView.findViewById(ItemIDs[i]) instanceof ImageView) {  
	               ImageView iv = (ImageView) convertView.findViewById(ItemIDs[i]); 
	               Object data = list.get(position).get(flag[i]);
	              
	               if (data instanceof Integer) {  
	            	   iv.setImageResource((Integer)data);  
	                  } else if (data instanceof Bitmap) { 
	                	  iv.setImageBitmap((Bitmap) data);
	                  }else 
	                 {
	        	          
	                	try {
//	                		 Log.d("MyAdapter====="+i,"data==="+data);
	        	        	  if(data==null||data.equals(""))
	        	        		iv.setImageResource(R.drawable.headdefault); 
	      		          	else
	    			            ImageLoader.getInstance().displayImage((String)data,iv,options );
	    			      } catch (Exception e) {
	                      
	    			    }
	                 }
	           }
	           convertView.setBackgroundResource(R.drawable.list_rect_selector);
	       }  
	       return convertView;  
	   }  
  }
  
