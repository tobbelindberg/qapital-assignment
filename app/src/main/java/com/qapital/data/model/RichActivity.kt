package com.qapital.data.model

import com.qapital.data.TimestampItem
import com.qapital.domain.model.Activity
import com.qapital.domain.model.User
import java.util.*

data class RichActivity(val activity: Activity, val user: User, override val timestamp: Date) : TimestampItem