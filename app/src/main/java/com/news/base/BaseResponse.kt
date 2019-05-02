package com.news.base

import com.google.gson.annotations.SerializedName


data class BaseResponse<T> (
        @SerializedName(value="value") val model : T?,
        @SerializedName(value = "error")val isError:Boolean,
        @SerializedName(value="code")val code : Int,
        @SerializedName(value="message")val message :String
)

