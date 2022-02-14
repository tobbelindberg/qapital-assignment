package com.qapital.di

import com.qapital.domain.services.UserDatabaseService
import dagger.Binds
import dagger.Module
import dagger.Reusable

/**
 * Provides mock Database services.
 */
@Module
abstract class TestDatabaseServiceModule {

    @Binds
    @Reusable
    abstract fun provideUserDatabaseService(service: MockUserRoomDatabaseService): UserDatabaseService
}