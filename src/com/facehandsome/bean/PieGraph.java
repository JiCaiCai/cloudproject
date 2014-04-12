package com.facehandsome.bean;

public class PieGraph {
	private int value;
	private String color;
	
	public PieGraph(int val, String col) {
		this.value = val;
		this.color = col;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
