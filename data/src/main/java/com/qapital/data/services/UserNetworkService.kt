package com.qapital.data.services

import com.qapital.data.mapper.toDomain
import com.qapital.data.network.Endpoints
import com.qapital.domain.model.User
import com.qapital.domain.services.UserService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UserNetworkService
@Inject constructor(private val restEndpoints: Endpoints) : UserService {

    override fun getUser(userId: Long): Observable<User> {
        return restEndpoints.getUser(userId)
            .map { it.toDomain() }
    }
}