package com.news

import com.news.di.createNetworkClient
import com.news.network.RemoteNewsApi
import com.news.ui.NewsViewModel
import com.news.utils.BASE_URL
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single { createNetworkClient(BASE_URL) }
    single { (get() as Retrofit).create(RemoteNewsApi::class.java) }
    viewModel {
        NewsViewModel(get())
    }
}