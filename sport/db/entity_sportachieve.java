package com.desay.sport.db;

import java.io.Serializable;
public class entity_sportachieve implements Serializable{
	private long starttime ;
	private int achievetype ;
	private int achievename ;
	private int achieverecord ;
	private String upload;
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public int getAchievetype() {
		return achievetype;
	}
	public void setAchievetype(int achievetype) {
		this.achievetype = achievetype;
	}
	public int getAchievename() {
		return achievename;
	}
	public void setAchievename(int achievename) {
		this.achievename = achievename;
	}
	public int getAchieverecord() {
		return achieverecord;
	}
	public void setAchieverecord(int achieverecord) {
		this.achieverecord = achieverecord;
	}
	public String getUpload() {
		return upload;
	}
	public void setUpload(String upload) {
		this.upload = upload;
	}
	
}
