package com.teksen.newsapp.service;

import com.teksen.newsapp.NewsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NewsApiService {

    @GET("/api/v1/news")
    Call<List<NewsDTO>> getAllNews();

    @GET("/api/v1/news/{categoryName}")
    Call<List<NewsDTO>> findNewsByCategoryName(@Path("categoryName") String categoryName);

    @GET("/api/v1/news/fetch/{categoryName}")
    Call<List<NewsDTO>> findNewsBySearchQuery(@Path("categoryName") String categoryName);

    @GET("/api/v1/news/{id}")
    Call<NewsDTO> getNewsById(@Path(("id")) Long id);

    @POST("/api/v1/news")
    Call<NewsDTO> createNews(@Body NewsDTO newsDTO);

    @PUT("/api/v1/news")
    Call<NewsDTO> updateNews(@Body NewsDTO newsDTO);

    @DELETE("/api/v1/news/{id}")
    Call<String> deleteNews(@Path("id") Long id);
}
