package com.dan.lnhospital.retroface;

import com.dan.lnhospital.AppVar.AppVariable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zainab on 11/1/2017.
 */

public class RetroWraper
{
   private static final String IP_AUTO = "http://192.168.1.127:8080/"; //cloud api


    public static ApiInterface getRetroService()
    {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(150, TimeUnit.SECONDS)
                .connectTimeout(150, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_AUTO)

                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(ApiInterface.class);
    }

}
