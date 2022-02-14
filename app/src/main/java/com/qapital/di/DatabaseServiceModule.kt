package com.qapital.di

import com.qapital.data.services.UserRoomDatabaseService
import com.qapital.domain.services.UserDatabaseService
import dagger.Binds
import dagger.Module
import dagger.Reusable

/**
 * Provides Database services.
 */
@Module
abstract class DatabaseServiceModule {

    @Binds
    @Reusable
    abstract fun provideUserDatabaseService(service: UserRoomDatabaseService): UserDatabaseService
}