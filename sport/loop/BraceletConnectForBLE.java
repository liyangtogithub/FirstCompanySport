package com.desay.sport.loop;

import java.sql.SQLException;
import java.util.ArrayList;

import com.desay.pstest.toast.showToast;
import com.desay.sport.main.MainTab;
import com.desay.sport.main.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
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


public class BraceletConnectForBLE extends Activity implements OnClickListener {
	private BroadcastReceiver receiver=null;
	public ProgressDialog progressDialog;
	private BluetoothAdapter mBluetoothAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bracelet_connect_fragmentforble);
		
		Button btn=(Button) findViewById(R.id.btn_connect);
		btn.setOnClickListener(this);
		TextView title=(TextView) findViewById(R.id.tv_title);
		title.setText(getString(R.string.loop_manage_text2));
		
		ImageView iv_return = (ImageView)findViewById(R.id.iv_return);
		
		iv_return.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}});
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(BLE.STOP_PR);
		 intentFilter.addAction(GattUtils.CONFIRM_MSG);
		 intentFilter.addAction(GattUtils.DISCONNECT_STATE_BROAD);
		 receiver=new ReadInfoBroadcast();
		 registerReceiver(receiver, intentFilter);
		progressDialog=new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(getString(R.string.bluetooth_connecting));
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
	    mBluetoothAdapter = bluetoothManager.getAdapter();
	}
	
	@Override
	protected void onStart() {
		
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
				startActivity(new Intent(BraceletConnectForBLE.this,BraceletConnectfail.class));
				finish();
			}else if(action.equals(GattUtils.DISCONNECT_STATE_BROAD)){
				startActivity(new Intent(BraceletConnectForBLE.this,BraceletConnectfail.class));
				finish();
			}else if(action.equals(BLE.STOP_PR)){
				if(progressDialog.isShowing())
					progressDialog.dismiss();
					showToast.normaltoast(BraceletConnectForBLE.this,getString(R.string.device_notexsit), Toast.LENGTH_SHORT);
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
			progressDialog.setMessage(getString(R.string.bluetooth_connecting));
			progressDialog.show();
			
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					if (! mBluetoothAdapter.isEnabled()  )				
						mBluetoothAdapter.enable();				
					while (!mBluetoothAdapter.isEnabled())
					{
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
					Intent intent=new Intent();
					intent.setAction(MainTab.START_SCAN);
					sendBroadcast(intent);
				}}).start();
			break;
		}
	}

}
