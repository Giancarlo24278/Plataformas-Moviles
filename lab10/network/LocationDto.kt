package uvg.giancarlo.lab10.network

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("dimension") val dimension: String
)

data class LocationResponse(
    @SerializedName("results") val results: List<LocationDto>
)