package com.news.base

import androidx.lifecycle.*
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel(), Observable, LifecycleObserver {

    var swipeLoadingStatus = MutableLiveData<Boolean>()

    var loadingStatus = MutableLiveData<Boolean>()

    val errorString = MutableLiveData<String>()

    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    protected val compositeDisposable = CompositeDisposable()


    open fun onRefresh() {

    }

    fun changeLoadingStatus(status: Boolean) {
        loadingStatus.value = status
        loadingStatus.value = loadingStatus.value
    }


    override fun addOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(
        callback: Observable.OnPropertyChangedCallback
    ) {
        callbacks.remove(callback)
    }


    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }


    fun notifyPropertyChanged(fieldId: Int) {
        callbacks.notifyCallbacks(this, fieldId, null)
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unSubscribeViewModel() {
        compositeDisposable.dispose()
    }

}