package com.qapital.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qapital.domain.model.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: Long = 0,
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
) {

    fun toDomain() = User(id, displayName, avatarUrl)

    companion object {

        fun fromDomain(user: User) = UserEntity(
            user.userId,
            user.displayName,
            user.avatarUrl,
            System.currentTimeMillis()
        )
    }
}