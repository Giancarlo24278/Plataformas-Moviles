package uvg.giancarlo.lab10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.giancarlo.lab10.repository.CharacterRepository
import uvg.giancarlo.lab10.room.AppDatabase
import uvg.giancarlo.lab10.room.CharacterEntity

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository

    private val _charactersState = MutableStateFlow<UiState<List<CharacterEntity>>>(UiState.Loading)
    val charactersState: StateFlow<UiState<List<CharacterEntity>>> = _charactersState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CharacterRepository(database.characterDao())

        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _charactersState.value = UiState.Loading

            repository.allCharacters.collect { charactersFromDb ->
                if (charactersFromDb.isEmpty()) {
                    val result = repository.syncCharactersFromApi()

                    if (result.isFailure) {
                        _charactersState.value = UiState.Error(
                            "Error al cargar personajes. Verifica tu conexión."
                        )
                    }
                } else {

                    _charactersState.value = UiState.Success(charactersFromDb)
                }
            }
        }
    }
}