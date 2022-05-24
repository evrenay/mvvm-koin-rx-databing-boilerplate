package com.news

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication :  Application() {




    override fun onCreate() {
        super.onCreate()

        loadKoin()



    }


    private fun loadKoin() {
   startKoin { androidContext(this@NewsApplication)
       modules(appModule) }


    }








}