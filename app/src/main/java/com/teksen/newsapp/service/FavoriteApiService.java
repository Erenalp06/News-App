package com.teksen.newsapp.service;

import com.teksen.newsapp.NewsDTO;
import com.teksen.newsapp.dto.FavoriteDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteApiService {

    @GET("/api/v1/favorites/{email}")
    Call<List<NewsDTO>> getFavoritesByEmail(@Path("email") String email);

    @POST("/api/v1/favorites/check")
    Call<Boolean> checkNewsForFavorite(@Body FavoriteDTO favoriteDTO);

    @POST("/api/v1/favorites")
    Call <FavoriteDTO> createFavoriteForUser(@Body FavoriteDTO favoriteDTO);
}
