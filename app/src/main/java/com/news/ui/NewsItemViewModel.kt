package com.news.ui

import com.news.base.BaseRecyclerItemViewModel
import com.news.model.NewsPublisher

class NewsItemViewModel : BaseRecyclerItemViewModel<NewsPublisher>() {

    var article : String ?= null

    override fun setValues() {
        article = model.description
    }
}