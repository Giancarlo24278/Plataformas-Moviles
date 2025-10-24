package uvg.giancarlo.lab10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.giancarlo.lab10.repository.CharacterRepository
import uvg.giancarlo.lab10.room.AppDatabase
import uvg.giancarlo.lab10.room.CharacterEntity
import kotlin.random.Random

data class CharacterDetails(
    val name: String,
    val species: String,
    val status: String,
    val gender: String
)

class CharacterProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository

    private val _profileState = MutableStateFlow<UiState<CharacterDetails>>(UiState.Loading)
    val profileState: StateFlow<UiState<CharacterDetails>> = _profileState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CharacterRepository(database.characterDao())
    }

    fun loadCharacterProfile(name: String) {
        viewModelScope.launch {
            _profileState.value = UiState.Loading
            delay(2000) // Delay de 2 segundos

            try {
                // Buscar en Room por nombre
                val characters = mutableListOf<CharacterEntity>()
                repository.allCharacters.collect { list ->
                    characters.addAll(list)
                }

                val character = characters.find { it.name == name }

                if (character != null) {
                    _profileState.value = UiState.Success(
                        CharacterDetails(
                            name = character.name,
                            species = character.species,
                            status = character.status,
                            gender = character.gender
                        )
                    )
                } else {
                    _profileState.value = UiState.Error("Personaje no encontrado")
                }
            } catch (e: Exception) {
                _profileState.value = UiState.Error("Error al cargar el perfil")
            }
        }
    }
}