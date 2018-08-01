package com.dan.lnhospital.retroface;

import com.dan.lnhospital.bean.MessageBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface
{
    @GET("/dqms/MessageAPI?")
    Call<String> setMessageData(@Query(value = "a") String bean);

    @GET("/dqms/MessageDDAPI?")
    Call<String> setDisplayDelete(@Query(value = "a") String bean);
}
