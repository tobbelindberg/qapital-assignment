package com.qapital.domain.services

import com.qapital.domain.model.User
import io.reactivex.rxjava3.core.Observable

interface UserService {

    fun getUser(userId: Long): Observable<User>

}