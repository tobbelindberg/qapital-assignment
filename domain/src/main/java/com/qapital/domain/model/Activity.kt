package com.qapital.domain.model

import java.util.*

data class Activity(
    val message: String,
    val amount: Double,
    val userId: Long,
    val timestamp: Date
)