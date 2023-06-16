package com.desay.sport.db;

public class entity_sportrecord {
	private long starttime ;
	private long endtime ;
	private int sporttype ;
	private int distance ;
	private int durationtime ;
	private int calorie ;
	private float avgspeed ;
	private String upload;
	private int mode;
	private int pace;
	int footNum = 0;
	public int getFootNum()
	{
		return footNum;
	}
	public void setFootNum(int footNum)
	{
		this.footNum = footNum;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
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
	public int getSporttype() {
		return sporttype;
	}
	public void setSporttype(int sporttype) {
		this.sporttype = sporttype;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getDurationtime() {
		return durationtime;
	}
	public void setDurationtime(int durationtime) {
		this.durationtime = durationtime;
	}
	public int getCalorie() {
		return calorie;
	}
	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}
	public float getAvgspeed() {
		return avgspeed;
	}
	public void setAvgspeed(float avgspeed) {
		this.avgspeed = avgspeed;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	public int getPace() {
		return pace;
	}
	public void setPace(int pace) {
		this.pace = pace;
	}
	
}
