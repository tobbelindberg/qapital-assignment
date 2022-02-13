package com.qapital.base

import android.app.Application
import com.qapital.di.AppComponent
import com.qapital.di.AppModule
import com.qapital.di.DaggerAppComponent


open class QapitalApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()

    }

}