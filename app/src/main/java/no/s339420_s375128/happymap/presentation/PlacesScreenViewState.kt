package no.s339420_s375128.happymap.presentation

import com.google.android.gms.maps.model.LatLngBounds
import no.s339420_s375128.happymap.data.Place

sealed class PlacesScreenViewState {
    data object Loading : PlacesScreenViewState()
    data class PlaceList(
        // List of the mountains to display
        val mountains: List<Place>,

        // Bounding box that contains all of the mountains
        val boundingBox: LatLngBounds,

        // Switch indicating whether all the mountains or just the 14ers
        val showingAllPeaks: Boolean = false,
    ) : PlacesScreenViewState()
}