package com.qapital.utils

import java.util.*


fun Date.twoWeeksAgo(): Date {
    val theDate = this

    return Calendar.getInstance().apply {
        time = theDate
        add(Calendar.WEEK_OF_YEAR, -2)
    }.time
}