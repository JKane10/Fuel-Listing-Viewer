package com.jkane.fuelmap.application.dagger

import android.app.Application
import com.jkane.fuelmap.ui.fuel_entries.FuelEntriesActivity
import com.jkane.fuelmap.ui.fuel_entries.FuelEntriesViewModel
import com.jkane.fuelmap.ui.fuel_entries.fuel_list.FuelListFragment
import com.jkane.fuelmap.ui.fuel_entries.fuel_map.FuelMapFragment
import dagger.Component

@Component(
    modules = [
        NetworkModule::class
    ]
)
interface AppComponent {
    fun inject(app: Application)

    fun inject(fuelListFragment: FuelListFragment)

    fun inject(fuelMapFragment: FuelMapFragment)

    fun inject(fuelEntriesActivity: FuelEntriesActivity)

    fun inject(fuelEntriesViewModel: FuelEntriesViewModel)
}
