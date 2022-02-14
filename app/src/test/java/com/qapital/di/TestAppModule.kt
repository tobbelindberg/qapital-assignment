package com.qapital.di

import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.qapital.BuildConfig
import com.qapital.data.network.Endpoints
import com.qapital.data.network.converters.QueryConverterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Provides Test components and modules used all over the place here and hosts a mock server
 * that delivers the provided response.
 *
 * @param mockResponses The first string in the pair is the endpoint path and the second one is it's
 * corresponding response.
 */
@Module(includes = [NetworkServiceModule::class, TestDatabaseServiceModule::class])
class TestAppModule(
    private val mockAppContext: Context,
    private val okHttpIdleCallback: Runnable,
    vararg mockResponses: Pair<String, String>
) {

    companion object {
        private const val ACCEPT = "Accept"
        private const val APPLICATION_VND_V3 = "application/vnd.github.v3+json"
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX"
    }

    private val mockWebServer = MockWebServer()

    val mockResponseMap = mockResponses.toMap()

    init {
        mockWebServer.dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                return mockResponseMap[request.requestUrl?.encodedPath]?.let { response ->
                    MockResponse().setBody(response)
                } ?: MockResponse().setResponseCode(404)
            }
        }
        mockWebServer.start()
    }

    fun shutDownServer() {
        mockWebServer.shutdown()
    }

    @Provides
    fun provideAppContext(): Context {
        return mockAppContext
    }

    @Provides
    fun provideResources(): Resources {
        return mockAppContext.resources
    }

    @Provides
    fun provideBaseUrl(): String {
        return mockWebServer.url("/").toString()

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
                .addHeader(ACCEPT, APPLICATION_VND_V3).build()

            chain.proceed(request)
        }

        return builder.build().apply {
            dispatcher.idleCallback = okHttpIdleCallback
        }
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
}