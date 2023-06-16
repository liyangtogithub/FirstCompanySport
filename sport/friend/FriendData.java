package com.desay.sport.friend;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.desay.sport.db.entity_sportachieve;

import android.graphics.Bitmap;


public class FriendData implements Serializable{
	 /**
	 * 
	 */
	   private static final long serialVersionUID = 1L;
	   private List<entity_sportachieve> meallist;
	   private String myname;//用户名
	   private String showname;//昵称
	   private String headimg; // 头像
	   private String mycity; // 城市
	   private String mynum; //总次数
	   private String mymile;//总公里
	   private String mytime; //总用时
	   private String mystatus;//关注状态
	   private String myspeed; //最好速度
	   private String mylongd;  //最远距离
	   private String myltime;  //最长时间
	   private String sporttype1; //最远距离类型
	   private String sporttype2;  //最好速度类型
	   private String sporttype3;  //最长时间类型
	   private String medal;   //奖章ID
	   private int medalnum;  //总奖章数
	   private int friendnum;  //总好友数
	   
	   private String fname1;//好友用户名
	   private String fhdimg1; // 好友头像
	   private String fname2;//好友用户名
	   private String fhdimg2; // 好友头像
	   private String fname3;//好友用户名
	   private String fhdimg3; // 好友头像
	   private String fname4;//好友用户名
	   private String fhdimg4; // 好友头像
	   
	   
	   
	   public void setmyname(String myname)
		{
			this.myname=myname;
		}
		public String getmyname()
		{
			return myname;
		}
		public void setshowname(String showname)
		{
			this.showname=showname;
		}
		public String getshowname()
		{
			return showname;
		}
		public void setheadimg(String headimg)
		{
			this.headimg=headimg;
		}
		public String getheadimg()
		{
			return headimg;
		}
	    public void setmycity(String mycity)
	   {
			this.mycity=mycity;
		}
		public String getmycity()
		{
			return mycity;
		}
	   public void setmynum(String mynum)
		{
			this.mynum=mynum;
		}
		public String getmynum()
		{
			return mynum;
		}
		public void setmytime(String mytime)
		{
			this.mytime=mytime;
		}
		public String getmytime()
		{
			return mytime;
		}
		public void setmystatus(String mystatus)
		{
			this.mystatus=mystatus;
		}
		public String getmystatus()
		{
			return mystatus;
		}
		public void setmymile(String mymile)
		{
			this.mymile=mymile;
		}
		public String getmymile()
		{
			return mymile;
		}
		public void setmyspeed(String myspeed)
		{
			this.myspeed=myspeed;
		}
		public String getmyspeed()
		{
			return myspeed;
		}
		public void setmylongd(String mylongd)
		{
			this.mylongd=mylongd;
		}
		public String getmylongd()
		{
			return mylongd;
		}
		public void setmyltime(String myltime)
		{
			this.myltime=myltime;
		}
		public String getmyltime()
		{
			return myltime;
		}
		public void setsporttype1(String sporttype1)
		{
			this.sporttype1=sporttype1;
		}
		public String getsporttype1()
		{
			return sporttype1;
		}
		public void setsporttype2(String sporttype2)
		{
			this.sporttype2=sporttype2;
		}
		public String getsporttype2()
		{
			return sporttype2;
		}
		public void setsporttype3(String sporttype3)
		{
			this.sporttype3=sporttype3;
		}
		public String getsporttype3()
		{
			return sporttype3;
		}
		public void setmedal(String medal)
		{
			this.medal=medal;
		}
		public String getmedal()
		{
			return medal;
		}
		public void setmedalnum(int medalnum)
		{
			this.medalnum=medalnum;
		}
		public int getmedalnum()
		{
			return medalnum;
		}
		public void setfriendnum(int friendnum)
		{
			this.friendnum=friendnum;
		}
		public int getfriendnum()
		{
			return friendnum;
		}
		public void setmeallist(List<entity_sportachieve> meallist)
		{
			this.meallist=meallist;
		}
		public List<entity_sportachieve> getmeallist()
		{
			return meallist;
		}
		public void setfname1(String fname1)
		{
			this.fname1=fname1;
		}
		public String getfname1()
		{
			return fname1;
		}
		public void setfhdimg1(String fhdimg1)
		{
			this.fhdimg1=fhdimg1;
		}
		public String getfhdimg1()
		{
			return fhdimg1;
		}
		
		public void setfname2(String fname2)
		{
			this.fname2=fname2;
		}
		public String getfname2()
		{
			return fname2;
		}
		public void setfhdimg2(String fhdimg2)
		{
			this.fhdimg2=fhdimg2;
		}
		public String getfhdimg2()
		{
			return fhdimg2;
		}
		public void setfname3(String fname3)
		{
			this.fname3=fname3;
		}
		public String getfname3()
		{
			return fname3;
		}
		public void setfhdimg3(String fhdimg3)
		{
			this.fhdimg3=fhdimg3;
		}
		public String getfhdimg3()
		{
			return fhdimg3;
		}
		
		public void setfname4(String fname4)
		{
			this.fname4=fname4;
		}
		public String getfname4()
		{
			return fname4;
		}
		public void setfhdimg4(String fhdimg4)
		{
			this.fhdimg4=fhdimg4;
		}
		public String getfhdimg4()
		{
			return fhdimg4;
		}
		
}
