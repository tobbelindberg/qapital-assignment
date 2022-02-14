package com.qapital.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.qapital.BuildConfig
import com.qapital.data.database.AppDatabase
import com.qapital.data.network.Endpoints
import com.qapital.data.network.converters.QueryConverterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Provides components and modules used all over the place here.
 */
@Module(includes = [NetworkServiceModule::class, DatabaseServiceModule::class])
open class AppModule(private val appContext: Context) {

    companion object {
        private const val BASE_URL = "http://qapital-ios-testtask.herokuapp.com"
        private const val ACCEPT = "Accept"
        private const val ACCEPT_JSON = "application/json"
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX"
    }

    @Provides
    fun provideAppContext(): Context {
        return appContext
    }

    @Provides
    fun provideResources(): Resources {
        return appContext.resources
    }

    @Provides
    open fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setDateFormat(DATE_PATTERN).create()
    }

    @Provides
    open fun provideOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor())
        }
        builder.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .addHeader(ACCEPT, ACCEPT_JSON).build()

            chain.proceed(request)
        }

        return builder.build()
    }

    @Provides
    internal fun provideEndpoints(
        gson: Gson,
        baseUrl: String,
        okHttpClient: OkHttpClient
    ): Endpoints {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(QueryConverterFactory.create(DATE_PATTERN))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(Endpoints::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }
}