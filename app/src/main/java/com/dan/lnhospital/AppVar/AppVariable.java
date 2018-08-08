package com.dan.lnhospital.AppVar;

import com.dan.lnhospital.CustomDialog;
import com.dan.lnhospital.IpConfigDialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppVariable {
	
	 private static String IP ="192.168.1.1";//Server IP
	private static int type ; // list type 0 = fresh, 1 =Skip
	private static int docid;
	private static int calltoken; // call token for Skiplist
	private static String username;
	private static String password;
	private static String roomid;
	private static String callButtonStat = "Call";
	private static String SkipListButtonStat ="Skip List";
	private static String listtype = "Fresh List";
	private static CustomDialog dialog;
	private static IpConfigDialog dialog1;
	public static String  LOGINFLAG ="1"; 
	public static String  CALLFLAG="2";
	public static String  TREATFLAG="3";
	public static String   SKIPFLAG="4";
	public static String   SUMMARYFLAG="6";
	public static String   SKIPLISTFLAG="15";
	public static String   PATIENTDETAIL="16";
	public static String   CANCELFLAG = "17";
	public static boolean callStat;  // for removing repetition of Call button click in slow internet
	private final static String passwordIpconfig="dqms12345678";
	

	public static String getPasswordipconfig() {
		return passwordIpconfig;
	}

	public static IpConfigDialog getDialog1() {
		return dialog1;
	}

	public static void setDialog1(IpConfigDialog dialog1) {
		AppVariable.dialog1 = dialog1;
	}

	public static boolean isCallStat() {
		return callStat;
	}

	public static void setCallStat(boolean callStat) {
		AppVariable.callStat = callStat;
	}

	public static CustomDialog getDialog() {
		return dialog;
	}

	public static void setDialog(CustomDialog dialog) {
		AppVariable.dialog = dialog;
	}

	public static String getListtype() {
		return listtype;
	}

	public static void setListtype(String listtype) {
		AppVariable.listtype = listtype;
	}

	public static String getCallButtonStat() {
		return callButtonStat;
	}

	public static void setCallButtonStat(String callButtonStat) {
		AppVariable.callButtonStat = callButtonStat;
	}

	
	public static String getSkipListButtonStat() {
		return SkipListButtonStat;
	}

	public static void setSkipListButtonStat(String skipListButtonStat) {
		SkipListButtonStat = skipListButtonStat;
	}

	public static String getRoomid() {
		return roomid;
	}

	public static void setRoomid(String roomid) {
		AppVariable.roomid = roomid;
	}

	public static int getDocid() {
		return docid;
	}

	public static void setDocid(int docid) {
		AppVariable.docid = docid;
	}

	public static int getType() {
		return type;
	}

	public static void setType(int type) {
		AppVariable.type = type;
	}

	
	public static int getCalltoken() {
		return calltoken;
	}

	public static void setCalltoken(int calltoken) {
		AppVariable.calltoken = calltoken;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		AppVariable.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		AppVariable.password = password;
	}
	
	public static String getIP() {
		return IP;
	}

	public static void setIP(String iP) {
		IP = iP;
	}
}
