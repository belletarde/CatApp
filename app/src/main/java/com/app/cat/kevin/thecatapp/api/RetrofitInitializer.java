package com.app.cat.kevin.thecatapp.api;


import android.content.Context;
import android.content.res.Resources;


import com.app.cat.kevin.thecatapp.BuildConfig;
import com.app.cat.kevin.thecatapp.R;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInitializer {

    private Retrofit retrofit;

    private static final String BASE_URL = "https://api.thecatapi.com/";
    private static final String TOKEN_KEY = "x-api-key";
    private static final String TOKEN_VALUE = "04bfce0a-a91a-49f4-94b7-e0c67b0ff49a";
    private static final long TIME_OUT = 60L;

    public RetrofitInitializer() {
        OkHttpClient client = this.createHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public CatApi catApi() {
        return retrofit.create(CatApi.class);
    }

    private OkHttpClient createHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return (new OkHttpClient.Builder())
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .addHeader( TOKEN_KEY,TOKEN_VALUE)
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .cache(null).build();
    }
}
