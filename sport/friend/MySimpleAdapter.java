package com.desay.sport.friend;

import java.util.List;
import java.util.Map;

import org.jdom2.Text;

import com.desay.sport.main.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class MySimpleAdapter extends SimpleAdapter {  
private int[] mTo;  
private String[] mFrom;  
private ViewBinder mViewBinder;  
 
private List<? extends Map<String, ?>> mData;  
 
private int mResource;  
private LayoutInflater mInflater; 
private Handler Handlersearch;
DisplayImageOptions options;
//private int mbt=0;
private Context mcontext;
 
public MySimpleAdapter(Context context,Handler Handlersearch,List<? extends Map<String, ?>> data,  
  int resource, String[] from, int[] to) {  
  super(context, data, resource, from, to);  
  mData = data;  
  mResource = resource;  
  mFrom = from;  
  mTo = to;  
//  mbt=bt;
  mcontext=context;
  this.Handlersearch=Handlersearch;
  mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
  options = new DisplayImageOptions.Builder()
	.showImageOnFail(R.drawable.headdefault)
	.cacheInMemory()
	.cacheOnDisc()
	.build();
}  
 
/** 
 * @see android.widget.Adapter#getView(int, View, ViewGroup) 
 */  
public View getView(int position, View convertView, ViewGroup parent) {
	convertView=createViewFromResource(position, convertView, parent, mResource);
	addListener(convertView); 
    return convertView;  
}  
 
private View createViewFromResource(int position, View convertView,  
    ViewGroup parent, int resource) {  
    View v;  
    if (convertView == null) {  
        v = mInflater.inflate(resource, parent, false);  
  
        final int[] to = mTo;  
        final int count = to.length;  
        final View[] holder = new View[count];  
 
        for (int i = 0; i < count; i++) {  
        holder[i] = v.findViewById(to[i]);  
        }  
        v.setTag(holder);  
    } else {  
        v = convertView;  
   }  
     bindView(position, v);  
    return v;  
 }  
 
private void bindView(int position, View view) {  
       final Map dataSet = mData.get(position);  
       if (dataSet == null) {  
       return;  
  }  
 
 final ViewBinder binder = mViewBinder;  
 final View[] holder = (View[]) view.getTag();  
 final String[] from = mFrom;  
 final int[] to = mTo;  
 final int count = to.length;  
 
 for (int i = 0; i < count; i++) {  
  final View v = holder[i];  
  if (v != null) {  
   final Object data = dataSet.get(from[i]);  
   // if(data i)   
   String text = data == null ? "" : data.toString();  
   if (text == null) {  
    text = "";  
   }  
 
   boolean bound = false;  
   if (binder != null) {  
    bound = binder.setViewValue(v, data, text);  
   }  
 
   if (!bound) {  
    if (v instanceof Checkable) {  
     if (data instanceof Boolean) {  
       ((Checkable) v).setChecked((Boolean) data);  
      } else {  
       throw new IllegalStateException(v.getClass()  
         .getName()  
         + " should be bound to a Boolean, not a "  
                     + data.getClass());  }  
     } else if (v instanceof TextView) {  
    	 ((TextView)v).setText(text); 
               } else if (v instanceof Button) {  
              	 ((Button)v).setText(text); 
     } else if (v instanceof ImageView) {  
    	  //列表增加网络图片显示
        if (data instanceof Integer) {  
            setViewImage((ImageView) v, (Integer) data);  
        } else if (data instanceof Bitmap) {  
            setViewImage((ImageView) v, (Bitmap) data);  
                  }else 
                 {
        	          try {
    		          	if(text.equals(""))
    		          		setViewImage((ImageView) v,R.drawable.headdefault);
    		          	else
    			        ImageLoader.getInstance().displayImage(text,(ImageView) v,options );
    			      } catch (Exception e) {
//    				     Log.e("MySimpleAdapter====="+text, e.getMessage());
                      }
                 }
     } else {  
      throw new IllegalStateException(  
        v.getClass().getName()  
          + " is not a "  
          + " view that can be bounds by this SimpleAdapter");  
     }  
    }  
   }  
  }  
 }  
  
 public void setViewImage(ImageView v, int value) {  
  v.setImageResource(value);  
 }  
  
  public void setViewImage(ImageView v, Bitmap bm) {  
  ((ImageView) v).setImageBitmap(bm);  
 } 
  //列表增加按钮监听
	   public void addListener(final View convertView) {  
	final Button send=(Button)convertView.findViewById(R.id.search_actionb);
	send.setOnClickListener(  
	      new View.OnClickListener() {  
	      @Override  
	      public void onClick(View v) {  
	      String fname=(String) ((TextView)convertView.findViewById(R.id.search_name)).getText();//获取点中商品的名称
	      String showname=(String) ((TextView)convertView.findViewById(R.id.search_text1)).getText();
	      String fid=(String) ((TextView)convertView.findViewById(R.id.search_id)).getText();
	      Intent intent = new Intent();
		  intent.putExtra("fname",fname);
		  intent.putExtra("showname",showname);
		  intent.putExtra("fstatus",send.getText()+"");
		  intent.putExtra("fid",fid+"");
		  Bundle cmdbundle = intent.getExtras(); 
				Message msg = new Message();
				msg.what = 8;
				msg.setData(cmdbundle);
				Handlersearch.sendMessage(msg);	 
	        }  
	 });  
	 convertView.setOnClickListener(  
              new View.OnClickListener() {  
                  public void onClick(View v) { 
               	   String fname=(String) ((TextView)convertView.findViewById(R.id.search_name)).getText();//获取点中商品的名称
               	   Intent intent = new Intent();
				    	intent.putExtra("fname",fname);
					 	Bundle cmdbundle = intent.getExtras(); 
						Message msg = new Message();
						msg.what = 6;
				 		msg.setData(cmdbundle);
				 		Handlersearch.sendMessage(msg);	
                  }  
              }); 
	  
//	   ((Button)convertView.findViewById(R.id.search_lookb)).setOnClickListener(  
//              new View.OnClickListener() {  
//                  @Override  
//                  public void onClick(View v) { 
//               	   String fname=(String) ((TextView)convertView.findViewById(R.id.search_text1)).getText();//获取点中商品的名称
//               	   Intent intent = new Intent();
//				    	intent.putExtra("fname",fname);
//					 	Bundle cmdbundle = intent.getExtras(); 
//						Message msg = new Message();
//						msg.what = 6;
//				 		msg.setData(cmdbundle);
//				 		Handlersearch.sendMessage(msg);	
//                  }  
//              });
//	 ((LinearLayout)convertView.findViewById(R.id.list_id)).setOnClickListener(  
//       new View.OnClickListener() {  
//       @Override  
//       public void onClick(View v) { 
//    	   String fname=(String) ((TextView)convertView.findViewById(R.id.search_text1)).getText();//获取点中商品的名称
//    	   Intent intent = new Intent();
//		    	intent.putExtra("fname",fname);
//			 	Bundle cmdbundle = intent.getExtras(); 
//				Message msg = new Message();
//				msg.what = 6;
//		 		msg.setData(cmdbundle);
//		 		Handlersearch.sendMessage(msg);	
//       }  
//   });

 }
};  

