package com.news.ui

import androidx.appcompat.app.AppCompatActivity
import com.news.R
import com.news.base.BaseRecyclerAdapter
import com.news.model.NewsPublisher

class NewsItemAdapter(activity : AppCompatActivity) : BaseRecyclerAdapter<NewsPublisher, NewsItemViewModel,com.news.databinding.NewsItemBinding>(activity) {


    override fun onItemBinding(binding: com.news.databinding.NewsItemBinding, viewModel: NewsItemViewModel, position: Int) {

    }

    override fun getViewModel(): NewsItemViewModel {
       return NewsItemViewModel()
    }

    override fun getLayoutRes(): Int {
       return R.layout.news_item
    }
}