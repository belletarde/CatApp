package com.app.cat.kevin.thecatapp.api;

import com.app.cat.kevin.thecatapp.model.Cat;
import com.app.cat.kevin.thecatapp.model.FavouriteRequest;
import com.app.cat.kevin.thecatapp.model.FavouriteResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CatApi {

    @GET("v1/images/search")
    Single<List<Cat>> getCatList(@Query("page") int page, @Query("limit") int limit, @Query("order") String order, @Query("size") String size);

    @GET("v1/images/{catId}")
    Single<Cat> getCat(@Path("catId") String catId);

    @POST("v1/favourites/")
    Single<FavouriteResponse> likeCat(@Body FavouriteRequest favouriteRequest);

}