package com.facehandsome.bean;

import java.util.ArrayList;

public class BarGraph {
	private ArrayList<String> labels;
	private ArrayList<Dataset> datasets;
	
	public ArrayList<String> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}
	public ArrayList<Dataset> getDatasets() {
		return datasets;
	}
	public void setDatasets(ArrayList<Dataset> datasets) {
		this.datasets = datasets;
	}
}
