package com.desay.sport.data;

import java.util.ArrayList;
import java.util.List;

import com.baidu.platform.comapi.map.r;
import com.desay.sport.anim.animMedal;
import com.desay.sport.anim.animMedalClick;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.main.R;
import com.desay.sport.slidepage.MyApplication;
import com.desay.sport.slidepage.medalAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class UpdataMedalUI 
{
	private static final String TAG = "UpdataMedalUI_1";
	private Context um_context;
	private View view;
    private GridView gv_1;
    private GridView gv_2;
    private GridView gv_3;
    private GridView gv_4;
    private GridView gv_5;
    
    private entity_sportachieve []medaldata;
    private static int medaloffset1 = 2;
    private static int medaloffset2 = 13; 
    private static int medaloffset3 = 17; 
    private static int medaloffset4 = 28; 
    private static int medaloffset5 = 31; 
    private static int MedalCount   = 32;
    
    private static final int MEDALTYPE_1  = 0;
    private static final int MEDALTYPE_2  = 1; 
    private static final int MEDALTYPE_3  = 2; 
    private static final int MEDALTYPE_4  = 3; 
    private static final int MEDALTYPE_5  = 4;  
    
    int medalName = -1;
	public UpdataMedalUI(Context um_context,View view)
	{
		this.um_context=um_context;
		this.view=view;
	}
	public void init()
	{
	    gv_1 = (GridView) view.findViewById(R.id.gv_1);
	    gv_2 = (GridView) view.findViewById(R.id.gv_2);
	    gv_3 = (GridView) view.findViewById(R.id.gv_3);
	    gv_4 = (GridView) view.findViewById(R.id.gv_4);
	    gv_5 = (GridView) view.findViewById(R.id.gv_5);  
	  
		gv_1.setSelector(R.color.transparent);
		gv_2.setSelector(R.color.transparent);
		gv_3.setSelector(R.color.transparent);
		gv_4.setSelector(R.color.transparent);
		gv_5.setSelector(R.color.transparent);

	    
	    medaldata = new entity_sportachieve [MedalCount];
	}
	
	public void UpUi(List<entity_sportachieve> medal_list)
	{
		
	    for(int i = 0 ;i<MedalCount;i++)//初始化
	    {
	    	medaldata[i] = new entity_sportachieve();
	    	medaldata[i].setStarttime(-1);//据此判断当前是否获得此奖章
	    	medaldata[i].setAchieverecord(-1);
	    	medaldata[i].setAchievename(i);
	    }
		
		for (int i = 0; i < medal_list.size(); i++)
		{
			entity_sportachieve sportachieve = medal_list.get(i);	
			medaldata[sportachieve.getAchievename()] = sportachieve;
		}
		List<entity_sportachieve> list1 = new ArrayList<entity_sportachieve>();
		List<entity_sportachieve> list2 = new ArrayList<entity_sportachieve>();	
		List<entity_sportachieve> list3 = new ArrayList<entity_sportachieve>();
		List<entity_sportachieve> list4 = new ArrayList<entity_sportachieve>();
		List<entity_sportachieve> list5 = new ArrayList<entity_sportachieve>();
		
		int medalID = -1;
	    for(int i = 0 ;i<MedalCount;i++)//初始化
	    {
//	    	Log.d(TAG, "MedalCount==="+i);
//	    	Log.d(TAG, "medaldata.getStarttime="+medaldata[i].getStarttime());		
//	    	Log.d(TAG, "medaldataget.Achievename="+medaldata[i].getAchievename());	    	
//	    	Log.d(TAG, "medaldata.getAchieverecord="+medaldata[i].getAchieverecord()); 
	    	
	    	if(medaldata[i].getStarttime()!=-1 && medalID<medaldata[i].getAchievename())
	    	{//获取最大的奖章ID
	    		medalID = medaldata[i].getAchievename();
	    	}
	    	
	    	if(i<=medaloffset1)
	    	{
	    		medaldata[i].setAchievetype(MEDALTYPE_1);
	    		list1.add(medaldata[i]);
	    		if(i==medaloffset1)
	    		{
	    			gv_1.setAdapter(new medalAdapter(getShowList(list1,MEDALTYPE_1,medalID),um_context,R.layout.item_medal));
	    			medalID = -1;
	    		}
	    	}
	    	else if(i>medaloffset1 && i<=medaloffset2)
	    	{
	    		medaldata[i].setAchievetype(MEDALTYPE_2);
	    		list2.add(medaldata[i]);
	    		if(i==medaloffset2)
	    		{
	    			gv_2.setAdapter(new medalAdapter(getShowList(list2,MEDALTYPE_2,medalID),um_context,R.layout.item_medal));
	    			medalID = -1;
	    		}
	    	}
	    	else if(i>medaloffset2 && i<=medaloffset3)
	    	{
	    		medaldata[i].setAchievetype(MEDALTYPE_3);
	    		list3.add(medaldata[i]);
	    		if(i==medaloffset3)
	    		{
	    			gv_3.setAdapter(new medalAdapter(getShowList(list3,MEDALTYPE_3,medalID),um_context,R.layout.item_medal));
	    			medalID = -1;
	    			//getShowList(list3,MEDALTYPE_3,medalID)
	    		}
	    	}
	    	else if(i>medaloffset3+1 && i<=medaloffset4)//加1屏蔽迈出第一步
	    	{
	    		medaldata[i].setAchievetype(MEDALTYPE_4);
	    		list4.add(medaldata[i]);
	    		if(i==medaloffset4)
	    		{
	    			gv_4.setAdapter(new medalAdapter(getShowList(list4,MEDALTYPE_4,medalID),um_context,R.layout.item_medal));
	    			medalID = -1;
	    			//getShowList(list4,MEDALTYPE_4,medalID)
	    		}
	    	}	    	
	    	else if(i>medaloffset4 && i<=medaloffset5)
	    	{
	    		medaldata[i].setAchievetype(MEDALTYPE_5);
	    		list5.add(medaldata[i]);
	    		if(i==medaloffset5)
	    		{
	    			gv_5.setAdapter(new medalAdapter(getShowList(list5,MEDALTYPE_5,medalID),um_context,R.layout.item_medal));
	    			medalID = -1;
	    		}
	    	}

	    }    
    	setGridViewHeight(gv_1,MEDALTYPE_1);    	
    	setGridViewHeight(gv_2,MEDALTYPE_2); 
    	setGridViewHeight(gv_3,MEDALTYPE_3); 
    	setGridViewHeight(gv_4,MEDALTYPE_4); 
    	setGridViewHeight(gv_5,MEDALTYPE_5); 	
    	 gv_1.setOnItemClickListener(new gridViewListener());
    	 gv_2.setOnItemClickListener(new gridViewListener());
    	 gv_3.setOnItemClickListener(new gridViewListener());
    	 gv_4.setOnItemClickListener(new gridViewListener());
    	 gv_5.setOnItemClickListener(new gridViewListener());
    	
	    
	}
	
	public void setGridViewHeight(GridView listView,int type) { 
		ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
        int sizeoffset  = 0;
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i+=3) {  
        View listItem = listAdapter.getView(i, null, listView);  
        	listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();  
        } 
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight+sizeoffset;  
        listView.setLayoutParams(params);  
    }
	
	private List<entity_sportachieve> getShowList(List<entity_sportachieve> tlist,int type,int medalID) { 

            List<entity_sportachieve> list = new ArrayList<entity_sportachieve>();
            int size = getListNumber(getMedalOffset(type,medalID));
            size = size < tlist.size()?size:tlist.size();
            for(int i = 0;i<size;i++)
            	list.add(tlist.get(i));   
            return list;
	}
	
	private int getMedalOffset(int type,int medalid)//get the medal id in the list
	{
        int idoffset = 0;
        switch(type)
        {
            case MEDALTYPE_1:
            	idoffset = 0;
            	break;
            case MEDALTYPE_2:
                idoffset = 3;
                break;
            case MEDALTYPE_3:
            	idoffset = 14;
            	break;
            case MEDALTYPE_4:
            	idoffset = 18;
            	break;
            case MEDALTYPE_5:
            	idoffset = 29;
            	break;
        }
        return ( medalid-idoffset);
	}
	
	private int getListNumber(int medalposition)//get the number of list should show
	{
		int number = -1;
		if(medalposition<3)
			number = 3;
		else if(medalposition >=3 && medalposition<6)
			number = 6;
		else if(medalposition >=6 && medalposition<9)
			number = 9;
		else if(medalposition >=9 && medalposition<12)
			number = 12;
		else if(medalposition >=12 && medalposition<15)
			number = 15;
		return number;
	}
	
	class gridViewListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			if (parent.getId()== R.id.gv_1)
			{
				medalName = position;				
			}
			else if (parent.getId()== R.id.gv_2) {
				medalName = position+3;
			}
			else if (parent.getId()== R.id.gv_3) {
				medalName = position+14;
			}
			else if (parent.getId()== R.id.gv_4) {
				medalName = position+19;
			}else {
				medalName = position+29;
			}
			Intent i = new Intent(um_context,animMedalClick.class);
			i.putExtra("medalname", medalName);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			um_context.startActivity(i);
			
		}
		
	}

}
