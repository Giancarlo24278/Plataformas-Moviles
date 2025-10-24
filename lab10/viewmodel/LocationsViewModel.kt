package uvg.giancarlo.lab10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.giancarlo.lab10.repository.PlaceRepository
import uvg.giancarlo.lab10.room.AppDatabase
import uvg.giancarlo.lab10.room.PlaceEntity

data class LocationInfo(
    val id: String,
    val name: String,
    val type: String,
    val dimension: String
)

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaceRepository

    private val _locationsState = MutableStateFlow<UiState<List<LocationInfo>>>(UiState.Loading)
    val locationsState: StateFlow<UiState<List<LocationInfo>>> = _locationsState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PlaceRepository(database.placeDao())

        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _locationsState.value = UiState.Loading

            // Estrategia Offline First
            repository.allPlaces.collect { placesFromDb ->
                if (placesFromDb.isEmpty()) {
                    // No hay data local, traer del API
                    val result = repository.syncPlacesFromApi()

                    if (result.isFailure) {
                        _locationsState.value = UiState.Error(
                            "Error al cargar ubicaciones. Verifica tu conexión."
                        )
                    }
                } else {
                    // Mapear Entity a LocationInfo
                    val locations = placesFromDb.map { entity ->
                        LocationInfo(
                            id = entity.id.toString(),
                            name = entity.name,
                            type = entity.type,
                            dimension = entity.dimension
                        )
                    }
                    _locationsState.value = UiState.Success(locations)
                }
            }
        }
    }
}