package com.desay.sport.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface excuteDB {

	public  void openOrCreateDB();
	public void onUpgrade();
	public  String getDBPath();
	public  void closeDB();
	public  void dropTable();
    public  boolean tableIsExist();
	public  void CreateTable();
//	public  void UpdateDefaultRecord(Context context,String newname);
	public  void UpdateDefaultRecord(Context context, String newname,int dele);
	//插入更新记录
	public  void SaveUserInfo(Context context,entity_userinfo info);//保存数据
	public  void refreshTable_UserInfo(String username,HashMap<String , String> ChangeValue);
	public  void refreshTable_SportAchieve(String username,HashMap<String , String> ChangeValue);
	public void refreshTable_SportAchieve(String username,List<entity_sportachieve> list,String upload);
	public  void InsertSportRecord(String username,ContentValues contentValues);
	public  void InsertSportRecord(String username,List<entity_sportrecord> list,String upload);
	public  void InsertSportRoute(String username,ContentValues contentValues);
	public  void InsertSportRoute(String username,List<entity_sendroute> list,String upload);
	public  void UPSendRecordStatus(String name,ArrayList<entity_sendrecord>sendlist,int start,int end);//更新发送数据
	public void refreshTable_ActionMsg(String myname,List<Map<String,String>> datalist); 
	public void SaveUserPassword(Context context,String pass,String showname);
//	//删除记录
	public  void DelUserInfo(String username);	
	public  void DelSportRecord(String username);
	public  void DelSportRoute(String username);
	public  void DelSportAchieve(String username);
	public  void DeleteActionMsg();
	public  void DeleteActionMsg(String username);
	public  void DelHeatUploadData(String username,long starttime_begin, long starttime_end);
//	public  void DelLoopUploadData(String username,long starttime_begin, long starttime_end);
	
	//读取记录
	public  entity_userinfo GetUserInfo(Context context);
	public  boolean HaveUserInfo(Context context);
	public  entity_sportrecord GetSportRecord(String username,long starttime);//获取某条记录
	public  List<entity_sportrecord> GetSportRecord(String username,long starttime_begin,long starttime_end);// 获取starttime属性在某区间的记录
	public  List<entity_sportroute> GetSportRoute(String username,long starttime);
	public  List<entity_sportachieve> GetSportAchieve(String username,String classify,String upload);
	public List<entity_sendrecord> GetNoSendRecord(String username,long starttime_begin,long starttime_end);//读取未发送数据
	public String GetSendRoute(String username,long starttime);//读取相应经纬度
	public List<Map<String,Object>> GetActionMsg(String myname);
	
	//获取数据
	//(若starttime_begin和starttime_end两个参数为null,则默认范围为所有记录)
	public  long GetLastDistance(String username);
	public  long GetDistanceCount(String username,long starttime_begin,long starttime_end); //获取区间时间内运动距离总和 
	public  long GetDurationTimeCount(String username,long starttime_begin,long starttime_end);//获取区间时间内锻炼时间总和  
	public  long GetCalorieCount(String username,long starttime_begin,long starttime_end);//获取区间时间内消耗卡路里数总和   
	public  int GetSportTimes(String username,long starttime_begin,long starttime_end);//获取区间内运动次数
	//获取数据
	public  entity_sportrecord GetFastest(String username,int sporttype); //最快纪录
	public  entity_sportrecord GetFarthest(String username,int sporttype);//最远纪录  
	public  entity_sportrecord GetLongest(String username,int sporttype);//最长纪录  
	public  entity_sportrecord GetEst(String username,int sporttype,int esttype);//获取各种纪录  
	
	public entity_table getData_Everyday(String username, int year, int month,int day,int mode);
	public entity_table getData_EveryMonth(String username, int year, int month,int _mode);
	//更新上传状态
	public void RefreshUploadStatus_SportAchieve(String name,int type);//更新运动成就数据为已上传。
	
	public void ifCriticalRefresh(ContentValues contentValues);
	public int insertCriticalName(String userName);
	public void ifRefresh(ContentValues contentValues);
	public void medalOneDay(String username, ContentValues contentValues);
	public void ifInsert(ContentValues contentValues );
	public void medalAllDistance(int allFootDistance, int allBikeDistance,ContentValues contentValues);
	public void medalDuractionTime(int allDuractionTime,ContentValues contentValues);
	public void medalTimes(int allTimes, ContentValues contentValues);
	public void insertContentValues(String username, long startTime,int sportType);
	public int medalSportType(int sportType, String username,long startTime);
	public int getMedalName(String username);
	public List<Double> getGpsTablePoint(String username, long startTime);	
	public List<entity_sportroute> getMapPoint(String username, long startTime);
	public List<entity_sportrecord> getRecord(String username, int id_start,int id_over);
	public int getCount(String username);
	public void findMaxDisDur(entity_sportrecord sportrecord, String sql, String style);
	public void findMaxSpeed(entity_sportrecord sportrecord, String sql);
	//手环数据处理
	public void refreshSendHeat(String name);
//	public void InsertLoopRecord(String username,List<entity_sportloop> list,String upload);
	public void InsertHeatRecord(String username,List<entity_loopheat> list,String upload);
	
	public void insertIwanMac(entity_mac mac);
	public void delIwanMac(String username);	
	public entity_mac getIwanMac(String username);
	public void saveIwanMac(Context context,String addr,sportDB db);
	//删除最近周期内各表的记录
	public List<entity_sendrecord> DelRecentUploadData(String username,long starttime_begin,long starttime_end);//删除一个时间段的已上传数据
}
