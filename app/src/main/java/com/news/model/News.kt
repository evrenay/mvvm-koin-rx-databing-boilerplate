package com.news.model

import com.news.base.BaseModel
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("status") var status: String? = null,
    @SerializedName("articles") var articles: List<NewsPublisher> = emptyList()
) : BaseModel()