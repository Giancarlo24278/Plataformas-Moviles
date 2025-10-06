package uvg.giancarlo.lab10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class LocationInfo(
    val id: String,
    val name: String,
    val type: String,
    val dimension: String
)

class LocationsViewModel : ViewModel() {
    private val _locationsState = MutableStateFlow<UiState<List<LocationInfo>>>(UiState.Loading)
    val locationsState: StateFlow<UiState<List<LocationInfo>>> = _locationsState.asStateFlow()

    init {
        loadLocations()
    }

    fun loadLocations() {
        viewModelScope.launch {
            _locationsState.value = UiState.Loading
            delay(4000) // 4 segundos

            if (Random.nextFloat() < 0.2f) {
                _locationsState.value = UiState.Error("Error al cargar ubicaciones. Verifica tu conexión.")
            } else {
                val locations = listOf(
                    LocationInfo("1", "Earth (C-137)", "Planet", "Dimension C-137"),
                    LocationInfo("2", "Abadango", "Cluster", "Unknown"),
                    LocationInfo("3", "Citadel of Ricks", "Space station", "Unknown"),
                    LocationInfo("4", "Worldender's lair", "Planet", "Unknown")
                )
                _locationsState.value = UiState.Success(locations)
            }
        }
    }
}