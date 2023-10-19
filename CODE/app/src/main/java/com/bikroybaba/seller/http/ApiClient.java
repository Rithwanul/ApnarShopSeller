package com.bikroybaba.seller.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // live server
//    public static final String BASE_URL = "http://120.50.7.26:80/";
    // test server
    public static final String BASE_URL = "http://116.212.109.34:80/";
    private static Retrofit retrofit = null;

    public static Retrofit getBaseClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd' 'HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

}
