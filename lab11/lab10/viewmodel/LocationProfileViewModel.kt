package uvg.giancarlo.lab10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class LocationProfileViewModel : ViewModel() {
    private val _locationState = MutableStateFlow<UiState<LocationInfo>>(UiState.Loading)
    val locationState: StateFlow<UiState<LocationInfo>> = _locationState.asStateFlow()

    fun loadLocationProfile(locationId: String) {
        viewModelScope.launch {
            _locationState.value = UiState.Loading
            delay(2000) // 2 segundos

            if (Random.Default.nextFloat() < 0.2f) {
                _locationState.value = UiState.Error("Error al cargar la ubicación. Intenta de nuevo.")
            } else {
                val location = getLocationById(locationId)
                _locationState.value = UiState.Success(location)
            }
        }
    }

    private fun getLocationById(id: String): LocationInfo {
        return when (id) {
            "1" -> LocationInfo("1", "Earth (C-137)", "Planet", "Dimension C-137")
            "2" -> LocationInfo("2", "Abadango", "Cluster", "Unknown")
            "3" -> LocationInfo("3", "Citadel of Ricks", "Space station", "Unknown")
            "4" -> LocationInfo("4", "Worldender's lair", "Planet", "Unknown")
            else -> LocationInfo("1", "Earth (C-137)", "Planet", "Dimension C-137")
        }
    }
}