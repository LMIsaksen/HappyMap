package no.s339420_s375128.happymap.data

data class Place(
    val id: Int,
    val description: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)