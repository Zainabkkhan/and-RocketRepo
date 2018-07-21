package com.dan.lnhospital.bean;

public class PateintDetailHelper {

	
	private String pateint_name;
	private int  staus;
	private int token_num;
	
	
	public PateintDetailHelper(String pateint_name, int staus, int token_num) {
		
		this.pateint_name = pateint_name;
		this.staus = staus;
		this.token_num = token_num;
	}
	
	public String getPateint_name() {
		return pateint_name;
	}
	public void setPateint_name(String pateint_name) {
		this.pateint_name = pateint_name;
	}
	public int getStaus() {
		return staus;
	}
	public void setStaus(int staus) {
		this.staus = staus;
	}
	public int getToken_num() {
		return token_num;
	}
	public void setToken_num(int token_num) {
		this.token_num = token_num;
	} 
}
