package com.app.cat.kevin.thecatapp.api.service;

import android.util.Log;

import com.app.cat.kevin.thecatapp.api.RetrofitInitializer;
import com.app.cat.kevin.thecatapp.model.Cat;
import com.app.cat.kevin.thecatapp.model.FavouriteRequest;
import com.app.cat.kevin.thecatapp.model.FavouriteResponse;
import com.app.cat.kevin.thecatapp.model.googleColorApiRequest.ImageColorRecognizeRequest;
import com.app.cat.kevin.thecatapp.model.googleColorApiResponse.ImageColorRecognizeResponse;
import com.app.cat.kevin.thecatapp.utils.RxFunctions;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;

public class CatApiService {

    private RetrofitInitializer retrofitInitializer;
    private static final String RETROFIT_ERROR = "ERROR";

    public CatApiService() {
        retrofitInitializer = new RetrofitInitializer();
    }

    public Single<Cat> getCatById(String id) {

        return retrofitInitializer
                .catApi()
                .getCat(id)
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e(RETROFIT_ERROR, "Api getCatById error " + throwable));
    }

    public Single<List<Cat>> getCatList(int page) {
        return retrofitInitializer
                .catApi()
                .getCatList(page, 10, "desc", "full")
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e(RETROFIT_ERROR, "Api getCatList error " + throwable));
    }

    public Single<FavouriteResponse> likeCat(FavouriteRequest favouriteRequest){
        return retrofitInitializer
                .catApi()
                .likeCat(favouriteRequest)
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e(RETROFIT_ERROR, "Api likeCat error " + throwable));
    }

    public  Single<ResponseBody> downloadCatImage(String url) {
        return retrofitInitializer
                .catApi()
                .downloadCatImage(url)
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e(RETROFIT_ERROR, "Api cat image error " + throwable));
    }

    public  Single<ImageColorRecognizeResponse> getImageColors(ImageColorRecognizeRequest imageColorRecognizeRequest) {
        return retrofitInitializer
                .catApi()
                .getImageColors("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyAuztboj9qXf9hEiMU2DTKWF6zc7CuVSZU", imageColorRecognizeRequest)
                .compose(RxFunctions.applySingleSchedulers())
                .doOnError( throwable -> Log.e(RETROFIT_ERROR, "Api cat image error " + throwable));
    }
}
