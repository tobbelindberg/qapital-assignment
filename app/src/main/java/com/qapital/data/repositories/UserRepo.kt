package com.qapital.data.repositories

import com.qapital.domain.model.User
import com.qapital.domain.services.UserDatabaseService
import com.qapital.domain.services.UserService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In the repos is where I would also inject a cache that would cache the repositories and such.
 */
@Singleton
class UserRepo
@Inject constructor(private val userService: UserService, private val userDatabaseService: UserDatabaseService) {

    fun getUser(userId: Long): Observable<User> {
        return userDatabaseService.getUser(userId)
            .concatWith(fetchUser(userId))
            .firstElement()
            .toObservable()
    }

    private fun fetchUser(userId: Long) = userService.getUser(userId)
        .doOnNext(userDatabaseService::insertUser)
}