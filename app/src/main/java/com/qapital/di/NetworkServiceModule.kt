package com.qapital.di

import com.qapital.data.services.ActivitiesNetworkService
import com.qapital.data.services.UserNetworkService
import com.qapital.domain.services.ActivitiesService
import com.qapital.domain.services.UserService
import dagger.Binds
import dagger.Module
import dagger.Reusable

/**
 * Provides Network services.
 */
@Module
abstract class NetworkServiceModule {

    @Binds
    @Reusable
    abstract fun provideActivitiesService(service: ActivitiesNetworkService): ActivitiesService

    @Binds
    @Reusable
    abstract fun provideUserService(service: UserNetworkService): UserService
}