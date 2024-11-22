package no.s339420_s375128.happymap.api

import no.s339420_s375128.happymap.data.Place
import retrofit2.Response
import retrofit2.http.*

interface PlacesApi {

    @GET("jsonout.php")
    suspend fun getPlaces(): Response<List<Place>>

    @FormUrlEncoded
    @POST("jsonin.php")
    suspend fun addPlace(
        @Field("description") description: String,
        @Field("address") address: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double
    ): Response<Void>
}