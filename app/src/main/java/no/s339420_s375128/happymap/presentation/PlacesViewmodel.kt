package no.s339420_s375128.happymap.presentation

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.s339420_s375128.happymap.api.RetrofitClient
import no.s339420_s375128.happymap.data.Place
import java.util.Locale

class PlacesViewModel : ViewModel() {

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

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
                val response = RetrofitClient.instance.addPlace(description, address, latitude, longitude)

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

    suspend fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    addresses[0].getAddressLine(0)
                } else {
                    "Ukjent adresse"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                "Kunne ikke hente adresse"
            }
        }
    }
}