package com.himanshurawat.goldenhour.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NetworkService {


    @GET("api/place/findplacefromtext/jsoninputtype=textquery&fields=name,geometry&key=AIzaSyDBl6vIZYyyN4xGpQElinzhBUWbWU9LoT8")
    fun getPlaces(@Query("input")query: String): Call<SearchResponse>

}