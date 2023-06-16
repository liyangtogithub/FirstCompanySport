package com.desay.sport.multimedia;

import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddMusic extends Activity{

	ImageView iv_return;
	Button addbt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
  		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmusic);
		iv_return = (ImageView) findViewById(R.id.iv_return);
		((TextView) findViewById(R.id.tv_title)).setText(getString(R.string.addmusic));
		addbt=(Button) findViewById(R.id.add);
		iv_return.setOnClickListener(OnClick);
		addbt.setOnClickListener(OnClick);
	}
	OnClickListener OnClick = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.iv_return:
				finish();
				break;
			case R.id.add:
				Intent intent=new Intent();
				intent.setClass(AddMusic.this, MusicLib.class);
				startActivity(intent);
				finish();
				break;
			}
		}};
}
