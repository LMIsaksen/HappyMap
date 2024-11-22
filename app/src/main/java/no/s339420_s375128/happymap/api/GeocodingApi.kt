package no.s339420_s375128.happymap.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {

    @GET("geocode/json")
    suspend fun getGeocodeAddress(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String
    ): Response<GeocodingResponse>
}