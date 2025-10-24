package uvg.giancarlo.lab10.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MainUiState(
    val currentScreen: String = "inicio",
    val selectedPersona: String = "",
    val isLoading: Boolean = false
)

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun updateSelectedPersona(persona: String) {
        _uiState.value = _uiState.value.copy(selectedPersona = persona)
    }

    fun clearSelectedPersona() {
        _uiState.value = _uiState.value.copy(selectedPersona = "")
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
}