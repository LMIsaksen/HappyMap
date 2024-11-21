package no.s339420_s375128.happymap

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import no.s339420_s375128.happymap.ui.theme.S339420_s375128_HappyMapTheme
import no.s339420_s375128.happymap.ui.FavouritePlacesMap

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val apiKey = BuildConfig.MAPS_API_KEY
        Log.d("API_KEY", "Loaded API Key: $apiKey")

        setContent {
            S339420_s375128_HappyMapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FavouritePlacesMap(innerPadding)
                }
            }
        }
    }
}



