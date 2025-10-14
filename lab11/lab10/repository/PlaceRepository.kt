package uvg.giancarlo.lab10.repository

import kotlinx.coroutines.flow.Flow
import uvg.giancarlo.lab10.data.LocationsDb
import uvg.giancarlo.lab10.room.PlaceDao
import uvg.giancarlo.lab10.room.PlaceEntity

class PlaceRepository(private val placeDao: PlaceDao) {

    val allPlaces: Flow<List<PlaceEntity>> = placeDao.getAllPlaces()

    suspend fun getPlaceById(id: Int): PlaceEntity? {
        return placeDao.getPlaceById(id)
    }

    suspend fun syncPlaces() {
        val places = LocationsDb.locations.map { location ->
            PlaceEntity(
                id = location.id,
                name = location.name,
                type = location.type,
                dimension = location.dimension
            )
        }
        placeDao.insertAllPlaces(places)
    }

    suspend fun deleteAllPlaces() {
        placeDao.deleteAllPlaces()
    }
}