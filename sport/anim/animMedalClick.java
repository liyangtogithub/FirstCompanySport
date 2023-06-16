package com.desay.sport.anim;
import com.desay.sport.data.SportData;
import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class animMedalClick extends Activity implements OnClickListener{
	private ImageView iv_medal;
	private ImageView iv_light;
	private TextView iv_congratulation;
	private TextView  tv_medaltoast;
	private RelativeLayout rl_text;
	private RelativeLayout rl_main;

	private int medalname = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.animmedal_click);
		medalname = getIntent().getIntExtra("medalname", -1);//奖章类型
		iv_medal = (ImageView)findViewById(R.id.iv_medal);
		iv_light = (ImageView)findViewById(R.id.iv_light);
		iv_congratulation = (TextView)findViewById(R.id.iv_congratulation);
		tv_medaltoast = (TextView)findViewById(R.id.tv_medaltoast);
		rl_text  = (RelativeLayout)findViewById(R.id.rl_text);
		rl_main  = (RelativeLayout)findViewById(R.id.rl_main);
		
		if(medalname!=-1)
		{
			Resources res =getResources();
			String[] medal_explain=res.getStringArray(R.array.medal_explain);
			iv_medal.setImageDrawable(SportData.getIconDrawable(animMedalClick.this,medalname,SportData.ICON_MEDAL));
			String toast = medal_explain[medalname] + getString(R.string.medal_explain_get);
			tv_medaltoast.setText(toast.replaceAll("\\n", ""));
			iv_congratulation.setText(res.getStringArray(R.array.medal_word)[medalname].replaceAll("\\n", "") );
		}
		
		iv_medal.setOnClickListener(this);
		iv_light.setOnClickListener(this);
		iv_congratulation.setOnClickListener(this);
		rl_text.setOnClickListener(this);
		rl_main.setOnClickListener(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onClick(View v) 
	{
           finish();
	}
}
