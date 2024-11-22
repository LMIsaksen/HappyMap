package no.s339420_s375128.happymap.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val viewModel: PlacesViewModel = viewModel()

    val places by viewModel.places.collectAsState(emptyList())

    var isDialogVisible by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchPlaces()
    }

    if (places.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(59.911, 10.746), 12f)
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                selectedLatLng = latLng
                isDialogVisible = true
            }
        ) {
            places.forEach { place ->
                val markerState = rememberMarkerState(position = LatLng(place.latitude, place.longitude))
                val clickedLatLng = LatLng(place.latitude, place.longitude)

                val placeExists = places.any {
                    LatLng(it.latitude, it.longitude) == clickedLatLng
                }

                if (placeExists) {
                    Marker(
                        state = markerState,
                        title = "${place.description} (Lat: ${place.latitude}, Lng: ${place.longitude})",
                        snippet = place.address
                    )
                } else {
                    Marker(
                        state = markerState,
                        title = place.description,
                        snippet = place.address,
                        onClick = {
                            selectedLatLng = clickedLatLng
                            description = place.description
                            address = place.address
                            isDialogVisible = true
                            true
                        }
                    )
                }

            }
        }
    }

    if (isDialogVisible) {
        AddPlaceDialog(
            onDismiss = { isDialogVisible = false },
            onSave = { desccription ->
                selectedLatLng?.let { latLng ->
                    viewModel.addPlace(
                        desccription,
                        address = "Osloveien 50",
                        latLng.latitude,
                        latLng.longitude
                    )
                    isDialogVisible = false
                }
            },
            description = description,
            onDescriptionChange = { description = it },
        )
    }
}

@Composable
fun AddPlaceDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Legg til et nytt sted") },
        text = {
            Column {
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Beskrivelse av ditt favorittsted") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(description)
                }
            ) {
                Text("Lagre")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Avbryt")
            }
        }
    )
}

