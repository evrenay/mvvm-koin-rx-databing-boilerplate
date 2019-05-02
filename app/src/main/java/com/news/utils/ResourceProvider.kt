package com.news.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

class ResourceProvider(val context: Context) {

    //TODO merge this class with ResourceUtil class

    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, value: String): String {
        return context.getString(resId, value)
    }

    fun getString(resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }



    fun getStringArray(resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    fun getColor(resId: Int): Int {
        return context.resources.getColor(resId)
    }


    fun getColorArray(resId: Int): TypedArray {
        return context.getResources().obtainTypedArray(resId);
    }

    fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }



}