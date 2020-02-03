package com.alim.ssn.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static Retrofit retrofit=null;
    private static final String baseUrl="http://192.168.1.37/blog/public/api/";

    public static Retrofit getRetrofit() {
        if (retrofit==null){

            Gson gson=new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit=new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(baseUrl)
                    .build();
        }
        return retrofit;
    }
}
