package com.facehandsome.bean;

public class Photo {
	private String name;
	private String descirption;
	private String path;
	private boolean handsome;
	private boolean isRandom;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isHandsome() {
		return handsome;
	}
	public void setHandsome(boolean handsome) {
		this.handsome = handsome;
	}
	public String getDescirption() {
		return descirption;
	}
	public void setDescirption(String descirption) {
		this.descirption = descirption;
	}
	public boolean isRandom() {
		return isRandom;
	}
	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}
}
