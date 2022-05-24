package com.news.base


abstract class BaseRecyclerItemViewModel<M : BaseModel> : BaseViewModel() {

    lateinit var model: M

    abstract fun setValues()

    open fun clearCache() {}

}