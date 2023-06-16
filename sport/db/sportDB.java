package com.desay.sport.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import com.desay.sport.data.SportData;
import com.desay.sport.main.BuildConfig;
import com.desay.sport.main.R;
import com.desay.sport.multimedia.Mp3Info;

import android.R.bool;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.desay.sport.data.SportData;
import com.desay.sport.data.SwitchDayNum;
import com.desay.sport.friend.SendServer;
import com.desay.sport.multimedia.Mp3Info;

public class sportDB implements excuteDB
{
	private SQLiteDatabase database;
	private String TAG = "sportDB";
	private Context context;
	Calendar calendarMinite = Calendar.getInstance();
	/* db name */
	private static final String databaseFilename = "sportDB.db";

	/* table name */
	public static final String TABLE_USERINFO = "UserInfo";
	public static final String TABLE_SPORTRECORD = "SportRecord";
	public static final String TABLE_SPORTROUTE = "SportRoute";
	public static final String TABLE_SPORTACHIEVE = "SportAchieve";
	public static final String TABLE_ACTIONMSG = "ActionMsg";
	public static final String TABLE_SPORTHEART = "SportHeart";
	public  static final  String TABLE_SPORTHANDLOOP = "sportHandLoop";
	public static final String TABLE_IWANMAC = "IwanMac";
	/* Table ID */
	public static final String ID_ID = "_id";
	public static final String ID_USERNAME = "_username";
	public static final String ID_IFUPLOAD = "_ifupload";
	/* UserInfo */
	public  static final  String ID_SHOWNAME = "_showname";
	public static final String ID_PASSWORD = "_password";
	public static final String ID_SEX = "_sex";
	public static final String ID_AGE = "_age";
	public static final String ID_HEIGHT = "_height";
	public static final String ID_WEIGHT = "_weight";
	public static final String ID_CITY = "_city";
	public static final String ID_HEADPHOTO = "_headphoto";
	/* SportRecord */
	public static final String ID_STARTTIME = "_starttime";
	public static final String ID_ENDTIME = "_endtime";
	public static final String ID_SPORTTYPE = "_sporttype";
	public static final String ID_DISTANCE = "_distance";
	public static final String ID_DURATIONTIME = "_durationtime";
	public static final String ID_CALORIE = "_calorie";
	public static final String ID_AVERAGE_SPEED = "_avgspeed";
	public static final String ID_MODE = "_mode";
	public  static final  String ID_FOOTNUM = "_footNum";
	/* SportRoute */
	public static final String ID_LONGITUDE = "_longitude";
	public static final String ID_LATITUDE = "_latitude";
	public static final String ID_INSTANT_SPEED = "_speed";
	/* SportAchieve */
	public static final String ID_CLASSIFY = "_classify";
	public static final String ID_ACHIEVETYPE = "_achievetype";
	public static final String ID_ACHIEVENAME = "_achievename";
	public static final String ID_ACHIEVERECORD = "_achieverecord";
	/* ActionMsg */
	public static final String ID_ACTIONSTATUS = "_status";
	public static final String ID_ACTIONTIME = "_actiontime";
	public  static final  String ID_ACTIONNAME="_fname";
	/* SportHeart */
	public static final String ID_HEART = "_heart";

	/* IwanMac */
	public static final String ID_MAC = "_mac";
	public static final String ID_SYNTIME="_syntime";
	public static final int TYPE_FASTEST = 0;
	public static final int TYPE_FARTHEST = 1;
	public static final int TYPE_LONGEST = 2;

	int medalName = -1;// 运动停止后，获得奖章的名字

	public sportDB(Context tcontext)
	{
		context = tcontext;
	}

	////////////////////	
	@Override
	public void openOrCreateDB() {
		// TODO Auto-generated method stub
		if (database==null|| !database.isOpen())
		{
//		String DATABASE_PATH=android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/wantsport/";	
//		String filename = DATABASE_PATH + "/" + databaseFilename;	
		database =context.openOrCreateDatabase(databaseFilename, Context.MODE_PRIVATE, null);
		}
	}
	
	@Override
	public String getDBPath() {
		// TODO Auto-generated method stub
		String path = null;
		try
		{
			openOrCreateDB();
			path = database.getPath();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
		
		return path;
	}
	
	@Override
	public void closeDB() {
		// TODO Auto-generated method stub
			if(database!=null && database.isOpen())
			database.close();
			database=null;
	}
	
	@Override
	public void CreateTable() {//创建4张表
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			database.execSQL("CREATE TABLE if not exists UserInfo ("+
					"_id INTEGER PRIMARY KEY , "+
					"_username text, " +
				"_password text, " +
					"_sex text, "+
					"_age text, "+
					"_height text, "+
					"_weight text, "+
					"_city text, "+
					"_headphoto text,"+
				"_ifupload text," +
				"_showname text);");
			
			database.execSQL("CREATE TABLE if not exists SportRecord ("+
					"_id INTEGER PRIMARY KEY , "+
					"_username text, " +
					"_starttime long, "+
					"_endtime long, "+
					"_sporttype text, "+
					"_distance text, "+
					"_durationtime text, "+
					"_calorie text, "+
					"_avgspeed text, "+
				"_footNum INTEGER, " +
				"_mode text, " +
				"_ifupload text);");
		
			database.execSQL("CREATE TABLE if not exists SportHeart ("+
					"_id INTEGER PRIMARY KEY , " +
					"_username text, "+
					"_starttime long, " + 
					"_heart INTEGER, "+
					"_ifupload text);");
         
			database.execSQL("CREATE TABLE if not exists sportHandLoop ("+
					"_id INTEGER PRIMARY KEY , " +
					"_starttime long, " +
					"_endtime long, "+
					"_footNum INTEGER, " +
					"_durationtime text);");
					 
			database.execSQL("CREATE TABLE if not exists SportRoute ("+
					"_id INTEGER PRIMARY KEY , "+
					"_username text, " +
					"_starttime long, "+
					"_longitude text, "+
					"_latitude text, "+
					"_distance text, "+
					"_speed text, "+
					"_ifupload text);");
			
			database.execSQL("CREATE TABLE if not exists SportAchieve ("+
					"_id INTEGER PRIMARY KEY , "+
					"_username text, " +
					"_classify text, "+
					"_starttime long, "+
					"_achievetype text, "+
					"_achievename text, "+
					"_achieverecord text, "+
					"_ifupload text);");
			
			database.execSQL("CREATE TABLE if not exists SportMusic ("+
					"_id INTEGER PRIMARY KEY , "+
					"_singer text, " +
					"_musicId text,"+
					"_time text, "+
					"_musicName text, "+
					"_musicPath text, "+
					"_albumKey text);");
			database.execSQL("CREATE TABLE if not exists ActionMsg ("+
					"_id INTEGER PRIMARY KEY , "+
					"_username text, " +
					"_status text, "+
					"_actiontime text, "+
				"_headphoto text, "+
				"_fname text, " +
	            "_showname text);");
			
			database.execSQL("CREATE TABLE if not exists IwanMac ("+
					"_id INTEGER PRIMARY KEY , " +
					"_username text, "+
					"_syntime text, "+
					"_mac text, "+
					"_ifupload text);");
		database.setVersion(getAppVersion()); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}	
	}
	public void onUpgrade() {
		int newVersion=0;
	   try
		{
		  openOrCreateDB();
		  int oldVersion=database.getVersion();
		  newVersion=getAppVersion();
//		  Log.d("onUpgrade========="+oldVersion,"newVersion"+newVersion);		
	      if (oldVersion<16 && oldVersion < newVersion) {
	    	  dropTable();
              CreateTable();
	       }
	      else if(oldVersion == 16 )
	      {
	    	  	openOrCreateDB();
				database.execSQL("CREATE TABLE if not exists sportHandLoop ("+
						"_id INTEGER PRIMARY KEY , " +
						"_starttime long, " +
						"_endtime long, "+
						"_footNum INTEGER, " +
						"_durationtime text);");
				database.setVersion(getAppVersion()); 
	      }
		}catch (Exception e)
		{
			e.printStackTrace();
				database.setVersion(newVersion); 
		}
		finally
		{
			closeDB();
		}	
	}
	
	public void InsertMusic(HashMap<String , String> ChangeValue){
		Cursor c = null;
		try
		{
			openOrCreateDB();
			int ID=0;
			 ContentValues newValues = new ContentValues();	
			 Iterator iter=ChangeValue.entrySet().iterator();
				while(iter.hasNext()){
				Map.Entry entry=(java.util.Map.Entry) iter.next();
				Object key=entry.getKey();
				Object val=entry.getValue();
				if(key.equals("_singer")){
					newValues.put("_singer", (String)val);
				}else if(key.equals("_time")){
				    newValues.put("_time", (String) val);
				}else if(key.equals("_musicName")){
					 newValues.put("_musicName", (String) val);
				}else if(key.equals("_musicPath")){
					newValues.put("_musicPath", (String) val);
				}else if(key.equals("_albumKey")){
					newValues.put("_albumKey", (String) val);
				} else if(key.equals("_musicId")){
					newValues.put("_musicId", (String) val);
					ID=Integer.parseInt((String)val);
				}    
				}    
				 c=database.query("SportMusic", null, "_musicId=?", new String[]{ID+""}, null, null, null);
				if(c.getCount()==0){
					database.insert("SportMusic", null, newValues);
				}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(c!=null)
			c.close();
	        closeDB();
		}
	
	}
	
	public void DelMusic(int musicId){
		try
		{
			openOrCreateDB();
			database.delete("SportMusic", "_musicId=?",new String[]{musicId+""});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}	
	}
	
	public List<Mp3Info> GetMusic(){
		
		Cursor c =null;
		List<Mp3Info> list=new ArrayList<Mp3Info>();
		try
		{
			openOrCreateDB();
			c=database.query("SportMusic", null, null, null, null, null, null);			
			c.moveToFirst();
			for(int i=0;i<c.getCount();i++){
				Mp3Info m=new Mp3Info();
				m.setId(Integer.parseInt(c.getString(c.getColumnIndex("_musicId"))));
				m.setAlbumkey(c.getString(c.getColumnIndex("_albumKey")));
				m.setMusicName(c.getString(c.getColumnIndex("_musicName")));
				m.setMusicPath(c.getString(c.getColumnIndex("_musicPath")));
				m.setSinger(c.getString(c.getColumnIndex("_singer")));
				m.setTime(Integer.parseInt(c.getString(c.getColumnIndex("_time"))));
				list.add(m);
				c.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(c!=null)
			c.close();
			closeDB();
		}
		return list;
	}
	@Override
	public void dropTable() {
		 
		try
		{
			openOrCreateDB();
			database.execSQL("DROP TABLE UserInfo" );
			database.execSQL("DROP TABLE SportRecord");	
			database.execSQL("DROP TABLE SportRoute");
			database.execSQL("DROP TABLE SportAchieve");
			database.execSQL("DROP TABLE ActionMsg");
			database.execSQL("DROP TABLE SportHeart");
			database.execSQL("DROP TABLE sportHandLoop");
		    database.execSQL("DROP TABLE IwanMac");
		    database.execSQL("DROP TABLE SynTime");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}	
	}

	@Override
	public boolean tableIsExist() {
		 
		 boolean result = false;
	        Cursor cursor = null;
		try
		{
			 openOrCreateDB();
			 String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+"UserInfo"+"'; ";
             cursor = database.rawQuery(sql, null);
             if(cursor.moveToNext()){
                     int count = cursor.getInt(0);
                     if(count>0){
                             result = true;
                     }
             }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (cursor!= null)
			cursor.close();
	        closeDB();
		}
        return result;
	}

	public entity_userinfo GetUserInfo(Context context)
	{
		Cursor c=null;
		entity_userinfo userinfo=new entity_userinfo();
		try
		{
			openOrCreateDB();
			
			 c=database.query(TABLE_USERINFO,null, ID_USERNAME+"=?", new String[]{SportData.getUserName(context)}, null, null, null);
			if(c !=null)
			{
				if(c.moveToFirst())
				{
					do{			
						userinfo.setSex(c.getString(c.getColumnIndex(ID_SEX)));	
						userinfo.setBirthday(c.getString(c.getColumnIndex(ID_AGE)));
						userinfo.setHeight(c.getString(c.getColumnIndex(ID_HEIGHT)));
						userinfo.setWeight(c.getString(c.getColumnIndex(ID_WEIGHT)));
						userinfo.setCity(c.getString(c.getColumnIndex(ID_CITY)));
						userinfo.setHeadPhoto(c.getString(c.getColumnIndex(ID_HEADPHOTO)));			
					userinfo.setUpLoad(c.getString(c.getColumnIndex(ID_IFUPLOAD)));	
					userinfo.setShowname(c.getString(c.getColumnIndex(ID_SHOWNAME)));
					}while(c.moveToNext());
				}	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();	
			closeDB();
		}

		return userinfo;
	}
	
	@Override
	public boolean HaveUserInfo(Context context) {
		// TODO Auto-generated method stub
		boolean flag = false;	
		Cursor c=null;
		try
		{
			openOrCreateDB();
			c=database.query(TABLE_USERINFO,null, ID_USERNAME+"=?", new String[]{SportData.getUserName(context)}, null, null, null);
			if(c !=null && c.getCount()>0)
			{
				flag = true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();	
			closeDB();
		}
		return flag;
	}
	
	@Override
	public void SaveUserInfo(Context context,entity_userinfo info) {
		
			HashMap<String, String> cv = new HashMap<String, String>();
			entity_userinfo preinfo = GetUserInfo(context);		
			if(preinfo.getSex()==null       || (!info.getSex().equals("")&&!preinfo.getSex().equals(info.getSex())))cv.put(ID_SEX, info.getSex());
			if(preinfo.getBirthday()==null       || (!info.getBirthday().equals("")&&!preinfo.getBirthday().equals(info.getBirthday())))cv.put(ID_AGE, info.getBirthday());
			if(preinfo.getHeight()==null    || (!info.getHeight().equals("")&&!preinfo.getHeight().equals(info.getHeight())))cv.put(ID_HEIGHT, info.getHeight());	
			if(preinfo.getWeight()==null    || (!info.getWeight().equals("")&&!preinfo.getWeight().equals(info.getWeight())))cv.put(ID_WEIGHT, info.getWeight());
			if(preinfo.getCity()==null      || (!info.getCity().equals("")&&!preinfo.getCity().equals(info.getCity())))cv.put(ID_CITY, info.getCity());
			if(preinfo.getHeadPhoto()==null || (!info.getHeadPhoto().equals("")&&!preinfo.getHeadPhoto().equals(info.getHeadPhoto())))cv.put(ID_HEADPHOTO, info.getHeadPhoto());	
			if(preinfo.getUpLoad()==null    || (!info.getUpLoad().equals("")&&!preinfo.getUpLoad().equals(info.getUpLoad())))cv.put(ID_IFUPLOAD, info.getUpLoad());
	        refreshTable_UserInfo(SportData.getUserName(context), cv);
	}
	public void SaveUserPassword(Context context,String pass,String showname) {
		// TODO Auto-generated method stub
		Log.e(TAG,"showname===="+showname);
			HashMap<String, String> cv = new HashMap<String, String>();
			if(pass!=null)
			   cv.put(ID_PASSWORD, pass);
			if(showname!=null)
			   cv.put(ID_SHOWNAME, showname);
	        refreshTable_UserInfo(SportData.getUserName(context), cv);
	}
	@Override
	public void refreshTable_UserInfo(String username,HashMap<String, String> ChangeValue) 
	{
			//首先查询数据库中是否有用户信息记录
		Cursor c= null;
		try
		{
			openOrCreateDB();
			ContentValues newValues = new ContentValues();	
			Iterator iter=ChangeValue.entrySet().iterator();
			Map.Entry entry;
			while(iter.hasNext())
			{
				entry=(java.util.Map.Entry) iter.next();
				newValues.put((String) entry.getKey(), (String) entry.getValue());
			}
			c=database.query(TABLE_USERINFO, null, "_username=?", new String[]{username}, null, null, null);
			System.out.println(c.getCount());
			 if(c.getCount()==0)
		     {		
		    	  newValues.put(ID_USERNAME,username);
		          database.insert(TABLE_USERINFO, null, newValues);
		     }
		     else
		     {  
		    	 database.update(TABLE_USERINFO,newValues, "_username=?", new String[]{username});
		    	 //若无记录则插入，有记录则更新记录
		     }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			 c.close();
		     closeDB();
		}
			
	//			newValues.put(ID_IFUPLOAD, upsend);//每次插入或更新操作，记录都设置成未上传
	}

	public void DelUserInfo(String username) {
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			database.delete(TABLE_USERINFO, ID_USERNAME+"=?",new String[]{username});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}		
	}
	
	@Override
	public void refreshTable_SportAchieve(String username,HashMap<String , String> ChangeValue) {//插入运动成就
		 
		 Cursor c = null;
		try
		{
			openOrCreateDB();
			if(ChangeValue.containsKey(ID_CLASSIFY)&&ChangeValue.containsKey(ID_ACHIEVETYPE)&&ChangeValue.containsKey(ID_ACHIEVENAME)){
				ContentValues newValues = new ContentValues();	
//				 newValues.put("_ifupload", "unupload");
				 Iterator iter=ChangeValue.entrySet().iterator();
				 newValues.put(ID_USERNAME, username);
					while(iter.hasNext()){
					Map.Entry entry=(java.util.Map.Entry) iter.next();
					Object key=entry.getKey();
					Object val=entry.getValue();					
					if(key.equals("_ifupload")){
						 newValues.put(ID_IFUPLOAD, (String) val);
					}else if(key.equals("_classify")){
						 newValues.put(ID_CLASSIFY, (String) val);
					}else if(key.equals("_achievetype")){
						  newValues.put(ID_ACHIEVETYPE, (String) val);
					}else if(key.equals("_achievename")){
						 newValues.put(ID_ACHIEVENAME,(String) val);
					}else if(key.equals("_achieverecord")){
						 newValues.put(ID_ACHIEVERECORD,(String) val);
					}else if(key.equals("_starttime")){
						/*String begins[]=((String)val).split(":");
						long start_time=Long.parseLong(begins[0])*100000000+Long.parseLong(begins[1])*1000000
								+Long.parseLong(begins[2])*10000+Long.parseLong(begins[3])*100+Long.parseLong(begins[4]);*/
						long start_time=Long.parseLong((String)val);
						 newValues.put(ID_STARTTIME,start_time);
					}
					}
		        
				//Cursor c=database.rawQuery("select count(*) as CM from SportAchieve where _achievename="+ChangeValue.get(ID_ACHIEVENAME)+";", null);
					  c=database.query(TABLE_SPORTACHIEVE, null, "_classify=? and _achievetype=? and _achievename=?", 
					new String[]{ChangeValue.get(ID_CLASSIFY),ChangeValue.get(ID_ACHIEVETYPE),ChangeValue.get(ID_ACHIEVENAME)}, null, null, null);
					System.out.println(c.getCount());
					 if(c.getCount()==0){
					database.insert(TABLE_SPORTACHIEVE, null, newValues);
				}else{
					database.update(TABLE_SPORTACHIEVE, newValues, "_classify=? and _achievetype=? and _achievename=?", 
							new String[]{ChangeValue.get(ID_CLASSIFY),ChangeValue.get(ID_ACHIEVETYPE),ChangeValue.get(ID_ACHIEVENAME)});
					
				}
		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}
				
			//1、查询hashmap中是否包含_classify，_achievetype，_achievename这三个键值
			//2、若包含则查询数据库中是否有这条记录，
		
		/*{
			//若查找到则更新这条记录，没有则插入记录
		}*/
		
	}

	@Override
	public void InsertSportRecord(String username,ContentValues contentValues) {//插入一条新运动记录
		// TODO Auto-generated method stub
		Cursor c=null;
		try
		{
		    openOrCreateDB();
		    contentValues.put(ID_USERNAME,username);
		    simpleInsertSportRecord(contentValues);
		}catch (Exception e)
		{
				e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}
	}
	/**
	 * 向数据库  记录表里  插入一条记录,前提条件：原来没有此记录
	 */
	public void simpleInsertSportRecord(ContentValues contentValues)
	{
		Cursor c = null;
		try
		{
			c = database.query(TABLE_SPORTRECORD, null," _username=? and _starttime=? ",
					new String[] { contentValues.get(ID_USERNAME)+"",
					contentValues.get(ID_STARTTIME)+"" }, null,null, null);
			if (c != null && c.getCount() == 0)
			{
				//System.out.println("cunchu footNum"+contentValues.get(ID_FOOTNUM));
				database.insert(TABLE_SPORTRECORD, null, contentValues);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
		}
	}

	@Override
	public void InsertSportRoute(String username,ContentValues contentValues) {//插入一条运动轨迹记录
		// TODO Auto-generated method stub
				try
				{
					openOrCreateDB();
					contentValues.put(ID_USERNAME, username);
					simpleInsertSportRoute(contentValues);
				}catch (Exception e)
				{
						e.printStackTrace();
				}
				finally
				{
					closeDB();
				}
	}
	
	public void simpleInsertSportRoute(ContentValues contentValues)
	{	
		database.insert(TABLE_SPORTROUTE, null, contentValues);
	}

	@Override
	public entity_sportrecord GetSportRecord(String username,long starttime) {//获取某次运动记录
		Cursor c=null;
		entity_sportrecord sportrecord=new entity_sportrecord();
		try
		{
			openOrCreateDB();				
		    c=database.query(TABLE_SPORTRECORD,null, "_username=? and _starttime=? and _mode = ?", new String[]{username,starttime+"","0"}, null, null, null);
			c.moveToFirst();		
			sportrecord.setAvgspeed(Float.parseFloat(c.getString(c.getColumnIndex(ID_AVERAGE_SPEED))));
			sportrecord.setCalorie(Integer.parseInt(c.getString(c.getColumnIndex(ID_CALORIE))));
			sportrecord.setDistance(Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE))));
			sportrecord.setDurationtime(Integer.parseInt(c.getString(c.getColumnIndex(ID_DURATIONTIME))));
			sportrecord.setEndtime(c.getLong(c.getColumnIndex(ID_ENDTIME)));
			sportrecord.setUpload(c.getString(c.getColumnIndex(ID_IFUPLOAD)));
			sportrecord.setStarttime(c.getLong(c.getColumnIndex(ID_STARTTIME)));
			sportrecord.setSporttype(Integer.parseInt(c.getString(c.getColumnIndex(ID_SPORTTYPE))));
			sportrecord.setPace(c.getInt(c.getColumnIndex(ID_FOOTNUM)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}
		return sportrecord;
	}

	
	@Override
	public List<entity_sportrecord> GetSportRecord(String username,long starttime_begin,
			long starttime_end) {
		Cursor c=null;
		List<entity_sportrecord> sportrecord_list=new ArrayList<entity_sportrecord>();
		try
		{
			openOrCreateDB();								
			c=database.query(TABLE_SPORTRECORD,null, "_username=? and _starttime>=? and _endtime<=?  and _mode = ?", new String[]{username,starttime_begin+"",starttime_end+"","0"}, null, null, null);
			c.moveToFirst();			
			for(int i=0;i<c.getCount();i++){
			entity_sportrecord sportrecord=new entity_sportrecord();
			sportrecord.setAvgspeed(Float.parseFloat(c.getString(c.getColumnIndex(ID_AVERAGE_SPEED))));
			sportrecord.setCalorie(c.getInt(c.getColumnIndex(ID_CALORIE)));
			sportrecord.setDistance(c.getInt(c.getColumnIndex(ID_DISTANCE)));
			sportrecord.setDurationtime(c.getInt(c.getColumnIndex(ID_DURATIONTIME)));
			sportrecord.setEndtime(c.getLong(c.getColumnIndex(ID_ENDTIME)));
			sportrecord.setStarttime(c.getLong(c.getColumnIndex(ID_STARTTIME)));
			sportrecord.setUpload(c.getString(c.getColumnIndex(ID_IFUPLOAD)));
			sportrecord.setSporttype(c.getInt(c.getColumnIndex(ID_SPORTTYPE)));
			sportrecord.setPace(c.getInt(c.getColumnIndex(ID_FOOTNUM)));
			sportrecord_list.add(sportrecord);
			c.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}	
		return sportrecord_list;
	}
	
	
	
	@Override
	public List<entity_sportroute> GetSportRoute(String username,long starttime) {//获取某次运动记录对应的运动轨迹
		Cursor c=null;
		List<entity_sportroute> sportroute_list=new ArrayList<entity_sportroute>();
		try
		{
			openOrCreateDB();					
			c=database.query(TABLE_SPORTROUTE,null, "_username=? and _starttime>=?", new String[]{username,starttime+""}, null, null, null);			
			c.moveToFirst();
			for(int i=0;i<c.getCount();i++){
			entity_sportroute sportroute=new entity_sportroute();
			sportroute.setDistance(Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE))));
			sportroute.setLatitude(Integer.parseInt(c.getString(c.getColumnIndex(ID_LATITUDE))));
			sportroute.setLongitude(Integer.parseInt(c.getString(c.getColumnIndex(ID_LONGITUDE))));
		sportroute.setSpeed(Float.parseFloat(c.getString(c.getColumnIndex(ID_INSTANT_SPEED))));
			sportroute.setStarttime(c.getLong(c.getColumnIndex(ID_STARTTIME)));
			sportroute_list.add(sportroute);
			c.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}	
		return sportroute_list;
	}

	public ArrayList<entity_sendrecord> GetNoSendRecord(String username,
			long starttime_begin, long starttime_end)
	{
		long stime;
		int dis=0;
		Cursor c=null;
		String mode="0";
		ArrayList<entity_sendrecord> sendrecord_list=new ArrayList<entity_sendrecord>();
		try
		{		
			openOrCreateDB();
			if(starttime_begin==0&&starttime_end==0)
			c=database.query(TABLE_SPORTRECORD,null, "_username=? and _ifupload=?", new String[]{String.valueOf(username),String.valueOf(SportData.UNUPLOAD)}, null, null, null);
			else
		    c=database.query(TABLE_SPORTRECORD,null, "_username=? and _ifupload=? and _starttime>=? and _endtime<=? ", new String[]{String.valueOf(username),String.valueOf(SportData.UNUPLOAD),starttime_begin+"",starttime_end+""}, null, null, null);
			if (c!=null&&c.getCount() > 0)
			{
			  c.moveToFirst();		  
			  for(int i=0;i<c.getCount();i++){
				dis=c.getInt(c.getColumnIndex(ID_DISTANCE));
				if(dis>0)
				{
					entity_sendrecord sendrecord=new entity_sendrecord();
				    sendrecord.setDistance(dis+"");
				    sendrecord.setCalorie(c.getString(c.getColumnIndex(ID_CALORIE)));
				    sendrecord.setDurationtime(c.getString(c.getColumnIndex(ID_DURATIONTIME)));
				    sendrecord.setSporttype(c.getString(c.getColumnIndex(ID_SPORTTYPE)));
			    sendrecord.setAvgspeed(Float.parseFloat(c.getString(c.getColumnIndex(ID_AVERAGE_SPEED))));
			    sendrecord.setPace(c.getInt(c.getColumnIndex(ID_FOOTNUM)));
				    stime=c.getLong(c.getColumnIndex(ID_STARTTIME));
				    sendrecord.setStarttime(stime);
			    sendrecord.setEndtime(c.getLong(c.getColumnIndex(ID_ENDTIME)));
			    mode=c.getString(c.getColumnIndex(ID_MODE));
			    sendrecord.setMode(mode);
			    if(mode!=null&&mode.equals("1"))
			       sendrecord.setmsg("");
			    else
			    sendrecord.setmsg(GetSendRoute(username,stime));
				    sendrecord_list.add(sendrecord);
				}
			    c.moveToNext();
			  }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}
		return sendrecord_list;
	}
	public String GetSendRoute(String username,long starttime) {
		
		Cursor c=null;
		StringBuffer sb = new StringBuffer();
		try
		{
			c=database.query(TABLE_SPORTROUTE,null, "_username=? and _starttime>=?", new String[]{username,starttime+""}, null, null, null);		
			if (c!=null&&c.getCount() > 0)
			{
			  c.moveToFirst();
			  for(int i=0;i<c.getCount();i++){
				  sb.append("<location>");
				  sb.append("<longitude>"+c.getString(c.getColumnIndex(ID_LONGITUDE))+"</longitude>");
				  sb.append("<latitude>"+c.getString(c.getColumnIndex(ID_LATITUDE))+"</latitude>");
				  sb.append("<distance>"+c.getString(c.getColumnIndex(ID_DISTANCE))+"</distance>");
				  sb.append("<speed>"+c.getString(c.getColumnIndex(ID_INSTANT_SPEED))+"</speed>");
				  sb.append("</location>");
			      c.moveToNext();
			  }
		   }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
		}			
		return sb.toString();
	}
    public void UPSendRecordStatus(String name,ArrayList<entity_sendrecord>sendlist,int start,int end) {
		
		long stimes = 0;
		
		try{
			openOrCreateDB();
		for(int i=start;i<end;i++)
		{
		  stimes=sendlist.get(i).getStarttime();
		  ContentValues values = new ContentValues();
  		  values.put("_ifupload", String.valueOf(SportData.UPLOAD));
  		  database.update(TABLE_SPORTRECORD, values,"_username=? and _ifupload=? and _starttime=? ", new String[]{String.valueOf(name),String.valueOf(SportData.UNUPLOAD),String.valueOf(stimes)});		
  		  database.update(TABLE_SPORTROUTE, values,"_username=? and _ifupload=? and _starttime=? ", new String[]{String.valueOf(name),String.valueOf(SportData.UNUPLOAD),String.valueOf(stimes)});		
		 }
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
		 
	}

	@Override
	public List<entity_sportachieve> GetSportAchieve(String username,String classify,String upload) {
		
		List<entity_sportachieve> sportachieve_list=new ArrayList<entity_sportachieve>();
		Cursor c=null;
		try
		{
			openOrCreateDB();		
			if(upload==null){
				c=database.query(TABLE_SPORTACHIEVE,null, "_username=? and _classify=?", new String[]{username,classify}, null, null, null);
			}else{
				c=database.query(TABLE_SPORTACHIEVE,null, "_username=? and _classify=? and _ifupload=?", new String[]{username,classify,upload}, null, null, null);
			}
			
			if (c != null && c.getCount() > 0)
			{
			c.moveToFirst();
			for(int i=0;i<c.getCount();i++){
				entity_sportachieve sportachieve=new entity_sportachieve();
				sportachieve.setAchievename(Integer.parseInt(c.getString(c.getColumnIndex(ID_ACHIEVENAME))));
				sportachieve.setAchieverecord(Integer.parseInt(c.getString(c.getColumnIndex(ID_ACHIEVERECORD))));
				sportachieve.setAchievetype(Integer.parseInt(c.getString(c.getColumnIndex(ID_ACHIEVETYPE))));
				sportachieve.setStarttime(c.getLong(c.getColumnIndex(ID_STARTTIME)));
				sportachieve_list.add(sportachieve);
			c.moveToNext();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}	
		return sportachieve_list;
	}
	


	@Override
	public long GetLastDistance(String username) {
		Cursor c=null;
		long distance = 0;
		try
		{
			openOrCreateDB();			
			String sql = "select * from " + TABLE_SPORTRECORD + " where  _username = '" + username + "' and _mode = 0 order by _starttime asc ";
			c = database.rawQuery(sql, null);
			//c=database.query(TABLE_SPORTRECORD,null, "_username=?", new String[]{username}, null, null, null);
			if ((c != null) && (c.getCount() > 0))
			{
				c.moveToLast();
				distance = c.getLong(c.getColumnIndex(ID_DISTANCE));	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}
		return distance;
	}


	@Override
	public void DelSportRecord(String username) {
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			database.delete(TABLE_SPORTRECORD,ID_USERNAME+"=? and _mode = ?",new String[]{username,"0"});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}	
	}

	@Override
	public void DelSportRoute(String username) {
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			database.delete(TABLE_SPORTROUTE, ID_USERNAME+"=?",new String[]{username});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}	
	}

	@Override
	public void DelSportAchieve(String username) {
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			database.delete(TABLE_SPORTACHIEVE, ID_USERNAME+"=?",new String[]{username});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
	}

	@Override
	public long GetDistanceCount(String username, long starttime_begin,
			long starttime_end) {
			
		Cursor c=null;
		long allDistance=0;		
		try {
			openOrCreateDB();
			if(starttime_begin==0&&starttime_end==0){
				c=database.query(TABLE_SPORTRECORD,null, "_username=? and _mode = ", new String[]{username , "0"}, null, null, null);
			}else{
			c=database.query(TABLE_SPORTRECORD,null, "_username=? and _starttime>=? and  _starttime<=? and _mode = ?",new String[]{username,starttime_begin+"",starttime_end+"","0"}, null, null, null, null);
			}
			if (c.getCount() > 0) {
				c.moveToFirst();
				for(int i=0;i<c.getCount();i++){
					long distance=Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE)));
					allDistance+=distance;
					c.moveToNext();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if (c != null && !c.isClosed())
	  			c.close();	
			closeDB();
		}
		return allDistance;
	}

	@Override
	public long GetDurationTimeCount(String username, long starttime_begin,
			long starttime_end) {
		Cursor c=null;
		long allDuration=0;
		try
		{
			openOrCreateDB();								
			if(starttime_begin==0&&starttime_end==0){
				c=database.query(TABLE_SPORTRECORD,null, "_username=? and _mode = ?", new String[]{username,"0"}, null, null, null);
			}else{
			c=database.query(TABLE_SPORTRECORD,null, "_username=? and _starttime>=? and  _starttime<=? and _mode = ?",new String[]{username,starttime_begin+"",starttime_end+"","0"}, null, null, null, null);
			}
			c.moveToFirst();
			for(int i=0;i<c.getCount();i++){
				long duration=Long.parseLong(c.getString(c.getColumnIndex(ID_DURATIONTIME)));
				allDuration+=duration;
				c.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}	
		return allDuration;
	}

	@Override
	public long GetCalorieCount(String username, long starttime_begin,
			long starttime_end) {
		Cursor c=null;
		long allcalorie=0;
		try
		{
			openOrCreateDB();	
			if(starttime_begin==0&&starttime_end==0){
				c=database.query(TABLE_SPORTRECORD,null, "_username=? and _mode = ?", new String[]{username,"0"}, null, null, null);
			}else{
			c=database.query(TABLE_SPORTRECORD,null, "_username=? and _starttime>=? and  _starttime<=? and _mode = ?",new String[]{username,starttime_begin+"",starttime_end+"","0"}, null, null, null, null);
			}
			c.moveToFirst();
			for(int i=0;i<c.getCount();i++){
				long calorie=Integer.parseInt(c.getString(c.getColumnIndex(ID_CALORIE)));
				allcalorie+=calorie;
				c.moveToNext();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}	
		return allcalorie;
	}
	
	@Override
	public int GetSportTimes(String username, long starttime_begin,
			long starttime_end)
	{
		int times = 0;

		Cursor c = null;
		try
		{
			openOrCreateDB();
			if (starttime_begin == 0 && starttime_end == 0)
			{
				c = database.query(TABLE_SPORTRECORD, null, "_username=? and _mode = ?",
						new String[]
						{ username ,"0"}, null, null, null);
			}
			else
			{
				c = database.query(TABLE_SPORTRECORD, null,
						"_username=? and _starttime>=? and  _starttime<=? and _mode = ?",
						new String[]
						{ username, starttime_begin + "", starttime_end + "" ,"0"},
						null, null, null, null);
			}
			if (c != null && c.getCount() > 0) times = c.getCount();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();
			closeDB();
		}
		return times;
	}

	/**
	 * 最快
	 */
	@Override
	public entity_sportrecord GetFastest(String username, int sporttype)
	{
		return GetEst(username, sporttype, TYPE_FASTEST);
	}

	/**
	 * 最远
	 */
	@Override
	public entity_sportrecord GetFarthest(String username, int sporttype)
	{
		return GetEst(username, sporttype, TYPE_FARTHEST);
	}

	/**
	 * 最久
	 */
	@Override
	public entity_sportrecord GetLongest(String username, int sporttype)
	{
		return GetEst(username, sporttype, TYPE_LONGEST);
	}

	@Override
	public entity_sportrecord GetEst(String username, int sporttype, int esttype)
	{
		entity_sportrecord sportrecord = new entity_sportrecord();
		sportrecord.setSporttype(sporttype);
		
		switch (esttype)
		{
		case TYPE_FASTEST:
			String sql_fast = "select _id , _avgspeed  from SportRecord  where _sporttype = "
					+ sporttype+ " and  _username= '"+ username + "' and _mode = 0 ;";
			findMaxSpeed(sportrecord, sql_fast);
			break;

		case TYPE_FARTHEST:
			String sql_distance = "select _id , _distance  from SportRecord  where _sporttype = "
					+ sporttype+ " and  _username= '"+ username + "' and _mode = 0 ;";
			findMaxDisDur(sportrecord, sql_distance, ID_DISTANCE);
			break;

		case TYPE_LONGEST:
			String sql_duraction = "select _id , _durationtime  from SportRecord  where _sporttype = "
					+ sporttype+ " and  _username= '"+ username + "' and _mode = 0 ;";
			findMaxDisDur(sportrecord, sql_duraction, ID_DURATIONTIME);
			break;
		}

		return sportrecord;
	}

	/**
	 * 查询速度最值
	 */
	public void findMaxSpeed(entity_sportrecord sportrecord, String sql)
	{
		Cursor c = null;
		Cursor c2 = null;
		int idTemp = 1;
		int idMax = 1;	
		float avgspeedTemp = 0;
		float avgspeedMax = 0;
		try
		{
			openOrCreateDB();
			c = database.rawQuery(sql, null);
			//Log.i(TAG, c.getCount() + "cursor.getCount()");
			if ((c != null) && (c.getCount() > 0))
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					avgspeedTemp = Float.parseFloat(c.getString(c.getColumnIndex(ID_AVERAGE_SPEED)));
					idTemp = c.getInt(c.getColumnIndex(ID_ID));	
					//Log.i(TAG,  "avgspeedTemp   "+avgspeedTemp);
					if (avgspeedTemp > avgspeedMax)
					{
						avgspeedMax =  avgspeedTemp;
						//Log.i(TAG,  "avgspeedTemp > avgspeedMax   "+avgspeedMax);
						idMax = idTemp;					
					}
					c.moveToNext();
				}
				//Log.i(TAG,  "avgspeedMax111111   "+avgspeedMax);
				String sql2 = "select *  from SportRecord  where _id = "+idMax+ ";";
				c2 = database.rawQuery(sql2, null);
				if ((c2 != null) && (c2.getCount() > 0))
				{
					c2.moveToFirst();
					sportrecord.setAvgspeed(Float.parseFloat(c2.getString(c2.getColumnIndex(ID_AVERAGE_SPEED))));
					sportrecord.setCalorie(Integer.parseInt(c2.getString(c2.getColumnIndex(ID_CALORIE))));
					sportrecord.setDistance(Integer.parseInt(c2.getString(c2.getColumnIndex(ID_DISTANCE))));
					sportrecord.setDurationtime(Integer.parseInt(c2.getString(c2.getColumnIndex(ID_DURATIONTIME)))  );
					sportrecord.setEndtime(c2.getLong(c2.getColumnIndex(ID_ENDTIME)));
					sportrecord.setStarttime(c2.getLong(c2.getColumnIndex(ID_STARTTIME)));					
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (c != null)
			{
				c.close();
			}
			if (c2 != null)
			{
				c2.close();
			}
			closeDB();
		}
	}
	
	/**
	 * 查询距离或持续时间最值
	 */
	public void findMaxDisDur(entity_sportrecord sportrecord, String sql, String style)
	{
		Cursor c = null;
		Cursor c2 = null;
		int idTemp = 1;
		int idMax = 1;		
		int distanceTemp = 0;
		int distanceMax = 0;
		try
		{
			openOrCreateDB();
			c = database.rawQuery(sql, null);
			//Log.i(TAG, c.getCount() + "cursor.getCount()");
			if ((c != null) && (c.getCount() > 0))
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					distanceTemp = Integer.parseInt(c.getString(c.getColumnIndex(style)));
					idTemp = c.getInt(c.getColumnIndex(ID_ID));	
					if (distanceTemp > distanceMax)
					{
						distanceMax =  distanceTemp;
						idMax = idTemp;						
					}
					c.moveToNext();
				}
				//Log.i(TAG,  "distanceMax   "+distanceMax);
				String sql2 = "select *  from SportRecord  where _id = "+idMax+ ";";
				c2 = database.rawQuery(sql2, null);
				if ((c2 != null) && (c2.getCount() > 0))
				{
					c2.moveToFirst();
					sportrecord.setAvgspeed(Float.parseFloat(c2.getString(c2.getColumnIndex(ID_AVERAGE_SPEED))));
					sportrecord.setCalorie(Integer.parseInt(c2.getString(c2.getColumnIndex(ID_CALORIE))));
					sportrecord.setDistance(Integer.parseInt(c2.getString(c2.getColumnIndex(ID_DISTANCE))));
					sportrecord.setDurationtime(Integer.parseInt(c2.getString(c2.getColumnIndex(ID_DURATIONTIME)))  );
					sportrecord.setEndtime(c2.getLong(c2.getColumnIndex(ID_ENDTIME)));
					sportrecord.setStarttime(c2.getLong(c2.getColumnIndex(ID_STARTTIME)));					
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (c != null)
			{
				c.close();
			}
			if (c2 != null)
			{
				c2.close();
			}
			closeDB();
		}
	}
	
	/**
	 * 查询数据库记录的表一共有几条gps数据
	 */
	public int getCount(String username)
	{	 
		Cursor cursor = null;
		int allNum = 0;
		try
		{
			openOrCreateDB();
			String sql = "select count( distinct(_id) ) from SportRecord where _username= '" 
						 + username+"' and _mode = 0";
			cursor = database.rawQuery(sql, null);
			if (cursor.moveToNext())
			{
				int count = cursor.getInt(0);
				if (count > 0)
				{
					allNum = count;
				}
			}
			//allNum =  cursor.getInt(0);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (cursor != null)
			{
				cursor.close();
			}
			closeDB();
		}
		return allNum;
	}
	/**
	 * 查询10条记录
	 */
	public List<entity_sportrecord> getRecord(String username, int id_start,
			int id_over)
	{
		List<entity_sportrecord> recordData = new ArrayList<entity_sportrecord>();
		Cursor c = null;
		try
		{
			openOrCreateDB();
			String sql = "select * from " + TABLE_SPORTRECORD
					+ " where  _username = '" + username  + "'" + " and _mode = 0 order by _starttime desc limit "+id_start+", "+id_over;
			c = database.rawQuery(sql, null);
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					entity_sportrecord sportrecord = new entity_sportrecord();
					
					sportrecord.setAvgspeed(Float.parseFloat(c.getString(c.getColumnIndex(ID_AVERAGE_SPEED))));
					sportrecord.setCalorie(Integer.parseInt(c.getString(c.getColumnIndex(ID_CALORIE))));
					sportrecord.setDistance(Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE))));
					sportrecord.setDurationtime(Integer.parseInt(c.getString(c.getColumnIndex(ID_DURATIONTIME) ) ) );
					sportrecord.setEndtime(c.getLong(c.getColumnIndex(ID_ENDTIME)));
					sportrecord.setStarttime(c.getLong(c.getColumnIndex(ID_STARTTIME)));
					sportrecord.setSporttype(Integer.parseInt(c.getString(c.getColumnIndex(ID_SPORTTYPE))));
					sportrecord.setMode(Integer.parseInt(c.getString(c.getColumnIndex(ID_MODE))));
					
					recordData.add(sportrecord);
					/*Log.i(TAG, cursor.getInt(cursor.getColumnIndex(ID_ID)) + "");
					Log.i(TAG, "cursor.getCount()" + cursor.getCount());*/
					c.moveToNext();
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (c != null)
			{
				c.close();
			}
			closeDB();
		}
		return recordData;
	}

	/**
	 * 返回某人一段时间画地图的点
	 */
	public List<entity_sportroute> getMapPoint(String username, long startTime)
	{

		List<entity_sportroute> sportrouteList = new ArrayList<entity_sportroute>();
		Cursor c = null;
		try
		{
			openOrCreateDB();
			c = database.query(TABLE_SPORTROUTE, null,
					"_starttime = ? and  _username= ?", new String[] {
							startTime + "", username }, null, null, null);
			
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					entity_sportroute sportroute = new entity_sportroute();				
					sportroute.setDistance(Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE))));
					sportroute.setSpeed(Float.parseFloat(c.getString(c.getColumnIndex(ID_INSTANT_SPEED))));
					sportroute.setLatitude(Double.parseDouble(c.getString(c.getColumnIndex(ID_LATITUDE))));
					sportroute.setLongitude(Double.parseDouble(c.getString(c.getColumnIndex(ID_LONGITUDE))));					
					sportrouteList.add(sportroute);
					c.moveToNext();
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (c != null)
			{
				c.close();
			}
			closeDB();
		}
		return sportrouteList;
	}

	/**
	 * 返回一段时间画gps速率图表的点
	 */
	public List<Double> getGpsTablePoint(String username, long startTime)
	{
		List<Double> yPointList = new ArrayList<Double>();
		Cursor c = null;
		double speed = 0;
		try
		{
			openOrCreateDB();
			c = database.query(TABLE_SPORTROUTE, null,
					"_starttime = ?  and  _username= ?", new String[] {
							startTime + "", username }, null, null, null);
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					speed = Double.parseDouble(c.getString(c
							.getColumnIndex(ID_INSTANT_SPEED)));
					yPointList.add(speed);
					c.moveToNext();
				}
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
			closeDB();
		}
		return yPointList;
	}

	/*
	 * 某一天每小时步数的和
	 */
	public entity_table getData_Everyday(String username, int year, int month,int day,int mode)
	{
		entity_table step = new entity_table();
		List<Integer> step_hour = new ArrayList<Integer>();
		int step_total = 0;
		int calorie    = 0;
		int distance   = 0;
		int hourstep ;
		Calendar cal = Calendar.getInstance();
		int hourNum = 24;// 一天有几小时
		try
		{			
			openOrCreateDB();
			for (int j = 0; j < hourNum; j++)
			{
				hourstep = 0;
				cal.set(year, month, day, j , 0, 0);// 每小时开始时间
				String startTime = cal.getTimeInMillis() + "";
				cal.clear();
				cal.set(year, month, day, j + 1, 0, 0);// 每小时结束时间
				String endTime = cal.getTimeInMillis() + "";
				cal.clear();
				Cursor cHand = database.query(TABLE_SPORTRECORD,null,
						"_starttime>= ?  and  _starttime < ? and  _username= ? and _mode = ?",
								new String[] { startTime, endTime, username ,mode+""},null, null, null);
				if (cHand != null && cHand.getCount() > 0)
				{
					cHand.moveToFirst();
					for (int i = 0; i < cHand.getCount(); i++)
					{
						hourstep += Integer.parseInt(cHand.getString(cHand.getColumnIndex(ID_FOOTNUM)));
						calorie  += Integer.parseInt(cHand.getString(cHand.getColumnIndex(ID_CALORIE)));
						distance += Integer.parseInt(cHand.getString(cHand.getColumnIndex(ID_DISTANCE)));
						cHand.moveToNext();
					}
				}
				step_total += hourstep ;
				step_hour.add(hourstep);
				if (cHand!= null)cHand.close();
			}
			step.setTableValue(step_hour);
			step.setData1(step_total);
			step.setData2(calorie);
			step.setData3(distance);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}	 
		return step;
	}
	
	/** 
	 * 分别返回某个月每一天  gps距离/手环步数  
	 */
	public entity_table getData_EveryMonth(String username, int year, int month,int _mode)
	{
		entity_table entity = new entity_table();
        int data1 = 0;
        int data2 = 0;
        int data3 = 0;
        int temp ;
        List<Integer> tablevalue = new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		int dayNumMonth = SwitchDayNum.getDayNum(year, month+1);//本月有几天
		//Cursor c = null;
		String Table_ID  = null;
		String Data1_ID  = null;
		switch(_mode)
		{
			case SportData.MODE_GPS:
				Table_ID = ID_DISTANCE;
				Data1_ID = ID_DURATIONTIME;
				break;
			case SportData.MODE_IWAN:
				Table_ID = ID_FOOTNUM;
				Data1_ID = ID_FOOTNUM;
				break;
		}
		
		try
		{
			openOrCreateDB();
			for (int j = 1; j <= dayNumMonth; j++)
			{
				temp = 0;
				cal.set(year, month, j, 0, 0, 0);//今日开始时间
				String startTime = cal.getTimeInMillis()+"";
				cal.clear();
				cal.set(year, month , j+1, 0, 0, 0);//明日开始时间
				String endTime = cal.getTimeInMillis()+"";
				cal.clear();
				Cursor c = database.query(TABLE_SPORTRECORD, null,
						"_starttime>= ?  and  _endtime<= ? and  _username= ? and _mode= ?",
						new String[]{ startTime, endTime, username ,_mode+""}, null, null, null);
				if (c != null && c.getCount() > 0)
				{
					c.moveToFirst();
					for (int i = 0; i < c.getCount(); i++)
					{	
						temp   += Integer.parseInt(c.getString(c.getColumnIndex(Table_ID)));
						data1  += Integer.parseInt(c.getString(c.getColumnIndex(Data1_ID)));
						data2  += Integer.parseInt(c.getString(c.getColumnIndex(ID_CALORIE)));
						data3  += Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE)));
						if (BuildConfig.DEBUG)Log.d("TAG_EveryMonth","tableValue="+temp+"----stepTotal="+data1);
						c.moveToNext();
					}
				}
				tablevalue.add(temp);
				if (c != null)
				{
					c.close();
				}
			}		
			entity.setTableValue(tablevalue);
			entity.setData1(data1);
			entity.setData2(data2);
			entity.setData3(data3);
						
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			closeDB();
		}	 
		return entity;
	}

	/**
	 * 判断是否获得奖章，是，则更新数据库，返回奖章名称，否，则返回-1
	 */
	public int getMedalName(String username)
	{
		int allTimes = 0;
		int allDuractionTime = 0;
		int allFootDistance = 0;
		int allBikeDistance = 0;
		long startTime = 0;
		int sportType = 0;
		int judgeThird = 0;
		medalName=-1;
		Cursor c = null;
		try
		{
			openOrCreateDB();
			c = database.query(TABLE_SPORTRECORD, null, " _username= ? and _mode = ?",
					new String[] { username ,"0"}, null, null, null);
			int count = c.getCount();
			if (c != null && count > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < count; i++)
				{
					int distance = Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE)));
					int duractionTime = Integer.parseInt(c.getString(c.getColumnIndex(ID_DURATIONTIME)));
					startTime = c.getLong(c.getColumnIndex(ID_STARTTIME));
					sportType = Integer.parseInt(c.getString(c.getColumnIndex(ID_SPORTTYPE)));
					
					if (distance == 0)medalName=-1;
					
					if (distance > 0)
					{
						// 运动类型奖章
					    // Log.i("db    ", "judgeThird"+judgeThird);
						if (judgeThird!=3)
						{							
							judgeThird = medalSportType(sportType, username,startTime);
						}

						allTimes++;
						allDuractionTime = allDuractionTime + duractionTime;
						if (sportType == 2) 
							allBikeDistance = allBikeDistance + distance;
						else
							allFootDistance = allFootDistance + distance;
					}

					c.moveToNext();
				}
				ContentValues contentValues = new ContentValues();
				contentValues.put(ID_USERNAME, username);
				contentValues.put(ID_CLASSIFY, "1");
				contentValues.put(ID_STARTTIME, startTime);
				contentValues.put(ID_ACHIEVETYPE,  "0");
				contentValues.put(ID_ACHIEVERECORD, "1");
				contentValues.put(ID_IFUPLOAD, SportData.UNUPLOAD);

				medalTimes(allTimes, contentValues);
				medalAllDistance(allFootDistance, allBikeDistance,contentValues);
				medalDuractionTime(allDuractionTime, contentValues);
				medalOneDay(username, contentValues);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
			closeDB();
		}

		return medalName;
	}

	/**
	 * 判断是否获得 第一次运动奖章,若三个奖章都已获得，就不判断了
	 */
	public int medalSportType(int sportType, String username,long startTime)
	{	  	
		int judgeThird= 0;
		Cursor c = null;
		List<Integer > medalNameList = new ArrayList<Integer>();
		try
		{
			c = database.query(TABLE_SPORTACHIEVE, null,"_username=? and _achievetype=?  ",
					new String[]{ username,  "0" }, null, null,null);
			judgeThird = c.getCount();
			if (c != null && judgeThird > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < judgeThird; i++)
				{
					int	 medalNameTemp = Integer.parseInt(c.getString(c.getColumnIndex(ID_ACHIEVENAME)));
					medalNameList.add(medalNameTemp);
					c.moveToNext();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			if (c != null)
				c.close();
		}
		//开始走路
		if (sportType == 0 && (!medalNameList.contains(0)) )
		{
			//Log.i("db    ", "medalSportType  0");
			insertContentValues(username, startTime, sportType);
			medalName = 0;
		}
		//开始跑步
		else if (sportType == 1 && (!medalNameList.contains(1)) )
		{
			//Log.i("db    ", "medalSportType  1");
			insertContentValues(username, startTime, sportType);
			medalName = 1 ;
		}
		//开始骑行
		else if (sportType == 2 && (!medalNameList.contains(2)) )
		{
			//Log.i("db    ", "medalSportType  2");
			insertContentValues(username, startTime, sportType);
			medalName = 2;
		}
		return judgeThird;
	}

	/**
	 * 将 第一次运动奖章插入奖章表
	 */
	public void insertContentValues(String username, long startTime,int sportType)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(ID_USERNAME, username);
		contentValues.put(ID_CLASSIFY, "1");
		contentValues.put(ID_STARTTIME, startTime);
		contentValues.put(ID_ACHIEVETYPE, "0");
		contentValues.put(ID_ACHIEVERECORD, "1");
		contentValues.put(ID_IFUPLOAD, SportData.UNUPLOAD);
		contentValues.put(ID_ACHIEVENAME, sportType + "");
		database.insert(TABLE_SPORTACHIEVE, null, contentValues);
	}

	/**
	 * 判断是否加入运动次数奖章
	 */
	public void medalTimes(int allTimes, ContentValues contentValues)
	{
		contentValues.put(ID_ACHIEVETYPE,  "1");
		if (allTimes >= 7)
		{
			contentValues.put(ID_ACHIEVENAME, "4");
			ifInsert(contentValues);
		}
		if (allTimes  >= 15)
		{
			contentValues.put(ID_ACHIEVENAME, "5");
			ifInsert(contentValues);
		}
		if (allTimes  >= 24)
		{
			contentValues.put(ID_ACHIEVENAME, "6");
			ifInsert(contentValues);
		}
		if (allTimes  >= 34)
		{
			contentValues.put(ID_ACHIEVENAME, "7");
			ifInsert(contentValues);
		}
		if (allTimes  >= 52)
		{
			contentValues.put(ID_ACHIEVENAME, "8");
			ifInsert(contentValues);
		}
		if (allTimes  >= 60)
		{
			contentValues.put(ID_ACHIEVENAME, "9");
			ifInsert(contentValues);
		}
		if (allTimes  >= 90)
		{
			contentValues.put(ID_ACHIEVENAME, "10");
			ifInsert(contentValues);
		}
		if (allTimes  >= 120)
		{
			contentValues.put(ID_ACHIEVENAME, "11");
			ifInsert(contentValues);
		}
		if (allTimes  >= 200)
		{
			contentValues.put(ID_ACHIEVENAME, "12");
			ifInsert(contentValues);
		}
		if (allTimes  >= 500)
		{
			contentValues.put(ID_ACHIEVENAME, "13");
			ifInsert(contentValues);
		}

	}

	/**
	 * 判断是否加入累积持续时间奖章
	 */
	public void medalDuractionTime(int allDuractionTime,ContentValues contentValues)
	{
		contentValues.put(ID_ACHIEVETYPE,  "3");
		if (allDuractionTime >= (5 * 3600) )
		{
			contentValues.put(ID_ACHIEVENAME, "14");
			ifInsert(contentValues);
		}
		if (allDuractionTime >= (20 * 3600))
		{
			contentValues.put(ID_ACHIEVENAME, "15");
			ifInsert(contentValues);
		}
		if (allDuractionTime >= (50 * 3600))
		{
			contentValues.put(ID_ACHIEVENAME, "16");
			ifInsert(contentValues);
		}
		if (allDuractionTime >= (100 * 3600))
		{
			contentValues.put(ID_ACHIEVENAME, "17");
			ifInsert(contentValues);
		}

	}

	/**
	 * 判断是否加入步行或骑车，累积运动距离奖章
	 */
	public void medalAllDistance(int allFootDistance, int allBikeDistance,ContentValues contentValues)
	{
		contentValues.put(ID_ACHIEVETYPE,  "2");
		// 步行
		if (allFootDistance >= (100 * 1000) )
		{
			contentValues.put(ID_ACHIEVENAME, "23");
			ifInsert(contentValues);
		}
		if (allFootDistance >= (500 * 1000))
		{
			contentValues.put(ID_ACHIEVENAME, "25");
			ifInsert(contentValues);
		}
		// 骑车
		if (allBikeDistance >= (100 * 1000) )
		{
			contentValues.put(ID_ACHIEVENAME, "24");
			ifInsert(contentValues);
		}
		if (allBikeDistance >= (500 * 1000) )
		{
			contentValues.put(ID_ACHIEVENAME, "26");
			ifInsert(contentValues);
		}
		if (allBikeDistance >= (3000 * 1000) )
		{
			contentValues.put(ID_ACHIEVENAME, "27");
			ifInsert(contentValues);
		}
		if (allBikeDistance >= (10000 * 1000))
		{
			contentValues.put(ID_ACHIEVENAME, "28");
			ifInsert(contentValues);
		}

	}

	/**
	 * 判断 如果此奖章存在则不加，若不存在，则加入
	 */
	public void ifInsert(ContentValues contentValues )
	{
		Cursor c = null;
		try
		{
			c = database.query(TABLE_SPORTACHIEVE, null,"_achievename=? and _username=?", 
					new String[]{ (String) contentValues.get(ID_ACHIEVENAME),
					(String) contentValues.get(ID_USERNAME) }, null,null, null);
			if (c != null && c.getCount() == 0)
			{
				database.insert(TABLE_SPORTACHIEVE, null, contentValues);
				medalName=Integer.parseInt((String) contentValues.get(ID_ACHIEVENAME));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}		
		}
	}

	/**
	 * 判断 能否获得一天内运动的奖章
	 */
	public void medalOneDay(String username, ContentValues contentValues)
	{
		Calendar cal = Calendar.getInstance();// 默认当前时间
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 0, 0, 0);
		long startTime = cal.getTimeInMillis();
		cal.add(Calendar.DATE, 1);
		long stopTime = cal.getTimeInMillis();
		Cursor c = null;
		int todayTimes = 0;
		int todayFootDistance = 0;
		int todayBikeDistance = 0;
		int distance = 0;
		try
		{
			c = database.query(TABLE_SPORTRECORD, null,"_username=? and _starttime>=? and _endtime<=?  and _mode = ?",
					new String[]{ username, startTime + "", stopTime + "" ,"0"}, null, null,null);
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					 distance = Integer.parseInt(c.getString(c.getColumnIndex(ID_DISTANCE)));
					
					//int duractionTime = Integer.parseInt(c.getString(c.getColumnIndex(ID_DURATIONTIME))); 
					//long dbStartTime = c.getLong(c.getColumnIndex(ID_STARTTIME));
					 
					int sportType = Integer.parseInt(c.getString(c.getColumnIndex(ID_SPORTTYPE)));

					if (distance > 0)
					{
						todayTimes++;
						if (sportType == 2) todayBikeDistance = todayBikeDistance+ distance;
						else
							todayFootDistance = todayFootDistance + distance;
					}
					c.moveToNext();
				}
				contentValues.put(ID_STARTTIME,  startTime);
				//一天运动两次
				if (todayTimes == 2 )
				{
					contentValues.put(ID_ACHIEVETYPE,  "1");
					contentValues.put(ID_ACHIEVENAME, "3");
					ifRefresh(contentValues);
				}
				contentValues.put(ID_ACHIEVETYPE,  "2");
				//一天步行或跑步奖章
				if ( todayFootDistance >= (5*1000) && todayFootDistance < (10*1000))
				{
					contentValues.put(ID_ACHIEVENAME, "19");
					ifRefresh(contentValues);
				}
				if (todayFootDistance >=  (10*1000)) {
					contentValues.put(ID_ACHIEVENAME, "20");
					ifRefresh(contentValues);
				}
				//一天骑行奖章
				if ( todayBikeDistance >= (10*1000) && todayBikeDistance < (30*1000))
				{
					contentValues.put(ID_ACHIEVENAME, "21");
					ifRefresh(contentValues);
				}
				if (todayBikeDistance >=  (30*1000)) {
					contentValues.put(ID_ACHIEVENAME, "22");
					ifRefresh(contentValues);
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
		}
	}

	/**
	 * 判断 如果此奖章存在则将“记录”字段加1，若不存在，则插入此奖章
	 */
	public void ifRefresh(ContentValues contentValues)
	{
		Cursor c = null;
		boolean sameDay= false;
		try
		{
			c = database.query(TABLE_SPORTACHIEVE, null , "_achievename=? and _username=?",
					new String[]
					{ (String) contentValues.get(ID_ACHIEVENAME),(String) contentValues.get(ID_USERNAME) }, null, null,null);
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
				long statTimeLast = (Long) contentValues.get(ID_STARTTIME);
				long statTimeThis = c.getLong(c.getColumnIndex(ID_STARTTIME));
				Calendar calendarLast = Calendar.getInstance();
				calendarLast.setTimeInMillis(statTimeLast);
				Calendar calendarThis = Calendar.getInstance();
				calendarThis.setTimeInMillis(statTimeThis);
				// 同一年的同一天
				if (calendarLast.get(Calendar.DAY_OF_YEAR) == calendarThis
						.get(Calendar.DAY_OF_YEAR))
				{
					if (calendarLast.get(Calendar.YEAR) == calendarThis.get(Calendar.YEAR))
					{
						sameDay = true;
					}
				}
				//不是  同一年的同一天
				if (!sameDay)
				{
					int achieveRecord = Integer.parseInt(c.getString(c.getColumnIndex(ID_ACHIEVERECORD)));
					achieveRecord++;
					contentValues.put(ID_ACHIEVERECORD, achieveRecord);
					contentValues.put(ID_IFUPLOAD, SportData.UNUPLOAD);
					database.update(TABLE_SPORTACHIEVE, contentValues,
							"_achievename=?  and _username=?", new String[]
							{ (String) contentValues.get(ID_ACHIEVENAME) ,(String) contentValues.get(ID_USERNAME)});
				}
			}
			else
			{
				database.insert(TABLE_SPORTACHIEVE, null, contentValues);
			}
			if (!sameDay)
			{
				medalName = Integer.parseInt((String) contentValues.get(ID_ACHIEVENAME));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
		}

	}

	/**
	 * 插入批评奖章，返回奖章ID名称, -1表示没插入批评奖章
	 */
	public int insertCriticalName(String userName)
	{
		medalName= -1;
		Cursor c = null;
		String sql = "select  * from " + TABLE_SPORTRECORD
				+ " where _username = '" + userName
				+ "' and  _mode = 0 order by _starttime desc  limit 1";
		try
		{
			openOrCreateDB();
			c = database.rawQuery(sql, null);
			if ((c != null) && (c.getCount() > 0))
			{
				c.moveToFirst();			
				long startTime = c.getLong(c.getColumnIndex(ID_STARTTIME));				
				ContentValues contentValues = new ContentValues();
				contentValues.put(ID_USERNAME, userName);
				contentValues.put(ID_CLASSIFY, "1");
				contentValues.put(ID_ACHIEVETYPE, "4");
				contentValues.put(ID_ACHIEVERECORD, "1");
				contentValues.put(ID_IFUPLOAD, SportData.UNUPLOAD);
				
				Calendar startCalendar = Calendar.getInstance();
				long curentTime = startCalendar.getTimeInMillis();
				if (curentTime < startTime)
				return -1 ;
				startCalendar.setTimeInMillis(startTime);
				int startYear = startCalendar.get(Calendar.YEAR);
				int startMonth = startCalendar.get(Calendar.MONTH);
				int startDay = startCalendar.get(Calendar.DAY_OF_MONTH);

				Calendar currentCalendar = Calendar.getInstance();
				int currentYear = currentCalendar.get(Calendar.YEAR);
				int currentMonth = currentCalendar.get(Calendar.MONTH);
				int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
				 				 				 
				if (currentYear == startYear && currentMonth == startMonth
						&& currentDay == startDay) return -1;
				contentValues.put(ID_STARTTIME, currentCalendar.getTimeInMillis());
			    int days = SwitchDayNum.getIntervalDays(startCalendar, currentCalendar);							
				//3天
				if (days % 3 == 0)
				{
					contentValues.put(ID_ACHIEVENAME,  "31");
					ifCriticalRefresh( contentValues);
				}
				//7天 
			    if (days % 7 == 0)
				{
			    	contentValues.put(ID_ACHIEVENAME,  "29");
			    	ifCriticalRefresh( contentValues);
				}				    
			    //1个月
				if (currentMonth != startMonth && currentDay == startDay)
				{
					contentValues.put(ID_ACHIEVENAME, "30");
					ifCriticalRefresh(contentValues);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
			closeDB();
		}

		return medalName;
	}
	/**
	 *  如果批评奖章存在，则将“记录”字段加1。若不存在，则直接插入
	 */
	public void ifCriticalRefresh(ContentValues contentValues)
	{
		Cursor c = null;
		
		try
		{
			c = database.query(TABLE_SPORTACHIEVE, null , "_achievename=? and _username=?",
					new String[]{ (String) contentValues.get(ID_ACHIEVENAME),
					(String) contentValues.get(ID_USERNAME) }, null, null,null);
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
			  
			    int achieveRecord = Integer.parseInt(c.getString(c.getColumnIndex(ID_ACHIEVERECORD)));
				achieveRecord++;
				contentValues.put(ID_ACHIEVERECORD, achieveRecord);
					
				//Log.i("", "contentValues" +ID_STARTTIME+"======"+calendar.getTimeInMillis());
					
				database.update(TABLE_SPORTACHIEVE, contentValues,
						"_achievename=?  and _username=?", new String[]
						{ (String) contentValues.get(ID_ACHIEVENAME),
								(String) contentValues.get(ID_USERNAME) });				   
			}
			else
			{
				database.insert(TABLE_SPORTACHIEVE, null, contentValues);
			}		
				medalName = Integer.parseInt((String) contentValues.get(ID_ACHIEVENAME));		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
		}
		
	}
	
	public List<entity_loopheat> getLoopHeart(String username)
	{
		List<entity_loopheat> heartList = new ArrayList<entity_loopheat>();
		Cursor c = null;
		try
		{
			openOrCreateDB();
			c = database.query(TABLE_SPORTHEART, null,"_username=? and _ifupload=?", new String[]{String.valueOf(username),String.valueOf(SportData.UNUPLOAD)}, null, null, null);
			if (c != null && c.getCount() > 0)
			{
				c.moveToFirst();
				for (int i = 0; i < c.getCount(); i++)
				{
					
					entity_loopheat sendheat=new entity_loopheat();
					sendheat.setStarttime(c.getLong(c.getColumnIndex(ID_STARTTIME)));
					sendheat.setheat(c.getColumnIndex(ID_HEART));
					heartList.add(sendheat);
					c.moveToNext();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			{
				c.close();
			}
			closeDB();
		}
		return heartList;
	}
	/*
	public void refreshSendLoop(String name) {
		try{
			  openOrCreateDB();
			  ContentValues values = new ContentValues();
	  		  values.put("_ifupload", String.valueOf(SportData.UPLOAD));
	  		  database.update(TABLE_SPORTHANDLOOP, values,"_username=? and _ifupload=? ", new String[]{String.valueOf(name),String.valueOf(SportData.UNUPLOAD)});			
		}catch(Exception e)
		{
				e.printStackTrace();
		}
		finally
		{
			 closeDB();
		}
	}
	*/
	public void refreshSendHeat(String name) {
		try{
			  openOrCreateDB();
			  ContentValues values = new ContentValues();
	  		  values.put("_ifupload", String.valueOf(SportData.UPLOAD));
	  		  database.update(TABLE_SPORTHEART, values,"_username=? and _ifupload=? ", new String[]{String.valueOf(name),String.valueOf(SportData.UNUPLOAD)});			
		}catch(Exception e)
		{
				e.printStackTrace();
		}
		finally
		{
			 closeDB();
		}
	}
	
	public void refreshIwanMac(String name) {
		try{
			  openOrCreateDB();
			  ContentValues values = new ContentValues();
	  		  values.put("_ifupload", String.valueOf(SportData.UPLOAD));
	  		  database.update(TABLE_IWANMAC, values,"_username=? and _ifupload=? ", new String[]{String.valueOf(name),String.valueOf(SportData.UNUPLOAD)});			
		}catch(Exception e)
		{
				e.printStackTrace();
		}
		finally
		{
			 closeDB();
		}
	}

	public void refreshTable_ActionMsg(String myname,List<Map<String,String>> datalist) 
	{	
		//首先查询数据库中是否有用户信息记录
	   Cursor c= null;
	   int a=datalist.size();
	   int b=0;
	   String sql="";
	   try
	   {
		  openOrCreateDB();
		  c=database.query(TABLE_ACTIONMSG,null,ID_USERNAME+"=?",new String[]{myname}, null, null, null);
		  b=c.getCount();
		  if(b>50)
			  database.delete(TABLE_ACTIONMSG,ID_USERNAME+"=?",new String[]{myname});
		  else if(a+b>50)
		  { 
//			  sql = "delete from ActionMsg where _id not in (select _id from ActionMsg order by _actiontime desc limit 0,"+(50-a)+")";
			  sql = "delete from ActionMsg where _username not in (select _username = '" + myname + "'from ActionMsg order by _actiontime desc limit 0,"+(50-a)+")";
			  database.execSQL(sql);
		  }
		  for(int i=0;i<datalist.size();i++)
		  {
		   ContentValues values = new ContentValues();
		   values.put(ID_USERNAME, String.valueOf(myname));
  		   values.put(ID_ACTIONNAME, String.valueOf(datalist.get(i).get("m_name")));
  		   values.put(ID_ACTIONSTATUS, String.valueOf(datalist.get(i).get("m_msg")));
  		   values.put(ID_ACTIONTIME, String.valueOf(datalist.get(i).get("m_day")));
  		   values.put(ID_HEADPHOTO, String.valueOf(datalist.get(i).get("m_headp")));
  		   values.put(ID_SHOWNAME, String.valueOf(datalist.get(i).get("m_name3")));
           database.insert(TABLE_ACTIONMSG, null, values);
		 }
		 
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
	    if(c!=null)
	    	c.close();
		closeDB();
	}
}
	public List<Map<String,Object>> GetActionMsg(String myname)
	{
		List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
		Cursor c= null;
		String string="";
		try
		{		
		  openOrCreateDB();
		  String sql = "select * from ActionMsg where  _username = '" + myname + "'order by _actiontime desc";
          c= database.rawQuery(sql, null);
//		  c=database.query(TABLE_ACTIONMSG,null,null, null, null, null, null);
		if (c!=null&&c.getCount() > 0)
		{
		  c.moveToFirst();		  
		  for(int i=0;i<c.getCount();i++){
			  HashMap<String, Object> map = new HashMap<String, Object>();
			    string = c.getString(c.getColumnIndex(ID_ID));
				map.put("m_id",string);
 				string = c.getString(c.getColumnIndex(ID_ACTIONNAME));
 				map.put("m_name",string);
 				string = c.getString(c.getColumnIndex(ID_SHOWNAME));
 				map.put("m_name3",string);
 				string = c.getString(c.getColumnIndex(ID_ACTIONSTATUS));
 				if(string.equals("0"))
 				   map.put("m_msg",context.getString(R.string.make_attention_str));
 				else
 				   map.put("m_msg",context.getString(R.string.no_attention_str));
 				string = c.getString(c.getColumnIndex(ID_ACTIONTIME));
 				map.put("m_day",string);
 				string=c.getString(c.getColumnIndex(ID_HEADPHOTO));
// 				if(string==null||string.equals(""))
//					map.put("m_headp",R.drawable.headdefault);
//				else
//				{
//				    byte[] photo = SportData.hexStringToBytes(string);
//					Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0,photo.length);
//					map.put("m_headp",bmp);
//				}
 				if(string==null||string.equals(""))
					map.put("m_headp",R.drawable.headdefault);
 				else
 				map.put("m_headp",string);
 				map.put("m_button", context.getString(R.string.delete));
 				datalist.add(map);
		    c.moveToNext();
		  }
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c!=null)
			c.close();
			closeDB();
		}
		return datalist;
	}
	public void DeleteActionMsg(String userid) {
		// TODO Auto-generated method stub
		try
		{
		openOrCreateDB();
		database.delete(TABLE_ACTIONMSG,ID_ID+"=?",new String[]{userid});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
		closeDB();
		}	
	}
	public void DeleteActionMsg()
	{
		Cursor c= null;
		try
		{
		 openOrCreateDB();
		 c=database.query(TABLE_ACTIONMSG,null,null, null, null, null, null);
		 if (c!=null&&c.getCount() > 0)
		 {
		 database.delete(TABLE_ACTIONMSG,null,null);
		 }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c!=null)
				   c.close();
		closeDB();
		}
	}
	@Override
	public void UpdateDefaultRecord(Context context, String newname,int dele) {
		// TODO Auto-generated method stub
		Cursor c = null;
		try
		{
		openOrCreateDB();
		ContentValues  cv=new ContentValues();
		cv.put(ID_USERNAME, newname); 
		 c=database.query(TABLE_USERINFO,null, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME}, null, null, null);
		if(c !=null && c.getCount() > 0)
		{
			if(dele==0)
				cv.put(ID_IFUPLOAD, SportData.UPLOAD); 
			else
				cv.put(ID_IFUPLOAD, SportData.UNUPLOAD);
			 database.update(TABLE_USERINFO,cv, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME});    
		}
		
		c=database.query(TABLE_SPORTRECORD,null, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME}, null, null, null);
		if(c !=null && c.getCount() > 0)
		{
			 cv.put(ID_IFUPLOAD, SportData.UNUPLOAD);
			 database.update(TABLE_SPORTRECORD,cv, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME});
		}
		
		c=database.query(TABLE_SPORTROUTE,null, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME}, null, null, null);
		if(c !=null && c.getCount() > 0)
		{
			cv.put(ID_IFUPLOAD, SportData.UNUPLOAD);
			database.update(TABLE_SPORTROUTE,cv, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME});
		}
		
		c=database.query(TABLE_SPORTACHIEVE,null, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME}, null, null, null);
		if(c !=null && c.getCount() > 0)
		  {	
			cv.put(ID_IFUPLOAD, SportData.UNUPLOAD);
			database.update(TABLE_SPORTACHIEVE,cv, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME});
		  }
//		  c=database.query(TABLE_SPORTHANDLOOP,null, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME}, null, null, null);
//		  if(c !=null && c.getCount() > 0)
//		  {	
//			cv.put(ID_IFUPLOAD, SportData.UNUPLOAD);
//			database.update(TABLE_SPORTHANDLOOP,cv, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME});
//		  }
		  c=database.query(TABLE_SPORTHEART,null, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME}, null, null, null);
		  if(c !=null && c.getCount() > 0)
		  {	
			cv.put(ID_IFUPLOAD, SportData.UNUPLOAD);
			database.update(TABLE_SPORTHEART,cv, ID_USERNAME+"=?", new String[]{SportData.DEFAULTUSERNAME});
		  }
		
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null )
			{
				c.close();
			}			 
			closeDB();
		}	
	}
	@Override
	public void RefreshUploadStatus_SportAchieve(String name,int type) {
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			ContentValues  cv=new ContentValues();
			cv.put(ID_IFUPLOAD, SportData.UPLOAD);
			database.update(TABLE_SPORTACHIEVE, cv,"_username=? and _ifupload=? and _classify=?", new String[]{name,SportData.UNUPLOAD,type+""});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
	}
	@Override
	public List<entity_sendrecord> DelRecentUploadData(String username,
			long starttime_begin, long starttime_end) {
		// TODO Auto-generated method stub
		try
		{
			String whereClause = "_username=? and _starttime>=? and  _starttime<=? and _ifupload=?";
            String whereArgs[] = new String[]{username,starttime_begin+"",starttime_end+"",SportData.UPLOAD};
			openOrCreateDB();
            database.delete(TABLE_SPORTRECORD, whereClause, whereArgs);
            database.delete(TABLE_SPORTROUTE, whereClause, whereArgs);     
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
		
		
		return null;
	}
	/*
	public void DelLoopUploadData(String username,
			long starttime_begin, long starttime_end) {
		// TODO Auto-generated method stub
		try
		{
			String whereClause = "_username=? and _starttime>=? and  _starttime<=? and _ifupload=?";
            String whereArgs[] = new String[]{username,starttime_begin+"",starttime_end+"",SportData.UPLOAD};
			openOrCreateDB();
            database.delete(TABLE_SPORTHANDLOOP, whereClause, whereArgs);    
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
	}
	*/
	public void DelHeatUploadData(String username,
			long starttime_begin, long starttime_end) {
		// TODO Auto-generated method stub
		try
		{
			String whereClause = "_username=? and _starttime>=? and  _starttime<=? and _ifupload=?";
            String whereArgs[] = new String[]{username,starttime_begin+"",starttime_end+"",SportData.UPLOAD};
			openOrCreateDB();
            database.delete(TABLE_SPORTHEART, whereClause, whereArgs);  
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
	}
	
	 public void InsertSportRecord(String username,List<entity_sportrecord> list,String upload)
	 {
			Cursor c=null;
			try
			{
				 openOrCreateDB();
				 database.beginTransaction();
				 for(int i=0;i<list.size();i++)
				 {
				    ContentValues values=new ContentValues();
					values.put(ID_USERNAME,username);
					values.put(ID_STARTTIME,list.get(i).getStarttime());
					values.put(ID_ENDTIME, list.get(i).getEndtime());
					values.put(ID_DISTANCE,list.get(i).getDistance()+"");
					values.put(ID_DURATIONTIME,list.get(i).getDurationtime()+"");
					values.put(ID_CALORIE,list.get(i).getCalorie()+"");	
					values.put(ID_IFUPLOAD, upload);
					values.put(ID_SPORTTYPE,list.get(i).getSporttype()+"");
					values.put(ID_AVERAGE_SPEED,list.get(i).getAvgspeed()+"");	
					values.put(ID_MODE,list.get(i).getMode()+"");
					values.put(ID_FOOTNUM,list.get(i).getPace()+"");
					database.insert(TABLE_SPORTRECORD, null, values);
				}
				 	database.setTransactionSuccessful();
			}catch (Exception e)
			{
					e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				if (c != null)
				c.close();
				closeDB();
			}  
	 }
	 
	 public void InsertSportRoute(String username,List<entity_sendroute> list,String upload)
	 {
			Cursor c=null;
			try
			{		
				 openOrCreateDB();
				 database.beginTransaction();
				 for(int i=0;i<list.size();i++)
				 {
				    ContentValues values=new ContentValues();
					values.put(ID_USERNAME,username);
					values.put(ID_DISTANCE,list.get(i).getDistance()+"");
					values.put(ID_STARTTIME,list.get(i).getStarttime());
					values.put(ID_LONGITUDE,list.get(i).getLongitude()+"");
					values.put(ID_LATITUDE,list.get(i).getLatitude()+"");
					values.put(ID_INSTANT_SPEED,list.get(i).getSpeed()+"");
					values.put(ID_IFUPLOAD, upload);
					database.insert(TABLE_SPORTROUTE, null, values);
				}
				 	database.setTransactionSuccessful();
			}catch (Exception e)
			{
					e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				if (c != null)
				c.close();
				closeDB();
			}  
	 }
	 /*
	 public void InsertLoopRecord(String username,List<entity_sportloop> list,String upload)
	 {
			Cursor c=null;
			try
			{
				 openOrCreateDB();
				 database.beginTransaction();
				 for(int i=0;i<list.size();i++)
				 {
				    ContentValues values=new ContentValues();
					values.put(ID_USERNAME,username);
					values.put(ID_STARTTIME,list.get(i).getStarttime());
					values.put(ID_FOOTNUM, list.get(i).getpace());
					values.put(ID_DISTANCE,list.get(i).getDistance()+"");
					values.put(ID_INSTANT_SPEED,list.get(i).getSpeed()+"");	
					values.put(ID_IFUPLOAD, upload);
					database.insert(TABLE_SPORTHANDLOOP, null, values);
				}
				 	database.setTransactionSuccessful();
			}catch (Exception e)
			{
					e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				if (c != null)
				c.close();
				closeDB();
			}  
	 }
	 */
	 public void InsertHeatRecord(String username,List<entity_loopheat> list,String upload)
	 {
			Cursor c=null;
			try
			{
				 openOrCreateDB();
				 database.beginTransaction();
				 for(int i=0;i<list.size();i++)
				 {
				    ContentValues values=new ContentValues();
					values.put(ID_USERNAME,username);
					values.put(ID_STARTTIME,list.get(i).getStarttime());
					values.put(ID_HEART, list.get(i).getheat());
					values.put(ID_IFUPLOAD, upload);
					database.insert(TABLE_SPORTHEART, null, values);
				}
				 	database.setTransactionSuccessful();
			}catch (Exception e)
			{
					e.printStackTrace();
			}
			finally
			{
				database.endTransaction();
				if (c != null)
				c.close();
				closeDB();
			}  
	 }
	@Override
	public void refreshTable_SportAchieve(String username,List<entity_sportachieve> list,String upload) {//插入运动成就	
		Cursor c=null;
		String whereClause = "_classify=? and _achievetype=? and _achievename=?";
        String whereArgs[] = null;
		
		try
		{		
			 openOrCreateDB();
			 database.beginTransaction();
			 for(int i=0;i<list.size();i++)
			 {
			    ContentValues values=new ContentValues();
				values.put(ID_USERNAME,username);
				values.put(ID_CLASSIFY,"1");//type is medal
				values.put(ID_STARTTIME,list.get(i).getStarttime());
				values.put(ID_ACHIEVETYPE,list.get(i).getAchievetype()+"");
				values.put(ID_ACHIEVENAME,list.get(i).getAchievename()+"");
				values.put(ID_ACHIEVERECORD,list.get(i).getAchieverecord()+"");
				values.put(ID_IFUPLOAD, upload);
				
				whereArgs = new String[]{"1",list.get(i).getAchievetype()+"",list.get(i).getAchievename()+""};
				c=database.query(TABLE_SPORTACHIEVE, null,whereClause,whereArgs, null, null, null);
				System.out.println(c.getCount());
				if(c.getCount()==0)
				{
					database.insert(TABLE_SPORTACHIEVE, null, values);
				}
				else
				{
					database.update(TABLE_SPORTACHIEVE, values,whereClause,whereArgs);	
				}
					
			  }
			 database.setTransactionSuccessful();
		}catch (Exception e)
		{
				e.printStackTrace();
		}
		finally
		{
			database.endTransaction();
			if (c != null)
			c.close();
			closeDB();
		}	
	}
	public int getAppVersion(){            
		try {                      
			PackageInfo info = context.getPackageManager().getPackageInfo(   
					context.getPackageName(), 0);            
			// 当前程序版本号，在AndroidManifest.xml中定义         
			return info.versionCode;               
			} catch (Exception e) {            
				return 1;            
			}       
	}

	@Override
	public void insertIwanMac(entity_mac mac) {
		// TODO Auto-generated method stub
		Cursor c= null;
		String username = SportData.getUserName(context);
		try
		{
			openOrCreateDB();
			ContentValues newValues = new ContentValues();	
			if(mac.getSynTime()!=null )newValues.put(ID_SYNTIME, mac.getSynTime());
			if(mac.getMac()!=null )newValues.put(ID_MAC, mac.getMac());
			if(mac.getUpLoad()!=null )newValues.put(ID_IFUPLOAD,mac.getUpLoad());
			c=database.query(TABLE_IWANMAC, null, "_username=?", new String[]{username}, null, null, null);
			System.out.println(c.getCount());
			 if(c.getCount()==0)
		     {		
		    	  newValues.put(ID_USERNAME,username);
		          database.insert(TABLE_IWANMAC, null, newValues);
		     }
		     else
		     {  
		    	 database.update(TABLE_IWANMAC,newValues, "_username=?", new String[]{username});
		    	 //若无记录则插入，有记录则更新记录
		     }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			 c.close();
		     closeDB();
		}
	}

	@Override
	public entity_mac getIwanMac(String username) {
		// TODO Auto-generated method stub
		Cursor c=null;
		entity_mac mac = new entity_mac();
		try
		{
			openOrCreateDB();
			c=database.query(TABLE_IWANMAC,null, ID_USERNAME+"=?", new String[]{username}, null, null, null);
			if(c !=null)
			{
				if(c.moveToFirst())
				{
					do{	
						mac.setSyntime(c.getString(c.getColumnIndex(ID_SYNTIME)));
						mac.setUpLoad(c.getString(c.getColumnIndex(ID_IFUPLOAD)));
						mac.setMac(c.getString(c.getColumnIndex(ID_MAC)));	
					}while(c.moveToNext());
				}	
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (c != null)
			c.close();	
			closeDB();
		}
		return mac;
	}
	
	 public void saveIwanMac(Context context,String addr,sportDB db)
	 {
   	//保存MAC并上报
      	entity_mac mac = new entity_mac();
      	mac.setMac(addr);
      	mac.setUpLoad(SportData.UNUPLOAD);
      	db.insertIwanMac(mac);
      	context.startService(new Intent(context,SendServer.class));
	 }

	@Override
	public void delIwanMac(String username) {
		// TODO Auto-generated method stub
		try
		{
			openOrCreateDB();
			database.delete(TABLE_IWANMAC, ID_USERNAME+"=?",new String[]{username});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeDB();
		}
	}

}
