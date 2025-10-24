package uvg.giancarlo.lab10.network

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("gender") val gender: String
)

data class CharacterResponse(
    @SerializedName("results") val results: List<CharacterDto>
)