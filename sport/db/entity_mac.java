package com.desay.sport.db;

public class entity_mac {
	private String mac =null;
	private String upload =null;
	private String syntime=null;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getUpLoad() {
		return upload;
	}
	
	public void setUpLoad(String upload) {
		this.upload = upload;
	}
	public void setSyntime(String syntime){
		this.syntime=syntime;
	}
	public String getSynTime(){
		return syntime;
	}
	
}
