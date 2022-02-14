package com.qapital.data.services

import com.qapital.data.database.AppDatabase
import com.qapital.data.database.entity.UserEntity
import com.qapital.domain.model.User
import com.qapital.domain.services.UserDatabaseService
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UserRoomDatabaseService
@Inject constructor(
    appDatabase: AppDatabase,
) : UserDatabaseService {

    companion object {
        private const val EXPIRE_IN_MILLIS = 86400000L // 24h
    }

    private val userDao = appDatabase.userDao()

    override fun insertUser(user: User) {
        userDao.insertUser(UserEntity.fromDomain(user))
    }

    override fun getUser(userId: Long): Observable<User> {
        return userDao.loadUser(userId)
            .filter { isNotExpired(it.timestamp) }
            .map { it.toDomain() }
            .toObservable()
    }

    private fun isNotExpired(timestamp: Long) = (System.currentTimeMillis() - timestamp) < EXPIRE_IN_MILLIS
}