package com.news.utils

import com.news.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateCalculator {

    private val SECONDS_OF_MINUTES = 60
    private val MINUTES_OF_HOUR = 60
    private val HOURS_OF_DAY = 24
    private val DAYS_OF_MONTHS = 30


    private val SERVER_DIFFERENCE = 3

    val HOUR = (3600 * 1000).toLong()

    fun calculateTime(
        resourceProvider: ResourceProvider,
        time: String,
        serverTime: String,
        format: String
    ): String {
        try {
            val sdf = object : SimpleDateFormat(format) {
                override fun format(
                    date: Date,
                    toAppendTo: StringBuffer,
                    pos: java.text.FieldPosition
                ): StringBuffer {
                    val toFix = super.format(date, toAppendTo, pos)
                    return toFix.insert(toFix.length - 2, ':')
                }
            }
            val createTime = sdf.parse(time)
            val now = sdf.parse(serverTime)


            val difference = now!!.time - createTime!!.time

            val secondCount = TimeUnit.SECONDS.convert(difference, TimeUnit.MILLISECONDS).toInt()
            if (secondCount < SECONDS_OF_MINUTES)
                return resourceProvider.getString(R.string.seconds_ago, secondCount.toString())

            val minuteCount = TimeUnit.MINUTES.convert(difference, TimeUnit.MILLISECONDS).toInt()
            if (minuteCount < MINUTES_OF_HOUR)
                return resourceProvider.getString(R.string.minutes_ago, minuteCount.toString())

            val hourCount = TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS).toInt()
            if (hourCount < HOURS_OF_DAY)
                return resourceProvider.getString(R.string.hours_ago, hourCount.toString())
            val dayCount = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS).toInt()
            if (dayCount < DAYS_OF_MONTHS)
                return resourceProvider.getString(R.string.days_ago, dayCount.toString())

            val monthCount = dayCount / 30
            return resourceProvider.getString(R.string.months_ago, monthCount.toString())

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return ""
    }
}
