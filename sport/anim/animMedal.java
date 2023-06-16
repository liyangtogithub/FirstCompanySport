package com.desay.sport.anim;
import com.desay.sport.data.SportData;
import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Context;
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

public class animMedal extends Activity implements AnimationListener,OnClickListener{
	private ImageView iv_medal;
	private ImageView iv_light;
	private ImageView iv_congratulation;
	private TextView  tv_medaltoast;
	private RelativeLayout rl_text;
	private RelativeLayout rl_main;
	private Animation scale_medal = null;
	private Animation alpha1 = null;
	private Animation rotate = null;
	private Context context = null;
	private int medalname = -1;
	private boolean IFAnimationFinish ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.animmedal);
		medalname = getIntent().getIntExtra("medalname", -1);//奖章类型
		IFAnimationFinish = false;
		context = animMedal.this;
		iv_medal = (ImageView)findViewById(R.id.iv_medal);
		iv_light = (ImageView)findViewById(R.id.iv_light);
		iv_congratulation = (ImageView)findViewById(R.id.iv_congratulation);
		tv_medaltoast = (TextView)findViewById(R.id.tv_medaltoast);
		rl_text  = (RelativeLayout)findViewById(R.id.rl_text);
		rl_main  = (RelativeLayout)findViewById(R.id.rl_main);
		
		if(medalname!=-1)
		{
			iv_medal.setImageDrawable(SportData.getIconDrawable(context,medalname,SportData.ICON_MEDAL));
			String toast = getString(R.string.medal_get)+getResources().getStringArray(R.array.medal_word)[medalname]+getString(R.string.tab5_medal);
			tv_medaltoast.setText(toast.replaceAll("\\n", ""));
		}
		
		iv_medal.setOnClickListener(this);
		iv_light.setOnClickListener(this);
		iv_congratulation.setOnClickListener(this);
		rl_text.setOnClickListener(this);
		rl_main.setOnClickListener(this);
		initAnim();
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if(animation.equals(scale_medal))
		{
			rl_text.setVisibility(View.VISIBLE);
			iv_congratulation.setVisibility(View.VISIBLE);		
			rl_text.startAnimation(alpha1);
			iv_congratulation.startAnimation(alpha1);
		}
		else if(animation.equals(alpha1))
		{
			iv_light.startAnimation(rotate);
		}
		else if(animation.equals(rotate))
		{
			IFAnimationFinish = true;
		}
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	
    private void initAnim()
    {
		scale_medal = AnimationUtils.loadAnimation(context,R.anim.scale); 
		scale_medal.setFillAfter(true);
		alpha1 = AnimationUtils.loadAnimation(context,R.anim.alphato1); 
		alpha1.setFillAfter(true);
		rotate = AnimationUtils.loadAnimation(context,R.anim.rotate_medal); 
		scale_medal.setAnimationListener(this);
		alpha1.setAnimationListener(this);
		rotate.setAnimationListener(this);
		iv_medal.startAnimation(scale_medal);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        if(IFAnimationFinish)
        {
           finish();
        }

	}
}
