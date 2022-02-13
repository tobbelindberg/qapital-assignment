package com.qapital.data.repositories

import com.qapital.domain.model.Activities
import com.qapital.domain.services.ActivitiesService
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In the repos is where I would also inject a cache that would cache the repositories and such.
 */
@Singleton
class ActivitiesRepo
@Inject constructor(private val activitiesService: ActivitiesService) {

    fun getActivities(from: Date, to: Date): Observable<Activities> {
        return activitiesService.getActivities(from, to)
    }
}