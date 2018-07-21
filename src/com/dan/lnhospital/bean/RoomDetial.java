package com.dan.lnhospital.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomDetial implements Serializable{

	
	private int room_id;
	private String room_num;
	private String dept_name;
	
	
	public RoomDetial(int room_id, String room_num, String dept_name) {
		super();
		this.room_id = room_id;
		this.room_num = room_num;
		this.dept_name = dept_name;
	}
	public int getRoom_id() {
		return room_id;
	}
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}
	public String getRoom_num() {
		return room_num;
	}
	public void setRoom_num(String room_num) {
		this.room_num = room_num;
	}
	public String getDept_name() {
		return dept_name;
	}
	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}
	
	
	
	
}
