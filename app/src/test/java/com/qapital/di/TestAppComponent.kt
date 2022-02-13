package com.qapital.di

import com.qapital.ui.activities.ActivitiesViewModelTest
import com.qapital.ui.activities.repository.RepositoryViewModelTestComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Main Test component that brings all the dagger stuff to life.
 */
@Singleton
@Component(modules = [AppModule::class])
interface TestAppComponent : AppComponent {

    fun inject(topRepositoriesViewModelTest: ActivitiesViewModelTest)

    fun repositoryViewModelTestBuilder(): RepositoryViewModelTestComponent.Builder

}