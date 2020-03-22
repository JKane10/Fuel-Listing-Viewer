package com.jkane.fuelmap.application.dagger

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jkane.fuelmap.BuildConfig

import com.jkane.fuelmap.application.network.api.FleetApi
import com.jkane.fuelmap.application.network.repo.FleetRepo
import com.jkane.fuelmap.application.network.repo.FleetRepoImpl
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule constructor(private val context: Context) {

    private val baseUrl = BuildConfig.BASE_URL

    @Provides
    fun provideFleetApi(): FleetApi {
        val client = OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(
                    it.request().newBuilder()
                        .addHeader("Authorization", BuildConfig.AUTH_KEY)
                        .addHeader("Account-Token", BuildConfig.ACCOUNT_TOKEN)
                        .build()
                )
            }
            .addInterceptor(ChuckInterceptor(context))
            .build()

        val gsonFactory = GsonConverterFactory.create(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(FleetApi::class.java)
    }

    @Provides
    fun provideFleetRepo(repo: FleetRepoImpl): FleetRepo = repo
}
