/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.desay.sport.update;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Config;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Util {
    private static String ANDROID_SECURE = "/mnt/sdcard/.android_secure";

    private static final String LOG_TAG = "Util";

    public static boolean isSDCardReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String makePath(String path1, String path2) {
        if (path1.endsWith(File.separator))
            return path1 + path2;

        return path1 + File.separator + path2;
    }

    public static String getSdDirectory() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static boolean isNormalFile(String fullName) {
        return !fullName.equals(ANDROID_SECURE);
    }
    
    /*
     * 采用了新的办法获取APK图标，之前的失败是因为android中存在的一个BUG,通过
     * appInfo.publicSourceDir = apkPath;来修正这个问题，详情参见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
        return null;
    }
    // does not include sd card folder
    private static String[] SysFileDirs = new String[] {
        "miren_browser/imagecaches"
    };

    public static boolean setText(View view, int id, String text) {
        TextView textView = (TextView) view.findViewById(id);
        if (textView == null)
            return false;

        textView.setText(text);
        return true;
    }

    public static boolean setText(View view, int id, int text) {
        TextView textView = (TextView) view.findViewById(id);
        if (textView == null)
            return false;

        textView.setText(text);
        return true;
    }

    // comma separated number
    public static String convertNumber(long number) {
        return String.format("%,d", number);
    }

    // storage, G M K B
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static class SDCardInfo {
        public long total;
        public long free;
    }

    /**
     * 获取sd卡空间信息
     * @return
     */
    public static SDCardInfo getSDCardInfo() {
        String sDcString = android.os.Environment.getExternalStorageState();

        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment.getExternalStorageDirectory();
            try {
                android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());
                // 获取SDCard上BLOCK总数
                long nTotalBlocks = statfs.getBlockCount();
                // 获取SDCard上每个block的SIZE
                long nBlocSize = statfs.getBlockSize();
                // 获取可供程序使用的Block的数量
                long nAvailaBlock = statfs.getAvailableBlocks();
                // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
                long nFreeBlock = statfs.getFreeBlocks();
                SDCardInfo info = new SDCardInfo();
                // 计算SDCard 总容量大小MB
                info.total = nTotalBlocks * nBlocSize;
                // 计算 SDCard 剩余大小MB
                info.free = nAvailaBlock * nBlocSize;
                return info;
            } catch (IllegalArgumentException e) {
                Log.e(LOG_TAG, e.toString());
            }
        }
        return null;
    }
    
    /**
     * 获取系统内部空间信息
     * @return
     */
    public static SDCardInfo getSystemRomInfo(){
    	File root = android.os.Environment.getDataDirectory();
    	 try {
             android.os.StatFs statfs = new android.os.StatFs(root.getPath()+"/data");
             // 获取SDCard上BLOCK总数
             long nTotalBlocks = statfs.getBlockCount();
             // 获取SDCard上每个block的SIZE
             long nBlocSize = statfs.getBlockSize();
             // 获取可供程序使用的Block的数量
             long nAvailaBlock = statfs.getAvailableBlocks();
             // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)
             long nFreeBlock = statfs.getFreeBlocks();
             SDCardInfo info = new SDCardInfo();
             // 计算SDCard 总容量大小MB
             info.total = nTotalBlocks * nBlocSize;
             // 计算 SDCard 剩余大小MB
             info.free = nAvailaBlock * nBlocSize;
             return info;
         } catch (IllegalArgumentException e) {
             Log.e(LOG_TAG, e.toString());
         }
    	 return null;
    }

    public static String formatDateString(Context context, long time) {
        DateFormat dateFormat = android.text.format.DateFormat
                .getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat
                .getTimeFormat(context);
        Date date = new Date(time);
        return dateFormat.format(date) + " " + timeFormat.format(date);
    }

    public static String sZipFileMimeType = "application/zip";

    public static int CATEGORY_TAB_INDEX = 0;
    public static int SDCARD_TAB_INDEX = 1;
    
    /** 
     * 获取软件版本号 
     * @param context 
     * @return 
     */  
    public static int  getVersionCode(Context context) {  
        // TODO Auto-generated method stub  
        int versionCode = 0;  
        // 获取软件版本号，对应AndroidManifest.xml下android:versionCode  
        try {  
            versionCode = context.getPackageManager().getPackageInfo(  
            		context.getPackageName(), 0).versionCode;  
        } catch (NameNotFoundException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return versionCode;  
    }  
    
    /**
     * 获取文件名字(无后缀)
     * @param path apk绝对路径
     * @return
     */
    public static String getAppName(String path){
    	String name="";
    	String pathPrefix="/";
    	String apkPrefix =".apk";
    	int start = path.lastIndexOf(pathPrefix);
    	int end = path.lastIndexOf(apkPrefix);
    	if(start>=0 && end >=0 && end > start){
    		name = path.substring(start+1, end);
    	}
    	return name;
    }
    
    /**
     * 获取手机内预置软件
     * @param context
     * @return
     */
    public static List<PackageInfo> getSystemApps(Context context) {  
        List<PackageInfo> apps = new ArrayList<PackageInfo>();  
        PackageManager pManager = context.getPackageManager();  
        //获取手机内所有应用  
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
        for (int i = 0; i < paklist.size(); i++) {  
            PackageInfo pak = (PackageInfo) paklist.get(i);  
            //判断是否为系统预装的应用程序  
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) > 0) {  
                // customs applications  
                apps.add(pak);  
            }  
        }  
        return apps;  
    }
    
    /**
     * 获取手机非内预置软件
     * @param context
     * @return
     */
    public static List<PackageInfo> getApps(Context context) {  
        List<PackageInfo> apps = new ArrayList<PackageInfo>();  
        PackageManager pManager = context.getPackageManager();  
        //获取手机内所有应用  
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
        for (int i = 0; i < paklist.size(); i++) {  
            PackageInfo pak = (PackageInfo) paklist.get(i);  
            //判断是否为非系统预装的应用程序  
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {  
                // customs applications  
                apps.add(pak);  
            }  
        }  
        return apps;  
    }  
    
    /**
     * 获取手机所有软件
     * @param context
     * @return
     */
    public static List<PackageInfo> getAllApps(Context context) {  
        List<PackageInfo> apps = new ArrayList<PackageInfo>();  
        PackageManager pManager = context.getPackageManager();  
        //获取手机内所有应用  
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);  
        for (int i = 0; i < paklist.size(); i++) {  
            PackageInfo pak = (PackageInfo) paklist.get(i);  
            apps.add(pak);  
        }  
        return apps;  
    }
    
    /**
     * 删除下载文件
     * @param filepath
     * @return
     */
    public static boolean cleanUpDownLoadFile(String filepath){
    	File updateFile = new File(filepath);
    	if(updateFile.exists()){
    	   //当不需要的时候，清除之前的下载文件，避免浪费用户空间
    	   Log.d(LOG_TAG, "delete file");
    	   return updateFile.delete(); 
    	}
    	return false;
    }
    
    /**
     * 判断3G/Wifi 是否连接
     * @param context
     * @return
     */
    public static boolean checkNetworkState(Context context){
    	ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo nInfo = connManager.getActiveNetworkInfo();
		if (nInfo != null && nInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//
//			if (nInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
//					|| nInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
//					|| nInfo.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
//				Log.d(LOG_TAG, "mobile connected in 2G");
//				return true;
//			} else {
//				System.out.println("type:" + nInfo.getSubtype());
//				System.out.println("not mobile");
//				return false;
//			}
			return true;
		} else if (nInfo != null && nInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			Log.d(LOG_TAG, "wifi connected");
			return true;
		} else
			return false;
    }
    
    public static int countPageSize(int total,int pagesize){
    	return (total+pagesize-1)/pagesize;
    }
    
//    /**
//     * 得到未安装apk的信息
//     * @param context
//     * @param archiveFilePath
//     * @return
//     */
//    public static LocalApp getUninatllApkInfo(Context context, String archiveFilePath){
//    	LocalApp app = new LocalApp();
//    	File file = new File(archiveFilePath);
//        PackageManager pm = context.getPackageManager();  
//        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);  
//        if(info != null){  
//            ApplicationInfo appInfo = info.applicationInfo;  
//            String appName = pm.getApplicationLabel(appInfo).toString();  
//            String packageName = appInfo.packageName;  
//            //Drawable icon = pm.getApplicationIcon(appInfo);
//            app.setAppName(appName);
//            app.setAppPackageName(packageName);
//            app.setAppVersion(info.versionCode);
//            app.setAppVersionName(info.versionName);
//            app.setAppPath(archiveFilePath);
//            long size = file.length();
//            app.setAppSize(FormetFileSize(size));
//        }
//        return app;
//    }  
    
    public static String FormetFileSize(long fileS)
    {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
    }
}
