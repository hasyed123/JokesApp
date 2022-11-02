package com.example.jokesapp.network

import com.example.jokesapp.app.model.Joke
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JokesApi {

    @GET("/joke/{category}")
    suspend fun getJoke(
        @Path("category")
        category: String,
        @Query("blacklistFlags")
        blacklistFlags: String? = "nsfw,religious,political,racist,sexist,explicit",
        @Query("type")
        type: String? = null,
        @Query("contains")
        contains: String? = null
    ): Joke
}