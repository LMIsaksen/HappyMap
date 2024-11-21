package no.s339420_s375128.happymap.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.s339420_s375128.happymap.api.RetrofitClient
import no.s339420_s375128.happymap.data.Place

class PlacesViewModel : ViewModel() {

    // LiveData som holder stedene
    val places = MutableLiveData<List<Place>>()

    // Funksjon for å hente steder
    fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getPlaces()  // Denne skal være suspend
                if (response.isSuccessful) {
                    places.value = response.body() ?: emptyList()  // Hent kroppen av responsen (listen med steder)
                } else {
                    Log.e("PlacesViewModel", "Feil ved henting av steder: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "Feil ved henting av steder: ${e.message}")
            }
        }
    }

    // Funksjon for å legge til et sted
    fun addPlace(description: String, address: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val place = Place(0, description, address, latitude, longitude)  // Lag et Place-objekt
                val response = RetrofitClient.instance.addPlace(description, address, latitude, longitude)  // Kall på addPlace funksjonen

                if (response.isSuccessful) {
                    fetchPlaces()
                } else {
                    Log.e("PlacesViewModel", "Feil ved å legge til sted: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("PlacesViewModel", "Feil ved å legge til sted: ${e.message}")
            }
        }
    }
}