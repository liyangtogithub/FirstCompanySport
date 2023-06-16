package com.desay.sport.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import android.widget.Toast;

import com.desay.pstest.toast.showToast;
import com.desay.sport.data.SportData;
import com.desay.sport.main.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


public class UpdateManager {
	private static final String TAG = "UpdateManager";
	private static final int HANDLER_CHECKVERSION = 100;
	private static final int HANDLER_DOWNLOAD = 101;
	public static String[] DOWNLOADSTATE;
    private static final int DOWNLOAD = 1;  
    private static final int DOWNLOAD_FINISH = 2;
    private static final int DOWNLOAD_ERROR = 3;
    
    //////////
    public static final int NEEDDOWNLOAD = 1;  
    public static final int NOTNEEDDOWNLOAD = 2;
    public static final int NETWORKERROR = 3;
    private int progress;  
    public String getDsExtendPath() {
		return dsExtendPath;
	}

	public String getDsRomPath() {
		return dsRomPath;
	}
	private boolean cancelUpdate=false;
    private ProgressBar mProgressBar;  
    private Dialog mDownloadDialog;  
	private static UpdateManager mUpdateManager;
	private Context mContext;
	private final static ConcurrentHashMap<String, OnUpdateListener> mUpdateListnerList = new ConcurrentHashMap<String, OnUpdateListener>();
	private final static ConcurrentHashMap<String, Thread> mDownLoadList = new ConcurrentHashMap<String, Thread>();
	private String serverAddress;
	private String dsExtendPath;
	private String dsRomPath;
	private FileInfo mSelfUpdateFileInfo;
	private AlertDialog ad ;
	/**
	 * @author jh
	 *
	 */
	public interface OnUpdateListener {
		/**
		 * @param version
		 * @param downUrl
		 */
		public void onFinishCheckVersion(int version, String downUrl);
		/**
		 */
		public void onStartCheckVersion();
	}

	/**
	 * @author jh
	 *
	 */
	public interface OnDownLoadListener {
		/**
		 */
		public void onDownLoadStart();
		/**
		 * @param process
		 */
		public void onDownLoading(int process);
		/**
		 */
		public void onDownLoadEnd();
		/**
		 * @param error
		 */
		public void onDownLoadError(String error);
	}
	
	public UpdateManager(Context context) {
		mUpdateListnerList.clear();
		mDownLoadList.clear();
		mContext = context;
		serverAddress = "http://care.desay.com/apk_update/";
		initDownloadPath();
	}
	
	private void initDownloadPath(){
		if (Util.isSDCardReady()) {
			dsExtendPath = Util.getSdDirectory()
					+ "/ds_download/";
		}else{
			dsExtendPath = "";
		}
		dsRomPath = Environment.getDataDirectory().getAbsolutePath()
				+ "/data/com.desay.sport.main/ds_download/";
	}
	
	public static void init(Context context){
		if(mUpdateManager ==null)
			mUpdateManager = new UpdateManager(context);
	}
	
	public static UpdateManager getInstance(){
		return mUpdateManager;
	}
	
	/**
	 * @param listener callback
	 */
	public void registerListener(String path,OnUpdateListener listener){
		if(mUpdateListnerList.containsKey(path)){
			return;
		}
		else
			mUpdateListnerList.put(path, listener);
	}
	/**
	 * @param path
	 */
	public void unRegisterListener(String path){
		mUpdateListnerList.remove(path);
	}
	
	
//	/**
//	 * check update
//	 * @param f
//	 */
//	public void checkVersion(FileInfo f){
//		if(f.getNewVersion()>0 && f.getNewVersion() == f.getVersion()){
//			return;
//		}
//		Bundle data = new Bundle();
//		data.putString("path", f.getFilePath());
//		data.putString("name", f.getFileName());
//		Message msg = new Message();
//		msg.setData(data);
//		msg.what = HANDLER_CHECKVERSION;
//		mUpdateHandler.sendMessage(msg);
//	}
//	
	 /** 
     * @author Administrator 
     * 
     */  
    private class ApkSelfUpdateThread extends Thread {
    	public synchronized void cancelUpdate(){
    		cancelUpdate = true;
    	}
    	
        @Override  
        public void run() {
        	InputStream is=null;
        	FileOutputStream fos=null;
        	cancelUpdate = false;
            try  
            {  
            	Thread.sleep(200);
            	String savePath="";
            	if(Util.isSDCardReady())
                   // savePath = UpdateManager.getInstance().getDsExtendPath();
            		savePath= Util.getSdDirectory()
        					+ "/ds_download/";        	
            	else
//            		savePath = UpdateManager.getInstance().getDsRomPath();
            		savePath = getDsRomPath();
				URL url = new URL(mSelfUpdateFileInfo.getDownUrl());
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				is = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				File apkFile = new File(savePath, Util.getAppName(mSelfUpdateFileInfo.getFilePath())+".apk");
				fos = new FileOutputStream(apkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mHandler.sendEmptyMessage(DOWNLOAD);
					if (numread <= 0) {
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!cancelUpdate);
			} catch (Exception e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
			} finally {
				try {
					if (is != null)
						is.close();
					if (fos != null)
						fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			ad.dismiss();
        }  
    }  
    
    private Handler mHandler = new Handler() {  
        public void handleMessage(android.os.Message msg) {  
            switch(msg.what){  
            case DOWNLOAD:  
               // System.out.println(progress);  
                mProgressBar.setProgress(progress);  
                break;  
            case DOWNLOAD_FINISH:  
                installApk();  
                break;  
            case DOWNLOAD_ERROR:
            	showToast.normaltoast(mContext,mContext.getString(R.string.checkupdate_toast_network_error),showToast.ONE_SECOND); 
            	break;
            }
            
        };  
    }; 
    
    public void showDownloadDialog(Context context) {
        AlertDialog.Builder builder = new Builder(context);
	    LayoutInflater inflater;
	    View layout=null;	
	    ad = builder.create();
        if (!ad.isShowing())
        {
                ad.show();
                inflater=  LayoutInflater.from(context);
        		layout = inflater.inflate(R.layout.softupdate_progress, null);
                ad.getWindow().setContentView(layout);
                ad.setCanceledOnTouchOutside(false); 
                ad.setOnKeyListener(new OnKeyListener(){
    				public boolean onKey(DialogInterface dialog, int keyCode,
    						KeyEvent event) {
    					 if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
    						    ad.dismiss();
    				        	return true;
    				        }  
    					return false;
    				}
    			});
                final TextView tv_title = (TextView)layout.findViewById(R.id.tv_title);
                mProgressBar = (ProgressBar) layout.findViewById(R.id.update_progress);
                final Button ib_cancel=(Button)layout.findViewById(R.id.btn_cancel);
                tv_title.setText(context.getString(R.string.app_update_title));

                OnClickListener clicklistener = new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch(v.getId())
						{
						case R.id.btn_cancel :
							ad.dismiss();
							cancelUpdate = true;  
							break;
						}
					}};
                ib_cancel.setOnClickListener(clicklistener); 
                downloadApk(); 
        }
//        AlertDialog.Builder builder = new Builder(context);  
//        builder.setTitle(R.string.app_update_title);  
//        final LayoutInflater inflater = LayoutInflater.from(context);  
//        View view = inflater.inflate(R.layout.softupdate_progress, null);  
//        mProgressBar = (ProgressBar) view.findViewById(R.id.update_progress);  
//        builder.setView(view);  
//        builder.setNegativeButton(R.string.update_dialog_btn_cancel, new OnClickListener() {  
//            @Override  
//            public void onClick(DialogInterface dialog, int which) {  
//                // TODO Auto-generated method stub  
//                dialog.dismiss();  
//                cancelUpdate = true;  
//            }
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}  
//        });  
//        mDownloadDialog = builder.create();  
//        mDownloadDialog.show();  
//        downloadApk();  
    }  
    /** 
     */  
    private void downloadApk() {
        // TODO Auto-generated method stub  
        new ApkSelfUpdateThread().start();  
    }
    
//    /**
//     * @param path
//     */
//    public void registerDownloadListener(String filepath,OnDownLoadListener listener){
//    	if(mDownloadListenerList.containsKey(filepath))
//    		return;
//    	else{
//    		mDownloadListenerList.put(filepath, listener);
//    	}
//    }
//    
//    /**
//     * @param filePath
//     */
//    public void unresgisterDownloadListener(String filePath){
//    	mDownloadListenerList.remove(filePath);
//    	cancelDownload(filePath);
//    }
    
    
    /**
     * @param filePath
     * @return
     */
    public boolean deleteDownloadFile(String filePath){
    	initDownloadPath();
    	String appName = Util.getAppName(filePath);
    	String romFile = dsRomPath+appName+".apk.temp";
    	String sdFile = "";
    	if(dsExtendPath!=null && !"".equals(dsExtendPath)){
    		sdFile = dsExtendPath+appName+".apk.temp";
    		Util.cleanUpDownLoadFile(sdFile);
    	}
    	 Util.cleanUpDownLoadFile(romFile);
    	return true;
    }
    
    
    /**
	 */
    public void checkUpdate(boolean isShow,Context context,Handler Handlerup) {  
    	switch(isUpdate(context))
    	{
    	case NEEDDOWNLOAD:
//    		showNoticeDialog(context);
    		Message msg = new Message();
				msg.what = 3;
			Handlerup.sendMessage(msg);
    		break;
    	case NOTNEEDDOWNLOAD:
        	if(isShow)
        	{
        		Message msg2 = new Message();
				msg2.what = 5;
			    Handlerup.sendMessage(msg2);
        	}
//        	Toast.makeText(mContext,mContext.getString(R.string.no_update),Toast.LENGTH_SHORT).show(); 
    		break;
    	case NETWORKERROR:
    		Message msg3 = new Message();
			msg3.what = 4;
		    Handlerup.sendMessage(msg3);
    		break;
    	}    
    }
    
    /** 
     * @return 
     */  
    public int isUpdate(Context context) {
    	HashMap<String,String> mHashMap = new HashMap<String,String>();
    	PackageManager pm = context.getPackageManager();
    	mSelfUpdateFileInfo = new FileInfo();
    	PackageInfo info=null;
		try {
			info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mSelfUpdateFileInfo.setFileName(pm.getApplicationLabel(info.applicationInfo).toString());
		mSelfUpdateFileInfo.setPackageName(info.applicationInfo.packageName);
		mSelfUpdateFileInfo.setVersion(info.versionCode);
		mSelfUpdateFileInfo.setVersionName(info.versionName);
		mSelfUpdateFileInfo.setFilePath(info.applicationInfo.publicSourceDir);
        int versionCode = getVersionCode(context);
        deleteDownloadFile(mSelfUpdateFileInfo.getFilePath());
        String serverPath =serverAddress;
        String appName = "WantSport";
        final String appPath = appName+".xml";
        try {
            mHashMap = TextUtils.parseXml(HttpUtils.getContent(serverPath+appPath));
        } catch (Exception e) {  
            // TODO: handle exception  
            e.printStackTrace();
//           mHandler.sendEmptyMessage(DOWNLOAD_ERROR);
            return NETWORKERROR;
        }  
        if(null != mHashMap && null != mHashMap.get("version")) {  
            int serviceCode = Integer.valueOf(mHashMap.get("version"));  
            Log.d(TAG, "serverVersion:"+serviceCode+" and localVersion:"+versionCode);
//            showToast.show(context, "serverVersion:"+serviceCode+" and localVersion:"+versionCode, 2000);
            if(serviceCode > versionCode) {
            	String downUrl = mHashMap.get("url");
            	mSelfUpdateFileInfo.setDownUrl(downUrl);
                return NEEDDOWNLOAD;  
            }
        }  
        return NOTNEEDDOWNLOAD;  
    }
    
    /** 
     * @param context 
     * @return 
     */  
    private int getVersionCode(Context context) {  
        // TODO Auto-generated method stub  
        int versionCode = 0;  
        try {  
            versionCode = context.getPackageManager().getPackageInfo(  
            		context.getPackageName(), 0).versionCode;  
        } catch (NameNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return versionCode;  
    }  

    public void showNoticeDialog(final Context context) {  
        // TODO Auto-generated method stub  
    	try{
        AlertDialog.Builder builder = new Builder(context);
	    LayoutInflater inflater;
	    View layout=null;	
	    final AlertDialog ad = builder.create();
	   
        if (!ad.isShowing())
        {
                ad.show();
                inflater=  LayoutInflater.from(context);
        		layout = inflater.inflate(R.layout.update, null);
                ad.getWindow().setContentView(layout);
                ad.setOnKeyListener(new OnKeyListener(){
    				public boolean onKey(DialogInterface dialog, int keyCode,
    						KeyEvent event) {
    					 if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
    				        	return true;
    				        }  
    					return false;
    				}
    			});
                final TextView tv_title = (TextView)layout.findViewById(R.id.tv_title);
                final TextView tv_toast = (TextView)layout.findViewById(R.id.tv_toast);
                final Button btn_update=(Button)layout.findViewById(R.id.btn_update);
                final Button btn_cancel=(Button)layout.findViewById(R.id.btn_cancel);
                tv_title.setText(context.getString(R.string.self_update_title));
                tv_toast.setText(context.getString(R.string.self_update_info));            
                
                tv_toast.setTextSize(20);
               OnClickListener clicklistener = new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch(v.getId())
						{
						case R.id.btn_update :
							ad.dismiss();
					    	if(Util.isSDCardReady())
					    		showDownloadDialog(context); 
					    	else
					    		showToast.normaltoast(mContext, mContext.getString(R.string.insertSD),showToast.TWO_SECOND);
							break;
							
						case R.id.btn_cancel :
							ad.dismiss();
							break;
						}
					}};
               btn_update.setOnClickListener(clicklistener);
               btn_cancel.setOnClickListener(clicklistener);     
        }
    	}catch(Exception e)
    	{}
        
//        
//        
//        
//        
//        
//        builder.setTitle(R.string.self_update_title);  
//        builder.setMessage(R.string.self_update_info);  
//        builder.setPositiveButton(R.string.self_updatebtn, new OnClickListener() {  
//            @Override  
//            public void onClick(DialogInterface dialog, int which) {  
//                // TODO Auto-generated method stub  
//                dialog.dismiss();  
//                showDownloadDialog(context);  
//            }  
//        });  
//        builder.setNegativeButton(R.string.self_updatebtn_later, new OnClickListener() {  
//            @Override  
//            public void onClick(DialogInterface dialog, int which) {  
//                // TODO Auto-generated method stub  
//                dialog.dismiss();  
//            }  
//        });  
//        Dialog noticeDialog = builder.create();  
//        noticeDialog.show();  
    } 
    
    
    
    
    
    /** 
     */  
    private void installApk()  
    {  
    	String savePath = Util.isSDCardReady()?dsExtendPath:dsRomPath;
        File apkfile = new File(savePath, Util.getAppName(mSelfUpdateFileInfo.getFilePath())+".apk");  
        if (!apkfile.exists())  
        {  
            return;  
        }  
        SharedPreferences.Editor editor3 = mContext.getSharedPreferences("setaim_msg",0).edit();		
		 editor3.putBoolean("haveUP",true);
		 editor3.commit();
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
        Intent i_upsilde = new Intent(SportData.RECEIVER_UPSILDE);
        mContext.sendBroadcast(i_upsilde);

//    	String savePath="";
//    	if(Util.isSDCardReady())
//            savePath = UpdateManager.getInstance().getDsExtendPath();
//    	else
//    		savePath = UpdateManager.getInstance().getDsRomPath();
//    	InstallManager install = new InstallManager(mContext);
//    	install.UpdateSelfApp(savePath+Util.getAppName(mSelfUpdateFileInfo.getFilePath())+".apk");
    }
}
