package com.dan.lnhospital.retroface;

import android.media.session.MediaSession;

import com.dan.lnhospital.bean.MessageBean;
import com.dan.lnhospital.bean.TokenBean;
import com.dan.lnhospital.bean.TokenDataReceiveBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface
{
    @GET("/dqms/MessageAPI?")
    Call<String> setMessageData(@Query(value = "a") String bean);

    @GET("/dqms/MessageDDAPI?")
    Call<String> setDisplayDelete(@Query(value = "a") String bean);

    @POST("dqms/TokenList?TokenDetail")
    Call<List<TokenDataReceiveBean>> getTokenList(@Body TokenBean bean);

}
