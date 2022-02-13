package com.qapital.data.repositories

import com.qapital.domain.model.User
import com.qapital.domain.services.UserDatabaseService
import com.qapital.domain.services.UserService
import com.qapital.utils.loge
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In the repos is where I would also inject a cache that would cache the repositories and such.
 */
@Singleton
class UserRepo
@Inject constructor(private val userService: UserService, private val userDatabaseService: UserDatabaseService) {

    companion object {
        private const val ONE_DAY_MILLIS = 86400000L
    }
    fun getUser(userId: Long): Observable<User> {
        return userDatabaseService.getUser(userId)
            .onErrorResumeNext{
                it.loge("*HOHA*")
                fetchUser(userId)
                    .map { System.currentTimeMillis() to it }
            }
            .flatMap {
                if((System.currentTimeMillis() - it.first) >  ONE_DAY_MILLIS){
                    fetchUser(userId)
                } else {
                    Observable.just(it.second)
                }
            }
    }

    private fun fetchUser(userId: Long) = userService.getUser(userId)
        .flatMap {user ->
            userDatabaseService.insertUser(user)
                .map {
                    user
                }
        }
}