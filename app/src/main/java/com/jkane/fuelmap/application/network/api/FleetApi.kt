package com.jkane.fuelmap.application.network.api

import com.jkane.fuelmap.application.network.models.FuelEntry
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FleetApi {

    @GET("v1/fuel_entries")
    fun getFuelEntries(
        @Query("page") page: Int,
        @QueryMap(encoded = true) filters: Map<String, String>?
    ): Observable<List<FuelEntry>>

}