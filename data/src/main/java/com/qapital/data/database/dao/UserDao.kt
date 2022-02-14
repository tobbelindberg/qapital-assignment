package com.qapital.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.qapital.data.database.entity.UserEntity
import io.reactivex.rxjava3.core.Maybe

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user where id=:userId")
    fun loadUser(userId: Long): Maybe<UserEntity>
}