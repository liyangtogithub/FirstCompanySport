package com.desay.sport.loop;

import java.sql.SQLException;
import java.util.ArrayList;

import com.desay.sport.data.SportData;
import com.desay.sport.db.sportDB;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BraceletConnectfail extends Activity implements OnClickListener {
	private BroadcastReceiver receiver=null;
	public ProgressDialog progressDialog;
	private sportDB db = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bracelet_guid_fragmentfail);
		
		Button btn=(Button) findViewById(R.id.btn_connect);
		btn.setOnClickListener(this);
		ImageView iv_return = (ImageView)findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		db = new sportDB(BraceletConnectfail.this);
		TextView title=(TextView) findViewById(R.id.tv_title);
		title.setText(getString(R.string.loop_manage_text2));
		IntentFilter intentFilter=new IntentFilter();
		 intentFilter.addAction(GattUtils.CONFIRM_MSG);
		 intentFilter.addAction(GattUtils.DISCONNECT_STATE_BROAD);
		 intentFilter.addAction(BluetoothLeService.CHANGE_ACTIVITY);
		 receiver=new ReadInfoBroadcast();
		 registerReceiver(receiver, intentFilter);
		progressDialog=new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.bluetooth_connecting));
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		if(progressDialog.isShowing())
			progressDialog.dismiss();
		super.onDestroy();
	}
	class ReadInfoBroadcast extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(action.equals(GattUtils.CONFIRM_MSG)){
				System.out.println("confirm.");
				if(progressDialog.isShowing())
					progressDialog.setMessage(getString(R.string.loop_confirm));
				//showToast.normaltoast(AddLoopActivity.this,getString(R.string.bluetooth_success), Toast.LENGTH_SHORT);					
			}else if(action.equals(GattUtils.DISCONNECT_STATE_BROAD)){
				if(progressDialog.isShowing())
					progressDialog.dismiss();
				Toast.makeText(BraceletConnectfail.this, getString(R.string.bluetooth_failure), Toast.LENGTH_SHORT).show();
			}else if(action.equals(BluetoothLeService.CHANGE_ACTIVITY)){
				//if(progressDialog.isShowing())
					//progressDialog.dismiss();
				String addreass=intent.getStringExtra("address");
				String change[]=addreass.split(":");
				String mac=change[0]+change[1]+change[2]+change[3]+change[4]+change[5];
				db.saveIwanMac(context,mac,db);
				sendBroadcast(new Intent(MainTab.WRITE_ACTIVITY));
				startActivity(new Intent(BraceletConnectfail.this,AddLoopActivity.class));
				finish();
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_connect:
			// TODO 添加手环
			System.out.println("connect new");
			Intent intent=new Intent();
			intent.setAction(MainTab.START_SCAN);
			sendBroadcast(intent);
			progressDialog.setMessage(getString(R.string.bluetooth_connecting));
			progressDialog.show();
			
		}
	}

}
