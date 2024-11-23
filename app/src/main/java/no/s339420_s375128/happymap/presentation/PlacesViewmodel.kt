package no.s339420_s375128.happymap.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.s339420_s375128.happymap.BuildConfig
import no.s339420_s375128.happymap.api.GeocodingClient
import no.s339420_s375128.happymap.api.RetrofitClient
import no.s339420_s375128.happymap.data.Place

class PlacesViewModel : ViewModel() {

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getPlaces()
                Log.d("PlacesViewModel", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    _places.value = response.body() ?: emptyList()
                    Log.d("PlacesViewModel", "Hentet steder: ${_places.value}")
                } else {
                    Log.e("PlacesViewModel", "Feil ved henting av steder: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "Feil ved henting av steder: ${e.message}")
            }
        }
    }

    fun addPlace(description: String, address: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val place = Place(0, description, address, latitude, longitude)
                val response =
                    RetrofitClient.instance.addPlace(description, address, latitude, longitude)
                if (response.isSuccessful) {
                    _places.value = _places.value + place
                    Log.d("PlacesViewModel", "Sted lagt til: $place")
                } else {
                    Log.e("PlacesViewModel", "Feil ved å legge til sted: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "Feil ved å legge til sted: ${e.message}")
            }
        }
    }

    suspend fun getAddressFromLatLng(latitude: Double, longitude: Double): String {
        try {
            val latLng = "$latitude,$longitude"
            val apiKey = BuildConfig.MAPS_API_KEY
            val response = GeocodingClient.instance.getGeocodeAddress(latLng, apiKey)

            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || body.results.isEmpty()) {
                    "Adresse ikke funnet"
                } else {
                    body.results[0].formatted_address
                }
            } else {
                "Feil ved geokoding"
            }
        } catch (e: Exception) {
            return "Kunne ikke hente adresse"
        }
    }
}