package com.qapital.di

import com.qapital.domain.model.User
import com.qapital.domain.services.UserDatabaseService
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MockUserRoomDatabaseService
@Inject constructor() : UserDatabaseService {

    override fun insertUser(user: User) {}

    override fun getUser(userId: Long): Observable<User> {
        return Maybe.empty<User>().toObservable()
    }

}