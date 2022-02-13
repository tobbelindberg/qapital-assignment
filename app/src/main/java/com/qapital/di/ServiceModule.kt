package com.qapital.di

import com.qapital.data.services.ActivitiesNetworkService
import com.qapital.data.services.UserNetworkService
import com.qapital.data.services.UserRoomDatabaseService
import com.qapital.domain.services.ActivitiesService
import com.qapital.domain.services.UserDatabaseService
import com.qapital.domain.services.UserService
import dagger.Binds
import dagger.Module
import dagger.Reusable

/**
 * Provides Network and database services.
 */
@Module
abstract class ServiceModule {

    @Binds
    @Reusable
    abstract fun provideActivitiesService(service: ActivitiesNetworkService): ActivitiesService

    @Binds
    @Reusable
    abstract fun provideUserService(service: UserNetworkService): UserService

    @Binds
    @Reusable
    abstract fun provideUserDatabaseService(service: UserRoomDatabaseService): UserDatabaseService
}