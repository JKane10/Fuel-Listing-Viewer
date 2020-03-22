package com.jkane.fuelmap.application.network.repo

import com.jkane.fuelmap.application.network.api.FleetApi
import com.jkane.fuelmap.application.network.models.FuelEntry
import io.reactivex.Observable
import javax.inject.Inject

class FleetRepoImpl @Inject constructor(
    private val fleetApi: FleetApi
) : FleetRepo {

    override fun getFuelEntries(
        page: Int,
        filters: Map<String, String>
    ): Observable<List<FuelEntry>> {
        return fleetApi.getFuelEntries(page, filters)
    }
}