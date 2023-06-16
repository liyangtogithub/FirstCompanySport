package com.desay.sport.plan;

import java.util.ArrayList;
import java.util.List;

public class planInfo {
	private   int durationtime ;	
	private String tired ;
	private List<planInfo_child> childplan;

	public int getDurationtime() {
		return durationtime;
	}

	public void setDurationtime(int durationtime) {
		this.durationtime = durationtime;
	}

	public String getTired() {
		return tired;
	}

	public void setTired(String tired) {
		this.tired = tired;
	}

	public List<planInfo_child> getChildplan() {
		return childplan;
	}

	public void setChildplan(List<planInfo_child> childplan) {
		this.childplan = childplan;
	}
	
}
