package uvg.giancarlo.lab10.repository

import kotlinx.coroutines.flow.Flow
import uvg.giancarlo.lab10.network.RetrofitInstance
import uvg.giancarlo.lab10.room.PlaceDao
import uvg.giancarlo.lab10.room.PlaceEntity

class PlaceRepository(private val placeDao: PlaceDao) {

    val allPlaces: Flow<List<PlaceEntity>> = placeDao.getAllPlaces()

    suspend fun getPlaceById(id: Int): PlaceEntity? {
        return placeDao.getPlaceById(id)
    }

    // Estrategia Offline First
    suspend fun syncPlacesFromApi(): Result<Unit> {
        return try {
            // Llamada al API
            val response = RetrofitInstance.api.getLocations()

            // Mapear DTO a Entity
            val places = response.results.map { dto ->
                PlaceEntity(
                    id = dto.id,
                    name = dto.name,
                    type = dto.type,
                    dimension = dto.dimension
                )
            }

            // Guardar en Room
            placeDao.insertAllPlaces(places)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAllPlaces() {
        placeDao.deleteAllPlaces()
    }
}