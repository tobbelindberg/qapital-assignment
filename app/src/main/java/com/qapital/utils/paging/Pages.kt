package com.qapital.utils.paging

import java.util.*

class Pages(
    var firstPageToLoad: Pair<Date, Date>,
    var nextPageToLoad: Pair<Date, Date>, var hasMorePages: Boolean = true, var oldest: Date = Calendar.getInstance().apply {
        timeInMillis = 0
    }.time
)