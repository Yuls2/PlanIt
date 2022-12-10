package com.planitse2022.planit.util.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private static RetrofitAPI retrofitAPI;
    private static final String BASEURL = "https://planit2022.cafe24.com/";

    private RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
    }

    public static RetrofitClient getInstance() {
        if(instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static RetrofitAPI getRetrofitAPI() {
        return retrofitAPI;
    }

    public static String getBaseurl() {
        return BASEURL;
    }
    public static String getImageurl(String dir, String fileName, String extension) {
        return BASEURL + "images/" + dir + "/" + fileName + "." + extension;
    }
    public static String getImageurl(String dir, String fileName) {
        return getImageurl(dir,fileName,"png");
    }
}
