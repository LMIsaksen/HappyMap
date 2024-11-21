package no.s339420_s375128.happymap.presentation

import com.google.android.gms.maps.model.LatLngBounds
import no.s339420_s375128.happymap.data.Place

sealed class PlacesScreenViewState {
    data object Loading : PlacesScreenViewState()
    data class PlaceList(
        val places: List<Place>,

        val boundingBox: LatLngBounds,

        val showingAllPlaces: Boolean = false,
    ) : PlacesScreenViewState()
}