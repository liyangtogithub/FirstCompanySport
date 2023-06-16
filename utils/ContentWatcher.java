package com.desay.utils;

import com.desay.pstest.toast.showToast;
import com.desay.sport.main.R;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentWatcher implements TextWatcher {

	private Context context = null;
	private EditText editText = null;
	private TextView tv_user = null;
	private ImageView iv_save = null;
	private boolean iffirstlogin = false;
	
	public ContentWatcher(Context tcontext, EditText editText,TextView username,ImageView savebutton,boolean iffirstlogin) {
		this.context = tcontext;
		this.editText = editText;
		this.tv_user = username;
		this.iv_save = savebutton;
		this.iffirstlogin = iffirstlogin;
	}

	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setLoginStatus(boolean iffirstlogin)
	{
		this.iffirstlogin = iffirstlogin;
	}
	
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		String s = arg0.toString();
		Log.d("tag", "s================"+s);
		if(arg0!=null && !s.equals(""))
		{
			Log.d("tag", "s================"+editText.getId());
			switch(editText.getId())
			{
			case R.id.et_height:
				if(Integer.valueOf(s)>230||Integer.valueOf(s)==0)
				{
					editText.setText("");
					showToast.normaltoast(context, context.getString(R.string.toast_heightiserror), showToast.ONE_SECOND);
				}
				break;
			case R.id.et_weight:
				if(Integer.valueOf(s)>250||Integer.valueOf(s)==0)
				{
					editText.setText("");
					showToast.normaltoast(context, context.getString(R.string.toast_weightiserror), showToast.ONE_SECOND);
				}
				break;
			}
		}
        if(!iffirstlogin)
        {
//    		tv_user.setText(context.getString(R.string.changeheadphoto));
    		iv_save.setVisibility(View.VISIBLE);
        }
		
	}

}
