package uvg.giancarlo.lab10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class CharactersViewModel : ViewModel() {
    private val _charactersState = MutableStateFlow<UiState<List<String>>>(UiState.Loading)
    val charactersState: StateFlow<UiState<List<String>>> = _charactersState.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _charactersState.value = UiState.Loading
            delay(4000) // 4 segundos

            if (Random.nextFloat() < 0.2f) {
                _charactersState.value = UiState.Error("Error al cargar personajes. Verifica tu conexión.")
            } else {
                val characters = listOf(
                    "Rick Sanchez",
                    "Morty Smith",
                    "Summer Smith",
                    "Beth Smith",
                    "Jerry Smith",
                    "Abadango Alien"
                )
                _charactersState.value = UiState.Success(characters)
            }
        }
    }
}