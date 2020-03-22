package com.jkane.fuelmap.application.network.observer

import android.util.Log
import com.jkane.fuelmap.application.network.models.FuelEntry
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class NetworkObserver : Observer<List<FuelEntry>> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: List<FuelEntry>) {

    }

    override fun onError(e: Throwable) {
        Log.d("NetworkObserver", e.toString())
    }
}