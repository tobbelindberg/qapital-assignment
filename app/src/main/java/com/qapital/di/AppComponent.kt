package com.qapital.di

import com.qapital.ui.MainActivity
import com.qapital.ui.activities.ActivitiesComponent
import dagger.Component
import javax.inject.Singleton

/**
 * Main component that brings all the dagger stuff to life.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun activitiesBuilder(): ActivitiesComponent.Builder

}