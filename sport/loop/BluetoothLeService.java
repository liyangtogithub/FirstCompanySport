/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.desay.sport.loop;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.desay.sport.data.SportData;


/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service
{
	private final static String TAG = BluetoothLeService.class.getSimpleName();

	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothGatt mBluetoothGatt;
	private BluetoothDevice mDevice;
	private String mBluetoothDeviceAddress;

	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String BATTERY_MSG="com.desay.battery";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	public final static String HISTORY_S_MSG="com.desay.sporthis";
	public final static String CHANGE_ACTIVITY="com.desay.change";
	
	public static final UUID BLE_SERVICE = UUID
			.fromString("0000180D-0000-1000-8000-00805f9b34fb");
	public static final UUID CCC = UUID
			.fromString("00002902-0000-1000-8000-00805f9b34fb");
	// Device Information Service
	public static final UUID DEVICE_INFORMATION = UUID
			.fromString("0000180A-0000-1000-8000-00805f9b34fb");
	public static final UUID SYSTEM_ID = UUID
			.fromString("00002a23-0000-1000-8000-00805f9b34fb");
	public static final UUID MODEL_NUMBER = UUID
			.fromString("00002a24-0000-1000-8000-00805f9b34fb");
	public static final UUID SERIAL_NUMBER = UUID
			.fromString("00002a25-0000-1000-8000-00805f9b34fb");
	public static final UUID SOFTWARE_REV_UUID = UUID
			.fromString("00002A28-0000-1000-8000-00805f9b34fb");
	public static final UUID MANUFATURE_NAME = UUID
			.fromString("00002A29-0000-1000-8000-00805f9b34fb");
	public static final UUID PNP_ID = UUID
			.fromString("00002a50-0000-1000-8000-00805f9b34fb");
	// Dis service
	public static final UUID DIS_UUID = UUID
			.fromString("0000180A-0000-1000-8000-00805f9b34fb");

	public static final UUID STEP_COUNTER_UUID = UUID
			.fromString("00003500-0000-1000-8000-00805f9b34fb");
	public static final UUID COUNTER_DATA_UUID = UUID
			.fromString("00003501-0000-1000-8000-00805f9b34fb");

	public static final UUID USER_INFO = UUID
			.fromString("00003100-0000-1000-8000-00805f9b34fb");
	public static final UUID USER_NAME = UUID
			.fromString("00003101-0000-1000-8000-00805f9b34fb");
	public static final UUID USER_PWD = UUID
			.fromString("00003102-0000-1000-8000-00805f9b34fb");
	public static final UUID USER_AUTH = UUID.fromString("00003107-0000-1000-8000-00805f9b34fb");
	// ����
	// 电池
	public static final UUID BATTERY_SERVICE = UUID
			.fromString("0000180f-0000-1000-8000-00805f9b34fb");
	public static final UUID BATTERY_CHARCTER = UUID
			.fromString("00002a19-0000-1000-8000-00805f9b34fb");


	public static final UUID TMP_TEMP = UUID
			.fromString("00003301-0000-1000-8000-00805f9b34fb");
	public static final UUID TMP_SPORT = UUID
			.fromString("00003302-0000-1000-8000-00805f9b34fb");
	public static final UUID TMP_HEART = UUID
			.fromString("00003303-0000-1000-8000-00805f9b34fb");
	// 时间
	public static final UUID TIME_SERVICE = UUID
			.fromString("00003000-0000-1000-8000-00805f9b34fb");
	public static final UUID CURRENT_TIME = UUID
			.fromString("00003001-0000-1000-8000-00805f9b34fb");
	public static final UUID ALARM_TIME=UUID
			.fromString("00003001-0000-1000-8000-00805f9b34fb");
	// ���ʣ����У��¶�
	public static final UUID TEMP_CHRACTER = UUID
			.fromString("00003401-0000-1000-8000-00805f9b34fb");
	public static final UUID TEMPERUTUER = UUID
			.fromString("00003400-0000-1000-8000-00805f9b34fb");
	public static final UUID G_SENSOR = UUID
			.fromString("00003500-0000-1000-8000-00805f9b34fb");
	public static final UUID HEART_RATE = UUID
			.fromString("00003600-0000-1000-8000-00805f9b34fb");
	public static final UUID HEART_AC = UUID
			.fromString("00003601-0000-1000-8000-00805f9b34fb");
	public static final UUID HEART_DC = UUID
			.fromString("00003602-0000-1000-8000-00805f9b34fb");
	public static final UUID HEART_DATA = UUID
			.fromString("00003603-0000-1000-8000-00805f9b34fb");
	public static final UUID HEART_OO = UUID
			.fromString("00003604-0000-1000-8000-00805f9b34fb");

	// ��ʷ
	public static final UUID HISTORY = UUID
			.fromString("00003700-0000-1000-8000-00805f9b34fb");
	public static final UUID HISTORY_SPORT = UUID
			.fromString("00003701-0000-1000-8000-00805f9b34fb");
	public static final UUID HISTORY_HEARTRATE = UUID
			.fromString("00003702-0000-1000-8000-00805f9b34fb");
	public static final UUID HISTORY_PARMERT = UUID
			.fromString("00003703-0000-1000-8000-00805f9b34fb");

	 private String username="";
	  
	@Override
	public void onCreate()
	{

		username = SportData.getUserName(this);

	}
	 /**
	  *若服务与蓝牙连接上或准备好发数据，就发通知给DeviceControlActivity
	 */
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback()
	{
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState)
		{
			String intentAction;
			if (newState == BluetoothProfile.STATE_CONNECTED)
			{
				intentAction = ACTION_GATT_CONNECTED;				
				broadcastUpdate(intentAction);
				mBluetoothGatt.discoverServices();

			}
			else if (newState == BluetoothProfile.STATE_DISCONNECTED)
			{
				intentAction = ACTION_GATT_DISCONNECTED;				
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status)
		{
			if (status == BluetoothGatt.GATT_SUCCESS)
			{
			
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
			}
			else
			{
				Log.i(TAG, "onServicesDiscovered received: " + status);
			}
		}

		/**
		  *接收读手环的数据,并传递给DeviceControlActivity
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status)
		{
			System.out.println("onCharacteristicRead-------------status="+status+characteristic.getUuid());

			if(BATTERY_CHARCTER.equals(characteristic.getUuid())){
				final byte[] data = characteristic.getValue();
				if (data != null ) {
						Intent intent =new Intent(BATTERY_MSG);
				       System.out.println(GattUtils.getIntValue(data,GattUtils.FORMAT_UINT8 , 0));
				        intent.putExtra(EXTRA_DATA,GattUtils.getIntValue(data,GattUtils.FORMAT_UINT8 , 0));
					    sendBroadcast(intent);
				}
			}else{
				final byte[] data = characteristic.getValue();
				if (data != null ) {
						Intent intent =new Intent(HISTORY_S_MSG);
				       System.out.println(GattUtils.getStringValue(data, 0));
				        intent.putExtra(EXTRA_DATA,GattUtils.getStringValue(data, 0));
					    sendBroadcast(intent);
				}
			
			}
			
		}

		/**
		  *写入手环数据?返回写入成功否和写入内容
		 */
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status)
		{
			System.out.println("--------write success----- status:" + status);
			String writeDataString = new String(characteristic.getValue());
			System.out.println("writeDataString-- " +writeDataString+ "  length "+writeDataString.length());
			if (characteristic.getUuid().equals(USER_AUTH))
			{
					//ReadAuth();
				setcurrentTime();
				
			}else if(characteristic.getUuid().equals(CURRENT_TIME)){
				setsport_p();
			}else if(characteristic.getUuid().equals(HISTORY_PARMERT)){
				writeName();
			}else if(characteristic.getUuid().equals(USER_NAME)){
				readBatt();
			}else if(characteristic.getUuid().equals(HISTORY_SPORT)){
				readHis_S();
			}
			

		};
		
		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status)
		{

			System.out.println("onDescriptorWriteonDescriptorWrite = " + status
					+ ", descriptor =" + descriptor.getUuid().toString());
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic)
		{
			//broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			if (characteristic.getValue() != null)
			{
				System.out.println(characteristic.getStringValue(0));
				Intent intent =new Intent(CHANGE_ACTIVITY);
				intent.putExtra("address", gatt.getDevice().getAddress());
				sendBroadcast(intent);
			}
			//System.out.println("--------onCharacteristicChanged-----");
		}
		/**
		  *没用?		 */
		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
		{
			System.out.println("rssi = " + rssi);
		}

	};
	
	/**
	  *发广播给DeviceControlActivity
	 */
	private void broadcastUpdate(final String action)
	{
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	
	/**
	  *为绑定此服务的类，提供此服务的对?	 */
	public class LocalBinder extends Binder
	{
		public BluetoothLeService getService()
		{
			return BluetoothLeService.this;
		}
	}
	/**
	  *在ServiceConnection中，被调?	 */
	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}
	/**
	 * 外界解除和此服务的绑定时，被回调
	 */
	@Override
	public boolean onUnbind(Intent intent)
	{
		close();
		return super.onUnbind(intent);
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize()
	{
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null)
		{
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null)
			{
				Log.i(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null)
		{
			Log.i(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	public boolean connect(String address) {
		System.out.println("connect to..");
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect. (先前连接的设备�?尝试重新连接)
		if (mBluetoothDeviceAddress != null
				&& address.equalsIgnoreCase(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				System.out.println("connecting..");
				return true;
			} else {
				return false;
			}
		}

	    BluetoothDevice device;
	    try {
	    	device= mBluetoothAdapter
					.getRemoteDevice(address.toUpperCase());
		} catch (Exception e) {
			// TODO: handle exception
			address=address.toLowerCase();
	    	device= mBluetoothAdapter
					.getRemoteDevice(address);
		}
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect()
	{
		if (mBluetoothAdapter == null || mBluetoothGatt == null)
		{
			Log.i(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * 使用完手环后，确保mBluetoothGatt被释?	 */
	public void close()
	{
		if (mBluetoothGatt == null)
		{
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	public void wirteCharacteristic(BluetoothGattCharacteristic characteristic)
	{

		if (mBluetoothAdapter == null || mBluetoothGatt == null)
		{
			Log.i(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.writeCharacteristic(characteristic);

	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic)
	{
		if (mBluetoothAdapter == null || mBluetoothGatt == null)
		{
			Log.i(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	public void ReadName()
	{
		 BluetoothGattService nameService = mBluetoothGatt.getService(  USER_INFO);
		if (nameService == null)
		{
			System.out.println("nameService == null");
			return;
		}

		BluetoothGattCharacteristic NAME = nameService/*.get(5)*/
				.getCharacteristic(USER_NAME);
		if (NAME == null)
		{
			System.out.println("NAME == null");
			return;
		}
		System.out.println("READ UUID = " + NAME.getUuid().toString());
		mBluetoothGatt.readCharacteristic(NAME);
	}
	
	public void ReadAuth()
	{
		 BluetoothGattService nameService = mBluetoothGatt.getService(  USER_INFO);
		if (nameService == null)
		{
			System.out.println("nameService == null");
			return;
		}

		BluetoothGattCharacteristic NAME = nameService/*.get(5)*/
				.getCharacteristic(USER_AUTH);
		if (NAME == null)
		{
			System.out.println("NAME == null");
			return;
		}
		System.out.println("READ UUID = " + NAME.getUuid().toString());
		mBluetoothGatt.readCharacteristic(NAME);

	}
	
	
	public void readHis_S()
	{
		 BluetoothGattService sportService = mBluetoothGatt.getService(  HISTORY);
		if (sportService == null)
		{
			System.out.println("nameService == null");
			return;
		}

		BluetoothGattCharacteristic sport = sportService/*.get(5)*/
				.getCharacteristic(HISTORY_SPORT);
		if (sport == null)
		{
			System.out.println("NAME == null");
			return;
		}
		System.out.println("READ UUID = " + sport.getUuid().toString());
		mBluetoothGatt.readCharacteristic(sport);

	}
	
    public void writeSportHis() 
    {
    	System.out.println("writeSportHis..");   	
    	BluetoothGattService His_h=mBluetoothGatt.getService(HISTORY);
    	if (His_h == null) {

            return ;
        }
    	BluetoothGattCharacteristic His_h_c=His_h.getCharacteristic(HISTORY_SPORT);
    	 if (His_h_c == null) {

             return ;
         }
    	byte[] value=new byte[1];
    	value[0]=(byte)(21&0xFF);
    	His_h_c.setValue(value);
        boolean result = mBluetoothGatt.writeCharacteristic(His_h_c);
    }
    
	public void readHis_H()
	{
		 BluetoothGattService heartService = mBluetoothGatt.getService(  HISTORY);
		if (heartService == null)
		{
			System.out.println("nameService == null");
			return;
		}

		BluetoothGattCharacteristic heart = heartService/*.get(5)*/
				.getCharacteristic(HISTORY_HEARTRATE);
		if (heart == null)
		{
			System.out.println("NAME == null");
			return;
		}
		System.out.println("READ UUID = " + heart.getUuid().toString());
		mBluetoothGatt.readCharacteristic(heart);
	}
	 
	
	public void readBatt()
	{
		 BluetoothGattService battService = mBluetoothGatt.getService(  BATTERY_SERVICE);
		if (battService == null)
		{
			System.out.println("nameService == null");
			return;
		}

		BluetoothGattCharacteristic batt = battService/*.get(5)*/
				.getCharacteristic(BATTERY_CHARCTER);
		if (batt == null)
		{
			System.out.println("NAME == null");
			return;
		}
		System.out.println("READ UUID = " + batt.getUuid().toString());
		mBluetoothGatt.readCharacteristic(batt);

	}
	
	 public void writeActivity() 
	    {
	    	System.out.println("write auth..");
	    	BluetoothGattService His_h=mBluetoothGatt.getService( USER_INFO);
	    	if (His_h == null) {
	            return ;
	        }
	    	BluetoothGattCharacteristic His_h_c=His_h.getCharacteristic(USER_AUTH);
	    	 if (His_h_c == null) {
	             return ;
	         }
	    	byte[] value=new byte[1];
	    	value[0]=(byte)(0x31);
	    	His_h_c.setValue(value);
	        mBluetoothGatt.writeCharacteristic(His_h_c);
	        
	    }
	 
	 public void setcurrentTime() 
	    {
	    	System.out.println("write zero..");
	    	BluetoothGattService timeservice=mBluetoothGatt.getService( TIME_SERVICE);
	    	if (timeservice == null) {
	            return ;
	        }
	    	BluetoothGattCharacteristic timeData=timeservice.getCharacteristic(CURRENT_TIME);
	    	 if (timeData == null) {
	             return ;
	         }

	    	 SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	 		System.out.println(df.format(new Date()));
	         timeData.setValue((df.format(new Date())).getBytes());
	        mBluetoothGatt.writeCharacteristic(timeData);
	        
	    }
	 
	    public void setsport_p() 
	    {
	    	System.out.println("set sport p");
	        BluetoothGattService StepCounter = mBluetoothGatt.getService( HISTORY);
	        if (StepCounter == null) {
	        	System.out.println("service null");
	            return;
	        }

	        BluetoothGattCharacteristic StepCounterData = StepCounter.getCharacteristic(HISTORY_PARMERT);
	        if (StepCounterData == null) {
	        	System.out.println("character null");
	            return;
	        }
	        StepCounterData.setValue(("2:00050020060").getBytes());
	       // StepCounterData.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
	        boolean result = mBluetoothGatt.writeCharacteristic(StepCounterData);
	    }
	    
	 
	 public void readcurrentTime() 
	    {
	    	System.out.println("write zero..");
	    	BluetoothGattService timeservice=mBluetoothGatt.getService( TIME_SERVICE);
	    	if (timeservice == null) {
	            return ;
	        }
	    	BluetoothGattCharacteristic timeData=timeservice.getCharacteristic(CURRENT_TIME);
	    	 if (timeData == null) {
	             return ;
	         }
	        mBluetoothGatt.readCharacteristic(timeData);
	    }
	 
	public void writeName() 
	    {
	    	System.out.println("writename..");
	    	
	    	BluetoothGattService nameService=mBluetoothGatt.getService( USER_INFO);
	    	if (nameService == null) {
	            return ;
	        }
	    	BluetoothGattCharacteristic namec=nameService.getCharacteristic(USER_NAME);
	    	 if (namec == null) {
	             return ;
	         }
	    	 namec.setValue(username.getBytes());
	          // StepCounterData.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
	         mBluetoothGatt.writeCharacteristic(namec);
	      
	    }
	
	public void enableAuth(){
		setCharacteristicNotification(mDevice, USER_INFO, USER_AUTH, true);
	}
	public void disableAuth(){
		setCharacteristicNotification(mDevice, USER_INFO, USER_AUTH, false);
	}
	public boolean setCharacteristicNotification(BluetoothDevice device, UUID serviceUuid, UUID characteristicUuid,
	        boolean enable) {	  
		
	    BluetoothGattCharacteristic characteristic = mBluetoothGatt.getService(serviceUuid).getCharacteristic(characteristicUuid);
	    mBluetoothGatt.setCharacteristicNotification(characteristic, enable);
	    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CCC);
	    descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : new byte[] { 0x00, 0x00 });
	    return mBluetoothGatt.writeDescriptor(descriptor); //descriptor write operation successfully started? 
	}
	 
}
