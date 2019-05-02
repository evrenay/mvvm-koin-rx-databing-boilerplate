package com.news.model

import com.news.base.BaseModel
import com.google.gson.annotations.SerializedName

data class NewsPublisher(
        @SerializedName("name") var name: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("url") var url: String? = null,
        @SerializedName("category") var category: String? = null) : BaseModel()