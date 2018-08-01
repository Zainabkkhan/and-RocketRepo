package com.dan.lnhospital.bean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MessageBean
{
    private String message;
    private String mFlag;
    private String docId;
    private String Id;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getmFlag() {
        return mFlag;
    }

    public void setmFlag(String mFlag) {
        this.mFlag = mFlag;
    }
}
