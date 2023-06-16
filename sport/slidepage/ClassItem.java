package com.desay.sport.slidepage;

import java.io.Serializable;

public class ClassItem implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int classId = 0;
	public String classIcon = "";
	public String className = "";
	public int partId = 0;
	public String partName = "";
	public String complate;
	public String duration;
	public String tired;
	public boolean ifTop = false;
	public boolean ifBottom=false;
	public ClassItem() {
		super();
	}
	
	public ClassItem(String className,String complate,String duration,String tired, int partId,
			String partName) {
		super();
		this.className = className;
		this.partId = partId;
		this.partName = partName;
		this.complate=complate;
		this.duration=duration;
		this.tired=tired;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public int getPartId() {
		return partId;
	}
	public void setPartId(int partId) {
		this.partId = partId;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
	
	public boolean isIfBottom() {
		return ifBottom;
	}

	public void setIfBottom(boolean ifBottom) {
		this.ifBottom = ifBottom;
	}

	public String getComplate() {
		return complate;
	}

	public void setComplate(String complate) {
		this.complate = complate;
	}

	public String getTime() {
		return duration;
	}

	public void setTime(String duration) {
		this.duration = duration;
	}

	public String getTired() {
		return tired;
	}

	public void setTired(String tired) {
		this.tired = tired;
	}
	
	
}
