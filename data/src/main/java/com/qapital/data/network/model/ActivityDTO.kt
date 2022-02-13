package com.qapital.data.network.model

import java.util.*

data class ActivityDTO(
    val message: String,
    val amount: Double,
    val userId: Long,
    val timestamp: Date,
)