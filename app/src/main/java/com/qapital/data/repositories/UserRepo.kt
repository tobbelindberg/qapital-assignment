package com.qapital.data.repositories

import com.qapital.domain.model.User
import com.qapital.domain.services.UserService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In the repos is where I would also inject a cache that would cache the repositories and such.
 */
@Singleton
class UserRepo
@Inject constructor(private val userService: UserService) {

    fun getUser(userId: Long): Observable<User> {
        return userService.getUser(userId)
    }
}