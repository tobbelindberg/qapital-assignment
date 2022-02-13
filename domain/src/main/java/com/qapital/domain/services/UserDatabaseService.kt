package com.qapital.domain.services

import com.qapital.domain.model.User
import io.reactivex.rxjava3.core.Observable

interface UserDatabaseService {

    fun insertUser(user: User): Observable<Long>

    fun getUser(userId: Long): Observable<Pair<Long, User>>
}