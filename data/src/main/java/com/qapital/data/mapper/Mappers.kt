package com.qapital.data.mapper

import com.qapital.data.network.model.ActivitiesDTO
import com.qapital.data.network.model.ActivityDTO
import com.qapital.data.network.model.UserDTO
import com.qapital.domain.model.Activities
import com.qapital.domain.model.Activity
import com.qapital.domain.model.User

fun ActivityDTO.toDomain() = Activity(message, amount, userId, timestamp)

fun UserDTO.toDomain() = User(userId, displayName, avatarUrl)

fun ActivitiesDTO.toDomain() = Activities(oldest, activities.map { it.toDomain() })