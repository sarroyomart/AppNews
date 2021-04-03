package com.example.appnews.AuxNews.api

data class NewsApiJSON(
    val news: List<New>,
    val page: Int,
    val status: String
)