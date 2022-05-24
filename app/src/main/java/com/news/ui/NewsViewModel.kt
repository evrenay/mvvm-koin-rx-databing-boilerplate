package com.news.ui

import androidx.lifecycle.MutableLiveData
import com.news.base.BaseViewModel
import com.news.model.NewsPublisher
import com.news.network.RemoteNewsApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsViewModel(private  val newsApi: RemoteNewsApi) : BaseViewModel() {


    val newsLiveData = MutableLiveData<List<NewsPublisher>>()


    fun getNews(){
        compositeDisposable.add(newsApi
                .getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                  //  loadingStatus.value = true
                }
                .doOnTerminate {
                   // loadingStatus.value = false
                }
                .subscribe(
                        {
                            response  ->
                            if(!response.status.equals("ok")){
                                errorString.value = response.status
                                return@subscribe
                            }

                           newsLiveData.value = response.articles






                        },
                        {

                        }
                ))
    }
}