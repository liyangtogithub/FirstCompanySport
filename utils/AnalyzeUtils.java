package com.desay.utils;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.desay.sport.data.SportData;
import com.desay.sport.db.entity_loopheat;
import com.desay.sport.db.entity_mac;
import com.desay.sport.db.entity_sendrecord;
import com.desay.sport.db.entity_sendroute;
import com.desay.sport.db.entity_sportachieve;
import com.desay.sport.db.entity_sportloop;
import com.desay.sport.db.entity_sportrecord;
import com.desay.sport.db.entity_sportroute;
import com.desay.sport.db.entity_userinfo;
import com.desay.sport.db.sportDB;
import com.desay.sport.friend.FriendData;
import com.desay.sport.friend.RankData;
import com.desay.sport.main.R;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;


public class AnalyzeUtils {
	
	private int num=0;
	private Context au_context;
	public AnalyzeUtils(Context au_context)
	{
		this.au_context=au_context;
	}
	public static Document createDocument(String xmlStr){
    	StringReader sr = new StringReader(xmlStr);
    	InputSource is = new InputSource(sr);
    	SAXBuilder builder = new SAXBuilder();
    	Document doc=null;
    	try {
    	doc = builder.build(is);
    	} catch (Exception e) {
           e.printStackTrace();
    	}
    	return doc;
    }
	public entity_userinfo analyze_load(String data)
    {
		String string="";
		entity_userinfo UVO =null;
		int zb=data.indexOf("<body>"); 
       if(zb>-1)
       {
    	   UVO = new entity_userinfo();
    	   data=data.substring(zb);
    	   try{
         	   Document doc=createDocument(data);
         	   Element root1 = doc.getRootElement();  // 得到整个文档的根
         	   Element record = root1.getChild("record");  //在根里面查名字为elementName的节点
         	   string=record.getChildTextTrim("sex");
         	   UVO.setSex(string);
      		   string=record.getChildTextTrim("birthday");
      		   UVO.setBirthday(string);
      		   string=record.getChildTextTrim("height");
      		   UVO.setHeight(string);
      		   string=record.getChildTextTrim("weight");
      		   UVO.setWeight(string);
      		   string=record.getChildTextTrim("city");
      		   UVO.setCity(string);
    		   string=record.getChildTextTrim("portraitUrl");
    		   UVO.setheadphotourl(string);
         	   }catch(Exception e)
         	   {
         		   e.printStackTrace();
         		  UVO=null;
         	   }
    	   
       }   	   
        return UVO;
    }
	public FriendData analyze_friendmsg(String data)
    {
		FriendData fd=null;
		int zb=data.indexOf("<body>");
       if(zb>-1)
       {
			fd=new FriendData();
  	       data=data.substring(zb);
     	   try{
     		  long miles=0;
     	   Document doc=createDocument(data);
     	   Element root1 = doc.getRootElement();  // 得到整个文档的根
     	  Element root = root1.getChild("record");  //在根里面查名字为elementName的节点
     	   String string="";
     	   String name="";
     	   string=root.getChildTextTrim("username");
  		   fd.setmyname(string);
		   string = root.getChildTextTrim("showname");
		   if(string==null||string.equals(""))
				string=fd.getmyname();
		   fd.setshowname(string);
  		   string=root.getChildTextTrim("portraitUrl");
  		   string=analyzeHeadPhote(string);
  		   fd.setheadimg(string);
  		   string=root.getChildTextTrim("city");
  		   fd.setmycity(string);
  		   string=root.getChildTextTrim("totalDistance");
  		   if(string==null)
  			 miles=0;
  		   else
  		     miles=Long.parseLong(string);
  		   fd.setmymile(SportData.getKilometer(miles));
  		   string=root.getChildTextTrim("sportCount");
  		   fd.setmynum(string);
  		   string=root.getChildTextTrim("totalDuration");
  		   string=SportData.getFormatTime(Long.parseLong(string));
		   fd.setmytime(string);
		   string=root.getChildTextTrim("maxDistance");
		   if(string==null)
	  			 miles=0;
	  		   else
	  		     miles=Long.parseLong(string);
  		   fd.setmylongd(au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
  		   string=root.getChildTextTrim("maxDistanceSportTypeCode");
  		   if(string==null)
 	 		  string="0";
  		   fd.setsporttype1(SportData.sporttpye(au_context,string));
  		   string=root.getChildTextTrim("maxSpeed");
  		   string=String.format("%4.2f",3.6*Float.parseFloat(string));
  	 	   fd.setmyspeed(string+au_context.getString(R.string.speed_u_str));
  	 	   string=root.getChildTextTrim("maxSpeedSportTypeCode");
  	 	   if(string==null)
  	 		  string="0";
		   fd.setsporttype2(SportData.sporttpye(au_context,string));
		   string=root.getChildTextTrim("maxDuration");
  		   fd.setmyltime(string+au_context.getString(R.string.time_u_str));
  		   string=root.getChildTextTrim("maxDurationSportTypeCode");
  		   if(string==null)
 	 		  string="0";
		   fd.setsporttype3(SportData.sporttpye(au_context,string));
		   string=root.getChildTextTrim("medalCount");
	       fd.setmedalnum(Integer.parseInt(string));
	       string=root.getChildTextTrim("friendCount");
	       fd.setfriendnum(Integer.parseInt(string));
		 
	       Element medalroot = root.getChild("medals");
	       List<Element> meadllist = medalroot.getChildren("medal");
	       List<entity_sportachieve> sportachieve_list=new ArrayList<entity_sportachieve>();
	       for(int i=0;i<meadllist.size();i++)
	       {
	    	   entity_sportachieve sportachieve=new entity_sportachieve();	    	   
	    	   Element book=(Element)meadllist.get(i);
//	    	   string= book.getChildTextTrim("generateTime");
	    	   string= book.getChildTextTrim("medalTypeCode");
	    	   sportachieve.setAchievetype(Integer.parseInt(string.trim())); 
	    	   string= book.getChildTextTrim("medalCode");
	    	   sportachieve.setAchievename(Integer.parseInt(string.trim()));
	    	   string= book.getChildTextTrim("time");
			   sportachieve.setAchieverecord(Integer.parseInt(string.trim()));
			   sportachieve_list.add(sportachieve);
	       }
	       fd.setmeallist(sportachieve_list);
	       Element friendroot = root.getChild("friends");
	       List<Element> friendlist = friendroot.getChildren("friend");
	       for(int i=0;i<friendlist.size();i++)
	       {   	   
	    	   Element book=(Element)friendlist.get(i);
	    	   if(i==0)
	    	   { 
	    		   name= book.getChildTextTrim("username");
	    		   string= book.getChildTextTrim("showname");
	    		   if(string==null||string.equals(""))
	    			   string=name;
	    	       fd.setfname1(string);
	    	       string= book.getChildTextTrim("portraitUrl");
	    	       string=analyzeHeadPhote(string);
	    	       fd.setfhdimg1(string);
	    	   }else if(i==1)
	    	   {
	    		   name= book.getChildTextTrim("username");
	    		   string= book.getChildTextTrim("showname");
	    		   if(string==null||string.equals(""))
	    			   string=name;
	    	       fd.setfname2(string);
	    	       string= book.getChildTextTrim("portraitUrl");
	    	       string=analyzeHeadPhote(string);
	    	       fd.setfhdimg2(string);
	    	   }else if(i==2)
	    	   {
	    		   name= book.getChildTextTrim("username");
	    		   string= book.getChildTextTrim("showname");
	    		   if(string==null||string.equals(""))
	    			   string=name;
	    	       fd.setfname3(string);
	    	       string= book.getChildTextTrim("portraitUrl");
	    	       string=analyzeHeadPhote(string);
	    	       fd.setfhdimg3(string);
	    	   }else if(i==3)
	    	   {
	    		   name= book.getChildTextTrim("username");
	    		   string= book.getChildTextTrim("showname");
	    		   if(string==null||string.equals(""))
	    			   string=name;
	    	       fd.setfname4(string);
	    	       string= book.getChildTextTrim("portraitUrl");
	    	       string=analyzeHeadPhote(string);
	    	       fd.setfhdimg4(string);
	    	   }
	    		   

	       }
	       string=root.getChildTextTrim("attentionState");
		   fd.setmystatus(string);
     	   }catch(Exception e)
     	   {
     		   e.printStackTrace();
     		   fd=null;
     	   }
         }
        return fd;
    }
	public List<Map<String,Object>> analyze_friendlist(String data,List<Map<String,Object>> datalist)
    {
    	 int zb=data.indexOf("<body>");
        if(zb>-1)
        {
  	       data=data.substring(zb);
     	   try{
     	   Document doc=createDocument(data);
     	   Element root = doc.getRootElement();  // 得到整个文档的根
    	   List<Element> list = root.getChildren("record");	
    	   String string="";
    	   String name="";
    	   String name3="";
 			for(int i=0;i<list.size();i++)
 			{
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				Element book=(Element)list.get(i);
 				name = book.getChildTextTrim("username");
				map.put("f_name1",name);
				name3= book.getChildTextTrim("showname");
				if(name3==null||name3.equals(""))
					name3=name;
				map.put("f_name3",name3);
				string = book.getChildTextTrim("alias");
				if(string==null||string.equals(""))
					string=name3;
				map.put("f_name2",string);
 				string= book.getChildTextTrim("portraitUrl");
 				string=analyzeHeadPhote(string);
// 				if(string==null||string.equals(""))
// 					map.put("f_headp",R.drawable.headdefault);
// 				else
// 				{
// 				    byte[] photo = SportData.hexStringToBytes(string);
// 					Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0,photo.length);
// 					map.put("f_headp",bmp);
// 				}
 				map.put("f_headp",string);
 				string =book.getChildTextTrim("city");
 				map.put("f_city",string);
 				string=book.getChildTextTrim("distanceTotal");
 				long miles=0;
 				if(string!=null)
				    miles=Long.parseLong(string);
 				map.put("f_miles",au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
 				datalist.add(map);
 			}
     	   }catch(Exception e)
     	   {
     		   e.printStackTrace();
     	   }
         }
	  return datalist;
    }
	public List<Map<String,Object>> analyze_myfriendlist(String data,List<Map<String,Object>> datalist)
    {
    	 int zb=data.indexOf("<body>");
    	 boolean adddata=true;
    	 if(zb>-1)
         {
  	       data=data.substring(zb);
     	   try{
     	   Document doc=createDocument(data);
     	   Element root = doc.getRootElement();  // 得到整个文档的根
    	   List<Element> list = root.getChildren("record");	
    	   String string="";
    	   String name="";
    	   String name3="";
 			for(int i=0;i<list.size();i++)
 			{
 				
 				Element book=(Element)list.get(i);
 				name = book.getChildTextTrim("username");
 				for(int j=0;j<datalist.size();j++)    	 
 			    { 		                
 				   if(datalist.get(j).get("f_name1").equals(name)) 
 					  adddata=false;
 			    }
 				if(adddata)
 				{
 					HashMap<String, Object> map = new HashMap<String, Object>();
				    map.put("f_name1",name);
					name3= book.getChildTextTrim("showname");
					if(name3==null||name3.equals(""))
						name3=name;
					map.put("f_name3",name3);
				    string = book.getChildTextTrim("alias");
				    if(string==null||string.equals(""))
					   string=name3;
				     map.put("f_name2",string);
 				     string= book.getChildTextTrim("portraitUrl");
 				     string=analyzeHeadPhote(string);
 			  	     map.put("f_headp",string);
 				     string =book.getChildTextTrim("city");
 				     map.put("f_city",string);
 				     string=book.getChildTextTrim("distanceTotal");
 				     long miles=0;
 				     if(string!=null)
				       miles=Long.parseLong(string);
 				     map.put("f_miles",au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
 				     datalist.add(map);
 				}
 				adddata=true;
 			}
     	   }catch(Exception e)
     	   {
     		   e.printStackTrace();
     	   }
        }
	  return datalist;
    }
	public List<Map<String,Object>> analyze_searchlist(String data,List<Map<String,Object>> datalist)
    {
    	 int zb=data.indexOf("<body>");
 	   if(zb>-1)
       {
 	       data=data.substring(zb);
 	      long miles=0;
    	   try{
    	   Document doc=createDocument(data);
    	   Element root = doc.getRootElement();  // 得到整个文档的根
   		   List<Element> list = root.getChildren("record");	  
			for(int i=0;i<list.size();i++)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				Element book=(Element)list.get(i);
				String name = book.getChildTextTrim("username");
				map.put("s_name",name);
				String string= book.getChildTextTrim("showname");
				if(string==null||string.equals(""))
					string=name;
				map.put("s_name3",string);
				string= book.getChildTextTrim("portraitUrl");
				string=analyzeHeadPhote(string);
				map.put("s_headp",string);
				string =book.getChildTextTrim("city");
				map.put("s_city",string);
				string=book.getChildTextTrim("distanceTotal");
				if(string!=null)
				    miles=Long.parseLong(string);
				else
					miles=0;
 				 map.put("s_miles",au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
 				 string=book.getChildTextTrim("attentionState");
 		        if(string!=null&&string.equals("0"))
 		        	map.put("s_status",au_context.getString(R.string.cancle_str));
 		        else
 		           map.put("s_status",au_context.getString(R.string.attention_str));
 				datalist.add(map);
			}
    	   }catch(Exception e)
    	   {
    		   e.printStackTrace();
    	   }
        }
	  return datalist;
    }
	 public String analyze_returntoast(String data)
     {
 		String isReg="";
 		int zb=data.indexOf("<body>");
 	    int zd=data.indexOf("</body>");  
 	    if(zb>-1&&zd>-1)
        {
     	   data=data.substring(zb+6,zd);
     	   isReg=data;
        }   	   
         return isReg;
     }
//	 public List<Map<String,Object>> analyze_msglist(String data)
//	    {
//			 List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
//	    	 int zb=data.indexOf("<body>");
//	    	 if(zb>-1)
//	         {
//	  	       data=data.substring(zb);
//	     	   try{
//	     	   Document doc=createDocument(data);
//	     	   Element root = doc.getRootElement();  // 得到整个文档的根
//	     	   String string="";
//	    	   List<Element> list = root.getChildren("record"); 
//	 			for(int i=0;i<list.size();i++)
//	 			{
//	 				HashMap<String, Object> map = new HashMap<String, Object>();
//	 				Element book=(Element)list.get(i);
//	 				string= book.getChildTextTrim("headImage");
//	 				if(string==null||string.equals(""))
//						map.put("m_headp",R.drawable.headphoto_default1);
//					else
//					{
//					    byte[] photo = SportData.hexStringToBytes(string);
//						Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0,photo.length);
//						map.put("m_headp",bmp);
//					}
//	 				string = book.getChildTextTrim("friend_name");
//	 				map.put("m_name",string);
//	 				string=book.getChildTextTrim("time");
//	 				map.put("m_day",string);
//	 				string =book.getChildTextTrim("attention_status");
//	 				if(string.equals("0"))
//	 				   map.put("m_msg",au_context.getString(R.string.make_attention_str));
//	 				else
//	 				   map.put("m_msg",au_context.getString(R.string.no_attention_str));
//	 				datalist.add(map);
//	 			}
//	     	   }catch(Exception e)
//	     	   {
//	     		   e.printStackTrace();
//	     	   }
//	         }
//	        datalist=new Test().putmsgtolist(au_context);
//		  return datalist;
//	    }
	 public int saveSQL_msglist(String data,sportDB db,String myname)
	    {
			 int msgn=0;
		     List<Map<String,String>> datalist = new ArrayList<Map<String,String>>();
	    	 int zb=data.indexOf("<body>");
	    	 if(zb>-1)
	         {
	  	       data=data.substring(zb);
	     	   try{
	     	   Document doc=createDocument(data);
	     	   Element root = doc.getRootElement();  // 得到整个文档的根
	     	   String string="";
	     	   String name="";
	    	   List<Element> list = root.getChildren("record"); 
	    	   
	 			for(int i=0;i<list.size();i++)
	 			{
	 				HashMap<String, String> map = new HashMap<String, String>();
	 				Element book=(Element)list.get(i);
	 				name = book.getChildTextTrim("username");
	 				map.put("m_name",name);
	 				string= book.getChildTextTrim("showname");
					if(string==null||string.equals(""))
						string=name;
					map.put("m_name3",string);
	 				string =book.getChildTextTrim("attentionStatus");
	 				map.put("m_msg",string);
	 				string=book.getChildTextTrim("time");
	 				map.put("m_day",string);
	 				string= book.getChildTextTrim("portraitUrl");
	 				string=analyzeHeadPhote(string);
					map.put("m_headp",string);
	 				datalist.add(map);
	 			}
	 			db.refreshTable_ActionMsg(myname,datalist);
	 			   msgn=datalist.size();
//	 			Log.d("saveSQL_msglist"+msgn,"list.size()"+list.size());
	     	   }catch(Exception e)
	     	   {
	     		   e.printStackTrace();
	     		   msgn=0;
	     		   db.closeDB();
	     	   }
	         }
		  return msgn;
	    }
	 public RankData analyze_rank(String data)
	 {
		 RankData RD=new RankData();
	     int zb=data.indexOf("<body>"); 
	     List<Map<String,Object>> datalist = new ArrayList<Map<String,Object>>();
	     if(zb>-1)
	     {
	    	   data=data.substring(zb);
	    	   try{
	    		  String string="";
	    		  String name="";
	    		  String name3="";
	    	      Document doc=createDocument(data);
	    	      Element root1 = doc.getRootElement();  // 得到整个文档的根
	    	      Element root = root1.getChild("SportRankingWithUser"); 
	    	      string=root.getChildTextTrim("distance");
	    	      long miles=0;
	 				if(string!=null)
					    miles=Long.parseLong(string);
	   		      RD.setrankmymile(au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
	   		      string=root.getChildTextTrim("ranking");
	   		      if(string==null)
	   		    	RD.setrankmynum(au_context.getString(R.string.no_rankn_str));
	   		      else
	              RD.setrankmynum(au_context.getString(R.string.rank_num_str,string));
	              Element rootlist = root.getChild("sportRankings");
	              List<Element> list = rootlist.getChildren("record");
				  for(int i=0;i<list.size();i++)
				  {
					HashMap<String, Object> map = new HashMap<String, Object>();
					Element book=(Element)list.get(i);
					name = book.getChildTextTrim("username");
					map.put("f_name1",name);
					name3= book.getChildTextTrim("showname");
					if(name3==null||name3.equals(""))
						name3=name;
					map.put("f_name3",name3);
					string = book.getChildTextTrim("alias");
					if(string==null||string.equals(""))
						string=name3;
					map.put("f_name2",string);
					string= book.getChildTextTrim("portraitUrl");
//					if(string==null||string.equals(""))
//						map.put("f_headp",R.drawable.headdefault);
//					else
//					{
//					    byte[] photo = SportData.hexStringToBytes(string);
//						Bitmap bmp = BitmapFactory.decodeByteArray(photo, 0,photo.length);
//						map.put("f_headp",bmp);
//					}
					string=analyzeHeadPhote(string);
					map.put("f_headp",string);
					string=book.getChildTextTrim("distance");
					if(string!=null)
					miles=Long.parseLong(string);
					else
					    miles=0;
					map.put("f_miles",au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
					string =book.getChildTextTrim("ranking");
					map.put("f_num",au_context.getString(R.string.rank_num_str,string));
					datalist.add(map);
				   }
			 	   RD.setListRankData(datalist);
			 	   RD.setrankmytime(getUPTime());
	    	   }catch(Exception e)
	    	   {
	    		   e.printStackTrace();
	    		   RD=null;
	    	   }
	        }
//	     RD=new Test().putrankdatatolist(au_context);
		return RD;
	 }
	 public RankData analyze_uprank(String data,RankData RD1,List<Map<String,Object>> datalist)
	 {
	     int zb=data.indexOf("<body>"); 
	     RankData RD=new RankData();
	   	 boolean adddata=true;
	     if(zb>-1)
	        {
	    	   data=data.substring(zb);
	    	   try{
	    		  String string="";
	    		  String name="";
	    		  String name3="";
	    		  if(RD1!=null)
	                 RD.setrankmytime(RD1.getrankmytime());
	    	      Document doc=createDocument(data);
	    	      Element root1 = doc.getRootElement();  // 得到整个文档的根
	    	      Element root = root1.getChild("SportRankingWithUser"); 
	    	      string=root.getChildTextTrim("distance");
	    	      long miles=0;
	 				if(string!=null)
					    miles=Long.parseLong(string);
	   		      RD.setrankmymile(au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
	   		      string=root.getChildTextTrim("ranking");
	   		      if(string==null)
	   		    	RD.setrankmynum(au_context.getString(R.string.no_rankn_str));
	   		      else
	                RD.setrankmynum(au_context.getString(R.string.rank_num_str,string));
	              Element rootlist = root.getChild("sportRankings");
	              List<Element> list = rootlist.getChildren("record");
				  for(int i=0;i<list.size();i++)
				  {
					  Element book=(Element)list.get(i);
					  name = book.getChildTextTrim("username");
					  for(int j=0;j<datalist.size();j++)    	 
		 			 { 		                
		 				 if(datalist.get(j).get("f_name1").equals(name)) 
		 					  adddata=false;
		 			 }
		 			 if(adddata)
		 			 {
					   HashMap<String, Object> map = new HashMap<String, Object>();
					   map.put("f_name1",name);
						name3= book.getChildTextTrim("showname");
						if(name3==null||name3.equals(""))
							name3=name;
						map.put("f_name3",name3);
					   string = book.getChildTextTrim("alias");
					   if(string==null||string.equals(""))
						  string=name3;
					   map.put("f_name2",string);
					   string= book.getChildTextTrim("portraitUrl");
					   string=analyzeHeadPhote(string);
					   map.put("f_headp",string);
					   string=book.getChildTextTrim("distance");
					   if(string!=null)
					      miles=Long.parseLong(string);
					   else
					      miles=0;
					   map.put("f_miles",au_context.getString(R.string.ldis_str,SportData.getKilometer(miles)));
					   string =book.getChildTextTrim("ranking");
					   map.put("f_num",au_context.getString(R.string.rank_num_str,string));
					   datalist.add(map);
		 			  }
		 			   adddata=true;
				   }
			 	   RD.setListRankData(datalist);
			 	   
	    	   }catch(Exception e)
	    	   {
	    		   e.printStackTrace();
	    		   RD=null;
	    	   }
	        }
		return RD;
	 }
	 public boolean GetRecord(String data,String name,long startytime,long endtime)
	 {
		 int zb=data.indexOf("<body>");
		 boolean freshok=false;
		 if(zb>-1)
         {
		   
		   data=data.substring(zb);
  	       String string="";
  	       long stime=0;
  	       long etime=0;
  	       int dis=0;
  	       sportDB db = null;
  	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  	   java.util.Date date1;
     	   try{
     		  db=new sportDB(au_context); 
     	      Document doc=createDocument(data);
     	      Element root = doc.getRootElement();  // 得到整个文档的根
     	      List<Element> list = root.getChildren("record");
     		 List<entity_sportrecord> listrecord=new ArrayList<entity_sportrecord>();
     		 List<entity_sendroute> listroute=new ArrayList<entity_sendroute>();
     	      for(int i=0;i<list.size();i++)
		     {
			    Element book=(Element)list.get(i);
			    entity_sportrecord sportrecord=new entity_sportrecord();
			    string = book.getChildTextTrim("startTime");
			    if(string!=null)
			   {
					try {
						date1 = format.parse(string);
						 stime=date1.getTime(); 
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			   }
			    else
				  stime=0;
			    sportrecord.setStarttime(stime);
			    string= book.getChildTextTrim("endTime");
			    if(string!=null)
			    {
					try {
						date1 = format.parse(string);
						etime=date1.getTime(); 
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			    }
			    else
			    	etime=0;
			   sportrecord.setEndtime(etime);
			   string= book.getChildTextTrim("sportTypeCode");
			   sportrecord.setSporttype(Integer.parseInt(string));
			   string= book.getChildTextTrim("distance");
			   dis=Integer.parseInt(string);
			   sportrecord.setDistance(dis);
			   string= book.getChildTextTrim("duration");
			   int duration=Integer.parseInt(string);
			   sportrecord.setDurationtime(duration);
			   string= book.getChildTextTrim("calorie");
			   sportrecord.setCalorie(Integer.parseInt(string));
			   string= book.getChildTextTrim("mode");
			   if(string==null||string.equals("")||string.equals("null"))
				   sportrecord.setMode(0);
			   else
			       sportrecord.setMode(Integer.parseInt(string));
			   
			   string= book.getChildTextTrim("avgspeed");
			   if(string==null||string.equals("")||string.equals("null"))
				   sportrecord.setAvgspeed((float) (dis*3.6/duration));
			   else
			       sportrecord.setAvgspeed(Float.parseFloat(string));
			   
			   string= book.getChildTextTrim("pace");
			   if(string==null||string.equals("")||string.equals("null"))
				   sportrecord.setPace(0);
			   else
			       sportrecord.setPace(Integer.parseInt(string));
			   listrecord.add(sportrecord);
			   
			   Element locaroot = book.getChild("locations");
			   if(locaroot!=null)
			   {
				   List<Element> locationlist = locaroot.getChildren("location");
				   for(int j=0;j<locationlist.size();j++)
				   {
					  entity_sendroute sportroute=new entity_sendroute();
					  Element locabook=(Element)locationlist.get(j);
					  sportroute.setStarttime(stime);
					  string= locabook.getChildTextTrim("longitude");
					  sportroute.setLongitude(string);
					  string= locabook.getChildTextTrim("latitude");
					  sportroute.setLatitude(string);
					  string= locabook.getChildTextTrim("distance");
					  sportroute.setDistance(string);
					  string= locabook.getChildTextTrim("speed");
					  sportroute.setSpeed(string);
					  listroute.add(sportroute);
				    }
			    }
			   freshok=true;
		     }
     	      if(freshok)
     	      { 
     	    	  db.DelRecentUploadData(name,startytime,endtime);
    		      db.InsertSportRecord(name,listrecord,SportData.UPLOAD);
    		      db.InsertSportRoute(name,listroute,SportData.UPLOAD);
    		      db.closeDB();
     	      }
     	  }catch(Exception e)
     	  {
     		   e.printStackTrace();
     		   db.closeDB();
     		   freshok=false;
     	  }
       }
		return freshok;
	}
//	 public boolean GetLoopRecord(String data,String name,long startytime,long endtime)
//	 {
//		 int zb=data.indexOf("<body>");
//		 boolean freshok=false;
//		 if(zb>-1)
//         {
//		   data=data.substring(zb);
//  	       String string="";
//  	       int dis=0;
//  	       sportDB db = null;
//     	   try{
//     		  db=new sportDB(au_context); 
//     	      Document doc=createDocument(data);
//     	      Element root = doc.getRootElement();  // 得到整个文档的根
//     	      Element record = root.getChild("record");
//   	          List<Element> list = record.getChildren("loops");
//     		 List<entity_sportloop> listrecord=new ArrayList<entity_sportloop>();
//     	      for(int i=0;i<list.size();i++)
//		     {
//			    Element book=(Element)list.get(i);
//			    entity_sportloop sportrecord=new entity_sportloop();
//			    string = book.getChildTextTrim("startTime");
//			    sportrecord.setStarttime(Long.parseLong(string));
//			    string= book.getChildTextTrim("pace");
//			    sportrecord.setpace(Integer.parseInt(string));
//			    string= book.getChildTextTrim("distance");
//			    sportrecord.setDistance(Integer.parseInt(string));
//			    string= book.getChildTextTrim("speed");
//			    sportrecord.setSpeed(Float.parseFloat(string));
//			    listrecord.add(sportrecord);
//			    freshok=true;
//		     }
//     	      if(freshok)
//     	      { 
//     	    	  db.DelLoopUploadData(name,startytime,endtime);
//    		      db.InsertLoopRecord(name,listrecord,SportData.UPLOAD);
//    		      db.closeDB();
//     	      }
//     	  }catch(Exception e)
//     	  {
//     		   e.printStackTrace();
//     		   db.closeDB();
//     		   freshok=false;
//     	  }
//       }
//		return freshok;
//	}
	 public void GetIwanMac(String data,String name)
	 {
		 int zb=data.indexOf("<body>");
		 String string="";
         entity_mac mac = new entity_mac();
		 if(zb>-1)
         {
		   data=data.substring(zb);
  	       sportDB db = null;
     	   try{
     		  db=new sportDB(au_context); 
     	      Document doc=createDocument(data);
     	      Element root = doc.getRootElement();
     	      Element record = root.getChild("record");
     	      string=record.getChildTextTrim("mac");
     	      if(string!=null&&!string.equals(""))
     	      { 
     	    	  mac.setMac(string);
     	          mac.setUpLoad(SportData.UPLOAD);
		          db.insertIwanMac(mac);
     	      }
	     	  }catch(Exception e)
	     	  {
	     		   e.printStackTrace();
	     	  }
		   	  finally
		   	  {
		   		     db.closeDB();
		   	  }
         	}
	}
	 public boolean GetHeatRecord(String data,String name,long startytime,long endtime)
	 {
		 int zb=data.indexOf("<body>");
		 boolean freshok=false;
		 if(zb>-1)
         {
		   
		   data=data.substring(zb);
  	       String string="";
  	       long stime=0;
  	       sportDB db = null;
//  	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//  	       java.util.Date date1;
     	   try{
     		  db=new sportDB(au_context); 
     	      Document doc=createDocument(data);
     	      Element root = doc.getRootElement();  // 得到整个文档的根
     	      Element record = root.getChild("record");
    	      List<Element> list = record.getChildren("value");
     		 List<entity_loopheat> listheat=new ArrayList<entity_loopheat>();
     	      for(int i=0;i<list.size();i++)
		     {
			    Element book=(Element)list.get(i);
			    entity_loopheat sportrecord=new entity_loopheat();
			    string = book.getChildTextTrim("startTime");
//			    if(string!=null)
//			   {
//					try {
//						date1 = format.parse(string);
//						 stime=date1.getTime(); 
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//			   }
//			    else
//				  stime=0;
			    sportrecord.setStarttime(Long.parseLong(string));
			   
			   string= book.getChildTextTrim("heart");
			   sportrecord.setheat(Integer.parseInt(string));
			   listheat.add(sportrecord);
			   freshok=true;
		     }
     	      if(freshok)
     	      { 
     	    	  db.DelHeatUploadData(name,startytime,endtime);
    		      db.InsertHeatRecord(name,listheat,SportData.UPLOAD);
    		      db.closeDB();
     	      }
     	  }catch(Exception e)
     	  {
     		   e.printStackTrace();
     		   db.closeDB();
     		   freshok=false;
     	  }
       }
		return freshok;
	}
	 public void GetMedals(String data,String name)
	 {
		 int zb=data.indexOf("<body>");	
		 boolean freshok=false;
		 if(zb>-1)
         {
		   
		   data=data.substring(zb);
  	       String string="";
  	       sportDB db = null;
  	       long time=0;
  	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
  	       java.util.Date date1;
  	       List<entity_sportachieve> listachieve=new ArrayList<entity_sportachieve>();
     	   try{
     		  db=new sportDB(au_context); 
     	      Document doc=createDocument(data);
     	      Element root = doc.getRootElement();  // 得到整个文档的根
  	          List<Element> meadllist =root.getChildren("record");
  	          for(int i=0;i<meadllist.size();i++)
  	          {   	   
  	        	   Element book=(Element)meadllist.get(i);
  		    	   entity_sportachieve sportachieve=new entity_sportachieve();	    	   
  		    	   string= book.getChildTextTrim("generateTime");
   	    	       if(string!=null)
  			       {
  					  try {
  						date1 = format.parse(string);
  						time=date1.getTime(); 
  					   } catch (ParseException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  				    	} 
  			        }else
  			        	time=0;
   	    	       sportachieve.setStarttime(time);
  		    	   string= book.getChildTextTrim("medalTypeCode");
  		    	   sportachieve.setAchievetype(Integer.parseInt(string.trim())); 
  		    	   string= book.getChildTextTrim("medalCode");
  		    	   sportachieve.setAchievename(Integer.parseInt(string.trim()));
  		    	   string= book.getChildTextTrim("time");
  				   sportachieve.setAchieverecord(Integer.parseInt(string.trim()));
  				   listachieve.add(sportachieve);   
  				   freshok=true;
  	          }
  	          if(freshok)
  	              db.refreshTable_SportAchieve(name, listachieve, SportData.UPLOAD);
     	  }catch(Exception e)
     	  {
     		   e.printStackTrace();
     		   db.closeDB();
     	  }
       }
	}
	 private String analyzeHeadPhote(String head)
	 {
		 int a=0;
		 
		 if(head==null)
			 head="";
		 else 
		{
			 a=head.lastIndexOf(".");
			 if (a>0)
			 head=head.substring(0, a)+au_context.getString(R.string.phote_56x56_str)+head.substring(a);
		}
		 return head;
	 }
		public String getUPTime() {
			Date date = new Date();
			DateFormat df1 = new SimpleDateFormat(au_context.getString(R.string.date_form));
			String time = df1.format(date);
			return time;
		}
}
