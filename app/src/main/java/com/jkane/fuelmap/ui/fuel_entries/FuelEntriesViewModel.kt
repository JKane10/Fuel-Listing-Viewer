package com.jkane.fuelmap.ui.fuel_entries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jkane.fuelmap.application.network.models.FuelEntry
import com.jkane.fuelmap.application.network.observer.NetworkObserver
import com.jkane.fuelmap.application.network.repo.FleetRepo
import io.reactivex.schedulers.Schedulers

/**
 * MainViewModel belonging to MainActivity and shared between sub fragments (list and map) as they
 * both pull from the same data set.
 *
 * Contains logic for maintaining and filtering the fuel entry data and acts as view state.
 * These values are observed by #[FuelEntriesActivity] and it's fragments
 *
 * isLoading - controls UI loading overlay.
 * filters - maintains state of data filters.
 * fuelEntries - LiveData wrapper around network object.
 * error - maintains error state for UI.
 */
class FuelEntriesViewModel : ViewModel() {

    private var fleetRepo: FleetRepo? = null

    val isLoading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val filters: MutableLiveData<MutableMap<String, String>> by lazy { MutableLiveData<MutableMap<String, String>>() }
    val page: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val fuelEntries: MutableLiveData<List<FuelEntry>> by lazy { MutableLiveData<List<FuelEntry>>() }
    val error: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    /**
     * Sets the initial state of the viewmodel values. Also provides the repo from a context aware
     * source
     *
     * @param fleetRepo - Reference to the network repository from a context aware source.
     */
    fun setInitialState(fleetRepo: FleetRepo?) {
        this.fleetRepo = fleetRepo
        isLoading.value = false
        filters.value = mutableMapOf()
        fuelEntries.value = listOf()
        page.value = 0
        if (fleetRepo != null) loadNextPage()
    }

    /**
     * If loading isn't already in progress, will increment the page count and trigger a network
     * request to get the next page of fuel entries.
     *
     * Request - Takes current page and current filters
     * OnSuccess - Will append new fuel entries to the current list of entries and set loading false
     * OnError - Will post an user friendly error message for the UI to display and sets loading to false
     */
    fun loadNextPage() {
        if (isLoading.value == false) {
            page.value = (page.value?.plus(1))
            val page = page.value ?: 1
            isLoading.postValue(true)
            fleetRepo?.let { repo ->
                repo.getFuelEntries(
                    page,
                    filters.value?.toMap() ?: mapOf()
                )
                    .subscribeOn(Schedulers.io())
                    .doOnNext {
                        fuelEntries.postValue(fuelEntries.value?.plus(it))
                        isLoading.postValue(false)
                    }.doOnError {
                        error.postValue("Sorry, something went wrong on our end.")
                        isLoading.postValue(false)
                    }.subscribe(NetworkObserver())
            }
        }
    }

    /**
     * Adds filter to filter map
     *
     * Filters must adhere to standards established at
     * https://developer.fleetio.com/docs/filtering-results
     *
     * @param key - key for new filter.
     * @param value - value for new filter.
     */
    fun addFilter(key: String, value: String) {
        filters.value?.put(key, value)
    }

    /**
     * Removes filter from filter map
     *
     * Filters must adhere to standards established at
     * https://developer.fleetio.com/docs/filtering-results
     *
     * @param key - key for filter to remove.
     */
    fun removeFilter(key: String) {
        filters.value?.remove(key)
    }

    /**
     * Clears fuel entry list, resets current page to 0, triggers loadNextPage
     *
     * This will essentially reset to the initialstate while maintaining view state and filters
     * and trigger the initial fuel entry page load.
     */
    fun clearAndRefresh() {
        page.value = 0
        fuelEntries.value = listOf()
        loadNextPage()
    }
}