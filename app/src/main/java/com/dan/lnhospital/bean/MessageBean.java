package com.dan.lnhospital.bean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MessageBean
{
//    @POST("/dqms/Meassage?mess=")
//    Call<String> sendAttendanceserver(@Body MessageBean bean);
    private String mess;
   // private int mFlag;

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

//    public int getmFlag() {
//        return mFlag;
//    }
//
//    public void setmFlag(int mFlag) {
//        this.mFlag = mFlag;
//    }
}
