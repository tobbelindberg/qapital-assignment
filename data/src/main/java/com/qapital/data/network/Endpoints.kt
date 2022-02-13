package com.qapital.data.network

import com.qapital.data.network.model.ActivitiesDTO
import com.qapital.data.network.model.UserDTO
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*


interface Endpoints {

    @GET("/activities")
    fun getActivities(
        @Query("from") from: Date,
        @Query("to") to: Date
    ): Observable<ActivitiesDTO>

    @GET("/users/{id}")
    fun getUser(
        @Path("id") userId: Long
    ): Observable<UserDTO>

}
