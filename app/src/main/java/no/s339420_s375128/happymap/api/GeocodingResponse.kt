package no.s339420_s375128.happymap.api

data class GeocodingResponse(
    val results: List<Result>,
    val status: String
)

data class Result(
    val formatted_address: String
)
