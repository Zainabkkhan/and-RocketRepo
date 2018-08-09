package com.dan.lnhospital.bean;

import com.google.gson.annotations.SerializedName;

public class TokenDataReceiveBean {

    @SerializedName("tokenno")
    private String tokenno;

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    @Override
    public String toString() {
//        return super.toString();
        return tokenno;
    }

}
