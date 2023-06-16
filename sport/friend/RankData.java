package com.desay.sport.friend;

import java.util.List;
import java.util.Map;

public class RankData {
	
   private List<Map<String,Object>> mListRankData;
   private String rankmynum;
   private String rankmymile;
   private String rankmytime;
   
   
   public void setrankmynum(String rankmynum)
	{
		this.rankmynum=rankmynum;
	}
	public String getrankmynum()
	{
		return rankmynum;
	}
	public void setrankmymile(String rankmymile)
	{
		this.rankmymile=rankmymile;
	}
	public String getrankmymile()
	{
		return rankmymile;
	}
	public void setrankmytime(String rankmytime)
	{
		this.rankmytime=rankmytime;
	}
	public String getrankmytime()
	{
		return rankmytime;
	}
   public void setListRankData(List<Map<String,Object>> mListRankData) {
	   this.mListRankData =mListRankData;
   }
	public List<Map<String,Object>> getListRankData() {
	    return mListRankData;
   }
}
