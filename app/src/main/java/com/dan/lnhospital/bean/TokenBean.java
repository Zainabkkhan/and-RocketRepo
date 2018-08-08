package com.dan.lnhospital.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenBean
{

    private int user_id;

    private String tFlag;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String gettFlag() {
        return tFlag;
    }

    public void settFlag(String tFlag) {
        this.tFlag = tFlag;
    }

}
