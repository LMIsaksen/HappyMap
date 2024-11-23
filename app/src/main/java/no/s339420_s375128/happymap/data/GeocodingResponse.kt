package no.s339420_s375128.happymap.data

data class GeocodingResponse(
    val results: List<Result>,
    val status: String
)

data class Result(
    val formatted_address: String
)
