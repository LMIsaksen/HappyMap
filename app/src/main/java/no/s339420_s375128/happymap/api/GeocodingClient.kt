package no.s339420_s375128.happymap.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeocodingClient {
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val instance: GeocodingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingApi::class.java)
    }
}