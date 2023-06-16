package com.desay.sport.friend;
import java.util.List;
import com.desay.sport.data.UpdataMedalUI;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;


public class friend_medaling extends Activity{

private static final String TAG = "tab4_record";
private LinearLayout medalLayout;
private RelativeLayout Rl;
private TextView tv_title;
private ImageView mbackimg;
private String mealstring="";
private UpdataMedalUI UMUI;
private View mainView;
List<entity_sportachieve> sportachieve_list;

@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);	
//	setContentView(R.layout.tab5_medal);
	LayoutInflater factory=getLayoutInflater();
    mainView=factory.inflate(R.layout.tab5_medal,null);//得到自定义对话框 
	setContentView(mainView);
	medalLayout = (LinearLayout) mainView.findViewById(R.id.medallayout);
	medalLayout.setBackgroundColor(getResources().getColor(R.color.pink));
	init();
	UMUI=new UpdataMedalUI(friend_medaling.this,mainView);
	UMUI.init();
	Intent intent = getIntent();
//	mealstring=intent.getExtras().getString("meal");
	sportachieve_list=(List<entity_sportachieve> )intent.getExtras().getSerializable("meal");;
	medalhandler.sendEmptyMessage(0);
	
}

@Override
	protected void onResume() {
		// TODO Auto-generated method stub
	Log.d(TAG, "tab4_record_onResume");
		super.onResume();
	}
private void init()
{
	
	Rl = (RelativeLayout) mainView.findViewById(R.id.rl_middle1_type);
	Rl.setVisibility(View.VISIBLE);
	mbackimg=(ImageView)mainView.findViewById(R.id.iv_return);
	tv_title = (TextView) mainView.findViewById(R.id.tv_title);
	tv_title.setText(getString(R.string.medal_title_str));
	mbackimg.setOnClickListener(new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{		
	    		friend_medaling.this.finish();
	    	}
	    });
}
Handler medalhandler = new Handler()
{
	@Override
	public void handleMessage(Message msg)
	{
		super.handleMessage(msg);
//		String [] medal_list = mealstring.split(";");
//		List<entity_sportachieve> sportachieve_list=new ArrayList<entity_sportachieve>();
//		for (int i = 1; i < medal_list.length; i++)
//		{
//			String [] medaldata = medal_list[i].split(",");
//			entity_sportachieve sportachieve=new entity_sportachieve();
//			sportachieve.setAchievetype(Integer.parseInt(medaldata[1].trim()));
//			sportachieve.setAchievename(Integer.parseInt(medaldata[2].trim()));
//			sportachieve.setAchieverecord(Integer.parseInt(medaldata[3].trim()));
//			sportachieve_list.add(sportachieve);
//			
//		}
		UMUI.UpUi(sportachieve_list);
	}

};

@Override
	protected void onStop() {
		// TODO Auto-generated method stub
	Log.d(TAG, "tab4_record_onStop");
		super.onStop();
	}
}
