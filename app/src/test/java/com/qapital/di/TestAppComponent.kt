package com.qapital.di

import com.qapital.ui.activities.ActivitiesViewModelTest
import dagger.Component
import javax.inject.Singleton

/**
 * Main Test component that brings all the dagger stuff to life.
 */
@Singleton
@Component(modules = [TestAppModule::class])
interface TestAppComponent : AppComponent {

    fun inject(topRepositoriesViewModelTest: ActivitiesViewModelTest)

}