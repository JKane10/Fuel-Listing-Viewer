package com.jkane.fuelmap.application

import android.app.Application
import com.facebook.stetho.Stetho
import com.jkane.fuelmap.application.dagger.AppComponent
import com.jkane.fuelmap.application.dagger.DaggerAppComponent
import com.jkane.fuelmap.application.dagger.NetworkModule

class App : Application() {
    companion object {
        private lateinit var application: com.jkane.fuelmap.application.App
        fun get(): com.jkane.fuelmap.application.App = application
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        appComponent = DaggerAppComponent
            .builder()
            .networkModule(NetworkModule(applicationContext))
            .build()
        appComponent.inject(this)
    }

    fun getAppComponent(): AppComponent = appComponent
}