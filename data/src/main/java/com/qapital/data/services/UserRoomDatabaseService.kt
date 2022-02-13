package com.qapital.data.services

import android.util.Log
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

    private val userDao = appDatabase.userDao()

    override fun insertUser(user: User): Observable<Long> {
        Log.d("HOHA", "*HOHA* about to insert")
        return userDao.insertUser(UserEntity.fromDomain(user))
            .doOnError {
                Log.e("HOHA", "*HOHA*", it)
            }
            .toObservable()
            .map {
                Log.d("HOHA", "*HOHA* ${it}")

                it
            }
    }

    override fun getUser(userId: Long): Observable<Pair<Long, User>> {
        return userDao.loadUser(userId)
            .map {
                it.timestamp to it.toDomain()
            }.toObservable()
    }
}