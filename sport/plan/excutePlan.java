package com.desay.sport.plan;

import java.util.List;

import android.content.Context;

public interface excutePlan{
	public List<List<planInfo>> readPlan_FirstStage(Context context,int planname);//获取第一阶段计划	
	public List<List<planInfo>> readPlan_SecondStage(Context context,int planname);//获取第二阶段计划
	public List<List<planInfo>> readPlan_ThirdStage(Context context,int planname);//获取第三阶段计划
	public List<List<planInfo>> readPlan(Context context,int planname,int stageindex);//获取阶段计划


}
