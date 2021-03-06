package com.app.cat.kevin.thecatapp.api;

import com.app.cat.kevin.thecatapp.model.Cat;
import com.app.cat.kevin.thecatapp.model.FavouriteRequest;
import com.app.cat.kevin.thecatapp.model.FavouriteResponse;
import com.app.cat.kevin.thecatapp.model.googleColorApiRequest.ImageColorRecognizeRequest;
import com.app.cat.kevin.thecatapp.model.googleColorApiResponse.ImageColorRecognizeResponse;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface CatApi {

    @GET("v1/images/search")
    Single<List<Cat>> getCatList(@Query("page") int page, @Query("limit") int limit, @Query("order") String order, @Query("size") String size);

    @GET("v1/images/{catId}")
    Single<Cat> getCat(@Path("catId") String catId);

    @POST("v1/favourites/")
    Single<FavouriteResponse> likeCat(@Body FavouriteRequest favouriteRequest);

    @GET
    Single<ResponseBody> downloadCatImage(@Url String fileUrl);

    @POST
    Single<ImageColorRecognizeResponse> getImageColors(@Url String fileUrl, @Body ImageColorRecognizeRequest imageColorRecognizeRequest);

}