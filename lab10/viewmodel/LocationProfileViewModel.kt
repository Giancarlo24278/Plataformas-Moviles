package uvg.giancarlo.lab10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.giancarlo.lab10.repository.PlaceRepository
import uvg.giancarlo.lab10.room.AppDatabase

class LocationProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PlaceRepository

    private val _locationState = MutableStateFlow<UiState<LocationInfo>>(UiState.Loading)
    val locationState: StateFlow<UiState<LocationInfo>> = _locationState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PlaceRepository(database.placeDao())
    }

    fun loadLocationProfile(locationId: String) {
        viewModelScope.launch {
            _locationState.value = UiState.Loading
            delay(2000) // Delay de 2 segundos

            try {
                val location = repository.getPlaceById(locationId.toInt())

                if (location != null) {
                    _locationState.value = UiState.Success(
                        LocationInfo(
                            id = location.id.toString(),
                            name = location.name,
                            type = location.type,
                            dimension = location.dimension
                        )
                    )
                } else {
                    _locationState.value = UiState.Error("Ubicación no encontrada")
                }
            } catch (e: Exception) {
                _locationState.value = UiState.Error("Error al cargar la ubicación")
            }
        }
    }
}