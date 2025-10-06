package uvg.giancarlo.lab10.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

data class CharacterDetails(
    val name: String,
    val species: String,
    val status: String,
    val gender: String
)

class CharacterProfileViewModel : ViewModel() {
    private val _profileState = MutableStateFlow<UiState<CharacterDetails>>(UiState.Loading)
    val profileState: StateFlow<UiState<CharacterDetails>> = _profileState.asStateFlow()

    fun loadCharacterProfile(name: String) {
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            delay(2000) // 2 segundos

            if (Random.nextFloat() < 0.2f) {
                _profileState.value = UiState.Error("Error al cargar el perfil. Intenta de nuevo.")
            } else {
                val details = getPersonaDetails(name)
                _profileState.value = UiState.Success(
                    CharacterDetails(name, details.first, details.second, details.third)
                )
            }
        }
    }

    private fun getPersonaDetails(name: String): Triple<String, String, String> {
        return when (name) {
            "Rick Sanchez" -> Triple("Human", "Alive", "Male")
            "Morty Smith" -> Triple("Human", "Alive", "Male")
            "Summer Smith" -> Triple("Human", "Alive", "Female")
            "Beth Smith" -> Triple("Human", "Alive", "Female")
            "Jerry Smith" -> Triple("Human", "Alive", "Male")
            "Abadango Alien" -> Triple("Alien", "Alive", "Unknown")
            else -> Triple("Human", "Alive", "Male")
        }
    }
}