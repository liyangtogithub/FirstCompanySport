package com.desay.sport.update;

import java.io.Serializable;

/**
 * 文件信息类
 * @author jh
 *
 */
public class FileInfo implements Serializable{
	private static final long serialVersionUID = 434718480503585579L;
	public final static int UPDATE_START = 1;
	public final static int UPDATE_PROCESS = 2;
	public final static int UPDATE_END = 3;
	private String fileName;
	private String filePath;
	private String packageName;
	private int fileType;
	private int version;
	private String versionName;
	private int newVersion;
	private String downUrl;
	private int updatestate;
	public int getUpdatestate() {
		return updatestate;
	}
	public void setUpdatestate(int updatestate) {
		this.updatestate = updatestate;
	}
	public String getDownUrl() {
		return downUrl;
	}
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public boolean isUpdate(){
		return newVersion>version;
	}
	
	@Override
	public String toString() {
		return this.filePath;
	}
}
