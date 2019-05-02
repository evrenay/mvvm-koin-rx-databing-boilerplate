package com.news

import android.app.Application
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication :  Application() {




    override fun onCreate() {
        super.onCreate()

        loadKoin()


        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Quicksand-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());


    }


    private fun loadKoin() {
   startKoin { androidContext(this@NewsApplication)
       modules(appModule) }


    }








}