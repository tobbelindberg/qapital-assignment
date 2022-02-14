package com.qapital.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qapital.data.database.dao.UserDao
import com.qapital.data.database.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(appContext: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    appContext,
                    AppDatabase::class.java,
                    "qapital_database"
                )
                    .build().also { INSTANCE = it }
            }
        }
    }
}