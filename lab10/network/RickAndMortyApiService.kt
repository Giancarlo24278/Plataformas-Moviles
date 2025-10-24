package uvg.giancarlo.lab10.network

import retrofit2.http.GET

interface RickAndMortyApiService {

    @GET("character")
    suspend fun getCharacters(): CharacterResponse

    @GET("location")
    suspend fun getLocations(): LocationResponse
}