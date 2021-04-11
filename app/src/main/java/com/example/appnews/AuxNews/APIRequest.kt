package com.example.appnews.AuxNews

import android.content.res.Resources
import com.example.appnews.AuxNews.api.NewsApiJSON
import com.example.appnews.R
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface APIRequest {

    //@GET("{link}")
    @GET
    suspend fun getNews(@Url url:String): NewsApiJSON

    /*@GET(String.get(link)
    suspend fun getNews(): NewsApiJSON*/


}