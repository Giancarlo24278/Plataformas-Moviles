package uvg.giancarlo.lab10.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uvg.giancarlo.lab10.datastore.UserPreferences
import uvg.giancarlo.lab10.repository.CharacterRepository
import uvg.giancarlo.lab10.repository.PlaceRepository
import uvg.giancarlo.lab10.repository.UserRepository
import uvg.giancarlo.lab10.room.AppDatabase

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data object Success : LoginState
    data class Error(val message: String) : LoginState
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository
    private val characterRepository: CharacterRepository
    private val placeRepository: PlaceRepository

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    init {
        val userPreferences = UserPreferences(application)
        userRepository = UserRepository(userPreferences)

        val database = AppDatabase.getDatabase(application)
        characterRepository = CharacterRepository(database.characterDao())
        placeRepository = PlaceRepository(database.placeDao())
    }

    fun login(userName: String) {
        if (userName.isBlank()) {
            _loginState.value = LoginState.Error("Por favor ingresa tu nombre")
            return
        }

        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // Simular delay de 4 segundos
                delay(4000)

                userRepository.saveUserName(userName)

                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}