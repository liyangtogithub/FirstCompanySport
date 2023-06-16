package com.desay.pstest.toast;
import com.desay.sport.main.R;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class showToast {
	
	public static final int ONE_SECOND = 1000;
	public static final int TWO_SECOND = 2000;
	public static final int THREE_SECOND = 3000;
	
	public showToast(Context context,String showtext)
	{
		
		
	}
	
	public static void normaltoast(Context context,String showtext,int duration)
	{
		Toast toast=new Toast(context);
		toast.setDuration(duration);
		toast.setGravity(Gravity.CENTER_HORIZONTAL |Gravity.BOTTOM, 0, 150);
		LayoutInflater inflater  =	LayoutInflater.from(context);	
		View layout = inflater.inflate(R.layout.showview, null);
		TextView tv= (TextView)layout.findViewById(R.id.Tv_showtext);
//		tv.setBackgroundResource(R.drawable.bg_toast);
//		tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
		tv.setText(showtext);
		tv.setTextSize(18);
		toast.setView(layout);
        toast.show();
	}
}
