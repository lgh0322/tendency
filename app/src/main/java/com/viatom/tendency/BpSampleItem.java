package com.viatom.tendency;

import java.util.ArrayList;
import java.util.Date;

public class BpSampleItem {
	private Date date;
	private ArrayList<Float> sysList = new ArrayList<>();
	private ArrayList<Float> disList = new ArrayList<>();

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ArrayList<Float> getSysList() {
		return sysList;
	}

	public ArrayList<Float> getDisList() {
		return disList;
	}

	public Float getMaxSys() {
		return sysList.parallelStream().max(Float::compareTo).orElse(-50.0f);
	}

	public Float getMinSys() {
		return sysList.parallelStream().min(Float::compareTo).orElse(-50.0f);
	}

	public Float getMaxDia() {
		return disList.parallelStream().max(Float::compareTo).orElse(-50.0f);
	}

	public Float getMinDia() {
		return disList.parallelStream().min(Float::compareTo).orElse(-50.0f);
	}

	public void addSys(float sysValue) {
		sysList.add(sysValue);
	}

	public void addDis(float disValue) {
		disList.add(disValue);
	}
}
