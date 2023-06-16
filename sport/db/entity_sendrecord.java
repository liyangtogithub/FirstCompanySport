package com.desay.sport.db;

public class entity_sendrecord {
	private long starttime ;
	private long endtime;
	private String sporttype ;
	private String distance ;
	private String durationtime ;
	private String calorie ;
	private String mode="";
	private float avgspeed ;
	private int pace;
	private String msg ;
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public long getEndtime() {
		return endtime;
	}
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	public String getSporttype() {
		return sporttype;
	}
	public void setSporttype(String sporttype) {
		this.sporttype = sporttype;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getDurationtime() {
		return durationtime;
	}
	public void setDurationtime(String durationtime) {
		this.durationtime = durationtime;
	}
	public String getCalorie() {
		return calorie;
	}
	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getmsg () {
		return msg ;
	}
	public void setmsg(String msg) {
		this.msg  = msg;
	}
	public int getPace() {
		return pace;
	}
	public void setPace(int pace) {
		this.pace = pace;
	}
	public float getAvgspeed() {
		return avgspeed;
	}
	public void setAvgspeed(float avgspeed) {
		this.avgspeed = avgspeed;
	}
	

}
