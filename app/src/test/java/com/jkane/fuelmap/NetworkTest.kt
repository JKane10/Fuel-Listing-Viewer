package com.jkane.fuelmap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jkane.fuelmap.application.network.api.FleetApi
import com.jkane.fuelmap.application.network.models.FuelEntry
import com.jkane.fuelmap.application.network.repo.FleetRepo
import com.jkane.fuelmap.application.network.repo.FleetRepoImpl
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var fleetRepo: FleetRepo

    @Before
    fun setup() {
        fleetRepo = FleetRepoImpl(
            Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(FleetApi::class.java)
        )
    }

    @Test
    fun `verify get fuel entries is successful`() {
        val observer = TestObserver<List<FuelEntry>>()
        val scheduler = TestScheduler()
        fleetRepo.getFuelEntries(1, mapOf())
            .subscribeOn(scheduler)
            .doOnNext { assert(true) }
            .doOnError { assert(false) }
            .subscribe(observer)
        scheduler.advanceTimeBy(10, TimeUnit.SECONDS) // Request has timed out.
    }
}