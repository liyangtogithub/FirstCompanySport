package com.desay.sport.plan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.desay.sport.main.R;

import android.content.Context;

public class planFunc implements excutePlan {
	private static final int STAGE_FIRST  = 0;
	private static final int STAGE_SECOND = 1;
	private static final int STAGE_THIRD  = 2;
	public static  final int PLANNAME_KEEPHEALTH  = R.raw.keephealth;
	List<planInfo> list;
	List<List<planInfo>> list_stage;
	List<planInfo_child> childList;
//	@Override
//	public List<planInfo> readPlan(Context context,String planname) {
//		// TODO Auto-generated method stub		
//		InputStream is=context.getResources().openRawResource(R.raw.keephealth);
//		byte[] buffer;
//		String json = null;
//		try {
//			buffer = new byte[is.available()];
//			is.read(buffer);
//			json=new String(buffer,"GBK");
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//		list=new ArrayList<planInfo>();
//		JSONObject obj;
//		JSONObject obj1;
//		try {
//			obj = new JSONObject(json);
//			JSONArray array1=obj.getJSONArray("project");			
//			for(int i=0;i<array1.length();i++){
//				obj1=array1.getJSONObject(i);
//				int time=obj1.getInt("durationtime");
//				String state=obj1.getString("tired");
//				JSONArray array2=obj1.getJSONArray("plan");
//				List<planInfo_child> childList=readList(array2);
//				planInfo plan=new planInfo();
//				plan.setDurationtime(time);
//				plan.setTired(state);
//				plan.setChildplan(childList);
//			list.add(plan);
//			
//			}
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return list;
//	}

//
//	@Override
//	public List<planInfo> readPlan(Context context,String planname, int index) {
//		
//		
//		
//		
//		// TODO Auto-generated method stub
////		planInfo plan = null;
////		InputStream is=context.getResources().openRawResource(R.raw.keephealth);
////		String json=null;
////		try {	
////		byte[] buffer=new byte[is.available()];
////		is.read(buffer);
////		json=new String(buffer,"GBK");
////		list=new ArrayList<planInfo>();
////		JSONObject obj=new JSONObject(json);
////		JSONArray array1=obj.getJSONArray("project");
////		JSONObject obj1=array1.getJSONObject(index);
////			JSONArray array2=obj1.getJSONArray("plan");
////			int time=obj1.getInt("durationtime");
////			String state=obj1.getString("tired");
////			List<planInfo_child> childList=readList(array2);			
////			plan=new planInfo();
////			plan.setChildplan(childList);
////			plan.setDurationtime(time);
////			plan.setTired(state);
////		} catch (Exception e) {
////			// TODO: handle exception
////		}
////		return plan;
//		
//	}
	@Override
	public List<List<planInfo>> readPlan_FirstStage(Context context, int planname) {
		// TODO Auto-generated method stub
		return readPlan(context,planname,STAGE_FIRST);
	}
	@Override
	public List<List<planInfo>> readPlan_SecondStage(Context context, int planname) {
		// TODO Auto-generated method stub
		return readPlan(context,planname,STAGE_SECOND);
	}
	@Override
	public List<List<planInfo>> readPlan_ThirdStage(Context context, int planname) {
		// TODO Auto-generated method stub
		return readPlan(context,planname,STAGE_THIRD);
	}
	@Override
	public List<List<planInfo>> readPlan(Context context, int planname, int stageindex) {
		// TODO Auto-generated method stub
		InputStream is=context.getResources().openRawResource(planname);
		byte[] buffer;
		String json = null;
		try {
			buffer = new byte[is.available()];
			is.read(buffer);
			json=new String(buffer,"GBK");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		list=new ArrayList<planInfo>();
		list_stage = new ArrayList<List<planInfo>>();
		JSONObject obj;
		JSONObject obj_stage;
		JSONObject obj_times;
		
		try {
			obj = new JSONObject(json);
			obj_stage=obj.getJSONArray("project").getJSONObject(stageindex);
			JSONArray array_week  = obj_stage.getJSONArray("week");
			for(int i=0;i<array_week.length();i++){
				list=new ArrayList<planInfo>();
				JSONArray array_times = array_week.getJSONObject(i).getJSONArray("times");
				for(int j = 0 ;j<array_times.length();j++)
				{
					obj_times=array_times.getJSONObject(j);
					int durationtime=obj_times.getInt("durationtime");
					String tired=obj_times.getString("tired");
					JSONArray plan=obj_times.getJSONArray("plan");
					List<planInfo_child> childList=readList(plan);
					planInfo planinfo=new planInfo();
					planinfo.setDurationtime(durationtime);
					planinfo.setTired(tired);
					planinfo.setChildplan(childList);
					list.add(planinfo);
				}	
				list_stage.add(list);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list_stage;
	}
	
	private List<planInfo_child> readList(JSONArray array) {
		// TODO Auto-generated method stub
		List<planInfo_child> onechild=new ArrayList<planInfo_child>();
		for(int i=0;i<array.length();i++){
			try {		
			JSONObject obj2=array.getJSONObject(i);
			String sport=obj2.getString("sportname");
			int durtion=obj2.getInt("duration");
			int pluse=obj2.getInt("pulse");
			planInfo_child child=new planInfo_child();
			child.setDuration(durtion);
			child.setPulse(pluse);
			child.setSportname(sport);
			onechild.add(child);			
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return onechild;
	}
	

}
