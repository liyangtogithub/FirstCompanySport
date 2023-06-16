package com.desay.sport.friend;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

public class ListViewBinder implements ViewBinder {  
	@Override  
	public boolean setViewValue(View view, Object data,String textRepresentation) {  
	 // TODO Auto-generated method stub   
	 if((view instanceof ImageView) && (data instanceof Bitmap)) {  
	    ImageView imageView = (ImageView) view;  
	    Bitmap bmp = (Bitmap) data;  
	    imageView.setImageBitmap(bmp);  
	    return true;  
	 }  
	 return false;  
   }
}
