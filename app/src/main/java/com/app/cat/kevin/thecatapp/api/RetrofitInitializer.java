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

    public RetrofitInitializer() {
        OkHttpClient client = this.createHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com/")
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
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .addHeader( "x-api-key","04bfce0a-a91a-49f4-94b7-e0c67b0ff49a")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .cache(null).build();
    }
}
