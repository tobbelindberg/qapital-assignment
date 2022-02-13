package com.qapital.domain.services

import com.qapital.domain.model.Activities
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*

interface ActivitiesService {

    fun getActivities(from: Date, to: Date): Single<Activities>

}