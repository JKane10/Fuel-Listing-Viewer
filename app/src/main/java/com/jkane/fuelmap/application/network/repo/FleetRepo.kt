package com.jkane.fuelmap.application.network.repo

import com.jkane.fuelmap.application.network.models.FuelEntry
import io.reactivex.Observable

interface FleetRepo {

    fun getFuelEntries(
        page: Int,
        filters: Map<String, String>
    ): Observable<List<FuelEntry>>
}