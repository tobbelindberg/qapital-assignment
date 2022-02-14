package com.qapital.domain.services

import com.qapital.domain.model.User
import io.reactivex.rxjava3.core.Observable

interface UserDatabaseService {

    fun insertUser(user: User)

    fun getUser(userId: Long): Observable<User>
}