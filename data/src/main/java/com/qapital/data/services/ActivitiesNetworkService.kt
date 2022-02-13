package com.qapital.data.services

import com.qapital.data.mapper.toDomain
import com.qapital.data.network.Endpoints
import com.qapital.domain.model.Activities
import com.qapital.domain.services.ActivitiesService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

class ActivitiesNetworkService
@Inject constructor(
    private val restEndpoints: Endpoints,
) : ActivitiesService {

    override fun getActivities(from: Date, to: Date): Single<Activities> {
        return restEndpoints.getActivities(from, to)
            .map {
                it.toDomain()
            }
    }
}