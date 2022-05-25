package com.news.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.news.R
import com.news.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class NewsActivity : BaseActivity<NewsViewModel, com.news.databinding.NewsArticlesBinding>() {

    private val newsList: NewsViewModel by viewModel()

    override fun getViewModel(): Class<NewsViewModel> {
        return NewsViewModel::class.java
    }

    override fun getLayoutRes(): Int {
        return R.layout.news_articles
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = newsList
        super.onCreate(savedInstanceState)

        binding.adapter = NewsItemAdapter(this)
        binding.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        newsList.getNews()

        newsList.newsLiveData.observe(this) { list ->
            if (list != null) {
                binding.adapter?.update(list)
            }

        }
    }

}
