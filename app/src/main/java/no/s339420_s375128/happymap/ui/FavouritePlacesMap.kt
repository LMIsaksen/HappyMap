package no.s339420_s375128.happymap.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import no.s339420_s375128.happymap.presentation.PlacesViewModel


@Composable
fun FavouritePlacesMap(paddingValues: PaddingValues) {
    // Få tak i PlacesViewModel ved hjelp av viewModel() i Compose
    val viewModel: PlacesViewModel = viewModel()

    // Husk at places er LiveData, så vi bruker .value for å få dataene
    val places = viewModel.places.observeAsState(emptyList()).value

    // Hent steder fra serveren når skjermen blir laget
    LaunchedEffect(Unit) {
        viewModel.fetchPlaces()
    }

    Log.d("FavouritePlacesMap", "Places: $places")

    // Sjekk om det er data for å vise kartet eller vise en progressindikator
    if (places.isEmpty()) {
        // Ingen steder lastet, vis en loading indikator
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        // Vi har steder, vis kartet med markører
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(59.911, 10.746), 5f)  // Initial zoom level
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState
        ) {
            places.forEach { place ->
                val markerState = rememberMarkerState(position = LatLng(place.latitude, place.longitude))

                Marker(
                    state = markerState,
                    title = place.description,
                    snippet = place.address
                )
            }
        }
    }
}