package com.app.cat.kevin.thecatapp.api;


import android.content.res.Resources;


import com.app.cat.kevin.thecatapp.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInitializer {

    private Retrofit retrofit;

    public RetrofitInitializer() {

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(5, TimeUnit.SECONDS);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request =
                            chain
                            .request()
                            .newBuilder()
                            .addHeader(
                                    Resources.getSystem().getString(R.string.api_key_name),
                                    Resources.getSystem().getString(R.string.api_key_value)).build();

            return chain.proceed(request);
        });
        OkHttpClient client = builder.build();

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
}
