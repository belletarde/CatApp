package com.app.cat.kevin.thecatapp.api.service;


import android.content.Context;
import android.util.Log;

import com.app.cat.kevin.thecatapp.api.RetrofitInitializer;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.app.cat.kevin.thecatapp.model.FavouriteRequest;
import com.app.cat.kevin.thecatapp.model.FavouriteResponse;
import com.app.cat.kevin.thecatapp.utils.RxFunctions;

import java.util.List;

import io.reactivex.Single;


public class CatApiService {

    private RetrofitInitializer retrofitInitializer;

    public CatApiService() {
        retrofitInitializer = new RetrofitInitializer();
    }

    public Single<Cat> getCatById(String id) {

        return retrofitInitializer
                .catApi()
                .getCat(id)
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e("ERROR", ""));
    }

    public Single<List<Cat>> getCatList(int page) {
        return retrofitInitializer
                .catApi()
                .getCatList(page, 10, "desc", "full")
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError(throwable -> Log.e("ERROR", ""));
    }

    public Single<FavouriteResponse> likeCat(FavouriteRequest favouriteRequest){
        return retrofitInitializer
                .catApi()
                .likeCat(favouriteRequest)
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e("ERROR", ""));
    }
}
