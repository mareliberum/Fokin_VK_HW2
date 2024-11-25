package com.example.hw2.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{
    @GET("v1/gifs/trending")
    suspend fun getTrendigGifs(
        @Query("api_key") apiKey : String,
        @Query("limit") limit : Int = 10,
        @Query("offset") offset: Int = 0
    ) : GiphyResponse
}