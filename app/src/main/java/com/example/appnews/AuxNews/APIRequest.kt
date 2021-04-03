package com.example.appnews.AuxNews

import com.example.appnews.AuxNews.api.NewsApiJSON
import retrofit2.http.GET

interface APIRequest {
    @GET("/v1/latest-news?country=ES&language=es&apiKey=ogNGuP76wuSgH33lpP-3peLhFK4OMsCU0anyd7QqnuXqG6ET")
    suspend fun getNews(): NewsApiJSON
}