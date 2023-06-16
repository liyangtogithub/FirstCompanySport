package com.desay.sport.slidepage;

import java.util.List;

import com.desay.sport.data.SportData;
import com.desay.sport.data.UpdataMedalUI;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class slide_medal extends Activity {
    private Context context = null;
	private UpdataMedalUI UMUI;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub	
		context = slide_medal.this;
        setContentView(R.layout.slide_medal);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        ((ImageView) findViewById(R.id.iv_return)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
        ((TextView) findViewById(R.id.tv_title)).setText(context.getResources().getStringArray(R.array.slide_menu)[SportData.PAGE_MEDAL]);
        
		UMUI=new UpdataMedalUI(context,getWindow().getDecorView());
		UMUI.init();
		medalhandler.sendEmptyMessage(0);
		super.onCreate(arg0);
	}
	
	Handler medalhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			sportDB spDB = new sportDB(context);
			// 所有奖章全查
			List<entity_sportachieve> medal_list = spDB.GetSportAchieve(
					SportData.getUserName(context), "1", null);

			//Log.i(tag, "medal_list.size()" + medal_list.size());
			UMUI.UpUi(medal_list);
		}

	};
}
