package com.news.network

import com.news.model.News
import io.reactivex.Observable
import retrofit2.http.GET

interface RemoteNewsApi {
    @GET("top-headlines?country=us")
    fun getNews(): Observable<News>
}