package com.jkane.fuelmap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jkane.fuelmap.application.network.models.FuelEntry
import com.jkane.fuelmap.application.network.repo.FleetRepo
import com.jkane.fuelmap.ui.fuel_entries.FuelEntriesViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class FuelEntriesViewModelTest {

    private lateinit var viewModel: FuelEntriesViewModel

    // Test values
    private val testFuelEntries = mutableListOf<FuelEntry>()
    private val testKey = "testKey"
    private val testValue = "testValue"

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        viewModel = FuelEntriesViewModel()
        testFuelEntries.add(
            FuelEntry(
                id = 1,
                vehicleName = "Test vehicle",
                totalAmount = 10.0,
                costPerMi = 2.0,
                usGallons = "5.0",
                pricePerVolumeUnit = 2.0,
                fuelTypeName = "ExTREME Fuel",
                vendorName = "QuickTrip",
                reference = "Reference"
            )
        )


    }

    @Test
    fun `test viewModel initial state`() {
        viewModel.apply {
            setInitialState(null)
            assert(!isLoading.value!!)
            assert(filters.value!!.isEmpty())
            assert(fuelEntries.value!!.isEmpty())
            assert(page.value == 0)
        }
    }

    @Test
    fun `test adding filters to viewmodel`() {
        viewModel.apply {
            setInitialState(null)
            addFilter(testKey, testValue)
            assert(filters.value!![testKey] == testValue)
        }
    }

    @Test
    fun `test removing filters from viewmodel`() {
        viewModel.apply {
            setInitialState(null)
            addFilter(testKey, testValue)
            assert(filters.value!![testKey] == testValue)
            removeFilter(testKey)
            assert(!filters.value!!.contains(testKey))
        }
    }

    @Test
    fun `test update entries`() {
        viewModel.fuelEntries.value = this.testFuelEntries
        viewModel.fuelEntries.value!![0].apply {
            assert(id == testFuelEntries[0].id)
            assert(vehicleName == testFuelEntries[0].vehicleName)
            assert(totalAmount == testFuelEntries[0].totalAmount)
        }
    }
}