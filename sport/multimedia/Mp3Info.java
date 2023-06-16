package com.desay.sport.multimedia;

import java.io.Serializable;

public class Mp3Info  implements Serializable{//实现序列化，内存放到硬盘
	/**
	 * 通过网络发送
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String singer;
	private int time;
	private String musicName;
	private String musicPath;
	private String albumkey;
	
	
	public String getAlbumkey() {
		return albumkey;
	}
	public void setAlbumkey(String albumkey) {
		this.albumkey = albumkey;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}

	public int getId() {
		return id;
	}
	public Mp3Info() {
		super();
	}
	public void setId(int i) {
		this.id = i;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int i) {
		this.time = i;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getMusicPath() {
		return musicPath;
	}
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
}
