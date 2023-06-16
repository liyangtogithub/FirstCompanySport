package com.desay.sport.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.desay.sport.main.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author wzq
 * �?��访问数据库得到省份与城市
 */
public class CityDB extends SQLiteOpenHelper {

	/**
	 * 构建�?��数据库操作对�?	 * @param context 当前程序的上下文对象
	 * @param dataname 数据库名
	 */
	public CityDB(Context context, String dataname){
		super(context, dataname, null, 2);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public static void importInitDatabase(Context context) {
    	String dirPath="/data/data/com.desay.sport.main/databases";
    	File dir = new File(dirPath);
    	if(!dir.exists()) {
    		dir.mkdir();
    	}

    	File dbfile = new File(dir, "db_passome.db");
    	try {
    		if(!dbfile.exists()) {
    			dbfile.createNewFile();
    		}	
    		InputStream is = context.getResources().openRawResource(R.raw.db_possome);
    		FileOutputStream fos = new FileOutputStream(dbfile);
    		byte[] buffere=new byte[is.available()];
    		is.read(buffere);
    		fos.write(buffere);
    		is.close();
    		fos.close();

    	}catch(FileNotFoundException  e){
    		e.printStackTrace();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
	}
	
	
	/**
	 * 得到�?��支持的省份或直辖市名称的String类型数组
	 * @return 支持的省份或直辖市数�?	 */
	public String[] getAllProvinces() {
		String[] columns={"name"};
		
		SQLiteDatabase db = getReadableDatabase();
		//查询获得游标
		Cursor cursor = db.query("provinces", columns, null, null, null, null, null);
		columns = null;
		int count= cursor.getCount();
		String[] provinces = new String[count];
		count=0;
		while(!cursor.isLast()) {
			cursor.moveToNext();
			provinces[count] = cursor.getString(0);
			count=count+1;
		}
		cursor.close();
		db.close();
		return provinces;
	}
	
	/**
	 * 根据省份数组来得到对应装有对应的城市名和城市编码的列表对�?	 * @param provinces 省份数组
	 * @return 索引0为对应的城市名的二维数组和索�?为对应城市名的二维数�?	 */
	public List<String[][]> getAllCityAndCode(String[] provinces) {
		int length= provinces.length;
		String[][] city = new String[length][];
		String[][] code = new String[length][];
		int count = 0;
		SQLiteDatabase db = getReadableDatabase();
		for(int i=0; i<length; i++) {
			Cursor cursor = db.query("citys", new String[]{"name", "city_num"},
					"province_id = ? ", new String[]{String.valueOf(i)}, null, null, null);
			count = cursor.getCount();
			city[i] = new String[count];
			code[i] = new String[count];
			count = 0;
			while(!cursor.isLast()) {
				cursor.moveToNext();
				city[i][count] = cursor.getString(0);
				code[i][count] = cursor.getString(1);
				count = count + 1;
			}
		    cursor.close();
		}
		db.close();
		List<String[][]> result = new ArrayList<String[][]>();
		result.add(city);
		result.add(code);
		return result;
	}
	
	/**
	 * 由城市名查询数据库来得到城市�?	 * @param cityName 城市�?	 * @return 城市�?	 */
	public String getCityCodeByName(String cityName) {
		Cursor cursor=null;
		String cityCode = null;
		SQLiteDatabase db = getReadableDatabase();
	try{		
		cursor = db.query("citys", new String[]{"city_num"},
				"name = ? ", new String[]{cityName}, null, null, null);
		
		if(!cursor.isLast()){
			cursor.moveToNext();
			cityCode = cursor.getString(0);
		}
	}catch(Exception e)
	{
			cityCode="";
			
	}
	   finally
       {
		 cursor.close();
		 db.close();
	   }
		return cityCode;
	}
}
