package no.s339420_s375128.happymap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import no.s339420_s375128.happymap.ui.theme.S339420_s375128_HappyMapTheme
import no.s339420_s375128.happymap.viewmodels.PlacesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S339420_s375128_HappyMapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PlacesScreen(modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}

@Composable
fun PlacesScreen(viewModel: PlacesViewModel = viewModel(), modifier: Modifier = Modifier) {
    // Hent dataene fra LiveData og observer
    val places = viewModel.places.observeAsState(emptyList()).value

    // Sjekk at dataene er hentet før du viser dem
    LaunchedEffect(Unit) {
        viewModel.fetchPlaces()  // Hent data når skjermen lastes
    }

    // Vis dataene i en liste
    LazyColumn(modifier = modifier) {
        items(places) { place ->
            Text(text = place.description)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    S339420_s375128_HappyMapTheme {
        PlacesScreen(modifier = Modifier.fillMaxSize())
    }
}