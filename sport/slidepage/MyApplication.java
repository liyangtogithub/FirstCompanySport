package com.desay.sport.slidepage;


import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.desay.pstest.toast.showToast;

/**
 * 程序第一次运行，检查网络和key值是否正确
 */
public class MyApplication extends Application {
	
    private static MyApplication AppInstance = null;
    public boolean m_bKeyRight = true;
    BMapManager mBMapManager = null;

    public static final String strKey = "E6665686E0DF6365770416EDF613DBFE478C0AB2";
	
    private static MyApplication instance;
    
	@Override
    public void onCreate() {
	    super.onCreate();
	    AppInstance = this;
		initEngineManager(this);
	}
	
	/**
	 * 初始化BMapManager，检查网络和key值是否正确
	 */
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
        	showToast.normaltoast(MyApplication.getInstance().getApplicationContext(), 
        			"BMapManager Initial wrong!",showToast.TWO_SECOND);
        }
	}
	
	public static MyApplication getInstance() {
		return AppInstance;
	}
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
   static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
               /* Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();*/
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
               /* Toast.makeText(DemoApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();*/
            }

        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
            	showToast.normaltoast(MyApplication.getInstance().getApplicationContext(), 
                        "Key Error!",showToast.TWO_SECOND);
                MyApplication.getInstance().m_bKeyRight = false;
            }
        }
    }
}